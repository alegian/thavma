package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.common.book.FigureFeature
import me.alegian.thavma.impl.common.book.PageBreakFeature
import me.alegian.thavma.impl.common.book.PageFeature
import me.alegian.thavma.impl.common.book.ParagraphFeature
import me.alegian.thavma.impl.common.book.TitleFeature
import me.alegian.thavma.impl.common.research.ResearchEntry
import me.alegian.thavma.impl.init.registries.deferred.ResearchEntries
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey

internal object ResearchBookContent {
  private val contentByEntry = mapOf(
    ResearchEntries.Thavma.THAVMA to listOf(
      title("Thavma"),
      paragraph(
        """
          I was merely toying with that wand -if it can even be called that- when this tome
          flew into my hands! I can sense great power within it.
        """
      ),
      paragraph(
        """
          The cover reads "Elements", but a lot of its pages appear blank, sealed by some magic.
        """
      ),
      paragraph(
        """
          To read them, I will first need to break that seal. It won't be easy... but
          I have a feeling it will be worth my efforts.
        """
      ),
      pageBreak(),
      paragraph(
        """
          I will document all my findings inside the book, so that I can recall them later.
        """
      ),
    ),
    ResearchEntries.Thavma.ARCANE_LENS to listOf(
      title("The Arcane Lens"),
      paragraph(
        """
          The part of the book I can read describes an arcane tool that "allows the user
          to see", whatever that might mean. I have a feeling that crafting it could assist
          my work in unsealing the other pages.
        """
      ),
      paragraph(
        """
          The blueprint describes a hexagonal device, much like a prism,
          made with those colorful crystals I found lying in a cave.
        """
      ),
      paragraph(
        """
          I should look at the world through its lens, maybe it will uncover something useful.
        """
      ),
    ),
    ResearchEntries.Thavma.INFUSION to listOf(
      figure(
        Texture("gui/images/infusion", 1916, 1036, 1916, 1036),
        180,
        101,
        "An image of the infusion altar",
      ),
    ),
  )

  fun featuresFor(entryKey: ResourceKey<ResearchEntry>): List<PageFeature> =
    contentByEntry[entryKey].orEmpty().mapIndexed { index, feature ->
      feature.create(PageFeature.translationId(ResearchEntry.translationId(entryKey), index))
    }

  fun translations(): Map<String, String> = buildMap {
    for ((entryKey, features) in contentByEntry) {
      val baseId = ResearchEntry.translationId(entryKey)
      features.forEachIndexed { index, feature ->
        feature.text?.let { put(PageFeature.translationId(baseId, index), it) }
      }
    }
  }
}

private class FeatureDefinition(
  val text: String?,
  val factory: (String) -> PageFeature,
) {
  fun create(translationId: String) = factory(translationId)
}

private fun title(text: String) = FeatureDefinition(text.normalize()) { translationId ->
  TitleFeature(Component.translatable(translationId).withStyle(ChatFormatting.BOLD))
}

private fun paragraph(text: String) = FeatureDefinition(text.normalize()) { translationId ->
  ParagraphFeature(Component.translatable(translationId))
}

private fun pageBreak() = FeatureDefinition(null) { PageBreakFeature() }

private fun figure(image: Texture, width: Int, height: Int, caption: String? = null) =
  FeatureDefinition(caption?.normalize()) { translationId ->
    FigureFeature(image, width, height, caption?.let { Component.translatable(translationId) })
  }

private fun String.normalize() = trimIndent().replace("\n", " ")
