package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.common.infusion.InfusionState
import me.alegian.thavma.impl.common.research.ResearchState
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

  val EXCHANGE_BLOCK = REGISTRAR.registerComponentType("exchange_block") {
    it.persistent(BuiltInRegistries.BLOCK.byNameCodec())
      .networkSynchronized(ByteBufCodecs.registry(Registries.BLOCK))
  }
}
