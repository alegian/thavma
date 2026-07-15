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

class EntryScreen(val entry: Holder<ResearchEntry>) : Screen(Component.literal("Book Entry")) {
  companion object {
    private val BG = Texture("gui/book/background", 510, 282, 512, 512)
  }

  private var currentPage = 0
  private val fontify = Minecraft.getInstance().font


  override fun init() {
    super.init()
    clearWidgets()

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
            initPage(entry.value().pages.getOrNull(currentPage))
            if (currentPage != 0) {
              Box({
                width = fixed(PageTurningWidget.LEFT_TEXTURE.width)
                height = fixed(PageTurningWidget.LEFT_TEXTURE.height)
              }) {
                afterLayout {
                  addRenderableWidget(PageTurningWidget(position, false) {
                    // rerender the screen for the new page(s)
                    turnPage(false)
                  })
                }
              }
            }
          }

          Row({
            size = grow()
          }) {
            initPage(entry.value().pages.getOrNull(currentPage + 1))
            if (entry.value().pages.getOrNull(currentPage + 2) != null) {
              Box({
                width = fixed(PageTurningWidget.RIGHT_TEXTURE.width)
                height = fixed(PageTurningWidget.RIGHT_TEXTURE.height)
              }) {
                afterLayout {
                  addRenderableWidget(PageTurningWidget(position, true) {
                    // rerender the screen for the new page(s)
                    turnPage(true)
                  })
                }
              }
            }
          }
        }
      }
    }
  }

  private fun PageTurnerRow() {
    Row({
      width = grow()
    }) {
      if (currentPage != 0) {
        Box({
          width = fixed(PageTurningWidget.LEFT_TEXTURE.width)
          height = fixed(PageTurningWidget.LEFT_TEXTURE.height)
        }) {
          afterLayout {
            addRenderableWidget(PageTurningWidget(position, false) {
              // rerender the screen for the new page(s)
              turnPage(false)
            })
          }
        }
      }
      Box({ width = grow() }) {}
      if (pages.getOrNull(currentPage + 2) != null) {
        Box({
          width = fixed(PageTurningWidget.RIGHT_TEXTURE.width)
          height = fixed(PageTurningWidget.RIGHT_TEXTURE.height)
        }) {
          afterLayout {
            addRenderableWidget(PageTurningWidget(position, true) {
              // rerender the screen for the new page(s)
              turnPage(true)
            })
          }
        }
      }
    }
  }

  fun turnPage(right: Boolean){
    if (right) currentPage += 2
    else currentPage -= 2
    init()
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

  private fun <T : PageFeature?> initPageFeature(feature: T) {
    if (feature != null) {
      val renderer = PAGE_FEATURE_RENDERERS[feature.type] as PageFeatureRenderer<T>
      renderer.initPageFeature(this, feature, maxWidth, fontify)
    }
  }

  override fun isPauseScreen() = false
}