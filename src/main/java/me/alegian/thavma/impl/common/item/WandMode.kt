package me.alegian.thavma.impl.common.item

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.util.StringRepresentable
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs

enum class WandMode : StringRepresentable{
  ABSORB_NODE,
  EXCAVATE;

  override fun getSerializedName() = name

  companion object {
    val CODEC = StringRepresentable.fromEnum(WandMode::values)
    val STREAM_CODEC = NeoForgeStreamCodecs.enumCodec<FriendlyByteBuf, WandMode>(WandMode::class.java)
  }
}