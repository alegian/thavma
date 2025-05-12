package me.alegian.thavma.impl.common.research

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.common.aspect.Aspect
import me.alegian.thavma.impl.common.util.Indices
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import java.util.*

data class SocketState(
  val indices: Indices,
  val aspect: Aspect? = null,
  val broken: Boolean = false,
  val locked: Boolean = false
) {
  // only for codec
  private constructor(i: Indices, oa: Optional<Aspect>, b: Boolean, l: Boolean) : this(i, oa.orElse(null), b, l)

  fun withAspect(a: Aspect?) = SocketState(indices, a, broken, locked)
  fun withBroken(b: Boolean) = SocketState(indices, aspect, b, locked)

  companion object {
    val CODEC = RecordCodecBuilder.create { builder ->
      builder.group(
        Indices.CODEC.fieldOf("indices").forGetter(SocketState::indices),
        Aspect.CODEC.optionalFieldOf("aspect").forGetter { s -> Optional.ofNullable(s.aspect) },
        Codec.BOOL.fieldOf("broken").forGetter(SocketState::broken),
        Codec.BOOL.fieldOf("locked").forGetter(SocketState::locked)
      ).apply(builder, ::SocketState)
    }

    val STREAM_CODEC = StreamCodec.composite(
      Indices.STREAM_CODEC, SocketState::indices,
      Aspect.STREAM_CODEC.apply(ByteBufCodecs::optional), { s -> Optional.ofNullable(s.aspect) },
      ByteBufCodecs.BOOL, SocketState::broken,
      ByteBufCodecs.BOOL, SocketState::locked,
      ::SocketState
    )
  }
}