package me.alegian.thavma.impl.init.registries

import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_INGOT
import net.minecraft.tags.BlockTags
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.SimpleTier

object T7Tiers {
    val THAVMITE_TIER = SimpleTier(
        BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
        1000,
        7f,
        2.5f,
        22
    ) { Ingredient.of(THAVMITE_INGOT) }
}
