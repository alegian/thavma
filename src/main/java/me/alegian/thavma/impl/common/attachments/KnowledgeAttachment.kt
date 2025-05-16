package me.alegian.thavma.impl.common.attachments

import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.common.codec.mutableSetOf
import me.alegian.thavma.impl.common.research.ResearchEntry
import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.attachment.AttachmentType

/**
 * Data Attachment, used on players to save what research is known
 */
data class KnowledgeAttachment(
  val knowledge: MutableSet<ResourceKey<ResearchEntry>> = mutableSetOf()
) {
  companion object {
    private val CODEC = RecordCodecBuilder.create {
      it.group(
        ResourceKey.codec(T7DatapackRegistries.RESEARCH_ENTRY).mutableSetOf().fieldOf("knowledge").forGetter(KnowledgeAttachment::knowledge)
      ).apply(it, ::KnowledgeAttachment)
    }

    val TYPE = AttachmentType
      .builder(::KnowledgeAttachment)
      .serialize(CODEC)
      .copyOnDeath()
      .build()
  }
}