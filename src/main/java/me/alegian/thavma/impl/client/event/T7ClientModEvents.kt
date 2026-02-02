package me.alegian.thavma.impl.client.event

import me.alegian.thavma.impl.client.*
import me.alegian.thavma.impl.client.extension.ArcaneLensItemExtensions
import me.alegian.thavma.impl.client.extension.BEWLRItemExtensionFactory
import me.alegian.thavma.impl.client.extension.WandItemExtensions
import me.alegian.thavma.impl.client.gui.WorkbenchScreen
import me.alegian.thavma.impl.client.gui.book.CraftingPageRenderer
import me.alegian.thavma.impl.client.gui.book.TextPageRenderer
import me.alegian.thavma.impl.client.gui.layer.ArcaneLensLayer
import me.alegian.thavma.impl.client.gui.layer.WandLayer
import me.alegian.thavma.impl.client.gui.research_table.ResearchScreen
import me.alegian.thavma.impl.client.gui.tooltip.AspectClientTooltipComponent
import me.alegian.thavma.impl.client.gui.tooltip.AspectTooltipComponent
import me.alegian.thavma.impl.client.model.WithTransformParentModel
import me.alegian.thavma.impl.client.particle.CrucibleBubbleParticle
import me.alegian.thavma.impl.client.particle.EternalFlameParticle
import me.alegian.thavma.impl.client.particle.InfusionItemParticle
import me.alegian.thavma.impl.client.particle.InfusionRuneParticle
import me.alegian.thavma.impl.client.renderer.blockentity.*
import me.alegian.thavma.impl.client.renderer.blockentity.withoutlevel.BlockItemBEWLR
import me.alegian.thavma.impl.client.renderer.blockentity.withoutlevel.NodeJarBEWLR
import me.alegian.thavma.impl.client.renderer.blockentity.withoutlevel.SealingJarBEWLR
import me.alegian.thavma.impl.client.renderer.entity.AngryZombieER
import me.alegian.thavma.impl.client.renderer.entity.FancyItemER
import me.alegian.thavma.impl.client.renderer.entity.VisER
import me.alegian.thavma.impl.client.texture.atlas.AspectAtlas
import me.alegian.thavma.impl.common.block.entity.*
import me.alegian.thavma.impl.init.registries.T7ItemProperties
import me.alegian.thavma.impl.init.registries.deferred.*
import me.alegian.thavma.impl.rl
import net.minecraft.client.renderer.ShaderInstance
import net.minecraft.client.renderer.item.ItemProperties
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.ModLoader
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.*
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers
import net.neoforged.neoforge.client.event.ModelEvent.RegisterGeometryLoaders
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import thedarkcolour.kotlinforforge.neoforge.forge.DIST
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS as KFF_MOD_BUS

private fun clientSetup(event: FMLClientSetupEvent) {
  ModLoader.postEvent(RegisterPageRenderersEvent())
  ItemProperties.register(T7Items.RESEARCH_SCROLL.get(), T7ItemProperties.COMPLETED) { stack, level, entity, seed ->
    val completed = stack.get(T7DataComponents.RESEARCH_STATE)?.completed ?: false
    if (completed) 1f else 0f
  }
}

private fun registerGuiLayers(event: RegisterGuiLayersEvent) {
  event.registerAboveAll(rl("vis"), WandLayer)
  event.registerAboveAll(rl("arcane_lens"), ArcaneLensLayer)
}

private fun registerEntityRenderers(event: RegisterRenderers) {
  event.registerBlockEntityRenderer(T7BlockEntities.AURA_NODE.get()) { _ -> AuraNodeBER() }
  event.registerBlockEntityRenderer(T7BlockEntities.CRUCIBLE.get()) { _ -> CrucibleBER() }
  event.registerBlockEntityRenderer(T7BlockEntities.SEALING_JAR.get()) { _ -> SealingJarBER() }
  event.registerBlockEntityRenderer(T7BlockEntities.WORKBENCH.get()) { _ -> WorkbenchBER() }
  event.registerBlockEntityRenderer(T7BlockEntities.RESEARCH_TABLE.get()) { _ -> ResearchTableBER() }
  event.registerBlockEntityRenderer(T7BlockEntities.MATRIX.get()) { _ -> MatrixBER() }
  event.registerBlockEntityRenderer(T7BlockEntities.PILLAR.get()) { _ -> PillarBER() }
  event.registerBlockEntityRenderer(T7BlockEntities.PEDESTAL.get()) { _ -> PedestalBER() }
  event.registerBlockEntityRenderer(T7BlockEntities.HUNGRY_CHEST.get()) { ctx -> HungryChestBER(ctx) }
  event.registerEntityRenderer(T7EntityTypes.FANCY_ITEM.get()) { ctx -> FancyItemER(ctx) }
  event.registerEntityRenderer(T7EntityTypes.VIS.get()) { ctx -> VisER(ctx) }
  event.registerEntityRenderer(T7EntityTypes.ANGRY_ZOMBIE.get()) { ctx -> AngryZombieER(ctx) }
}

