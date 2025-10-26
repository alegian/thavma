package me.alegian.thavma.impl.client.renderer.blockentity.withoutlevel

import com.mojang.blaze3d.vertex.PoseStack
import me.alegian.thavma.impl.client.renderer.blockentity.SealingJarBER
import me.alegian.thavma.impl.init.registries.T7Capabilities
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack

object SealingJarBEWLR : BlockEntityWithoutLevelRenderer(Minecraft.getInstance().blockEntityRenderDispatcher, Minecraft.getInstance().entityModels) {
  override fun renderByItem(stack: ItemStack, displayContext: ItemDisplayContext, poseStack: PoseStack, buffer: MultiBufferSource, packedLight: Int, packedOverlay: Int) {
    stack.getCapability(T7Capabilities.AspectContainer.ITEM)?.let { SealingJarBER.renderContents(it, poseStack, buffer, packedLight) }
  }
}
