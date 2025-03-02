package me.alegian.thavma.impl.init.registries

import me.alegian.thavma.impl.Thavma.rl
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import top.theillusivec4.curios.api.CuriosApi

object T7Tags {
  val WAND_HANDLE = TagKey.create(Registries.ITEM, rl("wand_handle"))
  val WAND_CORE = TagKey.create(Registries.ITEM, rl("wand_core"))
  val TESTA = TagKey.create(Registries.ITEM, rl("testa"))
  val CATALYST = TagKey.create(Registries.ITEM, rl("catalyst"))

  val SONIC = TagKey.create(Registries.DAMAGE_TYPE, rl("sonic"))

  object CrucibleHeatSourceTag {
    val BLOCK = TagKey.create(Registries.BLOCK, rl("crucible_heat_source"))

    val FLUID = TagKey.create(Registries.FLUID, rl("crucible_heat_source"))
  }

  object Curios {
    val HEAD = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(CuriosApi.MODID, "head"))
    val CHARM = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(CuriosApi.MODID, "charm"))
  }
}
