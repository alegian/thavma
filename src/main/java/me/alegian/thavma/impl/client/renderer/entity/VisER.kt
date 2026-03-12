package me.alegian.thavma.impl.client.renderer.entity

import com.mojang.blaze3d.vertex.PoseStack
import me.alegian.thavma.impl.client.particle.VisTrailParticles
import me.alegian.thavma.impl.common.data.capability.AspectContainer
import me.alegian.thavma.impl.init.registries.deferred.Aspects
import me.alegian.thavma.impl.common.entity.VisEntity
import me.alegian.thavma.impl.common.util.plus
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.culling.Frustum
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.phys.Vec3

// TODO: move to world rendering
class VisER(pContext: EntityRendererProvider.Context) : EntityRenderer<VisEntity>(pContext) {
  override fun render(visEntity: VisEntity, pEntityYaw: Float, pPartialTick: Float, poseStack: PoseStack, pBufferSource: MultiBufferSource, pPackedLight: Int) {
    val player = visEntity.player ?: return
    val playerHandPos = preparePlayerHandPosition(pPartialTick, player)
    val colors = AspectContainer.at(visEntity.level(), visEntity.blockPosition())?.aspects?.toSortedList()?.map { it.aspect.color and 0x00FFFFFF }
      ?.takeIf { it.isNotEmpty() }
      ?: Aspects.PRIMAL_ASPECTS.map { it.get().color and 0x00FFFFFF }
    spawnTrailParticles(visEntity, playerHandPos, colors)
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

private val visParticleTicks = mutableMapOf<Int, Int>()

private fun spawnTrailParticles(visEntity: VisEntity, target: Vec3, colors: List<Int>) {
  val tick = visEntity.tickCount
  if (visParticleTicks[visEntity.id] == tick) return
  visParticleTicks[visEntity.id] = tick

  val start = visEntity.position()
  val distance = start.distanceTo(target)
  val count = when {
    distance < 1.5 -> 2
    distance < 3.0 -> 3
    else -> 4
  }
  VisTrailParticles.spawnBurst(start, target, colors, tick + visEntity.id, count, 0.095f)
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
    val cameraPos = Minecraft.getInstance().gameRenderer.mainCamera.position
    val view = player.getViewVector(pPartialTick).normalize().scale(.45)
    val angle = Math.PI / 2 - player.getViewYRot(pPartialTick) / 360f * 2 * Math.PI
    position = cameraPos
    position += view
    position += Vec3(0.0, -0.16, 0.0)
    val horizontalOffset = Vec3(0.0, 0.0, (if (arm == HumanoidArm.RIGHT) -.14f else .14f).toDouble())
    position += horizontalOffset.yRot(angle.toFloat())
  } else { // for third person, we follow body rotation
    val angle = Math.PI / 2 - player.getPreciseBodyRotation(pPartialTick) / 360f * 2 * Math.PI
    val offset = Vec3(-1.0, (player.eyeHeight - .56f).toDouble(), (if (arm == HumanoidArm.RIGHT) -.4f else .4f).toDouble())
    position += offset.yRot(angle.toFloat())
  }
  return position
}
