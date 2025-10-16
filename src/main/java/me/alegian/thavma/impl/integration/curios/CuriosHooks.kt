package me.alegian.thavma.impl.integration.curios

import com.google.common.collect.LinkedHashMultimap
import com.google.common.collect.Multimap
import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.init.data.providers.T7ItemTagProvider
import me.alegian.thavma.impl.init.registries.deferred.T7Attributes
import me.alegian.thavma.impl.init.registries.deferred.T7Items
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.Item
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.data.event.GatherDataEvent
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS
import top.theillusivec4.curios.api.CuriosApi
import top.theillusivec4.curios.api.CuriosCapability
import top.theillusivec4.curios.api.SlotContext
import top.theillusivec4.curios.api.event.CurioChangeEvent
import top.theillusivec4.curios.api.type.capability.ICurio
import java.util.concurrent.CompletableFuture
import kotlin.jvm.optionals.getOrNull

/**
 * All direct references to Curios classes should be here
 */
internal class CuriosHooks : CuriosIntegration() {
  init {
    FORGE_BUS.addListener(::curioChange)
  }

  object Tags {
    val HEAD = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(CuriosApi.MODID, "head"))
    val CHARM = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(CuriosApi.MODID, "charm"))
  }

  fun curioChange(event: CurioChangeEvent) {
    if (event.to.item == T7Items.DAWN_CHARM.get())
      event.entity.removeEffect(MobEffects.DARKNESS)
  }

  override fun registerCapabilities(event: RegisterCapabilitiesEvent) {
    event.registerItem(
      CuriosCapability.ITEM,
      { stack, _ ->
        object : ICurio {
          override fun getStack() = stack
          override fun getAttributeModifiers(slotContext: SlotContext, id: ResourceLocation): Multimap<Holder<Attribute>, AttributeModifier> {
            val map = LinkedHashMultimap.create<Holder<Attribute>, AttributeModifier>()
            CuriosApi.addSlotModifier(map, Tags.HEAD.location().path, id, 1.0, AttributeModifier.Operation.ADD_VALUE)
            map.put(T7Attributes.REVEALING, AttributeModifier(id, 1.0, AttributeModifier.Operation.ADD_VALUE))
            return map
          }
        }
      },
      T7Items.GOGGLES_CURIO,
    )
    event.registerItem(CuriosCapability.ITEM, { stack, _ -> ICurio { stack } }, T7Items.DAWN_CHARM)
  }

  override fun gatherData(event: GatherDataEvent) {
    event.generator.addProvider(event.includeServer(), CuriosDataProvider(event.generator.packOutput, event.existingFileHelper, event.lookupProvider))
  }

  override fun isWearingCurio(entity: LivingEntity, item: Item): Boolean {
    return CuriosApi.getCuriosInventory(entity).getOrNull()?.isEquipped(item) ?: false
  }

  override fun addTags(provider: T7ItemTagProvider, lookupProvider: HolderLookup.Provider) {
    provider.tag(Tags.HEAD).add(T7Items.GOGGLES_CURIO.get())
    provider.tag(Tags.CHARM).add(T7Items.DAWN_CHARM.get())
  }

  private class CuriosDataProvider(output: PackOutput, fileHelper: ExistingFileHelper, lookupProvider: CompletableFuture<HolderLookup.Provider>) : top.theillusivec4.curios.api.CuriosDataProvider(Thavma.MODID, output, fileHelper, lookupProvider) {
    override fun generate(registries: HolderLookup.Provider, fileHelper: ExistingFileHelper) {
      createEntities(Thavma.MODID + "_player").addPlayer().addSlots(
        Tags.HEAD.location.path,
        Tags.CHARM.location.path
      )
    }
  }
}