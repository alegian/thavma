package me.alegian.thavma.impl.init.data.worldgen.spawn

import me.alegian.thavma.impl.init.registries.deferred.T7EntityTypes
import me.alegian.thavma.impl.rl
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BiomeTags
import net.minecraft.world.level.biome.MobSpawnSettings
import net.neoforged.neoforge.common.world.BiomeModifier
import net.neoforged.neoforge.common.world.BiomeModifiers
import net.neoforged.neoforge.registries.NeoForgeRegistries

object AngryZombieSpawn {
    private val BIOME_MODIFIER = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, rl("angry_zombie"))

    fun registerBiomeModifier(context: BootstrapContext<BiomeModifier>) {
        val biomeRegistry = context.lookup(Registries.BIOME)

        context.register(
            BIOME_MODIFIER,
            BiomeModifiers.AddSpawnsBiomeModifier(
                biomeRegistry.getOrThrow(BiomeTags.IS_OVERWORLD),
                listOf(MobSpawnSettings.SpawnerData(
                    T7EntityTypes.ANGRY_ZOMBIE.get(),
                    20,
                    1,
                    1
                ))
            )
        )
    }
}
