package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.Thavma
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.core.registries.BuiltInRegistries
import net.neoforged.neoforge.registries.DeferredRegister

object T7ParticleTypes {
    val REGISTRAR = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, Thavma.MODID)

    val CRUCIBLE_BUBBLE = REGISTRAR.register("crucible_bubble") { -> SimpleParticleType(true) }
    val ETERNAL_FLAME = REGISTRAR.register("eternal_flame") { -> SimpleParticleType(true) }
}
