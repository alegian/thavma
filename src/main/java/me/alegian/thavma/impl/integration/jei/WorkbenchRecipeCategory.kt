package me.alegian.thavma.impl.integration.jei

import me.alegian.thavma.impl.common.recipe.WorkbenchRecipe
import me.alegian.thavma.impl.common.recipe.translationId
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ARCANE_WORKBENCH
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.network.chat.Component
import net.minecraft.world.item.crafting.RecipeHolder

private const val WIDTH: Int = 116
private const val HEIGHT: Int = 54

internal class WorkbenchRecipeCategory(guiHelper: IGuiHelper) : AbstractRecipeCategory<RecipeHolder<WorkbenchRecipe>>(
  WORKBENCH,
  Component.translatable(T7RecipeTypes.WORKBENCH.get().translationId),
  guiHelper.createDrawableItemLike(ARCANE_WORKBENCH),
  WIDTH,
  HEIGHT
) {
  override fun setRecipe(builder: IRecipeLayoutBuilder, recipeHolder: RecipeHolder<WorkbenchRecipe>, focuses: IFocusGroup) {
    val recipe = recipeHolder.value()
    val resultItem = recipe.result
    val ingredients = recipe.ingredients

    builder.addOutputSlot(95, 19).addItemStack(resultItem).setOutputSlotBackground()

    var i = 0 // index of ingredient
    for (y in 0..2) {
      for (x in 0..2) {
        val slot = builder.addInputSlot(x * 18 + 1, y * 18 + 1).setStandardSlotBackground()

        if (y < recipe.pattern.height() && x < recipe.pattern.width())
          slot.addIngredients(ingredients[i++])
      }
    }
  }

  companion object {
    val WORKBENCH = RecipeType.createRecipeHolderType<WorkbenchRecipe>(T7RecipeTypes.WORKBENCH.id)
  }
}
