package me.alegian.thavma.impl.integration.emi.recipe

import dev.emi.emi.api.recipe.BasicEmiRecipe
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import me.alegian.thavma.impl.common.recipe.InfusionRecipe
import me.alegian.thavma.impl.integration.emi.EmiAspectWidget
import me.alegian.thavma.impl.rl
import net.minecraft.world.item.crafting.RecipeHolder
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private const val SLOT = 18
private const val RESULT_SLOT = 26
private const val ASPECT = 16
private const val PADDING = 8
private const val RADIUS = 32
private const val ARROW = 24
private const val WIDTH = 2 * RADIUS + 2 * PADDING + ARROW + SLOT + RESULT_SLOT
private const val HEIGHT = 2 * RADIUS + SLOT + PADDING + ASPECT

private val CIRCLE = EmiTexture(rl("textures/gui/jei/infusion_circles.png"), 0, 0, 64, 64, 64, 64, 64, 64)

class InfusionEmiRecipe(private val recipe: RecipeHolder<InfusionRecipe>) :
  BasicEmiRecipe(T7EmiCategories.INFUSION, recipe.id, WIDTH, HEIGHT) {
  init {
    inputs = recipe.value.ingredients.toList().map(EmiIngredient::of)
    outputs = listOf(EmiStack.of(recipe.value.result))
  }

  override fun addWidgets(widgets: WidgetHolder) {
    widgets.addTexture(CIRCLE, SLOT / 2, SLOT / 2)
    widgets.addTexture(EmiTexture.EMPTY_ARROW, 2 * RADIUS + SLOT + PADDING, RADIUS)

    widgets.addSlot(outputs[0], width - RESULT_SLOT, RADIUS - (RESULT_SLOT - SLOT) / 2).large(true).recipeContext(this)

    val components = recipe.value.components.toList().map(EmiIngredient::of)
    widgets.addSlot(EmiIngredient.of(recipe.value.base), RADIUS, RADIUS).drawBack(false)

    val angle = PI * 2 / components.size
    for ((i, component) in components.withIndex())
      widgets.addSlot(
        component,
        (sin(i * angle) * -RADIUS + RADIUS).toInt(),
        (cos(i * angle) * -RADIUS + RADIUS).toInt()
      ).drawBack(false)

    for ((i, stack) in recipe.value().aspects.withIndex())
      widgets.add(EmiAspectWidget(i * ASPECT, 2 * RADIUS + SLOT + PADDING, stack))
  }
}