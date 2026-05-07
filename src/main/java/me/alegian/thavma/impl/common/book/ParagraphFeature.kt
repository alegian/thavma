package me.alegian.thavma.impl.common.book

import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.common.book.PageFeature
import me.alegian.thavma.impl.init.data.worldgen.tree.trunk.SilverwoodTrunkPlacer
import me.alegian.thavma.impl.init.registries.deferred.PageFeatureTypes
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.network.chat.Style
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer.trunkPlacerParts
import java.util.Optional

class ParagraphFeature(val text: Component): PageFeature {
    override val type: PageFeatureType<*>
        get() = PageFeatureTypes.PARAGRAPH.get()

    override val coversWholePage = false
    override val mustStartPage = false
    override val mustOccupySetPage = false

    val font: Font = Minecraft.getInstance().font
    // if a recalibrated font size is used, I can multiply or divide by rendering scaling factor
    val LINE_HEIGHT = font.lineHeight + 2
    val lines = font.splitter.splitLines(text, pageWidth, Style.EMPTY)

    override val renderedHeight = LINE_HEIGHT * lines.size + LINE_HEIGHT * 2 / 3

    companion object {
        val CODEC = RecordCodecBuilder.mapCodec { builder ->
            builder.group(
                ComponentSerialization.CODEC.fieldOf("text").forGetter(ParagraphFeature::text)
            ).apply(builder, ::ParagraphFeature)
        }

        fun translationId(baseId: String, featureIndex: Int) = "$baseId.paragraphFeature$featureIndex"
    }

    //fun titleTranslationId(baseId: String, pageIndex: Int) = Page.translationId(baseId, pageIndex) + ".title"
    //fun paragraphFeatureTranslationId(baseId: String, index: Int) = Page.translationId(baseId, pageIndex) + ".paragraph$index"

}