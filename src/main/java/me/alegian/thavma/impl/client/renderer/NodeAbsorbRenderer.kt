package me.alegian.thavma.impl.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import me.alegian.thavma.impl.client.ClientHelper
import me.alegian.thavma.impl.client.T7Colors
import me.alegian.thavma.impl.client.util.setUpWandPose
import me.alegian.thavma.impl.client.util.transformOrigin
import me.alegian.thavma.impl.common.infusion.trajectoryLength
import me.alegian.thavma.impl.common.item.WandItem
import me.alegian.thavma.impl.common.item.WandItem.Companion.wandMode
import me.alegian.thavma.impl.common.item.WandMode
import me.alegian.thavma.impl.common.item.interactingBlockPos
import me.alegian.thavma.impl.common.util.minus
import me.alegian.thavma.impl.common.util.toVec3
import me.alegian.thavma.impl.common.util.use
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.player.PlayerRenderer
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.client.event.RenderLevelStageEvent

object NodeAbsorbRenderer {
  fun renderLevelAfterEntities(event: RenderLevelStageEvent) {
    if (event.stage != RenderLevelStageEvent.Stage.AFTER_ENTITIES) return
    val level = Minecraft.getInstance().level ?: return
    val players = level.players()
    val partialTick = event.partialTick.gameTimeDeltaTicks

    for (player in players) {
      if (player.useItem.wandMode != WandMode.ABSORB_NODE) return
      if (player.useItem.item !is WandItem) return
      val playerRenderer = ClientHelper.entityRenderDispatcher().getRenderer(player)
      if (playerRenderer !is PlayerRenderer) return

      val targetPos = player.useItem.interactingBlockPos?.center ?: return
      var wandTipPos = Vec3.ZERO
      event.poseStack.use {
        setUpWandPose(player, playerRenderer, partialTick)
        wandTipPos = transformOrigin().toVec3()
      }
      val cameraPos = ClientHelper.camera().position
      render(event.poseStack, ClientHelper.bufferSource(), partialTick, player.level().gameTime, targetPos-cameraPos, wandTipPos)
    }
  }

  fun render(poseStack: PoseStack, bufferSource: MultiBufferSource, partialTick: Float, gameTime: Long, targetPos: Vec3, wandTipPos: Vec3) {
    val length = trajectoryLength(targetPos, wandTipPos)
    val colorWithAlpha = 0x44000000 or (T7Colors.PURPLE and 0xffffff)
    renderFlyingAspects(targetPos, wandTipPos, 0.2, length - 1, length, poseStack, bufferSource, gameTime + partialTick, colorWithAlpha, 0.06)
  }
}