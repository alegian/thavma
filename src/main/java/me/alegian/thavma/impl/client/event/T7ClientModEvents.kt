package me.alegian.thavma.impl.client.event

import me.alegian.thavma.impl.client.T7Colors
import me.alegian.thavma.impl.client.T7RenderStateShards
import me.alegian.thavma.impl.client.T7VertexFormats
import me.alegian.thavma.impl.client.extension.BEWLRItemExtensionFactory
import me.alegian.thavma.impl.client.extension.OculusItemExtensions
import me.alegian.thavma.impl.client.extension.WandItemExtensions
import me.alegian.thavma.impl.client.gui.VisGuiOverlay
import me.alegian.thavma.impl.client.gui.WorkbenchScreen
import me.alegian.thavma.impl.client.gui.research_table.ResearchScreen
import me.alegian.thavma.impl.client.gui.tooltip.AspectClientTooltipComponent
import me.alegian.thavma.impl.client.gui.tooltip.AspectTooltipComponent
import me.alegian.thavma.impl.client.model.WithTransformParentModel
import me.alegian.thavma.impl.client.particle.CrucibleBubbleParticle
import me.alegian.thavma.impl.client.renderer.blockentity.*
import me.alegian.thavma.impl.client.renderer.entity.AngryZombieER
import me.alegian.thavma.impl.client.renderer.entity.FancyItemER
import me.alegian.thavma.impl.client.renderer.entity.VisER
import me.alegian.thavma.impl.client.texture.atlas.AspectAtlas
import me.alegian.thavma.impl.common.block.entity.MatrixBE
import me.alegian.thavma.impl.common.block.entity.PedestalBE
import me.alegian.thavma.impl.common.block.entity.PillarBE
import me.alegian.thavma.impl.common.block.entity.WorkbenchBE
import me.alegian.thavma.impl.init.registries.deferred.*
import me.alegian.thavma.impl.rl
import net.minecraft.client.renderer.ShaderInstance
import net.neoforged.api.distmarker.Dist
import net.neoforged.neoforge.client.event.*
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers
import net.neoforged.neoforge.client.event.ModelEvent.RegisterGeometryLoaders
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import thedarkcolour.kotlinforforge.neoforge.forge.DIST
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS as KFF_MOD_BUS

private fun registerGuiLayers(event: RegisterGuiLayersEvent) {
  event.registerAboveAll(rl("vis"), VisGuiOverlay.LAYER)
}

private fun registerEntityRenderers(event: RegisterRenderers) {
  event.registerBlockEntityRenderer(T7BlockEntities.AURA_NODE.get()) { _ -> AuraNodeBER() }
  event.registerBlockEntityRenderer(T7BlockEntities.CRUCIBLE.get()) { _ -> CrucibleBER() }
  event.registerBlockEntityRenderer(T7BlockEntities.WORKBENCH.get()) { _ -> WorkbenchBER() }
  event.registerBlockEntityRenderer(T7BlockEntities.RESEARCH_TABLE.get()) { _ -> ResearchTableBER() }
  event.registerBlockEntityRenderer(T7BlockEntities.MATRIX.get()) { _ -> MatrixBER() }
  event.registerBlockEntityRenderer(T7BlockEntities.PILLAR.get()) { _ -> PillarBER() }
  event.registerBlockEntityRenderer(T7BlockEntities.PEDESTAL.get()) { _ -> PedestalBER() }
  event.registerEntityRenderer(T7EntityTypes.FANCY_ITEM.get()) { ctx -> FancyItemER(ctx) }
  event.registerEntityRenderer(T7EntityTypes.VIS.get()) { ctx -> VisER(ctx) }
  event.registerEntityRenderer(T7EntityTypes.ANGRY_ZOMBIE.get()) { ctx -> AngryZombieER(ctx) }
}

private fun registerParticleProviders(event: RegisterParticleProvidersEvent) {
  event.registerSpriteSet(
    T7ParticleTypes.CRUCIBLE_BUBBLE.get()
  ) { pSprites -> CrucibleBubbleParticle.Provider(pSprites) }
}

private fun registerClientExtensions(event: RegisterClientExtensionsEvent) {
  for (wand in T7Items.WANDS.values())
    event.registerItem(WandItemExtensions(), wand)

  event.registerItem(OculusItemExtensions(), T7Items.OCULUS.get())
  event.registerItem(BEWLRItemExtensionFactory.create(WorkbenchBE().withDefaultAnimations()), T7Blocks.ARCANE_WORKBENCH.get().asItem())
  event.registerItem(BEWLRItemExtensionFactory.create(MatrixBE()), T7Blocks.MATRIX.get().asItem())
  event.registerItem(BEWLRItemExtensionFactory.create(PillarBE()), T7Blocks.PILLAR.get().asItem())
  event.registerItem(BEWLRItemExtensionFactory.create(PedestalBE()), T7Blocks.PEDESTAL.get().asItem())
}

