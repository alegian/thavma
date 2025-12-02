package me.alegian.thavma.impl.init.data.providers.aspects

import me.alegian.thavma.impl.common.aspect.Aspect
import me.alegian.thavma.impl.init.data.providers.T7DataMapProvider
import me.alegian.thavma.impl.init.registries.T7DataMaps
import me.alegian.thavma.impl.init.registries.deferred.Aspects
import net.minecraft.core.Holder
import net.neoforged.neoforge.common.data.DataMapProvider

object AspectRelations {
  fun gather(datamapProvider: T7DataMapProvider) =
    datamapProvider.builder(T7DataMaps.ASPECT_RELATIONS).run {
      add(Aspects.TERRA, listOf(Aspects.VITREUS, Aspects.METALLUM, Aspects.HERBA, Aspects.VICTUS))
      add(Aspects.IGNIS, listOf(Aspects.METALLUM, Aspects.LUX))
      add(Aspects.AQUA, listOf(Aspects.HERBA, Aspects.VICTUS, Aspects.ALKIMIA))
      add(Aspects.AETHER, listOf(Aspects.ALKIMIA, Aspects.COGNITIO, Aspects.TENEBRAE))

      add(Aspects.LUX, listOf())
      add(Aspects.VITREUS, listOf(Aspects.ORNATUS, Aspects.INSTRUMENTUM, Aspects.PRAEMUNIO))
      add(Aspects.METALLUM, listOf(Aspects.METALLUM, Aspects.PRAEMUNIO))
      add(Aspects.VICTUS, listOf(Aspects.HOSTILIS, Aspects.CIVILIS, Aspects.CORPUS))
      add(Aspects.HERBA, listOf())

      add(Aspects.ALKIMIA, listOf())
      add(Aspects.TENEBRAE, listOf())
      add(Aspects.INSTRUMENTUM, listOf(Aspects.FABRICO, Aspects.MACHINA))
      add(Aspects.FABRICO, listOf(Aspects.COGNITIO, Aspects.VAS, Aspects.ORNATUS))
      add(Aspects.MACHINA, listOf())
      add(Aspects.VAS, listOf())
      add(Aspects.COGNITIO, listOf(Aspects.CIVILIS))
      add(Aspects.ORNATUS, listOf(Aspects.CIVILIS))
      add(Aspects.HOSTILIS, listOf())
      add(Aspects.CORPUS, listOf())
      add(Aspects.PRAEMUNIO, listOf())
      add(Aspects.CIVILIS, listOf())
    }

  private fun DataMapProvider.Builder<List<Aspect>, Aspect>.add(aspect: Holder<Aspect>, relations: List<Holder<Aspect>>) =
    add(aspect, relations.map { it.value() }, false)
}