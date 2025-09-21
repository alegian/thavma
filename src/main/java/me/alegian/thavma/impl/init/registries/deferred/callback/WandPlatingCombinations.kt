package me.alegian.thavma.impl.init.registries.deferred.callback

import me.alegian.thavma.impl.common.wand.WandCoreMaterial
import me.alegian.thavma.impl.common.wand.WandPlatingMaterial
import me.alegian.thavma.impl.init.registries.deferred.T7Items.isWandRegistered
import me.alegian.thavma.impl.init.registries.deferred.T7Items.registerWand
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.callback.AddCallback

/**
 * Registry Callback to register all wand combinations of current
 * plating being registered.
 */
class WandPlatingCombinations(
  private val itemRegistry: Registry<Item>,
  private val coreRegistry: Registry<WandCoreMaterial>
) : AddCallback<WandPlatingMaterial> {
  override fun onAdd(
    platingRegistry: Registry<WandPlatingMaterial>,
    id: Int,
    key: ResourceKey<WandPlatingMaterial>,
    newPlating: WandPlatingMaterial
  ) {
    for (core in this.coreRegistry)
      if (core.registerCombinations && !isWandRegistered(newPlating, core))
        registerWand(this.itemRegistry, newPlating, core)
  }
}
