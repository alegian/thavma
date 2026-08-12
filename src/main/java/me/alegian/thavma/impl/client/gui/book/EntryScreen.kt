package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.gui.layout.*
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.common.book.PageFeature
import me.alegian.thavma.impl.common.research.ResearchEntry
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component

class EntryScreen(_entry: Holder<ResearchEntry>) : Screen(Component.literal("Book Entry")) {
  companion object {
    private val BG = Texture("gui/book/background", 510, 282, 512, 512)
  }

  private var currentPage = 0
  private val entry = _entry.value()

  private val pagination = Pagination(entry.pageFeatures)

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
        Column({
          size = grow()
        }) {
          Row({
            size = grow()
            paddingY = PageTurningWidget.LEFT_TEXTURE.height + 8
            paddingX = 32
            paddingBottom = 42
            gap = 48
          }) {
            Row({
              size = grow()
            }) {
              PaginatedColumn(pagination, currentPage, {
                size = grow()
                gap = 4
              }) { feature ->
                initPageFeature(feature)
              }
            }

            Row({
              size = grow()
            }) {
              PaginatedColumn(pagination, currentPage + 1, {
                size = grow()
                gap = 4
              }) { feature ->
                initPageFeature(feature)
              }
            }
          }
          PageTurnerRow()
        }
      }
    }
  }

  private fun PageTurnerRow() {
    Row({
      width = grow()
      paddingX = 30
      paddingY = 15
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
      Box({
        width = fixed(PageTurningWidget.RIGHT_TEXTURE.width)
        height = fixed(PageTurningWidget.RIGHT_TEXTURE.height)
      }) {
        afterLayout {
          if (pagination.hasPage(currentPage + 2)) {
            addRenderableWidget(PageTurningWidget(position, true) {
              // rerender the screen for the new page(s)
              turnPage(true)
            })
          }
        }
      }
    }
  }

  fun turnPage(right: Boolean) {
    if (right) currentPage += 2
    else currentPage -= 2
    init()
  }

  override fun renderBackground(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    renderTransparentBackground(guiGraphics)
  }

  // wrapper around unchecked cast
  private fun <T : PageFeature?> initPageFeature(feature: T) {
    if (feature != null) {
      val renderer = PAGE_FEATURE_RENDERERS[feature.type] as PageFeatureRenderer<T>
      renderer.initPageFeature(this, feature, this.font)
    }
  }

  override fun isPauseScreen() = false
}
