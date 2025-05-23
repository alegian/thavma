package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.common.wand.WandHandleMaterial
import me.alegian.thavma.impl.init.registries.T7Registries
import net.neoforged.neoforge.registries.DeferredRegister

object WandHandleMaterials {
    val REGISTRAR = DeferredRegister.create(T7Registries.WAND_HANDLE.key(), Thavma.MODID)

    val IRON = REGISTRAR.register("iron", ::WandHandleMaterial)

    val GOLD = REGISTRAR.register("gold", ::WandHandleMaterial)

    val ORICHALCUM = REGISTRAR.register("orichalcum", ::WandHandleMaterial)

    val THAVMITE = REGISTRAR.register("thavmite", ::WandHandleMaterial)
}
