package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.init.registries.deferred.T7EntityTypes
import me.alegian.thavma.impl.init.registries.deferred.T7EntityTypes.ANGRY_ZOMBIE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ROTTEN_BRAIN
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.EntityLootSubProvider
import net.minecraft.world.entity.EntityType
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.Items
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator
import java.util.stream.Stream

class T7EntityLootSubProvider(registries: HolderLookup.Provider) : EntityLootSubProvider(FeatureFlags.REGISTRY.allFlags(), registries) {
  override fun generate() {
    add(
      ANGRY_ZOMBIE.get(), LootTable.Builder()
        .withPool(
          LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1.0F))
            .add(
              LootItem.lootTableItem(Items.ROTTEN_FLESH)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
            )
        )
        .withPool(
          LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1.0f))
            .add(
              LootItem.lootTableItem(ROTTEN_BRAIN)
                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0f, 1.0f)))
            )
        )
    )
  }

  override fun getKnownEntityTypes(): Stream<EntityType<*>> {
    return T7EntityTypes.REGISTRAR.entries
      .stream()
      .map { e -> e.value() }
  }
}
