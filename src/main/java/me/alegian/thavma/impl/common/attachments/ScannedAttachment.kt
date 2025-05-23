package me.alegian.thavma.impl.common.attachments

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.common.codec.mutableSetOf
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

    val TYPE = AttachmentType
      .builder(::ScannedAttachment)
      .serialize(CODEC)
      .copyOnDeath()
      .build()
  }
}