private fun registerParticleProviders(event: RegisterParticleProvidersEvent) {
  event.registerSpriteSet(
    T7ParticleTypes.CRUCIBLE_BUBBLE.get(),
    CrucibleBubbleParticle::Provider
  )
  event.registerSpriteSet(
    T7ParticleTypes.ETERNAL_FLAME.get(),
    EternalFlameParticle::Provider
  )
  event.registerSpriteSet(
    T7ParticleTypes.INFUSION_ITEM.get(),
    InfusionItemParticle::Provider
  )
  event.registerSpriteSet(
    T7ParticleTypes.INFUSION_RUNE.get(),
    InfusionRuneParticle::Provider
  )
}

private fun registerClientExtensions(event: RegisterClientExtensionsEvent) {
  for (wand in T7Items.WANDS.values())
    event.registerItem(WandItemExtensions(), wand)

  event.registerItem(ArcaneLensItemExtensions(), T7Items.ARCANE_LENS.get())
  event.registerItem(BEWLRItemExtensionFactory.create(BlockItemBEWLR(WorkbenchBE().withDefaultAnimations())), T7Blocks.ARCANE_WORKBENCH.get().asItem())
  event.registerItem(BEWLRItemExtensionFactory.create(BlockItemBEWLR(MatrixBE())), T7Blocks.MATRIX.get().asItem())
  event.registerItem(BEWLRItemExtensionFactory.create(BlockItemBEWLR(PillarBE())), T7Blocks.PILLAR.get().asItem())
  event.registerItem(BEWLRItemExtensionFactory.create(BlockItemBEWLR(PedestalBE())), T7Blocks.PEDESTAL.get().asItem())
  event.registerItem(BEWLRItemExtensionFactory.create(BlockItemBEWLR(HungryChestBE())), T7Blocks.HUNGRY_CHEST.get().asItem())
  event.registerItem(BEWLRItemExtensionFactory.create(SealingJarBEWLR), T7Blocks.SEALING_JAR.get().asItem())
  event.registerItem(BEWLRItemExtensionFactory.create(NodeJarBEWLR), T7Items.NODE_JAR.get())
}

private fun registerReloadListenerEvent(event: RegisterClientReloadListenersEvent) {
  event.registerReloadListener(AspectAtlas)
}

private fun registerGeometryLoaders(event: RegisterGeometryLoaders) {
  event.register(WithTransformParentModel.ID, WithTransformParentModel.Loader)
}

private fun registerItemColorHandlers(event: RegisterColorHandlersEvent.Item) {
  for (aspect in Aspects.DATAGEN_PRIMALS) {
    event.register(
      { _, tintIndex ->
        when (tintIndex) {
          0 -> aspect.get().color
          else -> 0xFFFFFFFF.toInt()
        }
      },
      T7Blocks.INFUSED_DEEPSLATES[aspect],
      T7Blocks.INFUSED_STONES[aspect],
      T7Items.SHARDS[aspect]
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

  event.register(
    { _, tintIndex ->
      when (tintIndex) {
        0 -> T7Colors.GREEN
        else -> 0xFFFFFFFF.toInt()
      }
    },
    T7Blocks.ELEMENTAL_CORE.get()
  )
}

private fun registerBlockColorHandlers(event: RegisterColorHandlersEvent.Block) {
  for (aspect in Aspects.DATAGEN_PRIMALS) {
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

  event.register(
    { _, _, _, tintIndex ->
      when (tintIndex) {
        0 -> T7Colors.GREEN
        else -> 0xFFFFFFFF.toInt()
      }
    },
    T7Blocks.ELEMENTAL_CORE.get()
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

private fun registerPageRenderers(event: RegisterPageRenderersEvent) {
  event.register(PageTypes.TEXT.get(), TextPageRenderer)
  event.register(PageTypes.CRAFTING.get(), CraftingPageRenderer)
}

private fun registerKeyMappings(event: RegisterKeyMappingsEvent) {
  event.register(T7KeyMappings.FOCI)
}

private fun registerRenderBuffers(event: RegisterRenderBuffersEvent) {
  event.registerRenderBuffer(T7RenderTypes.FLYING_ASPECTS)
  event.registerRenderBuffer(T7RenderTypes.AURA_NODE)
}

fun registerClientModEvents() {
  if (DIST != Dist.CLIENT) return

  KFF_MOD_BUS.addListener(::clientSetup)
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
  KFF_MOD_BUS.addListener(::registerPageRenderers)
  KFF_MOD_BUS.addListener(::registerKeyMappings)
  KFF_MOD_BUS.addListener(::registerRenderBuffers)
}