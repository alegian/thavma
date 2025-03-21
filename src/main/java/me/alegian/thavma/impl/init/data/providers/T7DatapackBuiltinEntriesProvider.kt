package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.common.enchantment.ShriekResistance.LOCATION
import me.alegian.thavma.impl.init.data.worldgen.ore.InfusedOre
import me.alegian.thavma.impl.init.data.worldgen.ore.InfusedStoneOre
import me.alegian.thavma.impl.init.data.worldgen.spawn.AngryZombieSpawn
import me.alegian.thavma.impl.init.data.worldgen.tree.GreatwoodTree
import me.alegian.thavma.impl.init.data.worldgen.tree.SilverwoodTree
import me.alegian.thavma.impl.init.registries.T7Tags.SONIC
import net.minecraft.advancements.critereon.DamageSourcePredicate
import net.minecraft.advancements.critereon.TagPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.network.chat.Component
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
  }
}
