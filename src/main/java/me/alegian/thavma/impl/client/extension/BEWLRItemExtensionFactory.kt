package me.alegian.thavma.impl.client.extension

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions

object BEWLRItemExtensionFactory {
  fun create(bewlr: BlockEntityWithoutLevelRenderer): IClientItemExtensions {
    return object : IClientItemExtensions {
      override fun getCustomRenderer() = bewlr
    }
  }
}
