package me.alegian.thavma.impl.common.book

import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.init.registries.deferred.PageTypes
import net.minecraft.network.chat.ComponentSerialization
import java.util.Optional

class DynamicPage(val pageFeatures: List<PageFeature>): Page {
    // this might be a problem but for a future Tobias
    override val type: PageType<*>
        get() = PageTypes.TEXT.get()


//    companion object {
//        val CODEC = RecordCodecBuilder.mapCodec { builder ->
//            builder.group(
//                ComponentSerialization.CODEC.fieldOf("text").forGetter(ParagraphFeature::text)
//            ).apply(builder, ::ParagraphFeature)
//        }
//
//    }
}