package me.alegian.thavma.impl.integration.jei.category

import me.alegian.thavma.impl.common.recipe.WorkbenchRecipe
import me.alegian.thavma.impl.common.recipe.translationId
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ARCANE_WORKBENCH
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeTypes
import me.alegian.thavma.impl.integration.jei.JeiAspectWidget
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.network.chat.Component
import net.minecraft.world.item.crafting.RecipeHolder

private const val SLOT = 18
private const val ASPECT = 16
private const val PADDING = 4
private const val WIDTH = 116
private const val HEIGHT = 3 * SLOT + PADDING + ASPECT

internal class WorkbenchRecipeCategory(guiHelper: IGuiHelper) : AbstractRecipeCategory<RecipeHolder<WorkbenchRecipe>>(
  T7JeiCategories.WORKBENCH,
  Component.translatable(T7RecipeTypes.WORKBENCH.get().translationId),
  guiHelper.createDrawableItemLike(ARCANE_WORKBENCH),
  WIDTH,
  HEIGHT
) {
  override fun setRecipe(builder: IRecipeLayoutBuilder, recipeHolder: RecipeHolder<WorkbenchRecipe>, focuses: IFocusGroup) {
    val recipe = recipeHolder.value()
    val resultItem = recipe.result
    val ingredients = recipe.ingredients

    builder.addOutputSlot(95, SLOT + 1).addItemStack(resultItem).setOutputSlotBackground()

    var i = 0 // index of ingredient
    for (y in 0..2) {
      for (x in 0..2) {
        val slot = builder.addInputSlot(x * SLOT + 1, y * SLOT + 1).setStandardSlotBackground()

        if (y < recipe.pattern.height() && x < recipe.pattern.width())
          slot.addIngredients(ingredients[i++])
      }
    }
  }

  override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RecipeHolder<WorkbenchRecipe>, focuses: IFocusGroup) {
    val arrow = builder.addRecipeArrow()
    arrow.setPosition(61, (3 * SLOT - arrow.height) / 2)

    for ((i, stack) in recipe.value().aspects.withIndex())
      builder.addWidget(JeiAspectWidget(stack, i * ASPECT, 3 * SLOT + PADDING))
  }
}
