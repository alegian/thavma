package me.alegian.thavma.impl.common.event

import me.alegian.thavma.impl.common.data.capability.SimpleCurio
import me.alegian.thavma.impl.init.registries.T7AttributeModifiers.Revealing.GOGGLES_CURIO
import me.alegian.thavma.impl.init.registries.deferred.T7Attributes.REVEALING
import me.alegian.thavma.impl.init.registries.deferred.T7Items
import me.alegian.thavma.impl.init.registries.deferred.T7Items.DAWN_CHARM
import me.alegian.thavma.impl.init.registries.deferred.T7Items.GOGGLES
import net.minecraft.world.effect.MobEffects
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import top.theillusivec4.curios.api.CuriosCapability
import top.theillusivec4.curios.api.event.CurioAttributeModifierEvent
import top.theillusivec4.curios.api.event.CurioChangeEvent
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

fun registerCuriosEvents() {
  KFF_GAME_BUS.addListener(::curioAttributeModifiers)
  KFF_GAME_BUS.addListener(::curioChange)
  KFF_MOD_BUS.addListener(::registerCapabilities)
}