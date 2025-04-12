package me.alegian.thavma.impl.client.renderer.blockentity

import com.mojang.blaze3d.vertex.PoseStack
import me.alegian.thavma.impl.common.block.entity.ResearchTableBE
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.block.ModelBlockRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BedPart
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.AABB
import net.neoforged.neoforge.client.model.data.ModelData

class ResearchTableBER : BlockEntityRenderer<ResearchTableBE> {
  override fun render(be: ResearchTableBE, partialTick: Float, poseStack: PoseStack, bufferSource: MultiBufferSource, packedLight: Int, packedOverlay: Int) {
    renderBlockstate(be.blockState, poseStack, bufferSource, packedLight, packedOverlay)
  }

  /**
   * Render the blockstate JSON disregarding Block#getRenderShape.
   * Only the FOOT part renders.
   */
  private fun renderBlockstate(state: BlockState, poseStack: PoseStack, bufferSource: MultiBufferSource, packedLight: Int, packedOverlay: Int) {
    if (state.getValue(BlockStateProperties.BED_PART) != BedPart.FOOT) return

    val dispatcher = Minecraft.getInstance().blockRenderer
    val modelRenderer = ModelBlockRenderer(Minecraft.getInstance().blockColors)
    val renderType = RenderType.CUTOUT

    modelRenderer.renderModel(
      poseStack.last(),
      bufferSource.getBuffer(renderType),
      state,
      dispatcher.getBlockModel(state),
      1f,
      1f,
      1f,
      packedLight,
      packedOverlay,
      ModelData.EMPTY,
      renderType
    )
  }

  override fun shouldRenderOffScreen(blockEntity: ResearchTableBE): Boolean {
    return true
  }

  override fun getRenderBoundingBox(be: ResearchTableBE): AABB {
    return be.renderAABB
  }
}