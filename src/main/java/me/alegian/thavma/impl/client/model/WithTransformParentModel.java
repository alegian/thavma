package me.alegian.thavma.impl.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.ElementsModel;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static me.alegian.thavma.impl.ThavmaKt.rl;

/**
 * A custom model loader that adds "transform parent" to a block model.
 * The model renders normally, but inherits the ItemTransforms of the "transform parent".
 * Used by crucible, in order to inherit the default block-item transforms, since
 * cauldrons don't normally 3d block-items.
 */
@OnlyIn(Dist.CLIENT)
public class WithTransformParentModel implements IUnbakedGeometry<WithTransformParentModel> {
  public static final ResourceLocation ID = rl("with_transform_parent");
  public static final String TRANSFORM_PARENT_KEY = "transform_parent";
  private final BlockModel base;
  private final ResourceLocation transformParentLocation;
  public BlockModel transformParent;

  public WithTransformParentModel(BlockModel base, ResourceLocation transformParentLocation) {
    this.base = base;
    this.transformParentLocation = transformParentLocation;
  }

  @Override
  public @NotNull BakedModel bake(@NotNull IGeometryBakingContext context, @NotNull ModelBaker baker, @NotNull Function<Material, TextureAtlasSprite> spriteGetter, @NotNull ModelState modelState, @NotNull ItemOverrides overrides) {
    BakedModel bakedBase = new ElementsModel(this.base.getElements()).bake(context, baker, spriteGetter, modelState, overrides);
    return new WithTransformParentModel.Baked(bakedBase, this.transformParent);
  }

  @Override
  public void resolveParents(@NotNull Function<ResourceLocation, UnbakedModel> modelGetter, @NotNull IGeometryBakingContext context) {
    this.base.resolveParents(modelGetter);
    UnbakedModel unbakedmodel = modelGetter.apply(this.transformParentLocation);

    if (unbakedmodel == null) modelGetter.apply(ModelBakery.MISSING_MODEL_LOCATION);

    if (unbakedmodel instanceof BlockModel blockmodel)
      this.transformParent = blockmodel;
    else
      throw new IllegalStateException("WithTransformsParentModel transform parent has to be a block model.");
  }

  public static class Loader implements IGeometryLoader<WithTransformParentModel> {
    public static final Loader INSTANCE = new Loader();

    private Loader() {
    }

    @Override
    public @NotNull WithTransformParentModel read(JsonObject jsonObject, @NotNull JsonDeserializationContext context) throws JsonParseException {
      jsonObject.remove("loader");
      var transformParentLocation = ResourceLocation.parse(jsonObject.get(WithTransformParentModel.TRANSFORM_PARENT_KEY).getAsString());
      jsonObject.remove(WithTransformParentModel.TRANSFORM_PARENT_KEY);
      BlockModel base = context.deserialize(jsonObject, BlockModel.class);

      return new WithTransformParentModel(base, transformParentLocation);
    }
  }

  public static class Baked extends BakedModelWrapper<BakedModel> implements IDynamicBakedModel {
    BlockModel transformParent;

    public Baked(BakedModel base, BlockModel transformParent) {
      super(base);
      this.transformParent = transformParent;
    }

    @Override
    public ItemTransforms getTransforms() {
      return this.transformParent.getTransforms();
    }

    @Override
    public BakedModel applyTransform(ItemDisplayContext itemDisplayContext, PoseStack poseStack, boolean leftHand) {
      this.getTransforms().getTransform(itemDisplayContext).apply(leftHand, poseStack);
      return this;
    }
  }

  public static class Builder<B extends ModelBuilder<B>> extends CustomLoaderBuilder<B> {
    private ResourceLocation transformParentLocation;

    public Builder(B parent, ExistingFileHelper existingFileHelper) {
      super(
          WithTransformParentModel.ID,
          parent,
          existingFileHelper,
          false
      );
    }

    public WithTransformParentModel.Builder<B> transformParent(ResourceLocation transformParentLocation) {
      this.transformParentLocation = transformParentLocation;
      return this;
    }

    @Override
    public @NotNull JsonObject toJson(@NotNull JsonObject json) {
      if (this.transformParentLocation == null)
        throw new IllegalStateException("Transform Parent location is required for WithTransformParentModel");
      json.add(WithTransformParentModel.TRANSFORM_PARENT_KEY, new JsonPrimitive(this.transformParentLocation.toString()));
      return super.toJson(json);
    }
  }
}
