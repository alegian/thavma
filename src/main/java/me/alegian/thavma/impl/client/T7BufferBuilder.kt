package me.alegian.thavma.impl.client

import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import me.alegian.thavma.impl.client.T7VertexFormats.CENTER
import me.alegian.thavma.impl.client.T7VertexFormats.SCALE
import org.joml.Vector3f
import org.lwjgl.system.MemoryUtil

class T7BufferBuilder(parent: VertexConsumer) {
  private val parent: BufferBuilder
  private var pose: PoseStack.Pose? = null

  init {
    require(parent is BufferBuilder) { "Thavma Exception: parent must be a BufferBuilder" }
    this.parent = parent
  }

  fun setColor(pRed: Float, pGreen: Float, pBlue: Float, pAlpha: Float): T7BufferBuilder {
    parent.setColor(pRed, pGreen, pBlue, pAlpha)
    return this
  }

  fun setColor(packedColor: Int): T7BufferBuilder {
    parent.setColor(packedColor)
    return this
  }

  fun addVertex(pPose: PoseStack.Pose, pX: Float, pY: Float, pZ: Float): T7BufferBuilder {
    pose = pPose
    parent.addVertex(pPose, pX, pY, pZ)
    return this
  }

  fun setCenter(): T7BufferBuilder {
    pose.let {
      require(it != null) { "Thavma Exception: parent must be a BufferBuilder" }
      val i = parent.beginElement(CENTER)
      val center = it.pose().transformPosition(0f, 0f, 0f, Vector3f())
      if (i != -1L) {
        MemoryUtil.memPutFloat(i, center.x())
        MemoryUtil.memPutFloat(i + 4L, center.y())
        MemoryUtil.memPutFloat(i + 8L, center.z())
      }
    }

    return this
  }

  fun setScale(scale: Float): T7BufferBuilder {
    val i = parent.beginElement(SCALE)
    if (i != -1L) MemoryUtil.memPutFloat(i, scale)

    return this
  }
}
