package me.alegian.thavma.impl.common.recipe

import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeSerializers.ARCANE_WORKBENCH
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeTypes.WORKBENCH
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.ShapedRecipePattern
import net.minecraft.world.level.Level

class WorkbenchRecipe(val pattern: ShapedRecipePattern, result: ItemStack, aspects: AspectMap) : T7ItemRecipe<CraftingInput>(result) {
  val aspects = aspects
    get() = field.copy()

  override fun matches(input: CraftingInput, level: Level) = pattern.matches(input)

  override fun getIngredients() = pattern.ingredients()

  override fun getSerializer() = ARCANE_WORKBENCH.get()

  override fun getType() = WORKBENCH.get()
}
