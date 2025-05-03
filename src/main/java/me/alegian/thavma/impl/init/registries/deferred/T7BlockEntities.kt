package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.common.block.entity.*
import me.alegian.thavma.impl.common.data.capability.AspectContainer
import me.alegian.thavma.impl.init.registries.T7Capabilities
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.registries.DeferredRegister

object T7BlockEntities {
    val REGISTRAR = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Thavma.MODID)

    fun registerCapabilities(event: RegisterCapabilitiesEvent) {
        event.registerBlockEntity(
            Capabilities.FluidHandler.BLOCK, CRUCIBLE.get()
        ) { be, _ -> be.fluidHandler }
        event.registerBlockEntity(
            T7Capabilities.AspectContainer.BLOCK, CRUCIBLE.get()
        ) { be, _ -> AspectContainer(be) }
        event.registerBlockEntity(
            T7Capabilities.AspectContainer.BLOCK, ESSENTIA_CONTAINER.get()
        ) { be, _ -> AspectContainer(be, 64) }
        event.registerBlockEntity(
            T7Capabilities.AspectContainer.BLOCK, AURA_NODE.get()
        ) { be, _ -> AspectContainer(be) }
    }

    val PEDESTAL = REGISTRAR.register("infusion_pedestal") { ->
        BlockEntityType.Builder.of(::PedestalBE, T7Blocks.PEDESTAL.get()).build(null)
    }

    val AURA_NODE = REGISTRAR.register("aura_node") { ->
        BlockEntityType.Builder.of(::AuraNodeBE, T7Blocks.AURA_NODE.get()).build(null)
    }

    val CRUCIBLE = REGISTRAR.register("crucible") { ->
        BlockEntityType.Builder.of(::CrucibleBE, T7Blocks.CRUCIBLE.get()).build(null)
    }

    val ESSENTIA_CONTAINER = REGISTRAR.register("essentia_container") { ->
        BlockEntityType.Builder.of(::EssentiaContainerBE, T7Blocks.ESSENTIA_CONTAINER.get()).build(null)
    }

    val WORKBENCH = REGISTRAR.register("arcane_workbench") { ->
        BlockEntityType.Builder.of(::WorkbenchBE, T7Blocks.ARCANE_WORKBENCH.get()).build(null)
    }

    val MATRIX = REGISTRAR.register("infusion_matrix") { ->
        BlockEntityType.Builder.of(::MatrixBE, T7Blocks.MATRIX.get()).build(null)
    }

    val PILLAR = REGISTRAR.register("infusion_pillar") { ->
        BlockEntityType.Builder.of(::PillarBE, T7Blocks.PILLAR.get()).build(null)
    }

    val RESEARCH_TABLE = REGISTRAR.register("research_table") { ->
        BlockEntityType.Builder.of(::ResearchTableBE, T7Blocks.RESEARCH_TABLE.get()).build(null)
    }
}
