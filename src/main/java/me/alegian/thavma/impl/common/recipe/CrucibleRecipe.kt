package me.alegian.thavma.impl.common.recipe

import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeSerializers
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeTypes
import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level

/**
 * Catalyst Items should be tagged as such, otherwise the crucible is going to melt them anyway
 */
class CrucibleRecipe(aspects: AspectMap, val catalyst: Ingredient, result: ItemStack) : T7ItemRecipe<SingleRecipeInput>(result) {
  val aspects = aspects
    get() = field.copy()

  override fun matches(input: SingleRecipeInput, level: Level): Boolean {
    return catalyst.test(input.getItem(0))
  }

  override fun getIngredients(): NonNullList<Ingredient> {
    return NonNullList.of(this.catalyst)
  }

  override fun getSerializer(): RecipeSerializer<*> {
    return T7RecipeSerializers.CRUCIBLE.get()
  }

  override fun getType(): RecipeType<*> {
    return T7RecipeTypes.CRUCIBLE.get()
  }
}
