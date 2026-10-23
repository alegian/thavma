package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.common.block.entity.HoleBE
import me.alegian.thavma.impl.common.infusion.InfusionState
import me.alegian.thavma.impl.common.item.WandMode
import me.alegian.thavma.impl.common.research.ResearchState
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.world.item.component.ItemContainerContents
import net.neoforged.neoforge.registries.DeferredRegister

object T7DataComponents {
  val REGISTRAR = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Thavma.MODID)

  val ASPECTS = REGISTRAR.registerComponentType("aspects") { builder ->
    builder
      .persistent(AspectMap.CODEC)
      .networkSynchronized(AspectMap.STREAM_CODEC)
  }

  val RESEARCH_STATE = REGISTRAR.registerComponentType("research_state") { builder ->
    builder
      .persistent(ResearchState.CODEC)
      .networkSynchronized(ResearchState.STREAM_CODEC)
  }

  val INFUSION_STATE = REGISTRAR.registerComponentType("infusion_state") { builder ->
    builder
      .persistent(InfusionState.CODEC)
  }

  val FOCUS = REGISTRAR.registerComponentType("focus") { builder ->
    builder
      .persistent(ItemContainerContents.CODEC)
      .networkSynchronized(ItemContainerContents.STREAM_CODEC)
  }

  val WAND_MODE = REGISTRAR.registerComponentType("wand_mode") { builder ->
    builder
      .persistent(WandMode.CODEC)
      .networkSynchronized(WandMode.STREAM_CODEC)
  }

  val INTERACTING_BLOCKPOS = REGISTRAR.registerComponentType("interacting_blockpos") { builder ->
    builder
      .persistent(BlockPos.CODEC)
      .networkSynchronized(BlockPos.STREAM_CODEC)
  }

  val EXCHANGE_BLOCK = REGISTRAR.registerComponentType("exchange_block") {
    it.persistent(BuiltInRegistries.BLOCK.byNameCodec())
      .networkSynchronized(ByteBufCodecs.registry(Registries.BLOCK))
  }

  val HOLE_STATE = REGISTRAR.registerComponentType("hole_state") {
    it.persistent(HoleBE.Companion.HoleState.CODEC)
  }
}
