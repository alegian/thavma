package me.alegian.thavma.impl.common.recipe;

import me.alegian.thavma.impl.common.aspect.AspectMap;
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeSerializers;
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class WorkbenchRecipe implements Recipe<CraftingInput> {
  protected final ShapedRecipePattern pattern;
  protected final ItemStack resultItem;
  protected final AspectMap resultAspects;

  public WorkbenchRecipe(ShapedRecipePattern pattern, ItemStack resultItem, AspectMap resultAspects) {
    this.pattern = pattern;
    this.resultItem = resultItem;
    this.resultAspects = resultAspects;
  }

  @Override
  public boolean matches(CraftingInput input, Level level) {
    return this.pattern.matches(input);
  }

  @Override
  public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
    return this.getResultItem(registries).copy();
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width >= this.pattern.width() && height >= this.pattern.height();
  }

  /**
   * For recipe book & JEI
   */
  @Override
  public ItemStack getResultItem(HolderLookup.Provider registries) {
    return this.resultItem;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return T7RecipeSerializers.INSTANCE.getARCANE_WORKBENCH().get();
  }

  @Override
  public RecipeType<?> getType() {
    return T7RecipeTypes.INSTANCE.getWORKBENCH().get();
  }

  public AspectMap assembleAspects() {
    return this.resultAspects.copy();
  }

  public ShapedRecipePattern getPattern() {
    return this.pattern;
  }

  public ItemStack getResultItem() {
    return this.resultItem;
  }

  public AspectMap getResultAspects() {
    return this.resultAspects;
  }
}
