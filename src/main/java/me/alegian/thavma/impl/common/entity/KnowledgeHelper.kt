package me.alegian.thavma.impl.common.entity

import me.alegian.thavma.impl.common.payload.KnowledgePayload
import me.alegian.thavma.impl.common.research.ResearchEntry
import me.alegian.thavma.impl.init.registries.deferred.T7Attachments
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

fun Player.setKnowledge(entry: ResourceKey<ResearchEntry>) = setKnowledge(listOf(entry.location().toString()))

private fun Player.hasKnowledge(key: String) = getData(T7Attachments.SCANNED).scanned.contains(key)

fun Player.hasKnowledge(researchEntry: ResourceKey<ResearchEntry>): Boolean {
  return hasKnowledge(researchEntry.location().toString())
}
