package me.alegian.thavma.impl.client.particle

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.particle.TextureSheetParticle
import net.minecraft.core.particles.SimpleParticleType

class InfusionRuneParticle(level: ClientLevel, x: Double, y: Double, z: Double, spriteSet: SpriteSet) : TextureSheetParticle(level, x, y, z) {
  init{
    pickSprite(spriteSet)
    hasPhysics = false
    gravity = -0.05f
    lifetime = 20
  }

  override fun getRenderType() = ParticleRenderType.PARTICLE_SHEET_OPAQUE

  class Provider(private val sprite: SpriteSet) : ParticleProvider<SimpleParticleType> {
    override fun createParticle(type: SimpleParticleType, level: ClientLevel, x: Double, y: Double, z: Double, xSpeed: Double, ySpeed: Double, zSpeed: Double) =
      InfusionRuneParticle(level, x, y, z, sprite)
  }
}