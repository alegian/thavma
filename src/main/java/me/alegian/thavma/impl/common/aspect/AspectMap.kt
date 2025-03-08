package me.alegian.thavma.impl.common.aspect

import com.google.common.collect.ImmutableList
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import me.alegian.thavma.impl.init.registries.deferred.Aspects.PRIMAL_ASPECTS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.REGISTRAR
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors

/**
 * Immutable.
 * Represents a map of Aspects to their amounts.
 * Any operation that adds or removes Aspects should return a new AspectMap.
 *
 *
 * Internally uses a LinkedHashMap (i.e. a SequencedMap) for deterministic iteration order.
 * Using a non-sequenced map might cause undefined behavior.
 */
class AspectMap(map: LinkedHashMap<Aspect, Int> = LinkedHashMap()) : Iterable<AspectStack> {
  /**
   * AspectMap.map is also immutable.
   * This is read-only access to copy the map into a new one.
   */
  protected val map: LinkedHashMap<Aspect, Int>

  init {
    this.map = LinkedHashMap(map)
  }

  fun add(aspect: Aspect, amount: Int): AspectMap {
    return builder().copyOf(this).add(aspect, amount).build()
  }

  fun add(other: AspectMap): AspectMap {
    val builder = builder().copyOf(this)
    other.forEach(Consumer { aspectStack: AspectStack -> builder.add(aspectStack) })
    return builder.build()
  }

  fun subtract(aspect: Aspect, amount: Int): AspectMap {
    return builder().copyOf(this).subtract(aspect, amount).build()
  }

  fun subtract(other: AspectMap): AspectMap {
    val builder = builder().copyOf(this)
    other.forEach(Consumer { aspectStack: AspectStack -> builder.subtract(aspectStack) })
    return builder.build()
  }

  fun scale(multiplier: Int): AspectMap {
    return builder().copyOf(this).scale(multiplier).build()
  }

  /**
   * Whether this AspectMap contains all aspects (in greater quantity) than another.
   * Useful for recipe checks
   */
  fun contains(other: AspectMap): Boolean {
    for (a in other.map.keys) if (this[a] < other[a]) return false
    return true
  }

  /**
   * Used for rendering Aura Node layers
   */
  fun toSortedList(): List<AspectStack> {
    return map.entries.stream()
      .map { e -> AspectStack.of(e.key, e.value) }
      .filter { a -> a.amount > 0 }
      .sorted(Comparator.comparingInt(AspectStack::amount))
      .toList()
  }

  operator fun get(aspect: Aspect): Int {
    return map.getOrDefault(aspect, 0)
  }

  // TODO: optimize
  fun displayedAspects(): ImmutableList<AspectStack> {
    return REGISTRAR.entries.stream()
      .map { da -> da.get() }
      .filter { a -> this[a] > 0 }
      .map { a -> AspectStack.of(a, this[a]) }
      .collect(ImmutableList.toImmutableList())
  }

  fun size(): Int {
    return map.size
  }

  val isEmpty: Boolean
    get() = map.values.stream().noneMatch { i -> i > 0 }

  fun copy(): AspectMap {
    return AspectMap(this.map)
  }

  override fun toString(): String {
    val str = StringBuilder()
    map.forEach { (k: Aspect?, v: Int?) -> str.append(k).append(v) }
    return str.toString()
  }

  override fun iterator(): MutableIterator<AspectStack> {
    return map.entries.stream().filter { e -> e.value > 0 }.map { e -> AspectStack.of(e.key, e.value) }.iterator()
  }

  override fun hashCode(): Int {
    return map.hashCode()
  }

  override fun equals(other: Any?): Boolean {
    if (other is AspectMap) return this.map == other.map
    return super.equals(other)
  }

  class Builder {
    private var map = LinkedHashMap<Aspect, Int>()

    fun copyOf(base: AspectMap): Builder {
      this.map = LinkedHashMap(base.map)
      return this
    }

    fun add(aspect: Aspect, amount: Int): Builder {
      val oldAmount = map.getOrDefault(aspect, 0)
      map[aspect] = oldAmount + amount
      return this
    }

    fun add(aspectStack: AspectStack): Builder {
      return this.add(aspectStack.aspect, aspectStack.amount)
    }

    fun subtract(aspect: Aspect, amount: Int): Builder {
      val oldAmount = map.getOrDefault(aspect, 0)
      map[aspect] = oldAmount - amount
      return this
    }

    fun subtract(aspectStack: AspectStack): Builder {
      return this.subtract(aspectStack.aspect, aspectStack.amount)
    }

    fun scale(multiplier: Int): Builder {
      map.forEach { (k: Aspect, v: Int) -> map[k] = v * multiplier }
      return this
    }

    fun build(): AspectMap {
      if (map.isEmpty()) return AspectMap()
      return AspectMap(this.map)
    }
  }

  companion object {
    private val PAIR_CODEC: Codec<Pair<Aspect, Int>> = Codec.pair(
      Aspect.CODEC.fieldOf("aspect").codec(),
      Codec.INT.fieldOf("amount").codec()
    )
    val PAIR_LIST_CODEC = PAIR_CODEC.listOf()

    val CODEC = object : Codec<AspectMap> {
      override fun <T> decode(dynamicOps: DynamicOps<T>, t: T): DataResult<Pair<AspectMap?, T>>? {
        val optionalListOfPairs = PAIR_LIST_CODEC.parse(dynamicOps, t)
          .resultOrPartial { errored -> println(errored) }

        return optionalListOfPairs.map { o ->
          o.stream().collect(
            Collectors.toMap(
              { obj -> obj.first },
              { obj -> obj.second })
          )
        }
          .map { m -> LinkedHashMap(m) }
          .map { map -> AspectMap(map) }
          .map { m -> Pair(m, t) }
          .map { result -> DataResult.success(result) }
          .orElse(DataResult.success(Pair(AspectMap(), t)))
      }

      override fun <T> encode(aspectMap: AspectMap, dynamicOps: DynamicOps<T>, t: T): DataResult<T> {
        val listOfPairs = aspectMap.map.entries.stream().filter { e: Map.Entry<Aspect, Int> -> e.value > 0 }.map { e: Map.Entry<Aspect, Int> -> Pair.of(e.key, e.value) }.toList()
        return PAIR_LIST_CODEC.encode(listOfPairs, dynamicOps, t)
      }
    }
    val MAP_STREAM_CODEC = ByteBufCodecs.map(
      ::LinkedHashMap,
      Aspect.STREAM_CODEC,
      ByteBufCodecs.INT
    )

    val STREAM_CODEC = StreamCodec.composite(MAP_STREAM_CODEC, AspectMap::map, ::AspectMap)

    fun randomPrimals(): AspectMap {
      val random = Random()
      val map = LinkedHashMap<Aspect, Int>()
      val primals = ArrayList(PRIMAL_ASPECTS)
      primals.shuffle()
      val randomPrimals = primals.subList(0, random.nextInt(primals.size) + 1)
      for (a in randomPrimals) map[a.get()] = random.nextInt(16) + 1
      return AspectMap(map)
    }

    fun builder(): Builder {
      return Builder()
    }
  }
}
