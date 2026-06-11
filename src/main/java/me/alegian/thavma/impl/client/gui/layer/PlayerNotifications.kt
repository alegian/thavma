package me.alegian.thavma.impl.client.gui.layer

import net.minecraft.Util
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
object PlayerNotifications {

  const val MAX_VISIBLE_PLAIN = 5
  const val MAX_VISIBLE_VOICE = 5
  const val FONT_SIZE = 0.35f

  data class Notification(
    val isVoice: Boolean,
    val text: Component,
    val player: LocalPlayer,
    val image: ResourceLocation? = null,
    val color: Int = 0xFFFFFF,
    val scale: Float = FONT_SIZE,
    val addedTime: Long          // Util.getMillis() at insertion — used for per-notification fade-in
  )

  private val queue = mutableListOf<Notification>()

  fun add(
    isVoice: Boolean,
    text: Component,
    player: LocalPlayer,
    image: ResourceLocation? = null,
    color: Int = 0xFFFFFF,
    scale: Float = FONT_SIZE
  ) {
    queue += Notification(
      isVoice = isVoice,
      text = text,
      player = player,
      image = image,
      color = color,
      scale = scale,
      addedTime = Util.getMillis()
    )
  }

  /** Non-mutating read — NotificationLayer decides when to clear. */
  internal fun getForPlayer(player: Player): List<Notification> =
    queue.filter { it.player == player }

  /** Called by NotificationLayer once the scroll-out animation completes. */
  internal fun clearForPlayer(player: Player, isVoice: Boolean) {
    queue.removeAll { it.player == player && it.isVoice == isVoice }
  }
}