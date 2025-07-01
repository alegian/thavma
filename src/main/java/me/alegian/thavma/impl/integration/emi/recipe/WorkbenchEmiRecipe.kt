package me.alegian.thavma.impl.integration.emi.recipe

import dev.emi.emi.api.recipe.BasicEmiRecipe
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import me.alegian.thavma.impl.common.recipe.WorkbenchRecipe
import me.alegian.thavma.impl.integration.emi.EmiAspectWidget
import me.alegian.thavma.impl.integration.emi.T7EmiCategories
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder

private const val SLOT = 18
private const val ASPECT = 16
private const val PADDING = 4
private const val WIDTH = 118
private const val HEIGHT = 3 * SLOT + PADDING + ASPECT

class WorkbenchEmiRecipe(private val recipe: RecipeHolder<WorkbenchRecipe>) :
  BasicEmiRecipe(T7EmiCategories.WORKBENCH, recipe.id, WIDTH, HEIGHT) {
  init {
    inputs = recipe.value.ingredients.toList().map(EmiIngredient::of)
    outputs = listOf(EmiStack.of(recipe.value.result))
  }

  override fun addWidgets(widgets: WidgetHolder) {
    widgets.addTexture(EmiTexture.EMPTY_ARROW, 60, 18)
    widgets.addSlot(outputs[0], 92, 14).large(true).recipeContext(this)

    var i = 0 // index of ingredient
    for (y in 0..2) {
      for (x in 0..2) {
        var emiIngredient: EmiIngredient = EmiStack.of(ItemStack.EMPTY)

        if (y < recipe.value.pattern.height() && x < recipe.value.pattern.width())
          emiIngredient = inputs[i]

        widgets.addSlot(emiIngredient, x * SLOT, y * SLOT)
        i++
      }
    }

    for ((i, stack) in recipe.value().aspects.withIndex())
      widgets.add(EmiAspectWidget(i * ASPECT, 3 * SLOT + PADDING, stack))
  }
}