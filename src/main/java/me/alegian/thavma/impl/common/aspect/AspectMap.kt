package me.alegian.thavma.impl.common.aspect

import com.mojang.serialization.Codec
import me.alegian.thavma.impl.init.registries.deferred.Aspects.PRIMAL_ASPECTS
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import java.util.*
import java.util.function.Consumer
import java.util.function.Supplier

/**
 * Immutable.
 * Represents a map of Aspects to their amounts.
 * Any operation that adds or removes Aspects should return a new AspectMap.
 *
 *
 * Internally uses a LinkedHashMap (i.e. a SequencedMap) for deterministic iteration order.
 * Using a non-sequenced map will cause undefined behavior.
 */
class AspectMap(map: Map<Aspect, Int> = LinkedHashMap()) : Iterable<AspectStack> {
  /**
   * AspectMap.map is also immutable.
   * This is read-only access to copy the map into a new one.
   */
  protected val map: LinkedHashMap<Aspect, Int>
  val size = map.size

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

  fun has(aspect:Aspect): Boolean {
    return map.containsKey(aspect)
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

    fun add(aspect: Supplier<Aspect>, amount: Int) = add(aspect.get(), amount)

    fun add(aspectStack: AspectStack): Builder {
      return this.add(aspectStack.aspect, aspectStack.amount)
    }

    fun subtract(aspect: Aspect, amount: Int): Builder {
      val oldAmount = map.getOrDefault(aspect, 0)
      map[aspect] = oldAmount - amount
      if (map[aspect] == 0) map.remove(aspect)
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
    // only used as in-between step
    val MAP_CODEC = Codec.unboundedMap(Aspect.CODEC, Codec.INT)

    val CODEC = MAP_CODEC.xmap(::AspectMap, AspectMap::map)

    // only used as in-between step
    val MAP_STREAM_CODEC = ByteBufCodecs.map(
      ::LinkedHashMap,
      Aspect.STREAM_CODEC,
      ByteBufCodecs.INT
    )

    val STREAM_CODEC = StreamCodec.composite(MAP_STREAM_CODEC, AspectMap::map, ::AspectMap)

    fun randomPrimals(scale: Int): AspectMap {
      val random = Random()
      val map = LinkedHashMap<Aspect, Int>()
      val primals = ArrayList(PRIMAL_ASPECTS)
      primals.shuffle()
      val randomPrimals = primals.subList(0, random.nextInt(primals.size) + 1)
      for (a in randomPrimals) map[a.get()] = random.nextInt(scale) + 1
      return AspectMap(map)
    }

    fun builder(): Builder {
      return Builder()
    }

    fun ofPrimals(amount: Int): AspectMap {
      val builder = Builder()
      for(a in PRIMAL_ASPECTS){
        builder.add(a.get(), amount)
      }
      return builder.build()
    }
  }
}
