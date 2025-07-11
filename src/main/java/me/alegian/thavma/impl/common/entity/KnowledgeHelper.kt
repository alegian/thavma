package me.alegian.thavma.impl.common.entity

import me.alegian.thavma.impl.client.clientRegistry
import me.alegian.thavma.impl.common.aspect.Aspect
import me.alegian.thavma.impl.common.payload.KnowledgePayload
import me.alegian.thavma.impl.common.research.ResearchEntry
import me.alegian.thavma.impl.common.util.serialize
import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import me.alegian.thavma.impl.init.registries.deferred.T7Attachments
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.network.PacketDistributor

fun Player.setKnowledge(newKnowledge: List<String>, firstPacket: Boolean = false) {
  val old = getData(T7Attachments.KNOWLEDGE)
  old.knowledge.addAll(newKnowledge)
  setData(T7Attachments.KNOWLEDGE, old)

  if (this is ServerPlayer)
    PacketDistributor.sendToPlayer(this, KnowledgePayload(newKnowledge, firstPacket))
}

fun Player.tryLearnResearch(researchKey: ResourceKey<ResearchEntry>) {
  if (knowsResearch(researchKey)) return
  setKnowledge(listOf(researchKey.serialize()))
}

fun Player.knowsResearch(researchKey: ResourceKey<ResearchEntry>): Boolean {
  val entry = clientRegistry(T7DatapackRegistries.RESEARCH_ENTRY)?.get(researchKey) ?: return false
  return entry.defaultKnown || knows(researchKey)
}

fun Player.tryLearnAspects(aspects: List<Aspect>) {
  setKnowledge(
    aspects
      .filter { !knowsAspect(it) }
      .map { it.resourceKey.serialize() }
  )
}

fun Player.knowsAspect(aspect: Aspect) = aspect.isPrimal || knows(aspect.resourceKey)

private fun Player.knows(resourceKey: ResourceKey<*>) =
  getData(T7Attachments.KNOWLEDGE).knowledge.contains(resourceKey.serialize())