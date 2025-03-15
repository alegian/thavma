package me.alegian.thavma.impl.common.recipe

import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeSerializers.ARCANE_WORKBENCH
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeTypes.WORKBENCH
import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level

class WorkbenchRecipe(val pattern: ShapedRecipePattern, result: ItemStack, aspects: AspectMap) : T7ItemRecipe<CraftingInput>(result) {
  val aspects = aspects
    get() = field.copy()

  override fun matches(input: CraftingInput, level: Level): Boolean {
    return pattern.matches(input)
  }

  override fun getIngredients(): NonNullList<Ingredient> {
    return pattern.ingredients()
  }

  override fun getSerializer(): RecipeSerializer<*> {
    return ARCANE_WORKBENCH.get()
  }

  override fun getType(): RecipeType<*> {
    return WORKBENCH.get()
  }
}
