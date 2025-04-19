package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import me.alegian.thavma.impl.rl
import net.minecraft.resources.ResourceKey

object ResearchCategories {
  val INTRODUCTION = ResourceKey.create(T7DatapackRegistries.RESEARCH_CATEGORY, rl("introduction"))
  val ALCHEMY = ResourceKey.create(T7DatapackRegistries.RESEARCH_CATEGORY, rl("alchemy"))
}