package me.alegian.thavma.impl.common.book

import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.init.registries.deferred.PageTypes
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization

class TextPage(val title: Component, val text: Component) : Page {
  override val type: PageType<*>
    get() = PageTypes.TEXT.get()

  companion object{
    val CODEC = RecordCodecBuilder.mapCodec { builder ->
      builder.group(
        ComponentSerialization.CODEC.fieldOf("title").forGetter(TextPage::title),
        ComponentSerialization.CODEC.fieldOf("text").forGetter(TextPage::text),
      ).apply(builder, ::TextPage)
    }
  }
}