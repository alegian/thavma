package me.alegian.thavma.impl.integration.jei

import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeTypes
import me.alegian.thavma.impl.rl
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.client.Minecraft

@JeiPlugin
internal class T7JEIPlugin : IModPlugin {
  override fun getPluginUid() = rl("jei")

  override fun registerCategories(registration: IRecipeCategoryRegistration) {
    registration.addRecipeCategories(WorkbenchRecipeCategory(registration.jeiHelpers.guiHelper))
    registration.addRecipeCategories(CrucibleRecipeCategory(registration.jeiHelpers.guiHelper))
  }

  override fun registerRecipes(registration: IRecipeRegistration) {
    val level = Minecraft.getInstance().level ?: return
    registration.addRecipes(WorkbenchRecipeCategory.WORKBENCH, level.recipeManager.getAllRecipesFor(T7RecipeTypes.WORKBENCH.get()))
    registration.addRecipes(CrucibleRecipeCategory.CRUCIBLE, level.recipeManager.getAllRecipesFor(T7RecipeTypes.CRUCIBLE.get()))
  }

  override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
    registration.addRecipeCatalyst(T7Blocks.ARCANE_WORKBENCH, WorkbenchRecipeCategory.WORKBENCH)
    registration.addRecipeCatalyst(T7Blocks.CRUCIBLE, CrucibleRecipeCategory.CRUCIBLE)
  }
}
