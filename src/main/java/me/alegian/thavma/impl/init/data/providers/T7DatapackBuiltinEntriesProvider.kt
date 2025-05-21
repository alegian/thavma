package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.common.aspect.Aspect
import me.alegian.thavma.impl.common.book.Page
import me.alegian.thavma.impl.common.book.TextPage
import me.alegian.thavma.impl.common.enchantment.ShriekResistance.LOCATION
import me.alegian.thavma.impl.common.research.ResearchCategory
import me.alegian.thavma.impl.common.research.ResearchEntry
import me.alegian.thavma.impl.common.research.SocketState
import me.alegian.thavma.impl.common.util.Indices
import me.alegian.thavma.impl.init.data.worldgen.ore.InfusedOre
import me.alegian.thavma.impl.init.data.worldgen.ore.InfusedStoneOre
import me.alegian.thavma.impl.init.data.worldgen.spawn.AngryZombieSpawn
import me.alegian.thavma.impl.init.data.worldgen.tree.GreatwoodTree
import me.alegian.thavma.impl.init.data.worldgen.tree.SilverwoodTree
import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import me.alegian.thavma.impl.init.registries.T7Tags.SONIC
import me.alegian.thavma.impl.init.registries.deferred.*
import me.alegian.thavma.impl.init.registries.deferred.util.DeferredAspect
import net.minecraft.ChatFormatting
import net.minecraft.advancements.critereon.DamageSourcePredicate
import net.minecraft.advancements.critereon.TagPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.enchantment.ConditionalEffect
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantment.Cost
import net.minecraft.world.item.enchantment.Enchantment.EnchantmentDefinition
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents
import net.minecraft.world.item.enchantment.LevelBasedValue
import net.minecraft.world.item.enchantment.effects.AddValue
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
import net.neoforged.neoforge.registries.NeoForgeRegistries
import org.joml.Vector2i
import java.util.*
import java.util.concurrent.CompletableFuture

class T7DatapackBuiltinEntriesProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : DatapackBuiltinEntriesProvider(output, registries, builder, setOf(Thavma.MODID)) {
  companion object {
    private val builder: RegistrySetBuilder = RegistrySetBuilder()
      .add(Registries.CONFIGURED_FEATURE) { bootstrap ->
        GreatwoodTree.registerConfigured(bootstrap)
        SilverwoodTree.registerConfigured(bootstrap)

        InfusedOre.registerConfigured(bootstrap)
        InfusedStoneOre.registerConfigured(bootstrap)
      }
      .add(Registries.PLACED_FEATURE) { ctx ->
        GreatwoodTree.registerPlaced(ctx)
        SilverwoodTree.registerPlaced(ctx)
        InfusedOre.registerPlaced(ctx)
        InfusedStoneOre.registerPlaced(ctx)
      }
      .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS) { ctx ->
        GreatwoodTree.registerBiomeModifier(ctx)
        SilverwoodTree.registerBiomeModifier(ctx)
        InfusedStoneOre.registerBiomeModifier(ctx)
        AngryZombieSpawn.registerBiomeModifier(ctx)
      }
      .add(Registries.ENCHANTMENT) { bootstrap ->
        val itemRegistry = bootstrap.lookup(Registries.ITEM)
        bootstrap.register(
          ResourceKey.create(
            Registries.ENCHANTMENT,
            LOCATION
          ),
          Enchantment(
            Component.literal("Shriek Resistance"),
            EnchantmentDefinition(
              itemRegistry.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
              Optional.empty(),
              5,
              4,
              Cost(10, 8),
              Cost(18, 8),
              2,
              listOf(EquipmentSlotGroup.ARMOR)
            ),
            HolderSet.empty(),
            DataComponentMap.builder()
              .set(
                EnchantmentEffectComponents.DAMAGE_PROTECTION, listOf(
                  ConditionalEffect(
                    AddValue(LevelBasedValue.perLevel(2.0f)),
                    Optional.of(
                      DamageSourceCondition.hasDamageSource(
                        DamageSourcePredicate.Builder.damageType().tag(TagPredicate.`is`(SONIC))
                      ).build()
                    )
                  )
                )
              ).build()
          )
        )
      }
      .add(T7DatapackRegistries.RESEARCH_CATEGORY) { ctx ->
        ctx.registerCategory(ResearchCategories.THAVMA, T7Items.BOOK.get().defaultInstance, 0f)
        ctx.registerCategory(ResearchCategories.ALCHEMY, T7Blocks.CRUCIBLE.get().asItem().defaultInstance, 1f)
      }
      .add(T7DatapackRegistries.RESEARCH_ENTRY) { ctx ->
        ResearchEntryBuilder(ResearchEntries.Thavma.THAVMA, Vector2i(0, -6), false, T7Items.BOOK.get().defaultInstance)
          .research(lockedAspect(2,0, Aspects.ORDO), lockedAspect(2, 4, Aspects.PRAECANTATIO))
          .addPage(simpleTextPage(3, true))
          .addPage(simpleTextPage(1, false))
          .addChild(ResearchEntries.Thavma.TREES)
          .addChild(ResearchEntries.Thavma.ORES)
          .build(ctx)

        ResearchEntryBuilder(ResearchEntries.Thavma.TREES, Vector2i(0, -3), false, T7Blocks.GREATWOOD_LOG.get().asItem().defaultInstance)
          .research(lockedAspect(2,0, Aspects.HERBA), lockedAspect(2, 4, Aspects.HERBA))
          .addChild(ResearchEntries.Thavma.RESEARCH_TABLE)
          .build(ctx)

        ResearchEntryBuilder(ResearchEntries.Thavma.ORES, Vector2i(2, -4), false, T7Items.SHARDS[Aspects.AQUA]!!.get().defaultInstance)
          .research(lockedAspect(2,0, Aspects.TERRA), lockedAspect(2, 4, Aspects.ORDO))
          .addChild(ResearchEntries.Thavma.OCULUS)
          .build(ctx)

        ResearchEntryBuilder(ResearchEntries.Thavma.OCULUS, Vector2i(2, -2), false, T7Items.OCULUS.get().defaultInstance)
          .research(lockedAspect(2,0, Aspects.LUX), lockedAspect(2, 4, Aspects.PRAECANTATIO), broken(2, 2))
          .addChild(ResearchEntries.Thavma.RESEARCH_TABLE)
          .addPage(simpleTextPage(3, true))
          .build(ctx)

        ResearchEntryBuilder(ResearchEntries.Thavma.RESEARCH_TABLE, Vector2i(0, 0), true, T7Blocks.RESEARCH_TABLE.get().asItem().defaultInstance)
          .research(lockedAspect(2,0, Aspects.PRAECANTATIO), lockedAspect(2, 4, Aspects.HERBA))
          .addChild(ResearchEntries.Thavma.WANDS)
          .addChild(ResearchEntries.Thavma.TECHNOLOGY)
          .addChild(ResearchEntries.Thavma.ALCHEMY)
          .addChild(ResearchEntries.Thavma.INFUSION)
          .build(ctx)

        ResearchEntryBuilder(ResearchEntries.Thavma.ALCHEMY, Vector2i(-2, 2), true, T7Blocks.CRUCIBLE.get().asItem().defaultInstance)
          .research(lockedAspect(2,0, Aspects.AQUA), lockedAspect(2, 4, Aspects.ALKIMIA))
          .build(ctx)

        ResearchEntryBuilder(ResearchEntries.Thavma.WANDS, Vector2i(-2, 4), true, T7Items.wandOrThrow(WandHandleMaterials.ARCANUM.get(), WandCoreMaterials.SILVERWOOD.get()).defaultInstance)
          .research(lockedAspect(2,0, Aspects.PRAECANTATIO), lockedAspect(2, 4, Aspects.INSTRUMENTUM))
          .build(ctx)

        ResearchEntryBuilder(ResearchEntries.Thavma.INFUSION, Vector2i(2, 2), true, T7Blocks.MATRIX.get().asItem().defaultInstance)
          .research(lockedAspect(2,0, Aspects.TERRA), lockedAspect(2, 4, Aspects.PRAECANTATIO))
          .build(ctx)

        ResearchEntryBuilder(ResearchEntries.Thavma.TECHNOLOGY, Vector2i(2, 4), true, T7Items.GOGGLES.get().defaultInstance)
          .research(lockedAspect(2,0, Aspects.INSTRUMENTUM), lockedAspect(2, 4, Aspects.HUMANUS))
          .build(ctx)

        ResearchEntryBuilder(ResearchEntries.Alchemy.ALCHEMY, Vector2i(0, 0), false, T7Blocks.CRUCIBLE.get().asItem().defaultInstance)
          .research(lockedAspect(2,0, Aspects.AQUA), lockedAspect(2, 4, Aspects.ALKIMIA))
          .build(ctx)
      }
  }
}

