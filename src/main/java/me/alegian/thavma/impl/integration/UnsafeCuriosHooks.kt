package me.alegian.thavma.impl.integration

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.init.data.providers.T7ItemTagProvider
import me.alegian.thavma.impl.init.registries.T7AttributeModifiers.Revealing.GOGGLES_CURIO
import me.alegian.thavma.impl.init.registries.deferred.T7Attributes.REVEALING
import me.alegian.thavma.impl.init.registries.deferred.T7Items
import me.alegian.thavma.impl.init.registries.deferred.T7Items.DAWN_CHARM
import me.alegian.thavma.impl.init.registries.deferred.T7Items.GOGGLES
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.data.event.GatherDataEvent
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS
import top.theillusivec4.curios.api.CuriosApi
import top.theillusivec4.curios.api.CuriosCapability
import top.theillusivec4.curios.api.event.CurioAttributeModifierEvent
import top.theillusivec4.curios.api.event.CurioChangeEvent
import top.theillusivec4.curios.api.type.capability.ICurio
import java.util.concurrent.CompletableFuture
import kotlin.jvm.optionals.getOrNull

internal class UnsafeCuriosHooks : CuriosIntegration() {
  init {
    FORGE_BUS.addListener(::curioAttributeModifiers)
    FORGE_BUS.addListener(::curioChange)
  }

  fun curioAttributeModifiers(event: CurioAttributeModifierEvent) {
    if (event.itemStack.item == T7Items.GOGGLES_CURIO.get())
      event.addModifier(REVEALING, GOGGLES_CURIO)
  }

  fun curioChange(event: CurioChangeEvent) {
    if (event.to.item == DAWN_CHARM.get())
      event.entity.removeEffect(MobEffects.DARKNESS)
  }

  override fun registerCapabilities(event: RegisterCapabilitiesEvent) {
    event.registerItem(
      CuriosCapability.ITEM,
      { itemStack, _ -> SimpleCurio(itemStack) },
      GOGGLES, DAWN_CHARM
    )
  }

  override fun gatherData(event: GatherDataEvent) {
    event.generator.addProvider(event.includeServer(), CuriosDataProvider(event.generator.packOutput, event.existingFileHelper, event.lookupProvider))
  }

  override fun isWearingCurio(entity: LivingEntity, item: Item): Boolean {
    return CuriosApi.getCuriosInventory(entity).getOrNull()?.isEquipped(item) ?: false
  }

  object Tags {
    val HEAD = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(CuriosApi.MODID, "head"))
    val CHARM = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(CuriosApi.MODID, "charm"))
  }

  private class CuriosDataProvider(output: PackOutput, fileHelper: ExistingFileHelper, lookupProvider: CompletableFuture<HolderLookup.Provider>) : top.theillusivec4.curios.api.CuriosDataProvider(Thavma.MODID, output, fileHelper, lookupProvider) {
    override fun generate(registries: HolderLookup.Provider, fileHelper: ExistingFileHelper) {
      createEntities(Thavma.MODID + "_player").addPlayer().addSlots(
        Tags.HEAD.location.path,
        Tags.CHARM.location.path
      )
    }
  }

  override fun addTags(provider: T7ItemTagProvider, lookupProvider: HolderLookup.Provider) {
    provider.tag(Tags.HEAD).add(T7Items.GOGGLES_CURIO.get())
    provider.tag(Tags.CHARM).add(DAWN_CHARM.get())
  }

  private class SimpleCurio(private val stack: ItemStack) : ICurio {
    override fun getStack(): ItemStack {
      return stack
    }
  }
}