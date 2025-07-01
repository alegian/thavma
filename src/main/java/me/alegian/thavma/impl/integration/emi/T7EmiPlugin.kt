package me.alegian.thavma.impl.integration.emi

import dev.emi.emi.api.EmiEntrypoint
import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiStack
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import me.alegian.thavma.impl.init.registries.deferred.T7Items
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeTypes
import me.alegian.thavma.impl.integration.RecipeViewerAliases
import me.alegian.thavma.impl.integration.emi.recipe.CrucibleEmiRecipe
import me.alegian.thavma.impl.integration.emi.recipe.InfusionEmiRecipe
import me.alegian.thavma.impl.integration.emi.recipe.T7EmiCategories
import me.alegian.thavma.impl.integration.emi.recipe.WorkbenchEmiRecipe
import net.minecraft.network.chat.Component
import net.minecraft.world.level.ItemLike

@EmiEntrypoint
internal class T7EmiPlugin : EmiPlugin {
  override fun register(registry: EmiRegistry) {
    registry.addCategory(T7EmiCategories.WORKBENCH)
    registry.addCategory(T7EmiCategories.CRUCIBLE)
    registry.addCategory(T7EmiCategories.INFUSION)

    registry.addWorkstation(T7EmiCategories.WORKBENCH, T7Blocks.ARCANE_WORKBENCH)
    registry.addWorkstation(T7EmiCategories.CRUCIBLE, T7Blocks.CRUCIBLE)
    registry.addWorkstation(T7EmiCategories.INFUSION, T7Blocks.MATRIX)

    val recipeManager = registry.recipeManager
    for (workbenchRecipe in recipeManager.getAllRecipesFor(T7RecipeTypes.WORKBENCH.get()))
      registry.addRecipe(WorkbenchEmiRecipe(workbenchRecipe))
    for (crucibleRecipe in recipeManager.getAllRecipesFor(T7RecipeTypes.CRUCIBLE.get()))
      registry.addRecipe(CrucibleEmiRecipe(crucibleRecipe))
    for (infusionRecipe in recipeManager.getAllRecipesFor(T7RecipeTypes.INFUSION.get()))
      registry.addRecipe(InfusionEmiRecipe(infusionRecipe))

    registry.addAlias(EmiStack.of(T7Items.BOOK.get()), Component.translatable(RecipeViewerAliases.BOOK))
    for (infusedBlock in T7Blocks.INFUSED_STONES.values + T7Blocks.INFUSED_DEEPSLATES.values)
      registry.addAlias(EmiStack.of(infusedBlock.get()), Component.translatable(RecipeViewerAliases.ORE))
  }
}

fun EmiRegistry.addWorkstation(category: EmiRecipeCategory, workstation: ItemLike) =
  addWorkstation(category, EmiStack.of(workstation.asItem()))