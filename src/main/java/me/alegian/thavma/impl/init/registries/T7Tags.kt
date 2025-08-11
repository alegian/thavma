package me.alegian.thavma.impl.init.registries

import me.alegian.thavma.impl.rl
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey

object T7Tags {
  object DamageTypes {
    val SONIC = TagKey.create(Registries.DAMAGE_TYPE, rl("sonic"))
  }

  object Items {
    val INFUSED_STONES = TagKey.create(Registries.ITEM, rl("infused_stones"))
    val WAND_HANDLES = TagKey.create(Registries.ITEM, rl("wand_handles"))
    val WAND_CORES = TagKey.create(Registries.ITEM, rl("wand_cores"))
    val SHARDS = TagKey.create(Registries.ITEM, rl("shards"))
    val CATALYSTS = TagKey.create(Registries.ITEM, rl("catalysts"))
    val GOGGLES = TagKey.create(Registries.ITEM, rl("goggles"))
    val STEP_HEIGHT = TagKey.create(Registries.ITEM, rl("step_height"))
    val FOCI = TagKey.create(Registries.ITEM, rl("foci"))
  }

  object Blocks {
    val INFUSED_STONES = TagKey.create(Registries.BLOCK, rl("infused_stones"))
    val CRUCIBLE_HEAT_SOURCES = TagKey.create(Registries.BLOCK, rl("crucible_heat_sources"))
  }

  object Fluids {
    val CRUCIBLE_HEAT_SOURCES = TagKey.create(Registries.FLUID, rl("crucible_heat_sources"))
  }
}
