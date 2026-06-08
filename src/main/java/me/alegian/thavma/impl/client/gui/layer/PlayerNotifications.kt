package me.alegian.thavma.impl.client.gui.layer

import net.minecraft.Util
import net.minecraft.resources.ResourceLocation

object PlayerNotifications {

  const val DISPLAY_DURATION_MS = 3_000L
  const val FADE_IN_DURATION_MS = 750L
  const val MAX_VISIBLE = 5

  data class Notification(
    val text: String,
    val image: ResourceLocation? = null,
    val color: Int = 0xFFFFFF,   // RGB, used for text and icon tint
    val fontSize: Float = 0.5f,  // scale relative to base Minecraft font
    val expire: Long,
    val created: Long
  )

  private val queue = ArrayDeque<Notification>()

  fun add(
    text: String,
    image: ResourceLocation? = null,
    color: Int = 0xFFFFFF,
    fontSize: Float = 0.5f
  ) {
    val now = Util.getMillis()
    // First notification gets a head-start so it doesn't fade immediately
    val bonus = if (queue.isEmpty()) DISPLAY_DURATION_MS / 2 else 0L
    queue += Notification(
      text = text,
      image = image,
      color = color,
      fontSize = fontSize,
      expire = now + DISPLAY_DURATION_MS + bonus,
      created = now + FADE_IN_DURATION_MS
    )
  }

  /**
   * Called each frame. Prunes expired entries and refreshes expiry on
   * non-front entries so they don't vanish while the front one is fading.
   */
  internal fun pollActive(now: Long): List<Notification> {
    val live = mutableListOf<Notification>()
    var front = true
    for (n in queue) {
      if (n.expire >= now) {
        live += if (front) n else n.copy(expire = now + DISPLAY_DURATION_MS)
      }
      front = false
    }
    queue.clear()
    queue += live
    return live
  }
}