package me.alegian.thavma.impl.common.recipe

import me.alegian.thavma.impl.init.registries.deferred.T7RecipeSerializers
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeTypes
import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level

class InfusionRecipe(val base: Ingredient, result: ItemStack, val ingredients: List<Ingredient>) : T7ItemRecipe<SingleRecipeInput>(result) {
  override fun matches(input: SingleRecipeInput, level: Level) = base.test(input.getItem(0))

  override fun getIngredients() = NonNullList.copyOf(ingredients + base)

  override fun getSerializer() = T7RecipeSerializers.INFUSION.get()

  override fun getType() = T7RecipeTypes.INFUSION.get()
}