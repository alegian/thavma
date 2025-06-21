package me.alegian.thavma.impl.integration.jei

import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import me.alegian.thavma.impl.init.registries.deferred.T7Items
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeTypes
import me.alegian.thavma.impl.integration.jei.category.CrucibleRecipeCategory
import me.alegian.thavma.impl.integration.jei.category.InfusionRecipeCategory
import me.alegian.thavma.impl.integration.jei.category.T7Categories
import me.alegian.thavma.impl.integration.jei.category.WorkbenchRecipeCategory
import me.alegian.thavma.impl.rl
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.registration.IIngredientAliasRegistration
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component

@JeiPlugin
internal class T7JEIPlugin : IModPlugin {
  override fun getPluginUid() = rl("jei")

  override fun registerCategories(registration: IRecipeCategoryRegistration) {
    registration.addRecipeCategories(WorkbenchRecipeCategory(registration.jeiHelpers.guiHelper))
    registration.addRecipeCategories(CrucibleRecipeCategory(registration.jeiHelpers.guiHelper))
    registration.addRecipeCategories(InfusionRecipeCategory(registration.jeiHelpers.guiHelper))
  }

  override fun registerRecipes(registration: IRecipeRegistration) {
    val level = Minecraft.getInstance().level ?: return
    registration.addRecipes(T7Categories.WORKBENCH, level.recipeManager.getAllRecipesFor(T7RecipeTypes.WORKBENCH.get()))
    registration.addRecipes(T7Categories.CRUCIBLE, level.recipeManager.getAllRecipesFor(T7RecipeTypes.CRUCIBLE.get()))
    registration.addRecipes(T7Categories.INFUSION, level.recipeManager.getAllRecipesFor(T7RecipeTypes.INFUSION.get()))

    registration.addIngredientInfo(T7Items.ROTTEN_BRAIN.get(), Component.translatable(JEIDescriptions.ROTTEN_BRAIN))
    registration.addIngredientInfo(T7Items.BOOK.get(), Component.translatable(JEIDescriptions.BOOK))
    registration.addIngredientInfo(T7Blocks.ARCANE_WORKBENCH.get(), Component.translatable(JEIDescriptions.ARCANE_WORKBENCH))
    registration.addIngredientInfo(T7Blocks.RESEARCH_TABLE.get(), Component.translatable(JEIDescriptions.RESEARCH_TABLE))
    registration.addIngredientInfo(T7Blocks.CRUCIBLE.get(), Component.translatable(JEIDescriptions.CRUCIBLE))
    registration.addIngredientInfo(T7Blocks.GREATWOOD_LOG.get(), Component.translatable(JEIDescriptions.GREATWOOD))
    registration.addIngredientInfo(T7Blocks.SILVERWOOD_LOG.get(), Component.translatable(JEIDescriptions.SILVERWOOD))
    for (infusedBlock in T7Blocks.INFUSED_STONES.values + T7Blocks.INFUSED_DEEPSLATES.values)
      registration.addIngredientInfo(infusedBlock.get(), Component.translatable(JEIDescriptions.INFUSED_STONES))
  }

  override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
    registration.addRecipeCatalyst(T7Blocks.ARCANE_WORKBENCH, T7Categories.WORKBENCH)
    registration.addRecipeCatalyst(T7Blocks.CRUCIBLE, T7Categories.CRUCIBLE)
    registration.addRecipeCatalyst(T7Blocks.MATRIX, T7Categories.INFUSION)
  }

  override fun registerIngredientAliases(registration: IIngredientAliasRegistration) {
    registration.addAlias(VanillaTypes.ITEM_STACK, T7Items.BOOK.get().defaultInstance, JEIAliases.BOOK)
    for (infusedBlock in T7Blocks.INFUSED_STONES.values + T7Blocks.INFUSED_DEEPSLATES.values)
      registration.addAlias(VanillaTypes.ITEM_STACK, infusedBlock.get().asItem().defaultInstance, JEIAliases.ORE)
  }
}
