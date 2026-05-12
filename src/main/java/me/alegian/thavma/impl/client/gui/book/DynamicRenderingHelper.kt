package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.common.book.FigureFeature
import me.alegian.thavma.impl.common.book.FormattedTextFeature
import me.alegian.thavma.impl.common.book.PageFeature
import me.alegian.thavma.impl.common.book.ParagraphFeature
import me.alegian.thavma.impl.common.book.RecipeFeature
import me.alegian.thavma.impl.common.book.TitleFeature
import net.minecraft.client.gui.Font
import net.minecraft.network.chat.Component
import kotlin.math.min

private fun PageFeature.renderedHeight(pageWidth: Int, font: Font): Int {
  val lineHeight = font.lineHeight + 2
  return when (this) {
    is ParagraphFeature -> font.split(this.text, pageWidth).size * lineHeight
    is TitleFeature -> font.split(this.text, pageWidth).size * lineHeight + 16
    is FigureFeature -> if (caption != null) font.split(
      this.caption,
      pageWidth
    ).size * lineHeight + this.textureHeight else this.textureHeight

    is RecipeFeature -> 96
    is FormattedTextFeature -> text.size * lineHeight
    else -> throw IllegalArgumentException("This PageFeature $this does not have renderedHeight implemented yet")
  }
}

/**
 *   Return a list of PageFeatures containing a list of lines - FormattedCharSequence
 *   or possibly a Figure with just the image and its processed caption - FormattedCharSequence,
 *   so that they all fit on their
 *   respective pages (represented by list indices)
 */
