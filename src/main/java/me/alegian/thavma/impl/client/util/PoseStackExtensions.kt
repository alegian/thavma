package me.alegian.thavma.impl.client.util

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import me.alegian.thavma.impl.client.ClientHelper
import me.alegian.thavma.impl.common.util.minus
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.client.renderer.entity.player.PlayerRenderer
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3
import org.joml.Matrix4f
import org.joml.Vector3f

fun PoseStack.translate(offset: Vec3?) {
  if (offset != null) translate(offset.x, offset.y, offset.z)
}

fun PoseStack.transformOrigin() = last().pose().transformPosition(Vector3f())

fun PoseStack.scale(scale: Float) = scale(scale, scale, scale)

fun PoseStack.translateXY(x: Number, y: Number) = translate(x.toDouble(), y.toDouble(), 0.0)
fun PoseStack.translateXY(vec: Vec2) = translateXY(vec.x, vec.y)

fun PoseStack.scaleXY(scale: Number) = scale(scale.toFloat(), scale.toFloat(), 1f)

fun PoseStack.rotateZ(deg: Number) = mulPose(Matrix4f().rotateZ((deg.toFloat() / 180 * Math.PI).toFloat()))
fun PoseStack.rotateX(deg: Number) = mulPose(Matrix4f().rotateX((deg.toFloat() / 180 * Math.PI).toFloat()))
fun PoseStack.rotateY(deg: Number) = mulPose(Matrix4f().rotateY((deg.toFloat() / 180 * Math.PI).toFloat()))

fun PoseStack.setUpWandPose(player: AbstractClientPlayer, playerRenderer: PlayerRenderer, partialTick: Float) {
  translate(player.getPosition(partialTick) - ClientHelper.camera().position)
  if (player == ClientHelper.player() && ClientHelper.firstPerson()) {
    translate(0f, player.eyeHeight, 0f)
    mulPose(ClientHelper.camera().rotation())

    // go to first person hand
    val sideMultiplier = if (player.mainArm == HumanoidArm.RIGHT) 1.0 else -1.0
    translate(0.6 * sideMultiplier, -0.3, -0.5)
    // go to the tip of the wand
    translate(-0.4, 0.1, -0.2)
  } else {
    mulPose(Axis.YP.rotationDegrees(-player.getPreciseBodyRotation(partialTick)))
    // go to arm pivot point (hours of reverse engineering led to this constant)
    translate(0.0, 19 / 16.0, 0.0)
    playerRenderer.model.translateToHand(player.mainArm, this)
    // go to the tip of the wand
    translate(0.0, -0.6, 0.8)
  }
}
