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
    val ITEM = DataMapType
      .builder(
        rl("aspect_content"),
        Registries.ITEM,
        AspectMap.CODEC
      )
      .synced(AspectMap.CODEC, true)
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
}
