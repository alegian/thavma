package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.gui.layout.*
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.common.book.Page
import me.alegian.thavma.impl.common.book.PageFeature
import me.alegian.thavma.impl.common.research.ResearchEntry
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec2

class EntryScreen(entry: Holder<ResearchEntry>) : Screen(Component.literal("Book Entry")) {
  companion object {
    private val BG = Texture("gui/book/background", 510, 282, 512, 512)
  }

  private var currentPage = 0
  private val fontify = Minecraft.getInstance().font

  private var maxWidth = BG.width/2 - 65
  private var maxHeight = BG.height - 90

  // maxHeight is height of background texture minus padding (32 top 42 bottom)
  var pages = pagifyFeatures(entry.value().pageFeatures, maxHeight, maxWidth, fontify)

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
          Row({
            size = grow()
          }) {
            initPageFeatures(pages.getOrNull(currentPage))
            if (currentPage != 0) {
              Box({
                width = fixed(PageTurningWidget.LEFT_TEXTURE.width)
                height = fixed(PageTurningWidget.LEFT_TEXTURE.height)
              }) {
                afterLayout {

                  //addRenderableWidget(PageTurningWidget(position, false) {
                  addRenderableWidget(PageTurningWidget(Vec2(position.x - maxWidth + 40, position.y + maxHeight + 30), false) {
                    // reinitiate the screen for this research entry when clicked
                    // with an updated page
                    // clearWidgets() is essential, also clears underline formatting!
                    currentPage -= 2
                    clearWidgets()
                    init()
                  })
                }
              }
            }
          }

          Row({
            size = grow()
          }) {
            initPageFeatures(pages.getOrNull(currentPage + 1))
            if (pages.getOrNull(currentPage + 2) != null) {
              Box({
                width = fixed(PageTurningWidget.RIGHT_TEXTURE.width)
                height = fixed(PageTurningWidget.RIGHT_TEXTURE.height)
              }) {
                afterLayout {

                  addRenderableWidget(PageTurningWidget(Vec2(position.x - 10, position.y + maxHeight + 30), true) {
                    // reinitiate the screen for this research entry when clicked
                    // with an updated page
                    // clearWidgets() is essential, also clears underline formatting!
                    currentPage += 2
                    clearWidgets()
                    init()
                  })
                }
              }
            }
          }
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

  private fun initPageFeatures(features: List<PageFeature>?) {
    if (features != null) {
      val renderer = DynamicFeaturesRenderer
      renderer.initPageFeatures(this, features, maxWidth)
    }
  }

  override fun isPauseScreen() = false
}