package me.alegian.thavma.impl.common.loot

import com.mojang.serialization.MapCodec
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import me.alegian.thavma.impl.init.registries.deferred.T7Items.EYE_OF_WARDEN
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.loot.LootContext
import net.neoforged.neoforge.common.loot.LootModifier
import net.neoforged.neoforge.common.loot.LootTableIdCondition

class WardenLootModifier : LootModifier(
  arrayOf(
    LootTableIdCondition.builder(EntityType.WARDEN.getDefaultLootTable().location()).build()
  )
) {
  override fun doApply(generatedLoot: ObjectArrayList<ItemStack>, context: LootContext): ObjectArrayList<ItemStack> {
    generatedLoot.add(ItemStack(EYE_OF_WARDEN.get()))
    return generatedLoot
  }

  override fun codec() = CODEC

  companion object {
    val CODEC = MapCodec.unit(WardenLootModifier())
  }
}
