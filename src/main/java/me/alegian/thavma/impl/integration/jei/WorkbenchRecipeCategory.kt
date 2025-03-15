package me.alegian.thavma.impl.integration.jei

import me.alegian.thavma.impl.common.recipe.WorkbenchRecipe
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ARCANE_WORKBENCH
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
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
  Component.translatable("gui.thavma.category.arcane_workbench"),
  guiHelper.createDrawableItemLike(ARCANE_WORKBENCH),
  WIDTH,
  HEIGHT
) {
  override fun setRecipe(builder: IRecipeLayoutBuilder, recipeHolder: RecipeHolder<WorkbenchRecipe>, focuses: IFocusGroup) {
    val recipe = recipeHolder.value()
    val resultItem = recipe.result
    val ingredients = recipe.ingredients

    val width = recipe.pattern.width()
    val height = recipe.pattern.height()
    builder.addOutputSlot(95, 19).addItemStack(resultItem).setOutputSlotBackground()


    val inputSlots: MutableList<IRecipeSlotBuilder> = ArrayList()
    for (y in 0..2) {
      for (x in 0..2) {
        val slot = builder.addInputSlot(x * 18 + 1, y * 18 + 1)
          .setStandardSlotBackground()
        inputSlots.add(slot)
      }
    }

    for (i in ingredients.indices) {
      val index = getCraftingIndex(i, width, height)
      val slot = inputSlots[index]

      val ingredient = ingredients[i]
      slot.addIngredients(ingredient)
    }
  }


  companion object {
    val WORKBENCH: RecipeType<RecipeHolder<WorkbenchRecipe>> = RecipeType.createRecipeHolderType(T7RecipeTypes.WORKBENCH.id)

    private fun getCraftingIndex(i: Int, width: Int, height: Int): Int {
      var index: Int
      if (width == 1) {
        index = if (height == 3) {
          (i * 3) + 1
        } else if (height == 2) {
          (i * 3) + 1
        } else {
          4
        }
      } else if (height == 1) {
        index = i + 3
      } else if (width == 2) {
        index = i
        if (i > 1) {
          index++
          if (i > 3) {
            index++
          }
        }
      } else if (height == 2) {
        index = i + 3
      } else {
        index = i
      }
      return index
    }
  }
}
