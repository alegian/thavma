package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.common.attachments.ScannedAttachment
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries

object T7Attachments {
  val REGISTRAR = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Thavma.MODID)

  val SCANNED = REGISTRAR.register("scanned") { -> ScannedAttachment.Companion.TYPE }
}
