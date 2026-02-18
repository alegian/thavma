package me.alegian.thavma.impl.init.registries.deferred.callback

import me.alegian.thavma.impl.common.item.WandItem
import me.alegian.thavma.impl.common.wand.WandCoreMaterial
import me.alegian.thavma.impl.common.wand.WandPlatingMaterial
import me.alegian.thavma.impl.init.registries.deferred.T7Items.WANDS
import me.alegian.thavma.impl.rl
import net.minecraft.core.Registry
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
  val coreCallback = AddCallback{ _, _, _, newCore ->
    for (plating in this.platingRegistry)
      if (plating.registerCombinations && !isWandRegistered(plating, newCore))
        registerWand(this.itemRegistry, plating, newCore)
  }
  val platingCallback = AddCallback{ _, _, _, newPlating ->
    for (core in this.coreRegistry)
      if (core.registerCombinations && !isWandRegistered(newPlating, core))
        registerWand(this.itemRegistry, newPlating, core)
  }

  companion object{
    private fun registerWand(registry: Registry<Item>, platingMaterial: WandPlatingMaterial, coreMaterial: WandCoreMaterial) {
      val platingName = platingMaterial.registeredName
      val coreName = coreMaterial.registeredName
      val wandName = WandItem.name(platingMaterial, coreMaterial)

      val newWand = WandItem(Item.Properties(), platingMaterial, coreMaterial)
      Registry.register(registry, rl(wandName), newWand)
      WANDS.put(platingName, coreName, newWand)
    }

    private fun isWandRegistered(platingMaterial: WandPlatingMaterial, coreMaterial: WandCoreMaterial): Boolean {
      val platingName = platingMaterial.registeredName
      val coreName = coreMaterial.registeredName
      return WANDS[platingName, coreName] != null
    }
  }
}


