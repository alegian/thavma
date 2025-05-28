package me.alegian.thavma.impl.client.extension

import me.alegian.thavma.impl.client.renderer.blockentity.withoutlevel.BlockItemBEWLR
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions

object BEWLRItemExtensionFactory {
  fun create(be: BlockEntity): IClientItemExtensions {
    return object : IClientItemExtensions {
      override fun getCustomRenderer() = BlockItemBEWLR(be)
    }
  }
}
