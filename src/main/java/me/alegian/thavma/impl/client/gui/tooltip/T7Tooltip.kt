package me.alegian.thavma.impl.client.gui.tooltip

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.network.chat.Component
import net.minecraft.util.FormattedCharSequence

/**
 * Allows multiple lines of text
 */
class T7Tooltip(vararg components: Component): Tooltip(components.first(), components.first()) {
  val cache by lazy{
    val tooltip = mutableListOf<FormattedCharSequence>()
    for (component in components) {
      tooltip.addAll(splitTooltip(Minecraft.getInstance(), component))
    }
    tooltip
  }

  override fun toCharSequence(minecraft: Minecraft): MutableList<FormattedCharSequence?> {
    return cache.toMutableList()
  }
}