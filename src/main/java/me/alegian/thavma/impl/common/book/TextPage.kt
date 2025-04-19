package me.alegian.thavma.impl.common.book

import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.init.registries.deferred.PageTypes
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import java.util.*

class TextPage(val title: Component?, val paragraphs: List<Component>) : Page {
  override val type: PageType<*>
    get() = PageTypes.TEXT.get()

  // TODO: make custom codec helper
  // only for codec
  private fun optionalTitle() = Optional.ofNullable(title)
  // only for codec
  private constructor(title: Optional<Component>, paragraphs: List<Component>) : this(title.orElse(null), paragraphs)

  companion object {
    val CODEC = RecordCodecBuilder.mapCodec { builder ->
      builder.group(
        ComponentSerialization.CODEC.optionalFieldOf("title").forGetter(TextPage::optionalTitle),
        ComponentSerialization.CODEC.listOf().fieldOf("paragraphs").forGetter(TextPage::paragraphs),
      ).apply(builder, ::TextPage)
    }

    fun titleTranslationId(baseId: String, pageIndex: Int) = Page.translationId(baseId, pageIndex) + ".title"
    fun paragraphTranslationId(baseId: String, pageIndex: Int, index: Int) = Page.translationId(baseId, pageIndex) + ".paragraph$index"
  }
}