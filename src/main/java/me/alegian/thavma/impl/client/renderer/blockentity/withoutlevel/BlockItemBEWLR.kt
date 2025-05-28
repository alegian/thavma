package me.alegian.thavma.impl.client.renderer.blockentity.withoutlevel

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity

class BlockItemBEWLR(private val blockEntity: BlockEntity) : BlockEntityWithoutLevelRenderer(Minecraft.getInstance().blockEntityRenderDispatcher, Minecraft.getInstance().entityModels) {
  override fun renderByItem(stack: ItemStack, displayContext: ItemDisplayContext, poseStack: PoseStack, buffer: MultiBufferSource, packedLight: Int, packedOverlay: Int) {
    val ber = Minecraft.getInstance().blockEntityRenderDispatcher.getRenderer(blockEntity) ?: return
    ber.render(blockEntity, 0f, poseStack, buffer, packedLight, packedOverlay)
  }
}
