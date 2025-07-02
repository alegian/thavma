package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.gui.layout.*
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.common.book.Page
import me.alegian.thavma.impl.common.research.ResearchEntry
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

private val BG = Texture("gui/book/background", 510, 282, 512, 512)
val SEPARATOR = Texture("gui/book/separator", 128, 16, 128, 16)

class EntryScreen(private val entry: ResearchEntry) : Screen(Component.literal("Book Entry")) {
  private val currentPage = 0

  override fun init() {
    super.init()

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
          paddingTop = 32
          paddingX = 32
          paddingBottom = 42
          gap = 48
        }) {
          afterLayout {
            addRenderableOnly(texture(BG))
          }

          Row({
            size = grow()
          }) {
            initPage(entry.pages.getOrNull(currentPage))
          }

          Row({
            size = grow()
          }) {
            initPage(entry.pages.getOrNull(currentPage + 1))
          }
        }
      }
    }
  }

  override fun renderBackground(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    renderTransparentBackground(guiGraphics)
  }

  public override fun <T : Renderable> addRenderableOnly(renderable: T): T {
    return super.addRenderableOnly(renderable)
  }

  fun getFont() = font

  // wrapper around unchecked cast
  private fun <T : Page?> initPage(page: T) {
    if (page == null) return
    val renderer = PAGE_RENDERERS[page.type] as PageRenderer<T>
    renderer.initPage(this, page)
  }

  override fun isPauseScreen() = false
}