package me.alegian.thavma.impl.common.entity

import me.alegian.thavma.impl.common.aspect.Aspect
import me.alegian.thavma.impl.common.payload.ResearchToastPayload
import me.alegian.thavma.impl.common.research.ResearchEntry
import me.alegian.thavma.impl.common.util.registry
import me.alegian.thavma.impl.common.util.serialize
import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import me.alegian.thavma.impl.init.registries.deferred.T7Attachments
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.network.PacketDistributor

fun ServerPlayer.addKnowledge(newKnowledge: List<String>) {
  val old = getData(T7Attachments.KNOWLEDGE)
  old.knowledge.addAll(newKnowledge)
  setData(T7Attachments.KNOWLEDGE, old)

  PacketDistributor.sendToPlayer(this, ResearchToastPayload(newKnowledge))
}

fun ServerPlayer.tryLearnResearch(entry: ResearchEntry) {
  if (knowsResearch(entry)) return
  val key = level().registry(T7DatapackRegistries.RESEARCH_ENTRY).getResourceKey(entry).get()
  addKnowledge(listOf(key.serialize()))
}

fun Player.knowsResearch(entry: ResearchEntry): Boolean {
  if (entry.defaultKnown) return true
  val key = level().registry(T7DatapackRegistries.RESEARCH_ENTRY).getResourceKey(entry).get()
  return knows(key)
}

fun ServerPlayer.tryLearnAspects(aspects: List<Aspect>) {
  addKnowledge(
    aspects
      .filter { !knowsAspect(it) }
      .map { it.resourceKey.serialize() }
  )
}

fun Player.knowsAspect(aspect: Aspect) = aspect.isPrimal || knows(aspect.resourceKey)

private fun Player.knows(resourceKey: ResourceKey<*>) =
  getData(T7Attachments.KNOWLEDGE).knowledge.contains(resourceKey.serialize())