fun spliceParagraphOrFigure(
  input: PageFeature, maxPageHeight: Int,
  currentHeight: Int, maxPageWidth: Int, font: Font
): List<PageFeature> {
  val result = mutableListOf<PageFeature>()
  println("maxHeight is $maxPageHeight, pageWidth is $maxPageWidth, processing page feature $input, current height is $currentHeight")

  if (input is ParagraphFeature) {
    // how many lines fit in the current page
    val lineHeight = font.lineHeight + 2
    println("lineheight is $lineHeight")

    // so that we get at least 2 lines at the end of the first page
    val linesRemainingAtStart: Int = (maxPageHeight - currentHeight) / lineHeight
    println("lines remaining until end of page are $linesRemainingAtStart")


    // so that we get at least 2 lines at the start of the last page
    // (making use of rounding down when dividing integers)
    val numOfLinesCoveringFullPages: Int =
      (input.renderedHeight(
        maxPageWidth,
        font
      ) - linesRemainingAtStart * lineHeight) / maxPageHeight * maxPageHeight / lineHeight
    println("number of lines covering whole pages is $numOfLinesCoveringFullPages")
    var linesCroppingOutAtEnd: Int =
      (input.renderedHeight(
        maxPageWidth,
        font
      ) - linesRemainingAtStart * lineHeight - numOfLinesCoveringFullPages * lineHeight) / lineHeight
    if (linesCroppingOutAtEnd < 0) linesCroppingOutAtEnd = 0
    println("number of lines cropping out at end are $linesCroppingOutAtEnd")
    val maxLinesPerPage = maxPageHeight / lineHeight
    println("max lines per page is $maxLinesPerPage")
    val numOfFullPagesCovered = numOfLinesCoveringFullPages / maxLinesPerPage
    println("number of full pages is $numOfFullPagesCovered")
    val lines = font.split(input.text, maxPageWidth)
    println("the text was split into this number of lines: ${lines.size}")
    val realLinesRemaining: Int = min(linesRemainingAtStart, lines.size)
    println("given the length of the text, this many lines are at the start: ${realLinesRemaining}")

    when {
//      lines.size == 1 && currentHeight + lineHeight <= maxPageHeight -> result += FormattedTextFeature(lines)
//
//      lines.size == 1 -> result += listOf(
//        FormattedTextFeature(listOf()),
//        FormattedTextFeature(lines)
//      )

      lines.size <= linesRemainingAtStart && currentHeight + lineHeight * lines.size <= maxPageHeight -> result += FormattedTextFeature(
        lines
      )

      linesRemainingAtStart != 1 && linesCroppingOutAtEnd != 1 -> {
        // separate into "underhang", middle and overhang
        val start = lines.slice(0 until realLinesRemaining)
        result += FormattedTextFeature(start)

        for (i in 0 until numOfFullPagesCovered) {
          val fullPage =
            lines.slice(linesRemainingAtStart + i * maxLinesPerPage until linesRemainingAtStart + (i + 1) * maxLinesPerPage)
          result += FormattedTextFeature(fullPage)
        }

        val end =
          lines.slice(lines.size - linesCroppingOutAtEnd until lines.size)
        //lines.slice(linesRemainingAtStart + numOfFullPagesCovered * maxLinesPerPage until lines.size - 1)
        result += FormattedTextFeature(end)
      }

      else -> {
        // add an empty paragraph to current page and continue on the next
        result += FormattedTextFeature(listOf())
        for (i in 0 until numOfFullPagesCovered) {
          val fullPage = lines.slice(i * maxLinesPerPage until (i + 1) * maxLinesPerPage)
          result += FormattedTextFeature(fullPage)
        }
        val finish =
          lines.slice(numOfFullPagesCovered * maxLinesPerPage until lines.size)
        result += FormattedTextFeature(finish)
      }
    }
    return result
  }

  if (input is FigureFeature) {
    with(input) {
      when {
        caption == null && currentHeight + textureHeight <= maxPageHeight -> result += input
        caption == null -> {
          result += FormattedTextFeature(listOf())
          result += this
        }

        currentHeight + textureHeight <= maxPageHeight -> {
          result += FigureFeature(image, null)
          result.addAll(
            spliceParagraphOrFigure(
              ParagraphFeature(caption),
              maxPageHeight,
              currentHeight + textureHeight,
              maxPageWidth, font
            )
          )
        }

        else -> {
          result += FormattedTextFeature(listOf())
          result += FigureFeature(image, null)
          result.addAll(
            spliceParagraphOrFigure(
              ParagraphFeature(caption),
              maxPageHeight,
              textureHeight,
              maxPageWidth,
              font
            )
          )
        }
      }
    }
    return result
  }
  throw IllegalArgumentException("The supplied input $input is not a Paragraph or Figure")
}

/**
 *  Returns a list of lists of features where every index represents a page.
 *  Features in the same list belong together on one page.
 */
