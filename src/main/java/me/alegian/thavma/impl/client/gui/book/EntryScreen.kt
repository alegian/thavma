package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.gui.layout.*
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.common.book.Page
import me.alegian.thavma.impl.common.research.ResearchEntry
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec2

private val BG = Texture("gui/book/background", 510, 282, 512, 512)

class EntryScreen(entry: ResearchEntry) : Screen(Component.literal("Book Entry")) {
  private var left = 0.0
  private var top = 0.0
  private val page = entry.pages.getOrNull(0)

  override fun init() {
    super.init()
    left = (width - BG.width) / 2.0
    top = (height - BG.height) / 2.0

    Row({
      width = fixed(this@EntryScreen.width)
      height = fixed(this@EntryScreen.height)
      align = Alignment.CENTER
    }) {
      Row({
        width = fixed(BG.width)
        height = fixed(BG.height)
      }) {
        Row({
          size = grow()
          paddingTop = 24
          paddingX = 24
          paddingBottom = 42
          gap = 32
        }) {
          afterLayout {
            addRenderableOnly(texture(BG))
          }

          Row({
            size = grow()
          }) {
            afterLayout {
              if (page != null) addRenderableOnly(pageRenderable(page, position, size))
            }
          }

          Row({
            size = grow()
          }) {
            afterLayout {
              if (page != null) addRenderableOnly(pageRenderable(page, position, size))
            }
          }
        }
      }
    }
  }

  override fun renderBackground(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    renderTransparentBackground(guiGraphics)
  }
}

private fun <T : Page> pageRenderable(page: T, position: Vec2, size: Vec2): Renderable {
  val renderer = PAGE_RENDERERS[page.type] as PageRenderer<T>
  return renderer.asRenderable(page, position, size)
}