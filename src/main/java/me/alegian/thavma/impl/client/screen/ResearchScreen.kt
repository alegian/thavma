package me.alegian.thavma.impl.client.screen

import me.alegian.thavma.impl.client.screen.layout.*
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.common.menu.ResearchMenu
import me.alegian.thavma.impl.common.menu.slot.RuneSlot
import me.alegian.thavma.impl.common.menu.slot.ScrollSlot
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

private const val BORDER = 5
private val BG = Texture("gui/research_table/bg", 243, 166, 256, 256)
private val ASPECTS_BG = Texture("gui/research_table/aspects_bg", 72, 125, 72, 125)
private val PUZZLE_BG = Texture("gui/research_table/puzzle_bg", 156, 156, 156, 156)
private val BUTTON = Texture("gui/research_table/button", 36, 8, 36, 8)
private val CIRCLE = Texture("gui/research_table/circle", 18, 18, 18, 18)

open class ResearchScreen(val menu: ResearchMenu, pPlayerInventory: Inventory, pTitle: Component) : T7ContainerScreen<ResearchMenu>(menu, pPlayerInventory, pTitle, BG) {
  override fun layout() {
    Row({
      size = grow()
      padding = BORDER
      gap = BORDER
    }) {
      Column({
        gap = BORDER
      }) {
        Row {
          Box({
            width = fixed(RuneSlot.TEXTURE.width)
            height = fixed(RuneSlot.TEXTURE.height)
          }) {
            addRenderableOnly(slot(menu.runeContainer.range.slot, RuneSlot.TEXTURE))
          }

          Box({
            width = fixed(ScrollSlot.TEXTURE.width)
            height = fixed(ScrollSlot.TEXTURE.height)
          }) {
            addRenderableOnly(slot(menu.scrollContainer.range.slot, ScrollSlot.TEXTURE))
          }
        }
        Column {
          TextureBox(ASPECTS_BG) { }
          Row {
            TextureBox(BUTTON) { }
            TextureBox(BUTTON) { }
          }
        }
      }

      TextureBox(PUZZLE_BG) {
        Row({
          padding = BORDER
        }) {
          TextureBox(CIRCLE) { }
        }
      }
    }
  }
}