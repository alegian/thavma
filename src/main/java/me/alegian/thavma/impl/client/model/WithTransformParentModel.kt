package me.alegian.thavma.impl.client.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.mojang.blaze3d.vertex.PoseStack
import me.alegian.thavma.impl.rl
import net.minecraft.client.renderer.block.model.BlockModel
import net.minecraft.client.renderer.block.model.ItemOverrides
import net.minecraft.client.renderer.block.model.ItemTransforms
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.model.*
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemDisplayContext
import net.neoforged.neoforge.client.model.BakedModelWrapper
import net.neoforged.neoforge.client.model.ElementsModel
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder
import net.neoforged.neoforge.client.model.generators.ModelBuilder
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.function.Function

/**
 * A custom model loader that adds "transform parent" to a block model.
 * The model renders normally, but inherits the ItemTransforms of the "transform parent".
 * Used by crucible, in order to inherit the default block-item transforms, since
 * cauldrons don't normally 3d block-items.
 */
class WithTransformParentModel(private val base: BlockModel, private val transformParentLocation: ResourceLocation) : IUnbakedGeometry<WithTransformParentModel> {
  var transformParent: BlockModel? = null

  override fun bake(context: IGeometryBakingContext, baker: ModelBaker, spriteGetter: Function<Material, TextureAtlasSprite>, modelState: ModelState, overrides: ItemOverrides): BakedModel {
    val bakedBase = ElementsModel(this.base.getElements()).bake(context, baker, spriteGetter, modelState, overrides)
    return Baked(bakedBase, transformParent ?: throw IllegalStateException("WithTransformsParentModel parent is null"))
  }

  override fun resolveParents(modelGetter: Function<ResourceLocation, UnbakedModel?>, context: IGeometryBakingContext) {
    this.base.resolveParents(modelGetter)
    val unbakedmodel = modelGetter.apply(this.transformParentLocation)

    if (unbakedmodel is BlockModel) this.transformParent = unbakedmodel
    else throw IllegalStateException("WithTransformsParentModel transform parent has to be a block model.")
  }

  object Loader : IGeometryLoader<WithTransformParentModel> {
    override fun read(jsonObject: JsonObject, context: JsonDeserializationContext): WithTransformParentModel {
      jsonObject.remove("loader")
      val transformParentLocation = ResourceLocation.parse(jsonObject.get(TRANSFORM_PARENT_KEY).asString)
      jsonObject.remove(TRANSFORM_PARENT_KEY)
      val base = context.deserialize<BlockModel>(jsonObject, BlockModel::class.java)

      return WithTransformParentModel(base, transformParentLocation)
    }
  }

  class Baked(base: BakedModel, var transformParent: BlockModel) : BakedModelWrapper<BakedModel>(base) {
    override fun getTransforms(): ItemTransforms {
      return this.transformParent.getTransforms()
    }

    override fun applyTransform(itemDisplayContext: ItemDisplayContext, poseStack: PoseStack, leftHand: Boolean): BakedModel {
      this.transforms.getTransform(itemDisplayContext).apply(leftHand, poseStack)
      return this
    }
  }

  class Builder<B : ModelBuilder<B>>(parent: B, existingFileHelper: ExistingFileHelper) : CustomLoaderBuilder<B>(
    ID,
    parent,
    existingFileHelper,
    false
  ) {
    private var transformParentLocation: ResourceLocation? = null

    fun transformParent(transformParentLocation: ResourceLocation): Builder<B> {
      this.transformParentLocation = transformParentLocation
      return this
    }

    override fun toJson(json: JsonObject): JsonObject {
      checkNotNull(this.transformParentLocation) { "Transform Parent location is required for WithTransformParentModel" }
      json.add(TRANSFORM_PARENT_KEY, JsonPrimitive(this.transformParentLocation.toString()))
      return super.toJson(json)
    }
  }

  companion object {
    val ID: ResourceLocation = rl("with_transform_parent")
    const val TRANSFORM_PARENT_KEY: String = "transform_parent"
  }
}
