package me.alegian.thavma.impl.client.renderer.geo

import me.alegian.thavma.impl.common.item.WandItem
import me.alegian.thavma.impl.common.wand.WandCoreMaterial
import me.alegian.thavma.impl.common.wand.WandHandleMaterial
import me.alegian.thavma.impl.rl
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.cache.`object`.GeoBone
import software.bernie.geckolib.model.DefaultedItemGeoModel
import software.bernie.geckolib.renderer.specialty.DynamicGeoItemRenderer

class WandRenderer(handleMaterial: WandHandleMaterial, coreMaterial: WandCoreMaterial) : DynamicGeoItemRenderer<WandItem>(DefaultedItemGeoModel(rl("wand"))) {
  private val handleLocation = handleTexture(handleMaterial.registeredLocation)
  private val coreLocation = coreTexture(coreMaterial.registeredLocation)

  override fun getTextureOverrideForBone(bone: GeoBone, animatable: WandItem, partialTick: Float): ResourceLocation? {
    return when (bone.name) {
      "handle" -> this.handleLocation
      "stick" -> this.coreLocation
      else -> null
    }
  }

  companion object {
    private fun handleTexture(registeredLocation: ResourceLocation) = texture(registeredLocation, "wand_handle_")

    private fun coreTexture(registeredLocation: ResourceLocation) = texture(registeredLocation, "wand_core_")

    private fun texture(registeredLocation: ResourceLocation, prefix: String)=
      registeredLocation.withPrefix("textures/item/$prefix").withSuffix(".png")
  }
}
