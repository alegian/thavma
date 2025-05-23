package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.common.research.ResearchCategory
import me.alegian.thavma.impl.common.research.ResearchEntry
import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import net.minecraft.resources.ResourceKey

object ResearchEntries {
  // used in datagen
  val CATEGORIES = mutableMapOf<ResourceKey<ResearchEntry>, ResourceKey<ResearchCategory>>()

  object Thavma {
    val THAVMA = register("thavma", ResearchCategories.THAVMA)
    val TREES = register("trees", ResearchCategories.THAVMA)
    val ORES = register("ores", ResearchCategories.THAVMA)
    val ARCANE_LENS = register("arcane_lens", ResearchCategories.THAVMA)
    val RESEARCH_TABLE = register("research_table", ResearchCategories.THAVMA)
    val ALCHEMY = register("alchemy", ResearchCategories.THAVMA)
    val WANDS = register("wands", ResearchCategories.THAVMA)
    val INFUSION = register("infusion", ResearchCategories.THAVMA)
    val TECHNOLOGY = register("technology", ResearchCategories.THAVMA)
  }

  object Alchemy {
    val ALCHEMY = register("alchemy", ResearchCategories.ALCHEMY)
  }
}

// prepend category id to entry id to avoid duplicates
private fun register(id: String, category: ResourceKey<ResearchCategory>): ResourceKey<ResearchEntry> {
  val key = ResourceKey.create(T7DatapackRegistries.RESEARCH_ENTRY, category.location().withSuffix("/$id"))
  ResearchEntries.CATEGORIES[key] = category
  return key
}