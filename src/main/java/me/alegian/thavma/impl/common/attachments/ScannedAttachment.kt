package me.alegian.thavma.impl.common.attachments

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.common.codec.listOf
import me.alegian.thavma.impl.common.codec.mutableSetOf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.neoforged.neoforge.attachment.AttachmentType

/**
 * Data Attachment, used on players to save what has been scanned
 * with the arcane lens
 */
data class ScannedAttachment(
  val scanned: MutableSet<String> = mutableSetOf()
) {
  companion object {
    private val CODEC = RecordCodecBuilder.create {
      it.group(
        Codec.STRING.mutableSetOf().fieldOf("scanned").forGetter(ScannedAttachment::scanned)
      ).apply(it, ::ScannedAttachment)
    }
    private val STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.STRING_UTF8.mutableSetOf(),
      ScannedAttachment::scanned,
      ::ScannedAttachment
    )

    val TYPE = AttachmentType
      .builder(::ScannedAttachment)
      .serialize(CODEC)
      .sync(STREAM_CODEC)
      .copyOnDeath()
      .build()
  }
}