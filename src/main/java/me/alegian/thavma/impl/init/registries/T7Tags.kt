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
    val WAND_HANDLE = TagKey.create(Registries.ITEM, rl("wand_handle"))
    val WAND_CORE = TagKey.create(Registries.ITEM, rl("wand_core"))
    val SHARD = TagKey.create(Registries.ITEM, rl("shard"))
    val CATALYST = TagKey.create(Registries.ITEM, rl("catalyst"))
    val GOGGLES = TagKey.create(Registries.ITEM, rl("goggles"))
  }

  object Blocks {
    val INFUSED_STONES = TagKey.create(Registries.BLOCK, rl("infused_stones"))
    val CRUCIBLE_HEAT_SOURCE = TagKey.create(Registries.BLOCK, rl("crucible_heat_source"))
  }

  object Fluids {
    val CRUCIBLE_HEAT_SOURCE = TagKey.create(Registries.FLUID, rl("crucible_heat_source"))
  }
}
