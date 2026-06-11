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

  const val MAX_VISIBLE_REG = 20
  const val MAX_VISIBLE_PRIO = 3
  const val FONT_SIZE = 0.35f

  data class Notification(
    val isPriority: Boolean,
    val text: Component,
    val player: LocalPlayer,
    val image: ResourceLocation? = null,
    val color: Int = 0xFFFFFF,
    val scale: Float = FONT_SIZE,
    val addedTime: Long          // Util.getMillis() at insertion — used for per-notification fade-in
  )

  private val queue = mutableListOf<Notification>()

  fun add(
    isPriority: Boolean,
    text: Component,
    player: LocalPlayer,
    image: ResourceLocation? = null,
    color: Int = 0xFFFFFF,
    scale: Float = FONT_SIZE
  ) {
    queue += Notification(
      isPriority = isPriority,
      text = text,
      player = player,
      image = image,
      color = color,
      scale = scale,
      addedTime = Util.getMillis()
    )
  }

  /** Non-mutating read — RegularNotifLayer decides when to clear. */
  internal fun getForPlayer(player: Player): List<Notification> =
    queue.filter { it.player == player }

  /** Called by RegularNotifLayer once the scroll-out animation completes. */
  internal fun clearForPlayer(player: Player, isPriority: Boolean) {
    queue.removeAll { it.player == player && it.isPriority == isPriority }
  }
}