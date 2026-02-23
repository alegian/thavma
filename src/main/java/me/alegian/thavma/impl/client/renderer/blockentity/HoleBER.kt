package me.alegian.thavma.impl.client.renderer.blockentity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import me.alegian.thavma.impl.common.block.entity.HoleBE
import me.alegian.thavma.impl.common.util.use
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.core.Direction
import net.minecraft.world.level.block.Block
import org.joml.Vector3f

class HoleBER : BlockEntityRenderer<HoleBE> {
  override fun render(be: HoleBE, partialTick: Float, poseStack: PoseStack, bufferSource: MultiBufferSource, combinedLight: Int, combinedOverlay: Int) {
    val vc = bufferSource.getBuffer(RenderType.endGateway())
    poseStack.run {
      translate(0.5f, 0.5f, 0.5f)
      for (direction in Direction.entries)
        renderQuad(be, vc, poseStack, direction)
    }
  }

  private fun renderQuad(be: HoleBE, vertexConsumer: VertexConsumer, poseStack: PoseStack, direction: Direction) {
    val level = be.level ?: return
    if (Block.shouldRenderFace(be.blockState, level, be.blockPos, direction, be.blockPos.relative(direction)))
      return

    poseStack.use {
      mulPose(direction.rotation)
      translate(0f, 0.4995f, 0f)
      vertexConsumer.addVertex(poseStack.last(), Vector3f(-0.5f, 0f, -0.5f))
      vertexConsumer.addVertex(poseStack.last(), Vector3f(0.5f, 0f, -0.5f))
      vertexConsumer.addVertex(poseStack.last(), Vector3f(0.5f, 0f, 0.5f))
      vertexConsumer.addVertex(poseStack.last(), Vector3f(-0.5f, 0f, 0.5f))
    }
  }
}
