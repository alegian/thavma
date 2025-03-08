package me.alegian.thavma.impl.common.aspect;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.netty.buffer.ByteBuf;
import me.alegian.thavma.impl.init.registries.deferred.Aspects;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Immutable.
 * Represents a map of Aspects to their amounts.
 * Any operation that adds or removes Aspects should return a new AspectMap.
 * <p>
 * Internally uses a LinkedHashMap (i.e. a SequencedMap) for deterministic iteration order.
 * Using a non-sequenced map might cause undefined behavior.
 */
public class AspectMap implements Iterable<AspectStack> {
  public static final AspectMap EMPTY = new AspectMap(new LinkedHashMap<>());
  public static final Codec<Pair<Aspect, Integer>> PAIR_CODEC = Codec.pair(
      Aspect.Companion.getCODEC().fieldOf("aspect").codec(),
      Codec.INT.fieldOf("amount").codec()
  );
  public static final Codec<List<Pair<Aspect, Integer>>> PAIR_LIST_CODEC = AspectMap.PAIR_CODEC.listOf();
  public static final Codec<AspectMap> CODEC = new Codec<>() {
    @Override
    public <T> DataResult<Pair<AspectMap, T>> decode(DynamicOps<T> dynamicOps, T t) {
      var optionalListOfPairs = AspectMap.PAIR_LIST_CODEC.parse(dynamicOps, t)
          .resultOrPartial(System.out::println);

      return optionalListOfPairs.map(o ->
              o.stream().collect(Collectors.toMap(Pair::getFirst, Pair::getSecond))
          )
          .map(LinkedHashMap::new)
          .map(AspectMap::new)
          .map(m -> new Pair<>(m, t))
          .map(DataResult::success)
          .orElse(DataResult.success(new Pair<>(AspectMap.EMPTY, t)));
    }

    @Override
    public <T> DataResult<T> encode(AspectMap aspectMap, DynamicOps<T> dynamicOps, T t) {
      var listOfPairs = aspectMap.map.entrySet().stream().filter(e -> e.getValue() > 0).map(e -> Pair.of(e.getKey(), e.getValue())).toList();
      return AspectMap.PAIR_LIST_CODEC.encode(listOfPairs, dynamicOps, t);
    }
  };
  public static final StreamCodec<ByteBuf, LinkedHashMap<Aspect, Integer>> MAP_STREAM_CODEC =
      ByteBufCodecs.map(
          LinkedHashMap::new,
          Aspect.Companion.getSTREAM_CODEC(),
          ByteBufCodecs.INT
      );
  public static final StreamCodec<ByteBuf, AspectMap> STREAM_CODEC =
      StreamCodec.composite(
          AspectMap.MAP_STREAM_CODEC, AspectMap::getMap,
          AspectMap::new
      );
  private final LinkedHashMap<Aspect, Integer> map;

  private AspectMap(LinkedHashMap<Aspect, Integer> map) {
    this.map = new LinkedHashMap<>(map);
  }

  public static AspectMap randomPrimals() {
    Random random = new Random();
    LinkedHashMap<Aspect, Integer> map = new LinkedHashMap<>();
    var primals = new ArrayList<>(Aspects.INSTANCE.getPRIMAL_ASPECTS());
    Collections.shuffle(primals);
    var randomPrimals = primals.subList(0, random.nextInt(primals.size()) + 1);
    for (var a : randomPrimals)
      map.put(a.get(), random.nextInt(16) + 1);
    return new AspectMap(map);
  }

  public static Builder builder() {
    return new Builder();
  }

  public AspectMap add(Aspect aspect, int amount) {
    return AspectMap.builder().copyOf(this).add(aspect, amount).build();
  }

  public AspectMap add(AspectMap other) {
    var builder = AspectMap.builder().copyOf(this);
    other.forEach(builder::add);
    return builder.build();
  }

  public AspectMap subtract(Aspect aspect, int amount) {
    return AspectMap.builder().copyOf(this).subtract(aspect, amount).build();
  }

  public AspectMap subtract(AspectMap other) {
    var builder = AspectMap.builder().copyOf(this);
    other.forEach(builder::subtract);
    return builder.build();
  }

  public AspectMap scale(int multiplier) {
    return AspectMap.builder().copyOf(this).scale(multiplier).build();
  }

  /**
   * Whether this AspectMap contains all aspects (in greater quantity) than another.
   * Useful for recipe checks
   */
  public boolean contains(AspectMap other) {
    if (other == AspectMap.EMPTY) return true;
    for (Aspect a : other.getMap().keySet()) if (this.get(a) < other.get(a)) return false;
    return true;
  }

  /**
   * AspectMap.map is also immutable.
   * This is read-only access to copy the map into a new one.
   */
  protected LinkedHashMap<Aspect, Integer> getMap() {
    return this.map;
  }

  /**
   * Used for rendering Aura Node layers
   */
  public List<AspectStack> toSortedList() {
    return this.map.entrySet().stream()
        .map(e -> AspectStack.Companion.of(e.getKey(), e.getValue()))
        .filter(a -> a.getAmount() > 0)
        .sorted(Comparator.comparingInt(AspectStack::getAmount))
        .toList();
  }

  public int get(Aspect aspect) {
    return this.map.getOrDefault(aspect, 0);
  }

  public ImmutableList<AspectStack> displayedAspects() {
    if (this == AspectMap.EMPTY) return ImmutableList.of();
    return Aspects.INSTANCE.getREGISTRAR().getEntries().stream().map(Supplier::get).filter(a -> this.get(a) > 0).map(a -> AspectStack.Companion.of(a, this.get(a))).collect(ImmutableList.toImmutableList());
  }

  public int size() {
    return this.map.size();
  }

  public boolean isEmpty() {
    return this == AspectMap.EMPTY || this.map.values().stream().noneMatch(i -> i > 0);
  }

  public AspectMap copy() {
    return new AspectMap(this.map);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    this.map.forEach((k, v) ->
        str.append(k).append(v)
    );
    return str.toString();
  }

  @Override
  public @NotNull Iterator<AspectStack> iterator() {
    return this.map.entrySet().stream().filter(e -> e.getValue() > 0).map(e -> AspectStack.Companion.of(e.getKey(), e.getValue())).iterator();
  }

  public static class Builder {
    private LinkedHashMap<Aspect, Integer> map = new LinkedHashMap<>();

    public Builder copyOf(AspectMap base) {
      this.map = new LinkedHashMap<>(base.getMap());
      return this;
    }

    public Builder add(Aspect aspect, int amount) {
      var oldAmount = this.map.getOrDefault(aspect, 0);
      this.map.put(aspect, oldAmount + amount);
      return this;
    }

    public Builder add(AspectStack aspectStack) {
      return this.add(aspectStack.getAspect(), aspectStack.getAmount());
    }

    public Builder subtract(Aspect aspect, int amount) {
      var oldAmount = this.map.getOrDefault(aspect, 0);
      this.map.put(aspect, oldAmount - amount);
      return this;
    }

    public Builder subtract(AspectStack aspectStack) {
      return this.subtract(aspectStack.getAspect(), aspectStack.getAmount());
    }

    public Builder scale(int multiplier) {
      this.map.forEach((k, v) -> this.map.put(k, v * multiplier));
      return this;
    }

    public AspectMap build() {
      if (this.map.isEmpty()) return AspectMap.EMPTY;
      return new AspectMap(this.map);
    }
  }
}
