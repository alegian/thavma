package me.alegian.thavma.impl.common.book

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.init.registries.deferred.PageFeatureTypes
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.util.FormattedCharSequence

class FormattedTextFeature(val text: List<FormattedCharSequence>) : PageFeature {
  override val coversOneWholePage: Boolean
    get() = false
  override val mustStartPage: Boolean
    get() = false
  override val mustOccupySetPage: Boolean
    get() = false

  override val type: PageFeatureType<*>
    get() = PageFeatureTypes.FORMATTED.get()

  override fun toString(): String {
    return "FormattedTextFeature with number of lines ${text.size}"
  }

  companion object {
    // this thing bypasses the nonexistent native CODEC for FormattedCharSequence
    // which we don't even need
    val CODEC: MapCodec<FormattedTextFeature> = RecordCodecBuilder.mapCodec { instance ->
      instance.group(
        ComponentSerialization.CODEC.listOf().fieldOf("text").forGetter { _ ->
          emptyList()
        }
      ).apply(instance) { components ->
        FormattedTextFeature(components.map { it.visualOrderText })
      }
    }
  }
}