package me.alegian.thavma.impl.integration.jei.category

import me.alegian.thavma.impl.common.recipe.InfusionRecipe
import me.alegian.thavma.impl.common.recipe.translationId
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeTypes
import me.alegian.thavma.impl.rl
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.network.chat.Component
import net.minecraft.world.item.crafting.RecipeHolder
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private const val SLOT = 18
private const val RESULT_SLOT = 26
private const val ASPECT = 16
private const val PADDING = 8
private const val RADIUS = 32
private const val ARROW = 22
private const val WIDTH = 2 * RADIUS + 2 * PADDING + ARROW + SLOT + RESULT_SLOT
private const val HEIGHT = 2 * RADIUS + SLOT + PADDING + ASPECT

internal class InfusionRecipeCategory(guiHelper: IGuiHelper) : AbstractRecipeCategory<RecipeHolder<InfusionRecipe>>(
  T7Categories.INFUSION,
  Component.translatable(T7RecipeTypes.INFUSION.get().translationId),
  guiHelper.createDrawableItemLike(T7Blocks.MATRIX),
  WIDTH,
  HEIGHT
) {
  val circlesDrawable = guiHelper.drawableBuilder(rl("textures/gui/jei/infusion_circles.png"), 0, 0, 64, 64).setTextureSize(64, 64).build()

  override fun setRecipe(builder: IRecipeLayoutBuilder, recipeHolder: RecipeHolder<InfusionRecipe>, focuses: IFocusGroup) {
    val recipe = recipeHolder.value()
    val resultItem = recipe.result

    builder.addOutputSlot(width + (RESULT_SLOT - 16) / 2 - RESULT_SLOT, RADIUS).addItemStack(resultItem).setOutputSlotBackground()

    val baseX = RADIUS + 1
    val baseY = RADIUS + 1
    builder.addInputSlot(baseX, baseY).addIngredients(recipe.base)

    val angle = PI * 2 / recipe.ingredients.size
    for ((i, ingredient) in recipe.ingredients.withIndex())
      builder.addInputSlot(
        (sin(i * angle) * -RADIUS + baseY).toInt(),
        (cos(i * angle) * -RADIUS + baseX).toInt()
      ).addIngredients(ingredient)
  }

  override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RecipeHolder<InfusionRecipe>, focuses: IFocusGroup) {
    builder.addDrawable(circlesDrawable).setPosition(SLOT / 2, SLOT / 2)
    builder.addRecipeArrow().setPosition(2 * RADIUS + SLOT + PADDING, RADIUS)

    for ((i, stack) in recipe.value.aspects.withIndex())
      builder.addWidget(_root_ide_package_.me.alegian.thavma.impl.integration.jei.AspectWidget(stack, i * ASPECT, 2 * RADIUS + SLOT + PADDING))
  }
}
