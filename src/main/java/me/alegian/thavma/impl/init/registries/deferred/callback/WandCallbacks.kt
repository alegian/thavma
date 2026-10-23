package me.alegian.thavma.impl.init.registries.deferred.callback

import me.alegian.thavma.impl.common.item.WandItem
import me.alegian.thavma.impl.common.wand.WandCoreMaterial
import me.alegian.thavma.impl.common.wand.WandPlatingMaterial
import me.alegian.thavma.impl.init.registries.deferred.T7Items.WANDS
import me.alegian.thavma.impl.rl
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.callback.AddCallback

/**
 * Registry Callback to register all wand combinations of a core/ plating
 */
class WandCallbacks(
  private val itemRegistry: Registry<Item>,
  private val platingRegistry: Registry<WandPlatingMaterial>,
  private val coreRegistry: Registry<WandCoreMaterial>
) {
  val coreCallback = AddCallback { _, _, coreKey, newCore ->
    for (platingEntry in platingRegistry.entrySet()) {
      registerWand(platingEntry.value, newCore, platingEntry.key, coreKey)
    }
  }
  val platingCallback = AddCallback { _, _, platingKey, newPlating ->
    for (coreEntry in coreRegistry.entrySet()) {
      registerWand(newPlating, coreEntry.value, platingKey, coreEntry.key)
    }
  }

  private fun registerWand(plating: WandPlatingMaterial, core: WandCoreMaterial, platingKey: ResourceKey<*>, coreKey: ResourceKey<*>) {
    if (plating.registerCombinations && core.registerCombinations && !isWandRegistered(plating, core)) {
      val newWand = WandItem(Item.Properties(), plating, core)
      Registry.register(itemRegistry, wandRL(platingKey, coreKey), newWand)
    }
  }

  companion object {
    private fun isWandRegistered(plating: WandPlatingMaterial, core: WandCoreMaterial): Boolean {
      return WANDS[plating, core] != null
    }

    private fun wandRL(platingKey: ResourceKey<*>, coreKey: ResourceKey<*>) =
      rl(platingKey.location().path + "_" + coreKey.location().path + "_wand")
  }
}


