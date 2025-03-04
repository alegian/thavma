package me.alegian.thavma.impl.integration

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.init.registries.T7AttributeModifiers.Revealing.GOGGLES_CURIO
import me.alegian.thavma.impl.init.registries.deferred.T7Attributes.REVEALING
import me.alegian.thavma.impl.init.registries.deferred.T7Items
import me.alegian.thavma.impl.init.registries.deferred.T7Items.DAWN_CHARM
import me.alegian.thavma.impl.init.registries.deferred.T7Items.GOGGLES
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.data.event.GatherDataEvent
import top.theillusivec4.curios.api.CuriosApi
import top.theillusivec4.curios.api.CuriosCapability
import top.theillusivec4.curios.api.CuriosDataProvider
import top.theillusivec4.curios.api.event.CurioAttributeModifierEvent
import top.theillusivec4.curios.api.event.CurioChangeEvent
import top.theillusivec4.curios.api.type.capability.ICurio
import java.util.concurrent.CompletableFuture
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS as KFF_GAME_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS as KFF_MOD_BUS

private fun curioAttributeModifiers(event: CurioAttributeModifierEvent) {
  if (event.itemStack.item == T7Items.GOGGLES_CURIO.get())
    event.addModifier(REVEALING, GOGGLES_CURIO)
}

private fun curioChange(event: CurioChangeEvent) {
  if (event.to.item == DAWN_CHARM.get())
    event.entity.removeEffect(MobEffects.DARKNESS)
}

private fun registerCapabilities(event: RegisterCapabilitiesEvent) {
  event.registerItem(
    CuriosCapability.ITEM,
    { itemStack, _ -> SimpleCurio(itemStack) },
    GOGGLES, DAWN_CHARM
  )
}

private fun gatherData(event: GatherDataEvent) {
  event.generator.addProvider(event.includeServer(), CuriosDataProvider(event.generator.packOutput, event.existingFileHelper, event.lookupProvider))
  event.generator.addProvider(event.includeServer(), CuriosItemTagProvider(event.generator.packOutput, event.lookupProvider, CompletableFuture(), event.existingFileHelper))
}

fun registerCuriosEvents() {
  KFF_GAME_BUS.addListener(::curioAttributeModifiers)
  KFF_GAME_BUS.addListener(::curioChange)
  KFF_MOD_BUS.addListener(::registerCapabilities)
  KFF_MOD_BUS.addListener(::gatherData)
}

object CuriosTags {
  val HEAD = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(CuriosApi.MODID, "head"))
  val CHARM = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(CuriosApi.MODID, "charm"))
}

private class CuriosDataProvider(output: PackOutput, fileHelper: ExistingFileHelper, lookupProvider: CompletableFuture<HolderLookup.Provider>) : CuriosDataProvider(Thavma.MODID, output, fileHelper, lookupProvider) {
  override fun generate(registries: HolderLookup.Provider, fileHelper: ExistingFileHelper) {
    createEntities(Thavma.MODID + "_player").addPlayer().addSlots(
      CuriosTags.HEAD.location.path,
      CuriosTags.CHARM.location.path
    )
  }
}

private class CuriosItemTagProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, blockTags: CompletableFuture<TagLookup<Block>>, existingFileHelper: ExistingFileHelper) : ItemTagsProvider(output, lookupProvider, blockTags, Thavma.MODID, existingFileHelper) {
  override fun addTags(pProvider: HolderLookup.Provider) {
    tag(CuriosTags.HEAD).add(T7Items.GOGGLES_CURIO.get())
    tag(CuriosTags.CHARM).add(DAWN_CHARM.get())
  }
}


class SimpleCurio(private val stack: ItemStack) : ICurio {
  override fun getStack(): ItemStack {
    return stack
  }
}