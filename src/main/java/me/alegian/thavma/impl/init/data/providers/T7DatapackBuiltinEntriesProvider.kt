package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.Thavma
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
import net.minecraft.advancements.critereon.DamageSourcePredicate
import net.minecraft.advancements.critereon.TagPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlotGroup
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
        ctx.register(ResearchCategories.TEST_CATEGORY, ResearchCategory("Test Category", 0f))
        ctx.register(ResearchCategories.SECOND, ResearchCategory("Second Category", 1f))
      }
      .add(T7DatapackRegistries.RESEARCH_ENTRY) { ctx ->
        val pageTest = TextPage(Component.literal("Test Page").withStyle(Style.EMPTY.withBold(true)), Component.literal("my page is awesome and data driven"))

        ctx.register(ResearchEntries.E0_0, ResearchEntry(ResearchCategories.TEST_CATEGORY, Vector2i(0, 0), false, listOf(), listOf(pageTest)))
        ctx.register(ResearchEntries.E4_m2, ResearchEntry(ResearchCategories.TEST_CATEGORY, Vector2i(4, -2), true, listOf(), listOf()))
        ctx.register(ResearchEntries.E1_12, ResearchEntry(ResearchCategories.TEST_CATEGORY, Vector2i(1, 12), false, listOf(), listOf()))
        ctx.register(ResearchEntries.E12_2, ResearchEntry(ResearchCategories.TEST_CATEGORY, Vector2i(12, 2), true, listOf(), listOf()))
        ctx.register(ResearchEntries.E4_7, ResearchEntry(ResearchCategories.TEST_CATEGORY, Vector2i(4, 7), true, listOf(), listOf()))
        ctx.register(ResearchEntries.E7_4, ResearchEntry(ResearchCategories.TEST_CATEGORY, Vector2i(7, 4), true, listOf(), listOf()))
        ctx.register(ResearchEntries.E2_m2, ResearchEntry(ResearchCategories.TEST_CATEGORY, Vector2i(2, -2), true, listOf(ResearchEntries.E0_0), listOf()))
        ctx.register(ResearchEntries.E4_m4, ResearchEntry(ResearchCategories.TEST_CATEGORY, Vector2i(4, -4), true, listOf(), listOf()))
        ctx.register(ResearchEntries.E2_m4, ResearchEntry(ResearchCategories.TEST_CATEGORY, Vector2i(2, -4), true, listOf(), listOf()))
        ctx.register(ResearchEntries.E3_3, ResearchEntry(ResearchCategories.TEST_CATEGORY, Vector2i(3, 3), false, listOf(ResearchEntries.E4_7, ResearchEntries.E7_4), listOf()))
        ctx.register(ResearchEntries.E3_m3, ResearchEntry(ResearchCategories.TEST_CATEGORY, Vector2i(3, -3), false, listOf(ResearchEntries.E4_m2), listOf()))
        ctx.register(ResearchEntries.E3_1, ResearchEntry(ResearchCategories.TEST_CATEGORY, Vector2i(3, 1), false, listOf(ResearchEntries.E1_12), listOf()))
        ctx.register(ResearchEntries.Em1_4, ResearchEntry(ResearchCategories.TEST_CATEGORY, Vector2i(-1, 4), false, listOf(ResearchEntries.E12_2), listOf()))

        ctx.register(ResearchEntries.SECOND_TAB_ENTRY, ResearchEntry(ResearchCategories.SECOND, Vector2i(0, 1), false, listOf(), listOf()))
      }
  }
}