fun pagifyFeatures(features: List<PageFeature>, maxHeight: Int, pageWidth: Int, font: Font): List<List<PageFeature>> {
  // maxHeight is height of background texture minus padding (32 top 42 bottom)
  //val maxHeight = this@EntryScreen.height - 74
  println("maxHeight is $maxHeight, pageWidth is $pageWidth")
  val partition = features.partition { !it.mustOccupySetPage }
  val pages = mutableListOf<List<PageFeature>>()
  val buffer = mutableListOf<PageFeature>()
  fun currentHeight() = buffer.sumOf { it.renderedHeight(pageWidth, font) }
  fun submitBufferAndClear() {
    pages.add(buffer.toList())
    println("===== Just submitted from buffer =====")
    buffer.forEach { println(it) }
    buffer.clear()
  }

  println("The list of features contains:")
  for (i in features) println(i)
  println("Divided into partitions of length ${partition.first.size} and ${partition.second.size}")
  println(partition.first)
  println(partition.second)

  // deal with elements without predetermined order (bulk of the logic)
  for (feature in partition.first) {
    println("Processing feature $feature in initial pagifyFeatures(), current height ${currentHeight()}")
    with(feature) {
      when {
        (this !is ParagraphFeature && this !is FigureFeature) && renderedHeight(
          pageWidth,
          font
        ) > maxHeight -> throw IllegalArgumentException(
          "The size of the element ${this::class.simpleName} is too large at ${
            renderedHeight(
              pageWidth,
              font
            )
          } while allowed $maxHeight."
        )

        coversOneWholePage -> {
          if (buffer.isNotEmpty()) submitBufferAndClear()
          pages += listOf(this)
        }

        mustStartPage -> {
          if (buffer.isNotEmpty()) submitBufferAndClear()
          if (this is ParagraphFeature || this is FigureFeature) {
            println("Currently have this in the result: $pages")
            val processed = spliceParagraphOrFigure(this, maxHeight, currentHeight(), pageWidth, font)
            if (processed.size < 2) buffer += processed.first()
            // only need to check this much since buffer is empty (starts page)
            else if (this is ParagraphFeature) {
              processed.slice(0 until processed.size - 1).forEach { pages += listOf(it) }
              buffer += processed.last()
            }
            // we are dealing with figure features now
            else {
              buffer += processed.first()
              buffer += processed[1]
              if (processed.size > 2) {
                submitBufferAndClear()
                for (j in 2 until processed.size - 1) {
                  pages += listOf(processed[j])
                }
                buffer += processed.last()
              }
            }
          } else buffer += this
        }

        (this is ParagraphFeature || this is FigureFeature) -> {
          val processed = spliceParagraphOrFigure(this, maxHeight, currentHeight(), pageWidth, font)
          if (processed.size < 2) buffer += processed.first()
          else if (this is ParagraphFeature) {
            buffer += processed.first()
            submitBufferAndClear()
            if (processed.size > 2) processed.slice(1 until processed.size - 1).forEach { pages += listOf(it) }
            buffer += processed.last()
          } else {
            // we are dealing with figure features now
            when {
              // Size 1 is already taken care of above. The result is at least size 2.
              // If the first thing in a processed Figure is a FormattedTextFeature,
              // it's an empty one to signify end of page. Size 2 means
              // there is only this emptiness + image.
              processed.first() is FormattedTextFeature && processed.size == 2 -> {
                submitBufferAndClear()
                buffer += processed.last()
              }
              // Now there are either exactly 2 elements and the first one is an image
              // that fits in the page and the second one is text,
              // or there are more than 2 elements and they can be anything.
              else -> {
                var i = 0
                // address possible page break
                if (processed.first() is FormattedTextFeature) {
                  submitBufferAndClear()
                  i++
                }
                // always takes the image next
                buffer += processed[i]
                // the check whether the start of the caption fits in the page with the
                // image is done by the Paragraph half of the function, so just add
                // all that remains
                buffer += processed[i + 1]
                if (processed.size > 2 + i) {
                  submitBufferAndClear()
                  for (j in 2 + i until processed.size - 1) {
                    pages += listOf(processed[j])
                  }
                  buffer += processed.last()
                }
              }
            }
          }
        }

        currentHeight() + renderedHeight(pageWidth, font) <= maxHeight -> buffer += this
        // the next check might be redundant:
        buffer.isEmpty() -> throw IllegalArgumentException(
          "The size of the element ${this::class.simpleName} is too large at ${
            renderedHeight(
              pageWidth,
              font
            )
          } while allowed $maxHeight."
        )

        else -> {
          submitBufferAndClear()
          buffer += this
        }
      }
    }
  }

  // add anything left over in the buffer
  if (buffer.isNotEmpty()) submitBufferAndClear()

  println("current state of pages before adding predetermined stuff is")
  for (i in pages) println(i)

  // finally add features with pre-determined positions (cannot be paragraphs)
  // these features have to be ordered correctly in the Research Entry builder
  partition.second.groupBy { it.preferredPageIndex }.forEach { pages.add(it.key, it.value) }

  println("the final state of pages is")
  for (i in pages) println(i)

  return pages
}