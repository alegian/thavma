package me.alegian.thavma.impl.common.book

import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.init.registries.deferred.PageTypes
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization

class TextPage(val title: Component, val paragraphs: List<Component>) : Page {
  override val type: PageType<*>
    get() = PageTypes.TEXT.get()

  companion object {
    val CODEC = RecordCodecBuilder.mapCodec { builder ->
      builder.group(
        ComponentSerialization.CODEC.fieldOf("title").forGetter(TextPage::title),
        ComponentSerialization.CODEC.listOf().fieldOf("paragraphs").forGetter(TextPage::paragraphs),
      ).apply(builder, ::TextPage)
    }

    fun titleTranslationId(baseId: String) = "$baseId.title"
    fun paragraphTranslationId(baseId: String, index: Int) = "$baseId.paragraph$index"
  }
}