private class ResearchEntryBuilder(
  private val key: ResourceKey<ResearchEntry>,
  private val pos: Vector2i,
  private val preferX: Boolean,
  private val icon: ItemStack
) {
  private val children = mutableListOf<ResourceKey<ResearchEntry>>()
  private val pages = mutableListOf<Page>()
  private val socketStates = mutableListOf<SocketState>()

  fun addChild(entryKey: ResourceKey<ResearchEntry>): ResearchEntryBuilder {
    children.add(entryKey)
    return this
  }

  fun addPage(makePage: (ResourceKey<ResearchEntry>, Int) -> Page): ResearchEntryBuilder {
    pages.add(makePage(key, pages.size))
    return this
  }

  fun research(vararg states: SocketState): ResearchEntryBuilder {
    socketStates.addAll(states)
    return this
  }

  fun build(ctx: BootstrapContext<ResearchEntry>) = ResearchEntries.CATEGORIES[key]?.let { cat ->
    ctx.register(key, ResearchEntry(cat, pos, preferX, children, pages, icon, Component.translatable(ResearchEntry.translationId(key)).withStyle(Rarity.UNCOMMON.styleModifier), socketStates))
  }
}

private fun BootstrapContext<ResearchCategory>.registerCategory(key: ResourceKey<ResearchCategory>, icon: ItemStack, sortIndex: Float) {
  register(key, ResearchCategory(Component.translatable(ResearchCategory.translationId(key)), sortIndex, icon))
}

private fun simpleTextPage(paragraphCount: Int, hasTitle: Boolean): (ResourceKey<ResearchEntry>, Int) -> Page {
  return { entryKey, pageIndex ->
    val baseId = ResearchEntry.translationId(entryKey)
    TextPage(
      if (hasTitle) simpleTitle(pageIndex, baseId) else null,
      simpleParagraphs(paragraphCount, pageIndex, baseId)
    )
  }
}

private fun simpleTitle(pageIndex: Int, baseId: String) =
  Component.translatable(TextPage.titleTranslationId(baseId, pageIndex)).withStyle(ChatFormatting.BOLD)

private fun simpleParagraphs(count: Int, pageIndex: Int, baseId: String) = List(count) { Component.translatable(TextPage.paragraphTranslationId(baseId, pageIndex, it)) }

private fun lockedAspect(row:Int, col:Int, a: DeferredAspect<Aspect>) = SocketState(Indices(row, col), a.get(), false, true)
private fun broken(row:Int, col:Int) = SocketState(Indices(row, col), null, true, true)
