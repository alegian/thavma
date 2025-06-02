package me.alegian.thavma.impl.common.aspect

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

/**
 * Used when converting AspectMaps to lists.
 * The element of these lists is the AspectStack.
 */
data class AspectStack(val aspect: Aspect, val amount: Int) {
  companion object {
    fun of(aspect: Aspect, amount: Int): AspectStack {
      return AspectStack(aspect, amount)
    }

    val CODEC = RecordCodecBuilder.create {
      it.group(
        Aspect.CODEC.fieldOf("aspect").forGetter(AspectStack::aspect),
        Codec.INT.fieldOf("amount").forGetter(AspectStack::amount),
      ).apply(it, ::AspectStack)
    }
    val STREAM_CODEC = StreamCodec.composite(
      Aspect.STREAM_CODEC, AspectStack::aspect,
      ByteBufCodecs.INT, AspectStack::amount,
      ::AspectStack
    )
  }
}
