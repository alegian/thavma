package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.common.research.ResearchCategory
import me.alegian.thavma.impl.common.research.ResearchEntry
import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import net.minecraft.resources.ResourceKey

object ResearchEntries {
  // used in datagen
  val CATEGORIES = mutableMapOf<ResourceKey<ResearchEntry>, ResourceKey<ResearchCategory>>()

  val THAVMA = register("thavma", ResearchCategories.THAVMA)
  val OCULUS = register("oculus", ResearchCategories.THAVMA)

  val ALCHEMY = register("alchemy", ResearchCategories.ALCHEMY)
}

// prepend category id to entry id to avoid duplicates
private fun register(id: String, category: ResourceKey<ResearchCategory>): ResourceKey<ResearchEntry> {
  val key = ResourceKey.create(T7DatapackRegistries.RESEARCH_ENTRY, category.location().withSuffix("/$id"))
  ResearchEntries.CATEGORIES[key] = category
  return key
}