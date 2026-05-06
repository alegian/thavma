package me.alegian.thavma.impl.init.registries

import me.alegian.thavma.impl.common.aspect.Aspect
import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.rl
import net.minecraft.core.registries.Registries
import net.neoforged.neoforge.registries.datamaps.AdvancedDataMapType
import net.neoforged.neoforge.registries.datamaps.DataMapType
import net.neoforged.neoforge.registries.datamaps.DataMapValueMerger

object T7DataMaps {
  object AspectContent {
    val ITEM = AdvancedDataMapType
      .builder(
        rl("aspect_content"),
        Registries.ITEM,
        AspectMap.CODEC
      )
      .synced(AspectMap.CODEC, true)
      .merger(hierarchicalMerger())
      .build()

    val ENTITY = DataMapType
      .builder(
        rl("aspect_content"),
        Registries.ENTITY_TYPE,
        AspectMap.CODEC
      )
      .synced(AspectMap.CODEC, true)
      .build()
  }

  val ASPECT_RELATIONS = AdvancedDataMapType
    .builder(
      rl("aspect_relations"),
      T7Registries.ASPECT_KEY,
      Aspect.CODEC.listOf()
    )
    .synced(Aspect.CODEC.listOf(), true)
    .merger(DataMapValueMerger.listMerger())
    .build()

  // prioritizes resourcekeys before tags
  fun <T, R> hierarchicalMerger(): DataMapValueMerger<R, T> {
    return DataMapValueMerger<R, T> { _, first, firstValue, second, secondValue ->
      if (second.right().isPresent) secondValue
      else if (first.right().isPresent) firstValue
      else secondValue
    }
  }
}
