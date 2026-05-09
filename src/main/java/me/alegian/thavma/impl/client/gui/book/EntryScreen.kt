package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.gui.layout.*
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.common.book.Page
import me.alegian.thavma.impl.common.book.PageFeature
import me.alegian.thavma.impl.common.book.ParagraphFeature
import me.alegian.thavma.impl.common.book.PlainTextFeature
import me.alegian.thavma.impl.common.research.ResearchEntry
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style

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
            initPage(entry.value().pages.getOrNull(currentPage + 1))
            if (entry.value().pages.getOrNull(currentPage + 2) != null) {
              Box({
                width = fixed(PageTurningWidget.RIGHT_TEXTURE.width)
                height = fixed(PageTurningWidget.RIGHT_TEXTURE.height)
              }) {
                afterLayout {

                  addRenderableWidget(PageTurningWidget(position, true) {
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

//  private fun initPageFeatures(features: List<PageFeature>) {
//    val renderer = DynamicFeaturesRenderer as PageFeatureRenderer<PageFeature>
//    renderer.initPageFeatures(this, features, currentPage)
//  }

  val renderer = DynamicFeaturesRenderer

  private fun initPageFeatures(features: List<PageFeature>) {
    renderer.initPageFeatures(this, features, currentPage)
  }


  /**
   *   Return a list of plain text features based on a paragraph so that they fit on their
   *   respective pages (represented by list idices)
   */
  private fun spliceParagraph(
    paragraph: ParagraphFeature, maxPageHeight: Int,
    currentHeight: Int
  ): List<PlainTextFeature> {

    val result = mutableListOf<PlainTextFeature>()
    // how many lines fit in the current page
    val lineHeight = paragraph.LINE_HEIGHT

    // so that we get at least 2 lines at the end of the first page
    val linesRemainingAtStart: Int = (maxPageHeight - currentHeight) / lineHeight

    // so that we get at least 2 lines at the start of the last page
    // (making use of rounding down when dividing integers)
    val numOfLinesCoveringFullPages: Int =
      (paragraph.renderedHeight - linesRemainingAtStart * lineHeight) / maxPageHeight / lineHeight
    val linesCroppingOutAtEnd: Int =
      (paragraph.renderedHeight - linesRemainingAtStart * lineHeight - numOfLinesCoveringFullPages * lineHeight) / lineHeight

    val maxLinesPerPage = maxPageHeight / lineHeight
    val numOfFullPagesCovered = numOfLinesCoveringFullPages / maxLinesPerPage
    val lines = paragraph.font.splitter.splitLines(paragraph.text, paragraph.pageWidth, Style.EMPTY)

    when {
      lines.size == 1 && currentHeight + lineHeight <= maxPageHeight -> result += PlainTextFeature(
        Component.literal(
          lines[0].string
        )
      )

      lines.size == 1 -> result += listOf(
        PlainTextFeature(Component.literal("")),
        PlainTextFeature(Component.literal(lines[0].string))
      )

      linesRemainingAtStart >= 2 && linesCroppingOutAtEnd != 1 -> {
        // separate into "underhang", middle and overhang
        val start = lines.slice(0 until linesRemainingAtStart).joinToString(" ") { it.string }
        result += PlainTextFeature(Component.literal(start))

        for (i in 0 until numOfFullPagesCovered) {
          val middle =
            lines.slice(linesRemainingAtStart + i * maxLinesPerPage until linesRemainingAtStart + (i + 1) * maxLinesPerPage)
              .joinToString(" ") { it.string }
          result += PlainTextFeature(Component.literal(middle))
        }

        val end =
          lines.slice(lines.size - 1 - linesCroppingOutAtEnd until lines.size - 1).joinToString(" ") { it.string }
        result += PlainTextFeature(Component.literal(end))
      }

      else -> {
        // add an empty paragraph to current page and continue on the next
        result += PlainTextFeature(Component.literal(""))
        for (i in 0 until numOfFullPagesCovered) {
          val page = lines.slice(i * maxLinesPerPage until (i + 1) * maxLinesPerPage).joinToString(" ") { it.string }
          result += PlainTextFeature(Component.literal(page))
        }
        val finish =
          lines.slice(numOfFullPagesCovered * maxLinesPerPage until lines.size).joinToString(" ") { it.string }
        result += PlainTextFeature(Component.literal(finish))
      }
    }

    return result
  }

  /**
   *  Returns a list of lists of features where every index represents a page.
   *  Features in the same list belong together on one page.
   */
  private fun pagifyFeatures(features: List<PageFeature>): List<List<PageFeature>> {
    val maxHeight = this@EntryScreen.height
    val partition = features.partition { !it.mustOccupySetPage }
    val result = mutableListOf<List<PageFeature>>()
    val buffer = mutableListOf<PageFeature>()
    fun currentHeight() = buffer.sumOf { it.renderedHeight }

    for (feature in partition.first) {

      with(feature) {
        when {
          this !is ParagraphFeature && renderedHeight > maxHeight -> throw IllegalArgumentException("The size of the element ${this::class.simpleName} is too large at $renderedHeight while allowed $maxHeight.")

          coversWholePage -> {
            if (buffer.isNotEmpty()) {
              result += buffer
              buffer.clear()
            }
            result += listOf(this)
          }

          mustStartPage -> {
            if (buffer.isNotEmpty()) {
              result += buffer
              buffer.clear()
            }
            if (this is ParagraphFeature) {
              val processed = spliceParagraph(this, maxHeight, currentHeight())
              if (processed.size == 1) buffer += processed.first()
              //only need to check this much since buffer is empty (starts page)
              else {
                processed.slice(0 until processed.size - 1).forEach { result += listOf(it) }
                buffer += processed.last()
              }
            } else buffer += this
          }

          this is ParagraphFeature -> {
            val processed = spliceParagraph(this, maxHeight, currentHeight())
            if (processed.size == 1) buffer += processed.first()
            // need to check one more value so as not to append null to result
            else if (processed.size == 2) {
              buffer += processed.first()
              result += buffer
              buffer.clear()
              buffer += processed.last()
            } else {
              buffer += processed.first()
              result += buffer
              buffer.clear()
              processed.slice(1 until processed.size - 1).forEach { result += listOf(it) }
              buffer += processed.last()
            }
          }

          currentHeight() + renderedHeight <= maxHeight -> buffer += this
          // the next check might be redundant:
          buffer.isEmpty() -> throw IllegalArgumentException("The size of the element ${this::class.simpleName} is too large at $renderedHeight while allowed $maxHeight.")
          else -> {
            result += buffer
            buffer.clear()
            buffer += this
          }
        }

      }

    }

    if (buffer.isNotEmpty()) result += buffer

    // finally add features with pre-determined positions (cannot be paragraphs)
    // these features have to be ordered correctly in the Research Entry builder
    partition.second.groupBy { it.preferredPageIndex }.forEach { result.add(it.key, it.value) }

    return result
  }

  override fun isPauseScreen() = false
}