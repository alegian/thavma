package me.alegian.thavma.impl.client.particle

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.particle.TextureSheetParticle
import net.minecraft.client.renderer.LightTexture
import net.minecraft.core.particles.SimpleParticleType

class EternalFlameParticle(level: ClientLevel, private val centerX: Double, private val centerY: Double, private val centerZ: Double, xSpeed: Double, ySpeed: Double, zSpeed: Double, spriteSet: SpriteSet) : TextureSheetParticle(level, centerX, centerY, centerZ, .0, .0, .0) {
  private var initialSize = 0f
  private val initialAlpha = 1f

  init {
    // add randomness to position
    xo += random.nextDouble() * 0.4 - 0.2
    yo += random.nextDouble() * 0.4 - 0.2
    zo += random.nextDouble() * 0.4 - 0.2
    setPos(xo, yo, zo)
    // undo the random that is hardcoded in speed
    xd = xSpeed
    yd = ySpeed
    zd = zSpeed

    hasPhysics = false
    gravity = -0.1f
    pickSprite(spriteSet)
    lifetime = 30
    quadSize *= 2f
    initialSize = quadSize
  }

  override fun tick() {
    super.tick()
    // spin a bit
    oRoll = roll
    roll += 0.2f
    // get smaller and fainter
    val normalizedLifeLeft = 1 - age.toFloat() / lifetime
    quadSize = initialSize * normalizedLifeLeft
    alpha = initialAlpha * normalizedLifeLeft
    // gravitate inward along X and Z
    val k = 0.08
    xd -= k * (x - centerX)
    zd -= k * (z - centerZ)
  }

  override fun getLightColor(partialTick: Float) = LightTexture.FULL_BRIGHT

  override fun getRenderType() = T7ParticleRenderTypes.ETERNAL_FLAME

  class Provider(private val sprite: SpriteSet) : ParticleProvider<SimpleParticleType> {
    override fun createParticle(type: SimpleParticleType, level: ClientLevel, x: Double, y: Double, z: Double, xSpeed: Double, ySpeed: Double, zSpeed: Double) =
      EternalFlameParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprite)
  }
}