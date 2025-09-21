package me.alegian.thavma.impl.client.renderer.geo

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import me.alegian.thavma.impl.common.item.WandItem
import me.alegian.thavma.impl.common.wand.WandCoreMaterial
import me.alegian.thavma.impl.common.wand.WandPlatingMaterial
import me.alegian.thavma.impl.init.registries.deferred.T7DataComponents
import me.alegian.thavma.impl.rl
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import software.bernie.geckolib.cache.`object`.BakedGeoModel
import software.bernie.geckolib.cache.`object`.GeoBone
import software.bernie.geckolib.model.DefaultedItemGeoModel
import software.bernie.geckolib.renderer.layer.GeoRenderLayer
import software.bernie.geckolib.renderer.specialty.DynamicGeoItemRenderer

class WandRenderer(handleMaterial: WandPlatingMaterial, coreMaterial: WandCoreMaterial) : DynamicGeoItemRenderer<WandItem>(DefaultedItemGeoModel(rl("wand"))) {
  private val handleLocation = handleTexture(handleMaterial.registeredLocation)
  private val coreLocation = coreTexture(coreMaterial.registeredLocation)

  init {
    addRenderLayer(FocusRenderLayer(this))
  }

  override fun getTextureOverrideForBone(bone: GeoBone, animatable: WandItem, partialTick: Float): ResourceLocation? {
    return when (bone.name) {
      "plating" -> this.handleLocation
      "circle" -> this.handleLocation
      "core" -> this.coreLocation
      else -> null
    }
  }

  companion object {
    private fun handleTexture(registeredLocation: ResourceLocation) = texture(registeredLocation, "wand_plating_")

    private fun coreTexture(registeredLocation: ResourceLocation) = texture(registeredLocation, "wand_core_")

    private fun texture(registeredLocation: ResourceLocation, prefix: String) =
      registeredLocation.withPrefix("textures/item/$prefix").withSuffix(".png")
  }
}

private class FocusRenderLayer(val wandRenderer: WandRenderer) : GeoRenderLayer<WandItem>(wandRenderer) {
  override fun render(poseStack: PoseStack, wand: WandItem, bakedModel: BakedGeoModel, renderType: RenderType?, bufferSource: MultiBufferSource, buffer: VertexConsumer?, partialTick: Float, packedLight: Int, packedOverlay: Int) {
    val mc = Minecraft.getInstance()
    val focus = wandRenderer.currentItemStack.get(T7DataComponents.FOCUS)?.nonEmptyItems()?.firstOrNull() ?: return
    val focusModel = mc.itemRenderer.getModel(focus, null, null, 0)
    val vc = bufferSource.getBuffer(RenderType.cutout())
    poseStack.run {
      translate(0f, 1.125f, 0f)
      mc.itemRenderer.renderModelLists(focusModel, ItemStack.EMPTY, packedLight, packedOverlay, poseStack, vc)
    }
  }
}
