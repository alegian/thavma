package me.alegian.thavma.impl.client.renderer

import me.alegian.thavma.impl.client.ClientHelper
import me.alegian.thavma.impl.client.particle.VisTrailParticles
import me.alegian.thavma.impl.client.util.setUpWandPose
import me.alegian.thavma.impl.client.util.transformOrigin
import me.alegian.thavma.impl.common.data.capability.AspectContainer
import me.alegian.thavma.impl.common.item.WandItem
import me.alegian.thavma.impl.common.item.WandItem.Companion.wandMode
import me.alegian.thavma.impl.common.item.WandMode
import me.alegian.thavma.impl.common.item.interactingBlockPos
import me.alegian.thavma.impl.common.util.toVec3
import me.alegian.thavma.impl.common.util.use
import me.alegian.thavma.impl.init.registries.deferred.Aspects
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.entity.player.PlayerRenderer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.client.event.RenderLevelStageEvent
import java.util.WeakHashMap

object NodeAbsorbRenderer {
  private class TrailState(var position: Vec3, var lastSpawnTick: Int? = null)
  private val trailStates = WeakHashMap<Player, TrailState>()

  fun renderLevelAfterEntities(event: RenderLevelStageEvent) {
    if (event.stage != RenderLevelStageEvent.Stage.AFTER_ENTITIES) return
    val level = Minecraft.getInstance().level ?: return
    val players = level.players()
    val partialTick = event.partialTick.gameTimeDeltaTicks

    for (player in players) {
      if (player.useItem.wandMode != WandMode.ABSORB_NODE) continue
      if (player.useItem.item !is WandItem) continue
      val playerRenderer = ClientHelper.entityRenderDispatcher().getRenderer(player)
      if (playerRenderer !is PlayerRenderer) continue

      val blockPos = player.useItem.interactingBlockPos ?: continue
      val targetPos = blockPos.center
      var wandTipPos = Vec3.ZERO
      event.poseStack.use {
        setUpWandPose(player, playerRenderer, partialTick)
        wandTipPos = transformOrigin().toVec3()
      }
      // The wand pose is camera-relative; particles need world-space positions.
      wandTipPos = wandTipPos.add(ClientHelper.camera().position)
      val trailState = trailStates.getOrPut(player) { TrailState(wandTipPos) }
      trailState.position = wandTipPos
      if (trailState.lastSpawnTick == player.tickCount) continue
      val colors = AspectContainer.at(level, blockPos)?.aspects?.toSortedList()?.map { it.aspect.color and 0x00FFFFFF }
        ?.takeIf { it.isNotEmpty() }
        ?: Aspects.REGISTRAR.entries.map { it.get() }.filter { it.isPrimal }.map { it.color and 0x00FFFFFF }
      trailState.lastSpawnTick = player.tickCount
      spawnTrailParticles(targetPos, trailState, colors, player.tickCount + player.id)
    }
  }

  private fun spawnTrailParticles(start: Vec3, target: TrailState, colors: List<Int>, seed: Int) {
    val distance = start.distanceTo(target.position)
    val count = when {
      distance < 1.5 -> 2
      distance < 3.0 -> 3
      else -> 4
    }
    VisTrailParticles.spawnBurst(start, target.position, colors, seed, count, 0.095f) { target.position }
  }
}
