package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.common.recipe.CrucibleRecipe
import me.alegian.thavma.impl.common.recipe.InfusionRecipe
import me.alegian.thavma.impl.common.recipe.WorkbenchRecipe
import me.alegian.thavma.impl.rl
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.registries.DeferredRegister

object T7RecipeTypes {
  val REGISTRAR: DeferredRegister<RecipeType<*>> = DeferredRegister.create(Registries.RECIPE_TYPE, Thavma.MODID)

  val CRUCIBLE = REGISTRAR.register("crucible") { -> RecipeType.simple<CrucibleRecipe>(rl("crucible")) }
  val WORKBENCH = REGISTRAR.register("arcane_workbench") { -> RecipeType.simple<WorkbenchRecipe>(rl("arcane_workbench")) }
  val INFUSION = REGISTRAR.register("infusion") { -> RecipeType.simple<InfusionRecipe>(rl("infusion")) }
}
