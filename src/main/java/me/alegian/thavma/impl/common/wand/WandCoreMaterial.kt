package me.alegian.thavma.impl.common.wand

import me.alegian.thavma.impl.init.registries.T7Registries.WAND_CORE
import net.minecraft.resources.ResourceLocation

class WandCoreMaterial(val registerCombinations: Boolean, val capacity: Int) {
  constructor(capacity: Int) : this(true, capacity)

  val registeredName
    get() = registeredLocation.path

  val registeredLocation: ResourceLocation
    get() {
      val key = WAND_CORE.getKey(this)
      requireNotNull(key) { "Thavma Exception: Trying to Access Unregistered Wand Handle" }
      return key
    }
}
