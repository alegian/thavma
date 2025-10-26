package me.alegian.thavma.impl.init.registries

import me.alegian.thavma.impl.common.data.capability.IAspectContainer
import me.alegian.thavma.impl.rl
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.ItemCapability

object T7Capabilities {
  object AspectContainer {
    val BLOCK = BlockCapability.createVoid(rl("aspect_container"), IAspectContainer::class.java)

    val ITEM = ItemCapability.createVoid(rl("aspect_container"), IAspectContainer::class.java)
  }
}
