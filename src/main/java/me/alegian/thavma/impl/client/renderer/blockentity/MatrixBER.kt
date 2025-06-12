package me.alegian.thavma.impl.client.renderer.blockentity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Axis
import me.alegian.thavma.impl.client.renderer.geo.layer.EmissiveGeoLayer
import me.alegian.thavma.impl.client.renderer.level.renderEssentia
import me.alegian.thavma.impl.client.util.translate
import me.alegian.thavma.impl.common.block.entity.MatrixBE
import me.alegian.thavma.impl.common.block.entity.itemHandler
import me.alegian.thavma.impl.common.data.capability.firstStack
import me.alegian.thavma.impl.common.infusion.trajectoryLength
import me.alegian.thavma.impl.common.util.use
import me.alegian.thavma.impl.init.registries.deferred.T7DataComponents.INFUSION_STATE
import me.alegian.thavma.impl.rl
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.phys.AABB
import software.bernie.geckolib.cache.`object`.BakedGeoModel
import software.bernie.geckolib.cache.`object`.GeoBone
import software.bernie.geckolib.model.DefaultedBlockGeoModel
import software.bernie.geckolib.renderer.GeoBlockRenderer
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.unaryMinus

class MatrixBER : GeoBlockRenderer<MatrixBE>(DefaultedBlockGeoModel(rl("infusion_matrix"))) {
  init {
    addRenderLayer(EmissiveGeoLayer<MatrixBE>(this))
  }

  override fun getRenderBoundingBox(blockEntity: MatrixBE) = AABB.INFINITE

  override fun renderRecursively(poseStack: PoseStack, animatable: MatrixBE, bone: GeoBone, renderType: RenderType, bufferSource: MultiBufferSource, buffer: VertexConsumer, isReRender: Boolean, partialTick: Float, packedLight: Int, packedOverlay: Int, colour: Int) {
    if (bone.name == "bone" && !animatable.hasRing ) return
    super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour)
  }

  override fun actuallyRender(poseStack: PoseStack, be: MatrixBE, model: BakedGeoModel, renderType: RenderType?, bufferSource: MultiBufferSource, buffer: VertexConsumer?, isReRender: Boolean, partialTick: Float, packedLight: Int, packedOverlay: Int, colour: Int) {
    super.actuallyRender(poseStack, be, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour)

    renderItem(be, poseStack, bufferSource, partialTick, packedLight, packedOverlay)

    val flyingAspects = be.get(INFUSION_STATE)?.flyingAspects
    if (flyingAspects?.isEmpty() ?: true) return

    poseStack.use {
      translate(-be.blockPos.center)
      val flyingStream = flyingAspects.filterNotNull().associate {
        Pair(
          it.blockPos.center,
          trajectoryLength(it.blockPos.center, be.drainPos)
        )
      }
      val ticks = Minecraft.getInstance().levelRenderer.ticks + partialTick
      for (f in flyingStream) {
        val firstIndex = flyingAspects.indexOfFirst { f.key == it?.blockPos?.center }
        val head = f.value - 1 - firstIndex
        val length = 1 + flyingAspects.indexOfLast { f.key == it?.blockPos?.center } - firstIndex
        val aspect = flyingAspects[firstIndex]?.aspectStack?.aspect ?: continue
        val colorWithAlpha = 0x44000000 or (aspect.color and 0xffffff)
        if (head < 0) continue
        renderEssentia(f.key, be.drainPos, head, length, poseStack, Minecraft.getInstance().renderBuffers().bufferSource(), ticks, colorWithAlpha)
      }
    }
  }
}

// item rotation speed
private const val DEGREES_PER_TICK = 5

private fun renderItem(be: MatrixBE, poseStack: PoseStack, bufferSource: MultiBufferSource, partialTick: Float, packedLight: Int, packedOverlay: Int) {
  val stack = be.itemHandler?.firstStack ?: return
  if (stack.isEmpty) return

  val itemRenderer = Minecraft.getInstance().itemRenderer
  val level = be.level ?: return

  val randSeed = be.blockPos.asLong().toInt()
  poseStack.use {
    translate(0f, 0.5f, 0f)
    mulPose(Axis.YP.rotationDegrees(((level.gameTime + partialTick) * DEGREES_PER_TICK) % 360))
    itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, packedLight, packedOverlay, poseStack, bufferSource, level, randSeed)
  }
}
