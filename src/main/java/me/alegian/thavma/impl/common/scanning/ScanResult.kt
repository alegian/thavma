package me.alegian.thavma.impl.common.scanning

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.util.StringRepresentable
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs

enum class ScanResult : StringRepresentable {
  UNSUPPORTED,
  LOCKED,
  SUCCESS;

  override fun getSerializedName() = name

  companion object {
    val CODEC = StringRepresentable.fromEnum(ScanResult::values)
    val STREAM_CODEC = NeoForgeStreamCodecs.enumCodec<FriendlyByteBuf, ScanResult>(ScanResult::class.java)
  }
}