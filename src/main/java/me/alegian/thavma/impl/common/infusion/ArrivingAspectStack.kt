package me.alegian.thavma.impl.common.infusion

import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.common.aspect.AspectStack
import net.minecraft.core.BlockPos
import net.minecraft.network.codec.StreamCodec

/**
 * represents a tick of infusion essentia flying
 */
data class ArrivingAspectStack(val blockPos: BlockPos, val aspectStack: AspectStack){
  companion object{
    val CODEC = RecordCodecBuilder.create {
      it.group(
        BlockPos.CODEC.fieldOf("blockPos").forGetter(ArrivingAspectStack::blockPos),
        AspectStack.CODEC.fieldOf("aspectStack").forGetter(ArrivingAspectStack::aspectStack)
      ).apply(it, ::ArrivingAspectStack)
    }
    val STREAM_CODEC = StreamCodec.composite(
      BlockPos.STREAM_CODEC, ArrivingAspectStack::blockPos,
      AspectStack.STREAM_CODEC, ArrivingAspectStack::aspectStack,
      ::ArrivingAspectStack
    )
  }
}