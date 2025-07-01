package me.alegian.thavma.impl.integration.emi.recipe

import dev.emi.emi.api.recipe.BasicEmiRecipe
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import me.alegian.thavma.impl.common.recipe.CrucibleRecipe
import me.alegian.thavma.impl.integration.emi.EmiAspectWidget
import me.alegian.thavma.impl.rl
import net.minecraft.world.item.crafting.RecipeHolder

private const val SLOT = 18
private const val RESULT_SLOT = 26
private const val ASPECT = 16
private const val PADDING = 8
private const val WIDTH = 118
private const val HEIGHT = RESULT_SLOT + PADDING + ASPECT

private val ARROWS = EmiTexture(rl("textures/gui/jei/crucible_arrows.png"), 0, 0, 46, 31, 46, 31, 46, 31)

class CrucibleEmiRecipe(private val recipe: RecipeHolder<CrucibleRecipe>) :
  BasicEmiRecipe(T7EmiCategories.CRUCIBLE, recipe.id, WIDTH, HEIGHT) {
  init {
    inputs = recipe.value.ingredients.toList().map(EmiIngredient::of)
    outputs = listOf(EmiStack.of(recipe.value.result))
  }

  override fun addWidgets(widgets: WidgetHolder) {
    widgets.addTexture(ARROWS, (width + SLOT - RESULT_SLOT - ARROWS.width) / 2, (RESULT_SLOT - ARROWS.height) / 2)

    widgets.addSlot(outputs[0], 92, 0).large(true).recipeContext(this)

    widgets.addSlot(inputs[0], 0, (RESULT_SLOT - SLOT) / 2)

    val offsetX = (width + SLOT - RESULT_SLOT - recipe.value.aspects.size * ASPECT) / 2
    for ((i, stack) in recipe.value().aspects.withIndex())
      widgets.add(EmiAspectWidget(offsetX + i * ASPECT, RESULT_SLOT + PADDING, stack))
  }
}