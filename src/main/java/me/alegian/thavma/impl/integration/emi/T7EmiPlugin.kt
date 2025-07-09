package me.alegian.thavma.impl.integration.emi

import dev.emi.emi.api.EmiEntrypoint
import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiInfoRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiStack
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import me.alegian.thavma.impl.init.registries.deferred.T7Items
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeTypes
import me.alegian.thavma.impl.integration.RecipeViewerAliases
import me.alegian.thavma.impl.integration.RecipeViewerDescriptions
import me.alegian.thavma.impl.integration.emi.recipe.CrucibleEmiRecipe
import me.alegian.thavma.impl.integration.emi.recipe.InfusionEmiRecipe
import me.alegian.thavma.impl.integration.emi.recipe.T7EmiCategories
import me.alegian.thavma.impl.integration.emi.recipe.WorkbenchEmiRecipe
import me.alegian.thavma.impl.rl
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

    registry.addRecipe(infoRecipe(T7Items.ROTTEN_BRAIN, Component.translatable(RecipeViewerDescriptions.ROTTEN_BRAIN), "rotten_brain"))
    registry.addRecipe(infoRecipe(T7Items.BOOK, Component.translatable(RecipeViewerDescriptions.BOOK), "book"))
    registry.addRecipe(infoRecipe(T7Blocks.ARCANE_WORKBENCH, Component.translatable(RecipeViewerDescriptions.ARCANE_WORKBENCH), "arcane_workbench"))
    registry.addRecipe(infoRecipe(T7Blocks.RESEARCH_TABLE, Component.translatable(RecipeViewerDescriptions.RESEARCH_TABLE), "research_table"))
    registry.addRecipe(infoRecipe(T7Blocks.CRUCIBLE, Component.translatable(RecipeViewerDescriptions.CRUCIBLE), "crucible"))
    registry.addRecipe(infoRecipe(T7Blocks.GREATWOOD_LOG, Component.translatable(RecipeViewerDescriptions.GREATWOOD), "greatwood_log"))
    registry.addRecipe(infoRecipe(T7Blocks.SILVERWOOD_LOG, Component.translatable(RecipeViewerDescriptions.SILVERWOOD), "silverwood_log"))
    registry.addRecipe(infoRecipe(T7Blocks.PILLAR, Component.translatable(RecipeViewerDescriptions.PILLAR), "infusion_pillar"))
    registry.addRecipe(infoRecipe(T7Items.RESEARCH_SCROLL, Component.translatable(RecipeViewerDescriptions.RESEARCH_SCROLL), "research_scroll"))
    for (infusedBlock in T7Blocks.INFUSED_STONES.values + T7Blocks.INFUSED_DEEPSLATES.values)
      registry.addRecipe(infoRecipe(infusedBlock, Component.translatable(RecipeViewerDescriptions.INFUSED_STONES), infusedBlock.id.path))
  }
}

private fun EmiRegistry.addWorkstation(category: EmiRecipeCategory, workstation: ItemLike) =
  addWorkstation(category, EmiStack.of(workstation))

// first slash marks the id as synthetic (does not correspond to some vanilla recipe)
private fun infoRecipe(itemLike: ItemLike, info: Component, id: String) =
  EmiInfoRecipe(listOf(EmiStack.of(itemLike)), listOf(info), rl("/info/$id"))