package me.alegian.thavma.impl.client.renderer.geo.layer

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Axis
import me.alegian.thavma.impl.common.block.entity.itemHandler
import me.alegian.thavma.impl.common.data.capability.firstStack
import me.alegian.thavma.impl.common.util.use
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.level.block.entity.BlockEntity
import software.bernie.geckolib.animatable.GeoAnimatable
import software.bernie.geckolib.cache.`object`.BakedGeoModel
import software.bernie.geckolib.renderer.GeoBlockRenderer
import software.bernie.geckolib.renderer.layer.GeoRenderLayer

// item rotation speed
private const val DEGREES_PER_TICK = 5

class ItemRenderLayer<T>(renderer: GeoBlockRenderer<T>, private val yOffset: Float) : GeoRenderLayer<T>(renderer) where T : BlockEntity, T : GeoAnimatable {
  override fun render(poseStack: PoseStack, be: T, bakedModel: BakedGeoModel, renderType: RenderType?, bufferSource: MultiBufferSource, buffer: VertexConsumer?, partialTick: Float, packedLight: Int, packedOverlay: Int) {
    val stack = be.itemHandler?.firstStack ?: return
    if (stack.isEmpty) return

    val itemRenderer = Minecraft.getInstance().itemRenderer
    val level = be.level ?: return

    val randSeed = be.blockPos.asLong().toInt()
    poseStack.use {
      translate(0f, yOffset, 0f)
      mulPose(Axis.YP.rotationDegrees(((level.gameTime + partialTick) * DEGREES_PER_TICK) % 360))
      itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, packedLight, packedOverlay, poseStack, bufferSource, level, randSeed)
    }
  }
}
