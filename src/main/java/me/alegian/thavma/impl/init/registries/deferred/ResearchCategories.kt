package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import me.alegian.thavma.impl.rl
import net.minecraft.resources.ResourceKey

object ResearchCategories {
  val TEST_CATEGORY = ResourceKey.create(T7DatapackRegistries.RESEARCH_CATEGORY, rl("test_category"))
  val SECOND = ResourceKey.create(T7DatapackRegistries.RESEARCH_CATEGORY, rl("second"))
}