package me.alegian.thavma.impl.client.particle

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.BreakingItemParticle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.ItemParticleOption
import net.minecraft.world.item.ItemStack

class InfusionItemParticle(level: ClientLevel, x: Double, y: Double, z: Double, xSpeed: Double, ySpeed: Double, zSpeed: Double, stack: ItemStack) : BreakingItemParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, stack) {
  init {
    hasPhysics = false
    gravity = 0f
    lifetime = 7
  }

  class Provider(private val sprites: SpriteSet) : ParticleProvider<ItemParticleOption> {
    override fun createParticle(type: ItemParticleOption, level: ClientLevel, x: Double, y: Double, z: Double, xSpeed: Double, ySpeed: Double, zSpeed: Double) =
      InfusionItemParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.item)
  }
}