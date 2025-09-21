package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.common.wand.WandPlatingMaterial
import me.alegian.thavma.impl.init.registries.T7Registries
import net.neoforged.neoforge.registries.DeferredRegister

object WandPlatingMaterials {
  val REGISTRAR = DeferredRegister.create(T7Registries.WAND_PLATING.key(), Thavma.MODID)

  val IRON = REGISTRAR.register("iron", ::WandPlatingMaterial)

  val GOLD = REGISTRAR.register("gold", ::WandPlatingMaterial)

  val ORICHALCUM = REGISTRAR.register("orichalcum", ::WandPlatingMaterial)

  val THAVMITE = REGISTRAR.register("thavmite", ::WandPlatingMaterial)
}
