package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.common.recipe.CrucibleRecipeSerializer
import me.alegian.thavma.impl.common.recipe.InfusionRecipeSerializer
import me.alegian.thavma.impl.common.recipe.WorkbenchRecipeSerializer
import net.minecraft.core.registries.Registries
import net.neoforged.neoforge.registries.DeferredRegister

object T7RecipeSerializers {
    val REGISTRAR = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Thavma.MODID)

    val CRUCIBLE = REGISTRAR.register("crucible") { -> CrucibleRecipeSerializer() }
    val ARCANE_WORKBENCH = REGISTRAR.register("arcane_workbench") { -> WorkbenchRecipeSerializer() }
    val INFUSION = REGISTRAR.register("infusion") { -> InfusionRecipeSerializer() }
}
