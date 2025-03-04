package me.alegian.thavma.impl

import me.alegian.thavma.impl.client.event.registerClientGameEvents
import me.alegian.thavma.impl.client.event.registerClientModEvents
import me.alegian.thavma.impl.common.event.registerCommonGameEvents
import me.alegian.thavma.impl.common.event.registerCommonModEvents
import me.alegian.thavma.impl.init.registries.deferred.*
import me.alegian.thavma.impl.integration.CuriosIntegration
import net.minecraft.resources.ResourceLocation
import net.neoforged.fml.common.Mod
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS as KFF_MOD_BUS

// must match value in META-INF/mods.toml
@Mod(Thavma.MODID)
object Thavma {
  const val MODID: String = "thavma"

  init {
    T7ArmorMaterials.REGISTRAR.register(KFF_MOD_BUS)
    T7Blocks.REGISTRAR.register(KFF_MOD_BUS)
    T7Items.REGISTRAR.register(KFF_MOD_BUS)
    T7BlockEntities.REGISTRAR.register(KFF_MOD_BUS)
    T7EntityTypes.REGISTRAR.register(KFF_MOD_BUS)
    T7Attachments.REGISTRAR.register(KFF_MOD_BUS)
    T7DataComponents.REGISTRAR.register(KFF_MOD_BUS)
    T7ParticleTypes.REGISTRAR.register(KFF_MOD_BUS)
    T7CreativeModeTabs.REGISTRAR.register(KFF_MOD_BUS)
    T7TrunkPlacerTypes.REGISTRAR.register(KFF_MOD_BUS)
    T7MenuTypes.REGISTRAR.register(KFF_MOD_BUS)
    T7RecipeTypes.REGISTRAR.register(KFF_MOD_BUS)
    T7RecipeSerializers.REGISTRAR.register(KFF_MOD_BUS)
    WandCoreMaterials.REGISTRAR.register(KFF_MOD_BUS)
    WandHandleMaterials.REGISTRAR.register(KFF_MOD_BUS)
    Aspects.REGISTRAR.register(KFF_MOD_BUS)
    Researches.REGISTRAR.register(KFF_MOD_BUS)
    T7Attributes.REGISTRAR.register(KFF_MOD_BUS)
    T7GlobalLootModifierSerializers.REGISTRAR.register(KFF_MOD_BUS)

    registerCommonModEvents()
    registerClientModEvents()
    registerCommonGameEvents()
    registerClientGameEvents()
    
    CuriosIntegration.init()
  }

  fun rl(path: String): ResourceLocation {
    return ResourceLocation.fromNamespaceAndPath(MODID, path)
  }
}
