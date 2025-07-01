package me.alegian.thavma.impl.common.recipe

import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeSerializers
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeTypes
import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level

/**
 * Catalyst Items should be tagged as such, otherwise the crucible is going to melt them anyway
 */
class CrucibleRecipe(aspects: AspectMap, val catalyst: Ingredient, result: ItemStack) : T7ItemRecipe<SingleRecipeInput>(result) {
  val aspects = aspects
    get() = field.copy()

  override fun matches(input: SingleRecipeInput, level: Level) = catalyst.test(input.getItem(0))

  override fun getIngredients() = NonNullList.copyOf(listOf(catalyst))

  override fun getSerializer() = T7RecipeSerializers.CRUCIBLE.get()

  override fun getType() = T7RecipeTypes.CRUCIBLE.get()
}
