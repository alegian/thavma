package me.alegian.thavma.impl.init.data.providers.aspects

import me.alegian.thavma.impl.init.data.providers.T7DataMapProvider
import me.alegian.thavma.impl.init.registries.T7DataMaps
import me.alegian.thavma.impl.init.registries.deferred.Aspects
import net.minecraft.core.HolderLookup

object AspectOpposites {
  fun gather(datamapProvider: T7DataMapProvider, lookupProvider: HolderLookup.Provider) =
    datamapProvider.builder(T7DataMaps.ASPECT_OPPOSITES).run {
      add(Aspects.ACULEUS.key, Aspects.TEXTURA.get(), false)
      add(Aspects.AER.key, Aspects.TERRA.get(), false)
      add(Aspects.ALIENIS.key, Aspects.COGNITIO.get(), false)
      add(Aspects.AQUA.key, Aspects.IGNIS.get(), false)
      add(Aspects.ARBOR.key, Aspects.CORPUS.get(), false)
      add(Aspects.CRESCENTIA.key, Aspects.ORBIS.get(), false)
      add(Aspects.DENSITAS.key, Aspects.PULVIS.get(), false)
      add(Aspects.EXANIMIS.key, Aspects.VITIUM.get(), false)
      add(Aspects.FABRICO.key, Aspects.METALLUM.get(), false)
      add(Aspects.FAMES.key, Aspects.MESSIS.get(), false)
      add(Aspects.HERBA.key, Aspects.LUCRUM.get(), false)
      add(Aspects.HUMANUS.key, Aspects.MACHINA.get(), false)
      add(Aspects.IDEA.key, Aspects.VACUOS.get(), false)
      add(Aspects.IMPETUS.key, Aspects.TUTAMEN.get(), false)
      add(Aspects.INSTRUMENTUM.key, Aspects.SOMNIUM.get(), false)
      add(Aspects.LUX.key, Aspects.TENEBRAE.get(), false)
      add(Aspects.MEMORIA.key, Aspects.PRAECANTATIO.get(), false)
      add(Aspects.MORTUUS.key, Aspects.VICTUS.get(), false)
      add(Aspects.MOTUS.key, Aspects.PERMUTATIO.get(), false)
      add(Aspects.ORDO.key, Aspects.PERDITIO.get(), false)
      add(Aspects.POTENTIA.key, Aspects.TEMPUS.get(), false)
      add(Aspects.ROSAURA.key, Aspects.VENENUM.get(), false)
      add(Aspects.SENSUS.key, Aspects.SPIRITUS.get(), false)
      add(Aspects.SONORUS.key, Aspects.VITREUS.get(), false)
    }
}
