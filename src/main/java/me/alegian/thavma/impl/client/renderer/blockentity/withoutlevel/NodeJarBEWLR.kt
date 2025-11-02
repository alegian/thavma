package me.alegian.thavma.impl.client.renderer.blockentity.withoutlevel

import com.mojang.blaze3d.vertex.PoseStack
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SEALING_JAR
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.client.RenderTypeHelper
import net.neoforged.neoforge.client.model.data.ModelData

object NodeJarBEWLR : BlockEntityWithoutLevelRenderer(Minecraft.getInstance().blockEntityRenderDispatcher, Minecraft.getInstance().entityModels) {
  override fun renderByItem(stack: ItemStack, displayContext: ItemDisplayContext, poseStack: PoseStack, buffer: MultiBufferSource, packedLight: Int, packedOverlay: Int) {
    val renderType = RenderTypeHelper.getEntityRenderType(RenderType.translucent(), false)
    Minecraft.getInstance().blockRenderer.renderSingleBlock(SEALING_JAR.get().defaultBlockState(), poseStack, buffer, packedLight, packedOverlay, ModelData.EMPTY, renderType)
  }
}
