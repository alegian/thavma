package me.alegian.thavma.impl.integration.jei

import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import me.alegian.thavma.impl.init.registries.deferred.T7Items
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeTypes
import me.alegian.thavma.impl.integration.RecipeViewerAliases
import me.alegian.thavma.impl.integration.RecipeViewerDescriptions
import me.alegian.thavma.impl.integration.jei.recipe.CrucibleRecipeCategory
import me.alegian.thavma.impl.integration.jei.recipe.InfusionRecipeCategory
import me.alegian.thavma.impl.integration.jei.recipe.T7JeiCategories
import me.alegian.thavma.impl.integration.jei.recipe.WorkbenchRecipeCategory
import me.alegian.thavma.impl.rl
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.registration.*
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component

@JeiPlugin
internal class T7JeiPlugin : IModPlugin {
  override fun getPluginUid() = rl("jei")

  override fun registerCategories(registration: IRecipeCategoryRegistration) {
    registration.addRecipeCategories(WorkbenchRecipeCategory(registration.jeiHelpers.guiHelper))
    registration.addRecipeCategories(CrucibleRecipeCategory(registration.jeiHelpers.guiHelper))
    registration.addRecipeCategories(InfusionRecipeCategory(registration.jeiHelpers.guiHelper))
  }

  override fun registerRecipes(registration: IRecipeRegistration) {
    val level = Minecraft.getInstance().level ?: return
    registration.addRecipes(T7JeiCategories.WORKBENCH, level.recipeManager.getAllRecipesFor(T7RecipeTypes.WORKBENCH.get()))
    registration.addRecipes(T7JeiCategories.CRUCIBLE, level.recipeManager.getAllRecipesFor(T7RecipeTypes.CRUCIBLE.get()))
    registration.addRecipes(T7JeiCategories.INFUSION, level.recipeManager.getAllRecipesFor(T7RecipeTypes.INFUSION.get()))

    registration.addIngredientInfo(T7Items.ROTTEN_BRAIN, Component.translatable(RecipeViewerDescriptions.ROTTEN_BRAIN))
    registration.addIngredientInfo(T7Items.BOOK, Component.translatable(RecipeViewerDescriptions.BOOK))
    registration.addIngredientInfo(T7Blocks.ARCANE_WORKBENCH, Component.translatable(RecipeViewerDescriptions.ARCANE_WORKBENCH))
    registration.addIngredientInfo(T7Blocks.RESEARCH_TABLE, Component.translatable(RecipeViewerDescriptions.RESEARCH_TABLE))
    registration.addIngredientInfo(T7Blocks.CRUCIBLE, Component.translatable(RecipeViewerDescriptions.CRUCIBLE))
    registration.addIngredientInfo(T7Blocks.GREATWOOD_LOG, Component.translatable(RecipeViewerDescriptions.GREATWOOD))
    registration.addIngredientInfo(T7Blocks.SILVERWOOD_LOG, Component.translatable(RecipeViewerDescriptions.SILVERWOOD))
    registration.addIngredientInfo(T7Blocks.PILLAR, Component.translatable(RecipeViewerDescriptions.PILLAR))
    registration.addIngredientInfo(T7Items.RESEARCH_SCROLL, Component.translatable(RecipeViewerDescriptions.RESEARCH_SCROLL))
    for (infusedBlock in T7Blocks.INFUSED_STONES.values + T7Blocks.INFUSED_DEEPSLATES.values)
      registration.addIngredientInfo(infusedBlock, Component.translatable(RecipeViewerDescriptions.INFUSED_STONES))
  }

  override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
    registration.addRecipeCatalyst(T7Blocks.ARCANE_WORKBENCH, T7JeiCategories.WORKBENCH)
    registration.addRecipeCatalyst(T7Blocks.CRUCIBLE, T7JeiCategories.CRUCIBLE)
    registration.addRecipeCatalyst(T7Blocks.MATRIX, T7JeiCategories.INFUSION)
  }

  override fun registerIngredientAliases(registration: IIngredientAliasRegistration) {
    registration.addAlias(VanillaTypes.ITEM_STACK, T7Items.BOOK.get().defaultInstance, RecipeViewerAliases.BOOK)
    for (infusedBlock in T7Blocks.INFUSED_STONES.values + T7Blocks.INFUSED_DEEPSLATES.values)
      registration.addAlias(VanillaTypes.ITEM_STACK, infusedBlock.get().asItem().defaultInstance, RecipeViewerAliases.ORE)
  }

  override fun registerGuiHandlers(registration: IGuiHandlerRegistration) {
    super.registerGuiHandlers(registration)
  }
}
