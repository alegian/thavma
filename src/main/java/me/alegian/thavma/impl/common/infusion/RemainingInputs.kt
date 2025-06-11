package me.alegian.thavma.impl.common.infusion

import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.common.recipe.InfusionRecipe
import net.minecraft.world.item.crafting.Ingredient

data class RemainingInputs(
  val ingredients: List<Ingredient>,
  val aspects: AspectMap
) {
  companion object {
    val CODEC = RecordCodecBuilder.create {
      it.group(
        Ingredient.LIST_CODEC.fieldOf("ingredients").forGetter(RemainingInputs::ingredients),
        AspectMap.CODEC.fieldOf("aspects").forGetter(RemainingInputs::aspects),
      ).apply(it, ::RemainingInputs)
    }

    fun of(recipe: InfusionRecipe) = RemainingInputs(recipe.ingredients, recipe.aspects)
  }
}