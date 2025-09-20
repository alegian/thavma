package me.alegian.thavma.impl.common.wand

import me.alegian.thavma.impl.init.registries.T7Registries.WAND_PLATING
import net.minecraft.resources.ResourceLocation


class WandPlatingMaterial(val registerCombinations: Boolean = true) {
  val registeredName
    get() = registeredLocation.path

  val registeredLocation: ResourceLocation
    get() {
      val key = WAND_PLATING.getKey(this)
      requireNotNull(key) { "Thavma Exception: Trying to Access Unregistered Wand Plating" }
      return key
    }
}
