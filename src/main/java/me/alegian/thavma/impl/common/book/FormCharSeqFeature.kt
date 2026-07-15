package me.alegian.thavma.impl.common.book

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.init.registries.deferred.PageFeatureTypes
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.util.FormattedCharSequence

class FormCharSeqFeature(
  val text: List<FormattedCharSequence>, override val startsPage: Boolean = false,
  override val forceIndex: Int? = null
) : PageFeature {
  override val type: PageFeatureType<*>
    get() = PageFeatureTypes.FORMCHARSEQ.get()

  companion object {
    // this thing bypasses the nonexistent native CODEC for FormattedCharSequence
    // which we don't even need
    val CODEC: MapCodec<FormCharSeqFeature> = RecordCodecBuilder.mapCodec { instance ->
      instance.group(
        ComponentSerialization.CODEC.listOf().fieldOf("text").forGetter { _ ->
          emptyList()
        }
      ).apply(instance) { components ->
        FormCharSeqFeature(components.map { it.visualOrderText })
      }
    }
  }
}