package me.alegian.thavma.impl.common.attachments

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.common.codec.mutableSetOf
import net.neoforged.neoforge.attachment.AttachmentType

/**
 * Data Attachment, used on players to save what is known.
 * It need not be a research, known aspects also use this system.
 * Therefore, it is one-way, you can only query "does player have research X", not
 * "all research for player" (since some may not be researches)
 */
data class KnowledgeAttachment(
  val knowledge: MutableSet<String> = mutableSetOf()
) {
  companion object {
    private val CODEC = RecordCodecBuilder.create {
      it.group(
        Codec.STRING.mutableSetOf().fieldOf("knowledge").forGetter(KnowledgeAttachment::knowledge)
      ).apply(it, ::KnowledgeAttachment)
    }

    val TYPE = AttachmentType
      .builder(::KnowledgeAttachment)
      .serialize(CODEC)
      .copyOnDeath()
      .build()
  }
}