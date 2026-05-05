package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.gui.layout.*
import me.alegian.thavma.impl.client.gui.research_table.ButtonWidget
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.common.book.Page
import me.alegian.thavma.impl.common.research.ResearchEntry
import me.alegian.thavma.impl.init.registries.deferred.ResearchEntries
import me.alegian.thavma.impl.rl
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.Level
import kotlin.math.ceil

class EntryScreen(private val entry: Holder<ResearchEntry>) : Screen(Component.literal("Book Entry")) {
  companion object {
    private val BG = Texture("gui/book/background", 510, 282, 512, 512)
  }

  private var currentPage = 0

  override fun init() {
    super.init()

    LayoutExtensions.currScreen = this
    Row({
      width = fixed(this@EntryScreen.width)
      height = fixed(this@EntryScreen.height)
      align = Alignment.CENTER
    }) {
      TextureBox(BG) {
        Row({
          size = grow()
          paddingTop = 32
          paddingX = 32
          paddingBottom = 42
          gap = 48
        }) {
        //for (doublepage in 0..ceil(entry.value().pages.size/2.0).toInt()){
          Row({
            size = grow()
          }) {
            initPage(entry.value().pages.getOrNull(currentPage))
              Box({
                  width = fixed(PageTurningWidget.LEFT_TEXTURE.width)
                  height = fixed(PageTurningWidget.LEFT_TEXTURE.height)
              }) {
                  afterLayout {
                      if (currentPage != 0) {
                      addRenderableWidget(PageTurningWidget(position, false) {
                          // reinitiate the screen for this research entry when clicked
                          // with an updated page
                          currentPage -= 2
                          init()
                      })}
                  }
              }
          }

          Row({
            size = grow()
          }) {
            initPage(entry.value().pages.getOrNull(currentPage + 1))
              Box({
                  width = fixed(PageTurningWidget.RIGHT_TEXTURE.width)
                  height = fixed(PageTurningWidget.RIGHT_TEXTURE.height)
              }) {
                  afterLayout {
                      if ((entry.value().pages.getOrNull(currentPage + 2) ?: 1) is Page) {
                      addRenderableWidget(PageTurningWidget(position, true) {
                          // reinitiate the screen for this research entry when clicked
                          // with an updated page
                          currentPage += 2
                          init()
                      })}
                  }
              }
          }
        //  }
        }
      }
    }
  }

  override fun renderBackground(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    renderTransparentBackground(guiGraphics)
  }

  // wrapper around unchecked cast
  private fun <T : Page?> initPage(page: T) {
    if (page == null) return
    val renderer = PAGE_RENDERERS[page.type] as PageRenderer<T>
    renderer.initPage(this, page)
  }

  override fun isPauseScreen() = false
}