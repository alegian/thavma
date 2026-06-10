package me.alegian.thavma.impl.client.gui.layer

import net.minecraft.Util
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.FormattedCharSequence
import net.minecraft.world.entity.player.Player
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
object PlayerNotifications {

  const val DISPLAY_DURATION_MS = 3_000L
  const val FADE_IN_DURATION_MS = 3_000L // originally 750L
  const val MAX_VISIBLE = 5

  data class Notification(
    val text: Component,
    val player: LocalPlayer,
    val image: ResourceLocation? = null,
    val color: Int = 0xFFFFFF,   // RGB, used for text and icon tint
    val scale: Float = 0.5f,  // CHANGEABLE scale relative to base Minecraft font
    val expire: Long,
    val created: Long
  )

  data class FormattedCharSeqNotification(
    val text: FormattedCharSequence,
    val player: LocalPlayer,
    val image: ResourceLocation? = null,
    val color: Int = 0xFFFFFF,   // RGB, used for text and icon tint
    val scale: Float = 0.5f,  // CHANGEABLE scale relative to base Minecraft font
    val expire: Long,
    val created: Long
  )

  private var queue = mutableListOf<Notification>()
  private var formattedQueue = mutableListOf<List<FormattedCharSeqNotification>>()

  fun add(
    text: Component,
    player: LocalPlayer,
    image: ResourceLocation? = null,
    color: Int = 0xFFFFFF,
    scale: Float = 0.5f
  ) {
    val now = Util.getMillis()
    // First notification gets a head-start so it doesn't fade immediately
    val bonus = if (queue.isEmpty()) DISPLAY_DURATION_MS / 2 else 0L
    queue += Notification(
      text = text,
      player = player,
      image = image,
      color = color,
      scale = scale,
      expire = now + DISPLAY_DURATION_MS + bonus,
      created = now + FADE_IN_DURATION_MS
    )
  }

  fun addFormatted(
    notifs: List<FormattedCharSequence>,
    template: Notification,
  ) {
    val formattedList = mutableListOf<FormattedCharSeqNotification>()
    val now = Util.getMillis()
    // First notification gets a head-start so it doesn't fade immediately
    val bonus = if (formattedQueue.isEmpty()) DISPLAY_DURATION_MS / 2 else 0L
    notifs.forEachIndexed { index, notif ->
      formattedList += FormattedCharSeqNotification(
        text = notif,
        player = template.player,
        image = if (index == 0) template.image else null,
        color = template.color,
        scale = template.scale,
        expire = now + DISPLAY_DURATION_MS + bonus,
        created = now + FADE_IN_DURATION_MS
      )
    }
    formattedQueue += formattedList.toList()
  }

  /**
   * Called each frame. Prunes expired entries and refreshes expiry on
   * non-front entries so they don't vanish while the front one is fading.
   */
  internal fun pollActive(now: Long, player: Player): List<Notification> {
    val live = mutableListOf<Notification>()
    var front = true
    val relevantQueue = queue.filter { it.player == player }
    for (notif in relevantQueue) {
      if (notif.expire >= now) {
        live += if (front) notif else notif.copy(expire = now + DISPLAY_DURATION_MS)
      }
      front = false
    }
    //queue.clear()
    queue = queue.filter { it.player != player } as MutableList<Notification>
    queue += live
    return live
  }

  internal fun pollActiveFormatted(now: Long, player: Player): List<List<FormattedCharSeqNotification>> {
    val live = mutableListOf<List<FormattedCharSeqNotification>>()
    var front = true
    val buffer = mutableListOf<FormattedCharSeqNotification>()
    //val relevantQueue = formattedQueue.mapIndexed { index, list -> index to list }.filter { it.second.first().player == player }
    val relevantQueue = formattedQueue.filter { it.first().player == player }
    for (list in relevantQueue) {
      if (list.first().expire >= now) {
        for (notif in list) {
          buffer += if (front) notif else notif.copy(expire = now + DISPLAY_DURATION_MS)
        }
        live += buffer.toList()
        buffer.clear()
      }
      front = false
    }
    //formattedQueue.clear()
    formattedQueue =
      formattedQueue.filter { it.first().player != player } as MutableList<List<FormattedCharSeqNotification>>
    formattedQueue += live
    return live
  }
}