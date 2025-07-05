package me.alegian.thavma.impl.client.renderer.entity

import com.mojang.blaze3d.vertex.PoseStack
import me.alegian.thavma.impl.client.T7Colors
import me.alegian.thavma.impl.client.renderer.renderFlyingAspects
import me.alegian.thavma.impl.client.util.translate
import me.alegian.thavma.impl.common.entity.VisEntity
import me.alegian.thavma.impl.common.infusion.trajectoryLength
import me.alegian.thavma.impl.common.util.use
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.culling.Frustum
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.phys.Vec3
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.plus
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.unaryMinus

// TODO: move to world rendering
class VisER(pContext: EntityRendererProvider.Context) : EntityRenderer<VisEntity>(pContext) {
  override fun render(visEntity: VisEntity, pEntityYaw: Float, pPartialTick: Float, poseStack: PoseStack, pBufferSource: MultiBufferSource, pPackedLight: Int) {
    val player = visEntity.player ?: return

    poseStack.use {
      translate(-visEntity.position()) // we are inside an entity renderer
      val playerHandPos = preparePlayerHandPosition(pPartialTick, player)
      val length = trajectoryLength(visEntity.position(), playerHandPos)
      val colorWithAlpha = 0x44000000 or (T7Colors.PURPLE and 0xffffff)
      renderFlyingAspects(visEntity.position(), playerHandPos, 0.2, length - 1, length, this, pBufferSource, visEntity.tickCount + pPartialTick, colorWithAlpha, 0.06)
    }
  }

  override fun getTextureLocation(pEntity: VisEntity) =
    InventoryMenu.BLOCK_ATLAS

  /**
   * The Vis Entity does not have a strict bounding box,
   * so we never cull it to avoid rendering bugs at the edge
   * of the screen.
   */
  override fun shouldRender(pLivingEntity: VisEntity, pCamera: Frustum, pCamX: Double, pCamY: Double, pCamZ: Double) = true
}

/**
 * Prepare a Vec3 position of the Player's hand. This is only an approximation,
 * and it does not follow the PlayerModel's animations, because using these
 * animation poses is complicated.
 */
private fun preparePlayerHandPosition(pPartialTick: Float, player: Player): Vec3 {
  var position = player.getPosition(pPartialTick)

  val arm = player.mainArm

  // for first person, if it is the client player, we follow the camera
  if (player === Minecraft.getInstance().player && Minecraft.getInstance().options.cameraType.isFirstPerson) {
    val angle = Math.PI / 2 - player.getViewYRot(pPartialTick) / 360f * 2 * Math.PI
    val translation = player.getViewVector(pPartialTick).normalize().scale(.1)
    position += Vec3(0.0, player.eyeHeight + 0.01, 0.0)
    position += translation
    val horizontalOffset = Vec3(0.0, 0.0, (if (arm == HumanoidArm.RIGHT) -.06f else .06f).toDouble())
    position += horizontalOffset.yRot(angle.toFloat())
  } else { // for third person, we follow body rotation
    val angle = Math.PI / 2 - player.getPreciseBodyRotation(pPartialTick) / 360f * 2 * Math.PI
    val offset = Vec3(-1.0, (player.eyeHeight - .56f).toDouble(), (if (arm == HumanoidArm.RIGHT) -.4f else .4f).toDouble())
    position += offset.yRot(angle.toFloat())
  }
  return position
}
