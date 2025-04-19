package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import me.alegian.thavma.impl.rl
import net.minecraft.resources.ResourceKey

object ResearchEntries {
  val THAVMA = ResourceKey.create(T7DatapackRegistries.RESEARCH_ENTRY, rl("thavma"))
  val OCULUS = ResourceKey.create(T7DatapackRegistries.RESEARCH_ENTRY, rl("oculus"))

  val ALCHEMY = ResourceKey.create(T7DatapackRegistries.RESEARCH_ENTRY, rl("alchemy"))
}