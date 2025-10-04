package me.alegian.thavma.impl.init.registries.deferred

import com.mojang.serialization.Codec
import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.common.attachments.KnowledgeAttachment
import me.alegian.thavma.impl.common.attachments.ScannedAttachment
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries

object T7Attachments {
  val REGISTRAR = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Thavma.MODID)

  val SCANNED = REGISTRAR.register("scanned") { -> ScannedAttachment.TYPE }

  val KNOWLEDGE = REGISTRAR.register("knowledge") { -> KnowledgeAttachment.TYPE }

  val LEVITATES = REGISTRAR.register("levitates") { -> AttachmentType.builder { -> false }.serialize(Codec.BOOL).build() }

  val ENDERPEARL_NO_DAMAGE = REGISTRAR.register("enderpearl_no_damage") { -> AttachmentType.builder { -> false }.serialize(Codec.BOOL).build() }
}
