package me.alegian.thavma.impl.client.util

import com.mojang.blaze3d.vertex.PoseStack
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