private fun registerReloadListenerEvent(event: RegisterClientReloadListenersEvent) {
  event.registerReloadListener(AspectAtlas.INSTANCE)
}

private fun registerGeometryLoaders(event: RegisterGeometryLoaders) {
  event.register(WithTransformParentModel.ID, WithTransformParentModel.Loader)
}

private fun registerItemColorHandlers(event: RegisterColorHandlersEvent.Item) {
  for (aspect in Aspects.PRIMAL_ASPECTS) {
    event.register(
      { _, tintIndex ->
        when (tintIndex) {
          0 -> aspect.get().color
          else -> 0xFFFFFFFF.toInt()
        }
      },
      T7Blocks.INFUSED_DEEPSLATES[aspect],
      T7Blocks.INFUSED_STONES[aspect],
      T7Items.TESTAS[aspect]
    )
  }
  event.register(
    { _, tintIndex ->
      when (tintIndex) {
        0 -> T7Colors.GREATWOOD_LEAVES
        else -> 0xFFFFFFFF.toInt()
      }
    },
    T7Blocks.GREATWOOD_LEAVES.get()
  )
  event.register(
    { _, tintIndex ->
      when (tintIndex) {
        0 -> T7Colors.SILVERWOOD_LEAVES
        else -> 0xFFFFFFFF.toInt()
      }
    },
    T7Blocks.SILVERWOOD_LEAVES.get()
  )
}

private fun registerBlockColorHandlers(event: RegisterColorHandlersEvent.Block) {
  for (aspect in Aspects.PRIMAL_ASPECTS) {
    event.register(
      { _, _, _, tintIndex ->
        when (tintIndex) {
          0 -> aspect.get().color
          else -> 0xFFFFFFFF.toInt()
        }
      },
      T7Blocks.INFUSED_DEEPSLATES[aspect]!!.get(),
      T7Blocks.INFUSED_STONES[aspect]!!.get()
    )
  }
  event.register(
    { _, _, _, tintIndex ->
      when (tintIndex) {
        0 -> T7Colors.GREATWOOD_LEAVES
        else -> 0xFFFFFFFF.toInt()
      }
    },
    T7Blocks.GREATWOOD_LEAVES.get()
  )
  event.register(
    { _, _, _, tintIndex ->
      when (tintIndex) {
        0 -> T7Colors.SILVERWOOD_LEAVES
        else -> 0xFFFFFFFF.toInt()
      }
    },
    T7Blocks.SILVERWOOD_LEAVES.get()
  )
}

private fun registerShaders(event: RegisterShadersEvent) {
  event.registerShader(
    ShaderInstance(event.resourceProvider, rl("aura_node"), T7VertexFormats.AURA_NODE)
  ) { T7RenderStateShards.auraNodeShader = it }
}

private fun registerClientTooltipComponentFactories(event: RegisterClientTooltipComponentFactoriesEvent) {
  event.register(
    AspectTooltipComponent::class.java, ::AspectClientTooltipComponent
  )
}

private fun registerScreens(event: RegisterMenuScreensEvent) {
  event.register(T7MenuTypes.WORKBENCH.get(), ::WorkbenchScreen)
  event.register(T7MenuTypes.RESEARCH.get(), ::ResearchScreen)
}

fun registerClientModEvents() {
  if (DIST != Dist.CLIENT) return

  KFF_MOD_BUS.addListener(::registerGuiLayers)
  KFF_MOD_BUS.addListener(::registerEntityRenderers)
  KFF_MOD_BUS.addListener(::registerParticleProviders)
  KFF_MOD_BUS.addListener(::registerClientExtensions)
  KFF_MOD_BUS.addListener(::registerReloadListenerEvent)
  KFF_MOD_BUS.addListener(::registerGeometryLoaders)
  KFF_MOD_BUS.addListener(::registerItemColorHandlers)
  KFF_MOD_BUS.addListener(::registerBlockColorHandlers)
  KFF_MOD_BUS.addListener(::registerShaders)
  KFF_MOD_BUS.addListener(::registerClientTooltipComponentFactories)
  KFF_MOD_BUS.addListener(::registerScreens)
}