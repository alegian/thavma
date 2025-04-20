package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.common.book.Page
import me.alegian.thavma.impl.common.book.TextPage
import me.alegian.thavma.impl.common.enchantment.ShriekResistance.LOCATION
import me.alegian.thavma.impl.common.research.ResearchCategory
import me.alegian.thavma.impl.common.research.ResearchEntry
import me.alegian.thavma.impl.init.data.worldgen.ore.InfusedOre
import me.alegian.thavma.impl.init.data.worldgen.ore.InfusedStoneOre
import me.alegian.thavma.impl.init.data.worldgen.spawn.AngryZombieSpawn
import me.alegian.thavma.impl.init.data.worldgen.tree.GreatwoodTree
import me.alegian.thavma.impl.init.data.worldgen.tree.SilverwoodTree
import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import me.alegian.thavma.impl.init.registries.T7Tags.SONIC
import me.alegian.thavma.impl.init.registries.deferred.ResearchCategories
import me.alegian.thavma.impl.init.registries.deferred.ResearchEntries
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import me.alegian.thavma.impl.init.registries.deferred.T7Items
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
        ctx.registerCategory(ResearchCategories.THAVMA, T7Items.THAUMONOMICON.get().defaultInstance, 0f)
        ctx.registerCategory(ResearchCategories.ALCHEMY, T7Blocks.CRUCIBLE.get().asItem().defaultInstance, 1f)
      }
      .add(T7DatapackRegistries.RESEARCH_ENTRY) { ctx ->
        ResearchEntryBuilder(ResearchEntries.THAVMA, Vector2i(0, 0), false, T7Items.THAUMONOMICON.get().defaultInstance)
          .addPage(simpleTextPage(3, true))
          .addPage(simpleTextPage(1, false))
          .addChild(ResearchEntries.OCULUS)
          .build(ctx)

        ResearchEntryBuilder(ResearchEntries.OCULUS, Vector2i(2, 2), false, T7Items.OCULUS.get().defaultInstance)
          .addPage(simpleTextPage(3, true))
          .build(ctx)

        ResearchEntryBuilder(ResearchEntries.ALCHEMY, Vector2i(0, 0), false, T7Blocks.CRUCIBLE.get().asItem().defaultInstance)
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

  fun addChild(entryKey: ResourceKey<ResearchEntry>): ResearchEntryBuilder {
    children.add(entryKey)
    return this
  }

  fun addPage(makePage: (ResourceKey<ResearchEntry>, Int) -> Page): ResearchEntryBuilder {
    pages.add(makePage(key, pages.size))
    return this
  }

  fun build(ctx: BootstrapContext<ResearchEntry>) = ResearchEntries.CATEGORIES[key]?.let { cat ->
    ctx.register(key, ResearchEntry(cat, pos, preferX, children, pages, icon, Component.translatable(ResearchEntry.translationId(key)).withStyle(Rarity.UNCOMMON.styleModifier)))
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
