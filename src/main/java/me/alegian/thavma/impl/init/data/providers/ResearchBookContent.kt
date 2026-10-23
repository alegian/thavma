package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.common.book.*
import me.alegian.thavma.impl.common.research.ResearchEntry
import me.alegian.thavma.impl.init.registries.deferred.ResearchEntries
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey

internal object ResearchBookContent {
  private val contentByEntry = mapOf(
    ResearchEntries.Thavma.WELCOME to listOf(
      paragraph(""),
      pageBreak(),
      figure(
        Texture("gui/images/elements_cover", 376, 511, 376, 511),
        148,
        202
      ),
      title("Table of Contents"),
      paragraph("Editorial - 498"),
      paragraph("Stepping Stones - 499"),
      paragraph("In Flux - 511"),
      paragraph("Window of Opportunity - 524"),
      paragraph("Review - 530"),
      paragraph("Research - 542"),
      paragraph("Illustratia - 585"),
      pageBreak(),
      paragraph("Editorial | 6 December 1971"),
      title("Novel thavmic research field: a month of excitement marks succeeding dozens of speculation"),
      paragraph("An automated workforce promises the processing of simple human input, but rather than stakeholders’ faith it will require a great deal of patience."),
      paragraph(""),
      paragraph("Four weeks have passed and alchemist Hxxxxx Maggiorana has hardly had a break from incoming correspondence. Following her precipitous breakthrough, it is difficult to imagine that just prior to last month’s discovery, the Thavmaturgical community did not yet universally regard the name with deference and prestige of the highest calibre."),
      paragraph("The newly appointed Professor at the Institute of Basic Thavmaturgical Foundations has certainly done its Genius Loci justice. She has rivalled the long-established residential Auromancy group’s nodal excitation discovery (B. Hveding et al. Elements 919, 503-507; 1969) and set out on the best track towards recapitulating the unparalleled commercial success of Antinon Whistledshe’s drilling apparatus (A. Whistledshe. Elements 471, 45-47; 1867). It is precisely his annual prize Maggiorana is expected to receive come spring, although the matching honour might as well equal founding a legacy of her own."),
      paragraph("As aspiring students and ambitious experts alike rally at the Institute’s doorstep, Maggiorana is looking for ways to expand the utilities of her trailblazing invention. The initial spark has, fascinatingly, been the outcome of a predominantly solitary investigation (H. R. Maggiorana et al. Elements 943, 482-490; 1971). There is, however, little doubt that further advances will only be attained through interdisciplinary collaboration. While currently illuminating a primarily alchemical achievement, the limelight is sure to shift in time towards behavioural linguistomics, artificial anthropology, ultrasonication-facilitated response audiometry or even experimental botany."),
      paragraph("We find ourselves at an exciting breakpoint. Avenues to downstream applications are beckoning forth and the brand-new branch of Thavmaturgy is bound to attract countless investors, spurring contractual research. But the spiked interest in innovation is not without concern. Are the creations conscious? Under what circumstances? Should research into certain capabilities be prioritised or banned? What are the limits of this new science? Indeed, thavmaturges, science communicators, ethicists, philosophers and policymakers have assembled at the Integrity Masterclass Symposium at Sxxxxx University, Lxxxx this week, discussing the demand for strict research regulations and ethics auditing. So far, they have been advocating for an international oversight mechanism similar to the Thavmarrhea Quarantine Treaty (I. Pössnicker et al. Elements 856, 570-572; 1964)."),
      paragraph("Given that they are the first discovery of such tremendous potential impact in the life of the general public in history, the future of golems appears highly uncertain. Strict surveillance implementations are in order before any patent decisions are made. It therefore comes with great disappointment that other institutions will not be given access, leaving Maggiorana’s team the sole amenders until then."),
      paragraph(""),
      paragraph("Elements 944, 498-499 (1971)")
    ),
    ResearchEntries.Thavma.THAVMA to listOf(
      paragraph("Ah, the nostalgia...: D"),
      paragraph("Oh yes, an awfully long time’s passed since anyone broke down reaching their 100th rejection milestone, and I sincerely hope this is your best shot doing anything remotely similar: D"),
      paragraph("Well, what an introduction. Welcome to the wondrous, rigorous, scientifically exact universe of Thavma. We’ve got magical crystals, enchanting aura nodes, enthralling concoctions, spellbinding wands, and, most importantly, charming aspects :3"),
      paragraph("But alas, our research’s been scattered. And by stars above, miss Fortune’s blind eyes must’ve burnt off selecting such a radiant intellectual to aid our cause!: D"),
      paragraph("To all our great relief, you’re only going to be recovering research, mostly. Someone else’s already done the heavy lifting for you, and as far as copyright goes, let me get this straight, it’s all public domain. Except for… maybe a few pieces here and there. But plagiarism is their least concern right about now: D"),
      pageBreak(),
      paragraph("Now then, your means to an end are the Thavmometer and the Research Table – [recipes and schematics included]. Word of advice: if you don’t understand something, quit whining and at least try to look busy not understanding something else. In this world, two wrongs can make a right: D"),
      paragraph("The role’s simple; run around like a headless chicken with one, record everything like your taxes depend on it, then go back to the other. Connect the dots, rinse, repeat. Read the tutorial in the next entry if unsure. You’ll know you’re doing it right if you start hearing voices: D"),
      paragraph("So, what’s the hold up? The world’s your oyster! Nab yourself some [paper and scribing tools], whack those flashing entries in this conspicuously interactive book and jam along! You’ll get help from me and others as you get stuck, so no biggie. Good luck: D")
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
    contentByEntry[entryKey].orEmpty().let { features ->
      features.mapIndexed { index, feature ->
        feature.create(PageFeature.translationId(ResearchEntry.translationId(entryKey), index, features.lastIndex))
      }
    }

  fun translations(): Map<String, String> = buildMap {
    for ((entryKey, features) in contentByEntry) {
      val baseId = ResearchEntry.translationId(entryKey)
      features.forEachIndexed { index, feature ->
        feature.text?.let { put(PageFeature.translationId(baseId, index, features.lastIndex), it) }
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
