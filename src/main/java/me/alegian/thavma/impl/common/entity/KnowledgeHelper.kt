package me.alegian.thavma.impl.common.entity

import me.alegian.thavma.impl.common.payload.KnowledgePayload
import me.alegian.thavma.impl.common.research.ResearchEntry
import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import me.alegian.thavma.impl.init.registries.deferred.T7Attachments
import net.minecraft.Util
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.network.PacketDistributor

fun Player.setKnowledge(newKnowledge: List<String>) {
  val old = getData(T7Attachments.KNOWLEDGE)
  old.knowledge.addAll(newKnowledge)
  setData(T7Attachments.KNOWLEDGE, old)

  if (this is ServerPlayer)
    PacketDistributor.sendToPlayer(this, KnowledgePayload(newKnowledge))
}

private fun Player.hasKnowledge(key: String) = getData(T7Attachments.SCANNED).scanned.contains(key)

fun Player.hasKnowledge(researchEntry: ResourceKey<ResearchEntry>): Boolean {
  return hasKnowledge(key(researchEntry))
}

private fun key(entry: ResourceKey<ResearchEntry>): String =
  Util.makeDescriptionId(
    T7DatapackRegistries.RESEARCH_ENTRY.location().path,
   entry.location()
  )
