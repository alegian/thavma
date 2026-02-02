package me.alegian.thavma.impl.init.data.providers.aspects

import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.common.item.itemResourceKey
import me.alegian.thavma.impl.init.registries.deferred.Aspects
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.data.DataMapProvider
import java.util.function.UnaryOperator

class AspectGen(val aspects: AspectMap) {
  constructor() : this(AspectMap())

  fun mutate(mutation: UnaryOperator<AspectMap>) =
    AspectGen(mutation.apply(aspects))

  fun add(other: AspectGen): AspectGen {
    return AspectGen(aspects.add(other.aspects))
  }

  fun <T> save(builder: DataMapProvider.Builder<AspectMap, T>, tag: TagKey<T>) {
    builder.add(tag, aspects, false)
  }

  fun <T> save(builder: DataMapProvider.Builder<AspectMap, T>, key: ResourceKey<T>) {
    builder.add(key, aspects, false)
  }

  fun save(builder: DataMapProvider.Builder<AspectMap, Item>, itemLike: ItemLike) =
    save(builder, itemLike.itemResourceKey)
}

object Mutations {
  val ORE = { it: AspectMap ->
    it.add(Aspects.TERRA, 2)
  }
  val RAW = { it: AspectMap ->
    it.scale(0.5).add(Aspects.TERRA, 2)
  }
  val STORAGE_BLOCK_4 = { it: AspectMap ->
    it.scale(4)
  }
  val STORAGE_BLOCK_9 = { it: AspectMap ->
    it.scale(9)
  }
  val PLATING = { it: AspectMap ->
    it.scale(2)
  }

  val BUTTON = { it: AspectMap -> it.scale(1) }
  val CHISELED = { it: AspectMap -> it.scale(1) }
  val CRACKED = { it: AspectMap -> it.scale(1) }
  val CUT = { it: AspectMap -> it.scale(1) }
  val MOSAIC = { it: AspectMap -> it.scale(1) }
  val STAIRS = { it: AspectMap -> it.scale(1) }
  val POLISHED = { it: AspectMap -> it.scale(1) }
  val WALL = { it: AspectMap -> it.scale(1) }
  val DOOR = { it: AspectMap -> it.scale(2) }
  val SIGN = { it: AspectMap -> it.scale(2) }
  val PRESSURE_PLATE = { it: AspectMap -> it.scale(2) }
  val WALL_SIGN = { it: AspectMap -> it.scale(2) }
  val FENCE = { it: AspectMap -> it.scale(1.5) }
  val FENCE_GATE = { it: AspectMap -> it.scale(4) }
  val SLAB = { it: AspectMap -> it.scale(0.5) }
  val TRAPDOOR = { it: AspectMap -> it.scale(3) }

  fun helmet(praemunio: Int) = { it: AspectMap ->
    it.scale(5).add(Aspects.PRAEMUNIO, praemunio)
  }

  fun chestplate(praemunio: Int) = { it: AspectMap ->
    it.scale(8).add(Aspects.PRAEMUNIO, praemunio)
  }

  fun leggings(praemunio: Int) = { it: AspectMap ->
    it.scale(7).add(Aspects.PRAEMUNIO, praemunio)
  }

  fun boots(praemunio: Int) = { it: AspectMap ->
    it.scale(4).add(Aspects.PRAEMUNIO, praemunio)
  }

  fun horseArmor(praemunio: Int) = { it: AspectMap ->
    it.scale(7).add(Aspects.PRAEMUNIO, praemunio)
  }

  fun wolfArmor(praemunio: Int) = { it: AspectMap ->
    it.scale(6).add(Aspects.PRAEMUNIO, praemunio)
  }

  fun sword(stick: AspectGen, instrumentum: Int) = { it: AspectMap ->
    it.scale(2).add(stick.aspects).add(Aspects.INSTRUMENTUM, instrumentum)
  }

  fun pickaxe(stick: AspectGen, instrumentum: Int) = { it: AspectMap ->
    it.scale(3).add(stick.aspects.scale(2)).add(Aspects.INSTRUMENTUM, instrumentum)
  }

  fun axe(stick: AspectGen, instrumentum: Int) = { it: AspectMap ->
    it.scale(3).add(stick.aspects.scale(2)).add(Aspects.INSTRUMENTUM, instrumentum)
  }

  fun hoe(stick: AspectGen, instrumentum: Int) = { it: AspectMap ->
    it.scale(2).add(stick.aspects.scale(2)).add(Aspects.INSTRUMENTUM, instrumentum)
  }

  fun shovel(stick: AspectGen, instrumentum: Int) = { it: AspectMap ->
    it.scale(1).add(stick.aspects.scale(2)).add(Aspects.INSTRUMENTUM, instrumentum)
  }

  fun hammer(stick: AspectGen, instrumentum: Int) = { it: AspectMap ->
    it.scale(6).add(stick.aspects).add(Aspects.INSTRUMENTUM, instrumentum)
  }
}
