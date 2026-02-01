package me.alegian.thavma.impl.client.renderer

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import me.alegian.thavma.impl.client.ClientHelper
import me.alegian.thavma.impl.client.util.transformOrigin
import me.alegian.thavma.impl.client.util.translate
import me.alegian.thavma.impl.common.entity.lerpedPosition
import me.alegian.thavma.impl.common.item.WandItem.Companion.equippedFocus
import me.alegian.thavma.impl.common.level.Excavation
import me.alegian.thavma.impl.common.util.minus
import me.alegian.thavma.impl.common.util.use
import me.alegian.thavma.impl.init.registries.deferred.Aspects
import me.alegian.thavma.impl.init.registries.deferred.T7Items
import net.minecraft.client.Minecraft
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BeaconRenderer
import net.minecraft.client.renderer.entity.player.PlayerRenderer
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.level.ClipContext
import net.neoforged.neoforge.client.event.RenderLevelStageEvent
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.ceil

object ExcavationRenderer {
  fun renderLevelAfterEntities(event: RenderLevelStageEvent) {
    if (event.stage != RenderLevelStageEvent.Stage.AFTER_ENTITIES) return
    val level = Minecraft.getInstance().level ?: return
    val players = level.players()
    val partialTick = event.partialTick.gameTimeDeltaTicks

    for (player in players) {
      if (player.useItem.equippedFocus?.item != T7Items.FOCUS_EXCAVATION.get()) return
      val playerRenderer = ClientHelper.entityRenderDispatcher().getRenderer(player)
      if (playerRenderer !is PlayerRenderer) return

      val from = player.lerpedPosition(partialTick).add(0.0, player.eyeHeight.toDouble(), 0.0)
      val to = from.add(player.getViewVector(partialTick).scale(Excavation.RANGE))
      val hitresult = level.clip(ClipContext(from, to, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player))
      val hitPos = hitresult.location.toVector3f()

      event.poseStack.use {
        setUpWandPose(player, playerRenderer, partialTick)
        render(event.poseStack, ClientHelper.bufferSource(), partialTick, player.level().gameTime, hitPos)
      }
    }
  }

  private fun PoseStack.setUpWandPose(player: AbstractClientPlayer, playerRenderer: PlayerRenderer, partialTick: Float) {
    translate(player.lerpedPosition(partialTick) - ClientHelper.camera().position)
    if (player == ClientHelper.player() && ClientHelper.firstPerson()) {
      translate(0f, player.eyeHeight, 0f)
      mulPose(ClientHelper.camera().rotation())

      // go to first person hand
      val sideMultiplier = if (player.mainArm == HumanoidArm.RIGHT) 1.0 else -1.0
      translate(0.6 * sideMultiplier, -0.3, -0.5)
      // go to the tip of the wand
      translate(-0.4, 0.1, -0.2)
    } else {
      mulPose(Axis.YP.rotationDegrees(-player.yBodyRot))
      // go to arm pivot point (hours of reverse engineering led to this constant)
      translate(0.0, 19 / 16.0, 0.0)
      playerRenderer.model.translateToHand(player.mainArm, this)
      // go to the tip of the wand
      translate(0.0, -0.6, 0.8)
    }
  }

  fun render(handPose: PoseStack, bufferSource: MultiBufferSource, partialTick: Float, gameTime: Long, hitPos: Vector3f) {
    handPose.use {
      // rotate towards target block
      val targetPos = hitPos - ClientHelper.camera().position.toVector3f()
      val wandTipPos = transformOrigin()
      val currentUp = Vector3f(0f, 1f, 0f)
      val localDiff = (targetPos - wandTipPos).mulDirection(handPose.last().pose().invert(Matrix4f()))
      val correctUp = localDiff.normalize(Vector3f())
      mulPose(Quaternionf().rotationTo(currentUp, correctUp))

      // undo built-in translation of beacon
      translate(-0.5, 0.0, -0.5)
      RenderSystem.disableCull()
      BeaconRenderer.renderBeaconBeam(
        handPose,
        bufferSource,
        BeaconRenderer.BEAM_LOCATION,
        partialTick,
        1f,
        gameTime,
        0,
        ceil(localDiff.length()).toInt(),
        Aspects.TERRA.get().color,
        0.3f,
        0.3f
      )
      RenderSystem.enableCull()
    }
  }
}