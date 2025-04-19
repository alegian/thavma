package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import me.alegian.thavma.impl.rl
import net.minecraft.resources.ResourceKey

object ResearchEntries {
  val WELCOME = ResourceKey.create(T7DatapackRegistries.RESEARCH_ENTRY, rl("welcome"))
  val OCULUS = ResourceKey.create(T7DatapackRegistries.RESEARCH_ENTRY, rl("oculus"))

  val SECOND_TAB_ENTRY = ResourceKey.create(T7DatapackRegistries.RESEARCH_ENTRY, rl("second_tab_entry"))
}