package me.alegian.thavma.impl.client

import me.alegian.thavma.impl.client.gui.toast.ResearchToast
import me.alegian.thavma.impl.common.item.WandItem
import me.alegian.thavma.impl.common.research.ResearchEntry
import me.alegian.thavma.impl.init.registries.deferred.T7Attributes.REVEALING
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.EquipmentSlot
import kotlin.jvm.optionals.getOrNull

fun clientPlayerHasRevealing() =
  Minecraft.getInstance().player?.getAttribute(REVEALING)?.value == 1.0

fun clientPlayerHoldingWand() =
  Minecraft.getInstance().player?.mainHandItem?.item is WandItem

fun getClientPlayerEquipmentItem(slot: EquipmentSlot) =
  Minecraft.getInstance().player?.getItemBySlot(slot)?.item

fun setScreen(screen: Screen) =
  Minecraft.getInstance().setScreen(screen)

fun <T> clientRegistry(key: ResourceKey<Registry<T>>) =
  Minecraft.getInstance().connection?.registryAccess()?.registry(key)?.getOrNull()

fun pushScreen(screen: Screen) = Minecraft.getInstance().pushGuiLayer(screen)

fun researchToast(entry: ResearchEntry) =
  Minecraft.getInstance().toasts.addToast(ResearchToast(entry))

fun clientSound(sound: SoundEvent, source: SoundSource, volume: Float, pitch: Float) {
  val level = Minecraft.getInstance().level ?: return
  val player = Minecraft.getInstance().player ?: return

  level.playSound(player, player.blockPosition(), sound, source, volume, pitch)
}