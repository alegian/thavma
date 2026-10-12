package me.alegian.thavma.impl.init.data.providers.aspects

import me.alegian.thavma.impl.common.aspect.Aspect
import me.alegian.thavma.impl.init.data.providers.T7DataMapProvider
import me.alegian.thavma.impl.init.registries.T7DataMaps
import me.alegian.thavma.impl.init.registries.deferred.Aspects
import net.minecraft.core.Holder
import net.neoforged.neoforge.common.data.DataMapProvider

object AspectComponents {
  fun gather(datamapProvider: T7DataMapProvider) =
    datamapProvider.builder(T7DataMaps.ASPECT_COMPONENTS).run {
      add(Aspects.LUX, listOf(Aspects.IGNIS))
      add(Aspects.VITREUS, listOf(Aspects.TERRA))
      add(Aspects.METALLUM, listOf(Aspects.TERRA, Aspects.IGNIS))
      add(Aspects.VICTUS, listOf(Aspects.TERRA, Aspects.AQUA))
      add(Aspects.HERBA, listOf(Aspects.TERRA, Aspects.AQUA))

      add(Aspects.ALKIMIA, listOf(Aspects.AQUA, Aspects.AETHER))
      add(Aspects.TENEBRAE, listOf(Aspects.AETHER))
      add(Aspects.INSTRUMENTUM, listOf(Aspects.VITREUS, Aspects.METALLUM))
      add(Aspects.FABRICO, listOf(Aspects.INSTRUMENTUM))
      add(Aspects.MACHINA, listOf(Aspects.METALLUM, Aspects.INSTRUMENTUM))
      add(Aspects.VAS, listOf(Aspects.FABRICO))
      add(Aspects.COGNITIO, listOf(Aspects.AETHER, Aspects.FABRICO))
      add(Aspects.ORNATUS, listOf(Aspects.VITREUS, Aspects.FABRICO))
      add(Aspects.HOSTILIS, listOf(Aspects.VICTUS, Aspects.TENEBRAE))
      add(Aspects.CORPUS, listOf(Aspects.VICTUS))
      add(Aspects.PRAEMUNIO, listOf(Aspects.VITREUS, Aspects.METALLUM, Aspects.FABRICO))
      add(Aspects.CIVILIS, listOf(Aspects.VICTUS, Aspects.COGNITIO, Aspects.ORNATUS))
    }

  private fun DataMapProvider.Builder<MutableSet<Aspect>, Aspect>.add(
    aspect: Holder<Aspect>,
    components: List<Holder<Aspect>>
  ) =
    add(aspect, components.map { it.value() }.toMutableSet(), false)
}
