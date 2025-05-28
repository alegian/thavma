package me.alegian.thavma.impl.client.renderer.entity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import me.alegian.thavma.impl.client.renderer.calculatePlayerAngle
import me.alegian.thavma.impl.common.util.use
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.block.model.ItemTransforms
import net.minecraft.client.renderer.entity.EnderDragonRenderer
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.ItemEntityRenderer
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.item.ItemDisplayContext
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions

class FancyItemER(pContext: EntityRendererProvider.Context) : EntityRenderer<ItemEntity>(pContext) {
  private val random: RandomSource = RandomSource.create()

  override fun render(pEntity: ItemEntity, pEntityYaw: Float, pPartialTicks: Float, pPoseStack: PoseStack, pBuffer: MultiBufferSource, pPackedLight: Int) {
    // WARNING: the two render method's poses are coupled
    pPoseStack.use {
      renderItem(pEntity, pPoseStack, pBuffer, pPartialTicks, pPackedLight)
      renderEnderDragonRays(pEntity, pPoseStack, pBuffer, pPartialTicks)
    }

    super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight)
  }

  override fun getTextureLocation(pEntity: ItemEntity) = InventoryMenu.BLOCK_ATLAS

  private fun renderItem(pEntity: ItemEntity, pPoseStack: PoseStack, pBuffer: MultiBufferSource, pPartialTicks: Float, pPackedLight: Int) {
    val itemRenderer = Minecraft.getInstance().itemRenderer
    val stack = pEntity.item
    random.setSeed(ItemEntityRenderer.getSeedForItemStack(stack).toLong())
    val bakedModel = itemRenderer.getModel(stack, pEntity.level(), null, pEntity.id)
    val flag = bakedModel.isGui3d
    val shouldBob = IClientItemExtensions.of(stack).shouldBobAsEntity(stack)
    val bobOffset = if (shouldBob) Mth.sin((pEntity.age.toFloat() + pPartialTicks) / 10.0f + pEntity.bobOffs) * 0.1f + 0.1f else 0f
    val scale = ItemTransforms.NO_TRANSFORMS.getTransform(ItemDisplayContext.GROUND).scale.y()
    pPoseStack.translate(0.0f, bobOffset + 0.25f * scale, 0.0f)
    // always faces player
    val angle = calculatePlayerAngle(pEntity.eyePosition)
    pPoseStack.mulPose(Axis.YP.rotation(angle))
    ItemEntityRenderer.renderMultipleFromCount(itemRenderer, pPoseStack, pBuffer, pPackedLight, stack, bakedModel, flag, random)
    // prepare for dragon rays render
    pPoseStack.translate(0f, 0f, -.5f)
    pPoseStack.mulPose(Axis.YP.rotation(-angle))
  }

  private fun renderEnderDragonRays(pEntity: ItemEntity, pPoseStack: PoseStack, pBuffer: MultiBufferSource, pPartialTicks: Float) {
    val spin = pEntity.getSpin(pPartialTicks)
    pPoseStack.mulPose(Axis.YP.rotation(spin))
    pPoseStack.scale(.1f, .1f, .1f)
    EnderDragonRenderer.renderRays(pPoseStack, 0.7f, pBuffer.getBuffer(RenderType.dragonRays()))
    EnderDragonRenderer.renderRays(pPoseStack, 0.7f, pBuffer.getBuffer(RenderType.dragonRaysDepth()))
  }
}
