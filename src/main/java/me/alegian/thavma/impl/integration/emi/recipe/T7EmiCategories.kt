package me.alegian.thavma.impl.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiStack
import me.alegian.thavma.impl.common.recipe.translationId
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeTypes
import net.minecraft.network.chat.Component
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.registries.DeferredHolder

object T7EmiCategories {
  val WORKBENCH = T7EmiRecipeCategory(T7RecipeTypes.WORKBENCH, T7Blocks.ARCANE_WORKBENCH)
  val CRUCIBLE = T7EmiRecipeCategory(T7RecipeTypes.CRUCIBLE, T7Blocks.CRUCIBLE)
  val INFUSION = T7EmiRecipeCategory(T7RecipeTypes.INFUSION, T7Blocks.MATRIX)
}

class T7EmiRecipeCategory<T : Recipe<*>>(val recipeHolder: DeferredHolder<RecipeType<*>, RecipeType<T>>, icon: ItemLike) : EmiRecipeCategory(recipeHolder.id, EmiStack.of(icon.asItem())) {
  override fun getName() = Component.translatable(recipeHolder.get().translationId)
}