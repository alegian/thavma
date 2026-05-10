package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.common.book.FigureFeature
import me.alegian.thavma.impl.common.book.FormattedTextFeature
import me.alegian.thavma.impl.common.book.PageFeature
import me.alegian.thavma.impl.common.book.ParagraphFeature

/**
 *   Return a list of PageFeatures containing a list of lines - FormattedCharSequence
 *   or possibly a Figure with just the image and its processed caption - FormattedCharSequence,
 *   so that they all fit on their
 *   respective pages (represented by list indices)
 */
fun spliceParagraphOrFigure(
  input: PageFeature, maxPageHeight: Int,
  currentHeight: Int
): List<PageFeature> {
  val result = mutableListOf<PageFeature>()

  if (input is ParagraphFeature) {
    // how many lines fit in the current page
    val lineHeight = input.LINE_HEIGHT

    // so that we get at least 2 lines at the end of the first page
    val linesRemainingAtStart: Int = (maxPageHeight - currentHeight) / lineHeight

    // so that we get at least 2 lines at the start of the last page
    // (making use of rounding down when dividing integers)
    val numOfLinesCoveringFullPages: Int =
      (input.renderedHeight - linesRemainingAtStart * lineHeight) / maxPageHeight / lineHeight
    val linesCroppingOutAtEnd: Int =
      (input.renderedHeight - linesRemainingAtStart * lineHeight - numOfLinesCoveringFullPages * lineHeight) / lineHeight

    val maxLinesPerPage = maxPageHeight / lineHeight
    val numOfFullPagesCovered = numOfLinesCoveringFullPages / maxLinesPerPage
    val lines = input.font.split(input.text, input.pageWidth)

    when {
      lines.size == 1 && currentHeight + lineHeight <= maxPageHeight -> result += FormattedTextFeature(lines)

      lines.size == 1 -> result += listOf(
        FormattedTextFeature(listOf()),
        FormattedTextFeature(lines)
      )

      linesRemainingAtStart != 1 && linesCroppingOutAtEnd != 1 -> {
        // separate into "underhang", middle and overhang
        val start = lines.slice(0 until linesRemainingAtStart)
        result += FormattedTextFeature(start)

        for (i in 0 until numOfFullPagesCovered) {
          val middle =
            lines.slice(linesRemainingAtStart + i * maxLinesPerPage until linesRemainingAtStart + (i + 1) * maxLinesPerPage)
          result += FormattedTextFeature(middle)
        }

        val end =
          lines.slice(lines.size - 1 - linesCroppingOutAtEnd until lines.size - 1)
        result += FormattedTextFeature(end)
      }

      else -> {
        // add an empty paragraph to current page and continue on the next
        result += FormattedTextFeature(listOf())
        for (i in 0 until numOfFullPagesCovered) {
          val page = lines.slice(i * maxLinesPerPage until (i + 1) * maxLinesPerPage)
          result += FormattedTextFeature(page)
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
          result += listOf()
          result += this
        }

        currentHeight + textureHeight <= maxPageHeight -> {
          result += FigureFeature(image, null)
          result.addAll(
            spliceParagraphOrFigure(
              ParagraphFeature(caption),
              maxPageHeight,
              currentHeight + textureHeight
            )
          )
        }

        else -> {
          result += FormattedTextFeature(listOf())
          result += FigureFeature(image, null)
          result.addAll(spliceParagraphOrFigure(ParagraphFeature(caption), maxPageHeight, textureHeight))
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
fun pagifyFeatures(features: List<PageFeature>, maxHeight: Int): List<List<PageFeature>> {
  // maxHeight is height of background texture minus padding (32 top 42 bottom)
  //val maxHeight = this@EntryScreen.height - 74
  val partition = features.partition { !it.mustOccupySetPage }
  val pages = mutableListOf<List<PageFeature>>()
  val buffer = mutableListOf<PageFeature>()
  fun currentHeight() = buffer.sumOf { it.renderedHeight }
  fun submitBufferAndClear() {
    pages += buffer
    buffer.clear()
  }

  // deal with elements without predetermined order (bulk of the logic)
  for (feature in partition.first) {
    with(feature) {
      when {
        (this !is ParagraphFeature && this !is FigureFeature) && renderedHeight > maxHeight -> throw IllegalArgumentException(
          "The size of the element ${this::class.simpleName} is too large at $renderedHeight while allowed $maxHeight."
        )

        coversOneWholePage -> {
          if (buffer.isNotEmpty()) submitBufferAndClear()
          pages += listOf(this)
        }

        mustStartPage -> {
          if (buffer.isNotEmpty()) submitBufferAndClear()
          if (this is ParagraphFeature || this is FigureFeature) {
            val processed = spliceParagraphOrFigure(this, maxHeight, currentHeight())
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
          val processed = spliceParagraphOrFigure(this, maxHeight, currentHeight())
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

        currentHeight() + renderedHeight <= maxHeight -> buffer += this
        // the next check might be redundant:
        buffer.isEmpty() -> throw IllegalArgumentException("The size of the element ${this::class.simpleName} is too large at $renderedHeight while allowed $maxHeight.")
        else -> {
          submitBufferAndClear()
          buffer += this
        }
      }
    }
  }

  // add anything left over in the buffer
  if (buffer.isNotEmpty()) submitBufferAndClear()

  // finally add features with pre-determined positions (cannot be paragraphs)
  // these features have to be ordered correctly in the Research Entry builder
  partition.second.groupBy { it.preferredPageIndex }.forEach { pages.add(it.key, it.value) }

  return pages
}