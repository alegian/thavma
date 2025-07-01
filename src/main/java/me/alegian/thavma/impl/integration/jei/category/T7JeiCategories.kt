package me.alegian.thavma.impl.integration.jei.category

import me.alegian.thavma.impl.init.registries.deferred.T7RecipeTypes
import mezz.jei.api.recipe.RecipeType

object T7JeiCategories {
  val WORKBENCH = RecipeType.createFromVanilla(T7RecipeTypes.WORKBENCH.get())
  val CRUCIBLE = RecipeType.createFromVanilla(T7RecipeTypes.CRUCIBLE.get())
  val INFUSION = RecipeType.createFromVanilla(T7RecipeTypes.INFUSION.get())
}