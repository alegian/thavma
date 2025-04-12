package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.common.menu.ResearchMenu
import me.alegian.thavma.impl.common.menu.WorkbenchMenu
import net.minecraft.core.registries.Registries
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.registries.DeferredRegister

object T7MenuTypes {
    val REGISTRAR = DeferredRegister.create(Registries.MENU, Thavma.MODID)

    val WORKBENCH = REGISTRAR.register("arcane_workbench") { -> MenuType(::WorkbenchMenu, FeatureFlags.DEFAULT_FLAGS) }
    val RESEARCH = REGISTRAR.register("research_table") { -> MenuType(::ResearchMenu, FeatureFlags.DEFAULT_FLAGS) }
}
