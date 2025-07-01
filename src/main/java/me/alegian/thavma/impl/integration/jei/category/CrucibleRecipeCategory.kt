package me.alegian.thavma.impl.integration.jei.category

import me.alegian.thavma.impl.common.recipe.CrucibleRecipe
import me.alegian.thavma.impl.common.recipe.translationId
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeTypes
import me.alegian.thavma.impl.integration.jei.JeiAspectWidget
import me.alegian.thavma.impl.rl
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.network.chat.Component
import net.minecraft.world.item.crafting.RecipeHolder

private const val WIDTH = 116
private const val SLOT = 18
private const val RESULT_SLOT = 26
private const val ASPECT = 16
private const val PADDING = 8
private const val HEIGHT = RESULT_SLOT + PADDING + ASPECT

internal class CrucibleRecipeCategory(guiHelper: IGuiHelper) : AbstractRecipeCategory<RecipeHolder<CrucibleRecipe>>(
  T7JeiCategories.CRUCIBLE,
  Component.translatable(T7RecipeTypes.CRUCIBLE.get().translationId),
  guiHelper.createDrawableItemLike(T7Blocks.CRUCIBLE),
  WIDTH,
  HEIGHT
) {
  val arrowsDrawable = guiHelper.drawableBuilder(rl("textures/gui/jei/crucible_arrows.png"), 0, 0, 46, 31).setTextureSize(46, 31).build()

  override fun setRecipe(builder: IRecipeLayoutBuilder, recipeHolder: RecipeHolder<CrucibleRecipe>, focuses: IFocusGroup) {
    val recipe = recipeHolder.value()
    val resultItem = recipe.result

    builder.addOutputSlot(95, (RESULT_SLOT - 16) / 2).addItemStack(resultItem).setOutputSlotBackground()

    val inputSlot = builder.addInputSlot(1, (RESULT_SLOT - 16) / 2).setStandardSlotBackground()
    inputSlot.addIngredients(recipe.catalyst)
  }

  override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RecipeHolder<CrucibleRecipe>, focuses: IFocusGroup) {
    val arrows = builder.addDrawable(arrowsDrawable)
    arrows.setPosition((width + SLOT - RESULT_SLOT - arrowsDrawable.width) / 2, (RESULT_SLOT - arrowsDrawable.height) / 2)

    // offset centers aspects
    val offsetX = (width + SLOT - RESULT_SLOT - recipe.value.aspects.size * ASPECT) / 2
    for ((i, stack) in recipe.value.aspects.withIndex())
      builder.addWidget(JeiAspectWidget(stack, offsetX + i * ASPECT, RESULT_SLOT + PADDING))
  }
}
