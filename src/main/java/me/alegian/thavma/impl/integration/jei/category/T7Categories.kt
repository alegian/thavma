package me.alegian.thavma.impl.integration.jei.category

import me.alegian.thavma.impl.common.recipe.CrucibleRecipe
import me.alegian.thavma.impl.common.recipe.WorkbenchRecipe
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeTypes
import mezz.jei.api.recipe.RecipeType

object T7Categories {
  val WORKBENCH = RecipeType.createRecipeHolderType<WorkbenchRecipe>(T7RecipeTypes.WORKBENCH.id)
  val CRUCIBLE = RecipeType.createRecipeHolderType<CrucibleRecipe>(T7RecipeTypes.CRUCIBLE.id)
}