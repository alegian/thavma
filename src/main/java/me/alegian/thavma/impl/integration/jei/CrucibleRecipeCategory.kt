package me.alegian.thavma.impl.integration.jei

import me.alegian.thavma.impl.common.recipe.CrucibleRecipe
import me.alegian.thavma.impl.common.recipe.translationId
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.network.chat.Component
import net.minecraft.world.item.crafting.RecipeHolder

private const val WIDTH: Int = 116
private const val HEIGHT: Int = 18 + 4 + 16

internal class CrucibleRecipeCategory(guiHelper: IGuiHelper) : AbstractRecipeCategory<RecipeHolder<CrucibleRecipe>>(
  CRUCIBLE,
  Component.translatable(T7RecipeTypes.CRUCIBLE.get().translationId),
  guiHelper.createDrawableItemLike(T7Blocks.CRUCIBLE),
  WIDTH,
  HEIGHT
) {
  override fun setRecipe(builder: IRecipeLayoutBuilder, recipeHolder: RecipeHolder<CrucibleRecipe>, focuses: IFocusGroup) {
    val recipe = recipeHolder.value()
    val resultItem = recipe.result

    builder.addOutputSlot(95, 1).addItemStack(resultItem).setOutputSlotBackground()

    val inputSlot = builder.addInputSlot(1, 1).setStandardSlotBackground()
    inputSlot.addIngredients(recipe.catalyst)
  }

  override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RecipeHolder<CrucibleRecipe>, focuses: IFocusGroup) {
    val arrow = builder.addRecipeArrow()
    arrow.setPosition(61, (18 - arrow.height) / 2)

    for ((i, stack) in recipe.value().aspects.withIndex())
      builder.addWidget(AspectWidget(stack, i * 16, 18 + 4))
  }

  companion object {
    val CRUCIBLE = RecipeType.createRecipeHolderType<CrucibleRecipe>(T7RecipeTypes.CRUCIBLE.id)
  }
}
