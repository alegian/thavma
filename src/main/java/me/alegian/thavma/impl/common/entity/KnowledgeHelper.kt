package me.alegian.thavma.impl.common.entity

import me.alegian.thavma.impl.common.aspect.Aspect
import me.alegian.thavma.impl.common.payload.ResearchToastPayload
import me.alegian.thavma.impl.common.research.ResearchEntry
import me.alegian.thavma.impl.common.util.serialize
import me.alegian.thavma.impl.init.registries.deferred.T7Attachments
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.network.PacketDistributor

fun ServerPlayer.addKnowledge(newKnowledge: List<ResourceKey<*>>) {
  val old = getData(T7Attachments.KNOWLEDGE)
  val serializedKnowledge = newKnowledge.map{it.serialize()}
  old.knowledge.addAll(serializedKnowledge)
  setData(T7Attachments.KNOWLEDGE, old)

  PacketDistributor.sendToPlayer(this, ResearchToastPayload(serializedKnowledge))
}

fun Player.knowsResearch(entry: Holder<ResearchEntry>): Boolean {
  if (entry.value().defaultKnown) return true
  return knows(entry.unwrapKey().get())
}

fun Player.knowsAspect(aspect: Aspect) = aspect.isPrimal || knows(aspect.resourceKey)

private fun Player.knows(resourceKey: ResourceKey<*>) =
  getData(T7Attachments.KNOWLEDGE).knowledge.contains(resourceKey.serialize())