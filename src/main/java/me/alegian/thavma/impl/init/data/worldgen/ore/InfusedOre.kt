package me.alegian.thavma.impl.init.data.worldgen.ore

import me.alegian.thavma.impl.init.registries.deferred.Aspects
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import me.alegian.thavma.impl.init.registries.deferred.linkedMapWithPrimalKeys
import me.alegian.thavma.impl.rl
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.PlacedFeature

object InfusedOre {
    private val CONFIGURED_FEATURES = linkedMapWithPrimalKeys { aspect ->
        ResourceKey.create(Registries.CONFIGURED_FEATURE, rl(aspect.id.path).withPrefix("ore_"))
    }

    val PLACED_FEATURES = linkedMapWithPrimalKeys { aspect ->
        ResourceKey.create(Registries.PLACED_FEATURE, rl(aspect.id.path).withPrefix("ore_"))
    }

    fun registerConfigured(context: BootstrapContext<ConfiguredFeature<*, *>>) {
        Aspects.DATAGEN_PRIMALS.forEach {
            registerConfiguredInfusedStone(context, CONFIGURED_FEATURES[it], T7Blocks.INFUSED_STONES[it]!!.get().defaultBlockState(), T7Blocks.INFUSED_DEEPSLATES[it]!!.get().defaultBlockState())
        }
    }

    fun registerPlaced(context: BootstrapContext<PlacedFeature>) {
        Aspects.DATAGEN_PRIMALS.forEach {
            registerPlacedInfusedStone(context, CONFIGURED_FEATURES[it], PLACED_FEATURES[it])
        }
    }
}