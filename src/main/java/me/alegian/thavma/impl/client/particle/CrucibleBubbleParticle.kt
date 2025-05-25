package me.alegian.thavma.impl.client.particle

import me.alegian.thavma.impl.common.block.entity.CrucibleBE
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.particles.SimpleParticleType
import kotlin.math.floor

class CrucibleBubbleParticle private constructor(pLevel: ClientLevel, pX: Double, pY: Double, pZ: Double, spriteSet: SpriteSet) : TextureSheetParticle(pLevel, pX, pY, pZ) {
  private var crucibleBE: CrucibleBE? = null
  private var toRemove = false

  init {
    this.lifetime = (Math.random() * 40 + 20).toInt()
    this.quadSize = .05f
    this.gravity = 0f
    this.pickSprite(spriteSet)
  }

  override fun tick() {
    if (toRemove) remove()
    super.tick()
  }

  override fun getRenderType() = ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT

  override fun remove() {
    super.remove()
    crucibleBE?.removeParticle(this)
    level.addParticle(ParticleTypes.BUBBLE_POP, this.x, this.y, this.z, 0.0, 0.0, 0.0)
  }

  fun scheduleRemove() {
    toRemove = true
  }

  fun setBlockEntity(be: CrucibleBE) {
    crucibleBE = be
  }

  class Provider(private val sprite: SpriteSet) : ParticleProvider<SimpleParticleType> {
    override fun createParticle(
      pType: SimpleParticleType,
      pLevel: ClientLevel,
      pX: Double,
      pY: Double,
      pZ: Double,
      pXSpeed: Double,
      pYSpeed: Double,
      pZSpeed: Double
    ): Particle {
      val particle = CrucibleBubbleParticle(pLevel, pX, pY, pZ, sprite)
      val be = pLevel.getBlockEntity(BlockPos(floor(pX).toInt(), floor(pY).toInt(), floor(pZ).toInt()))
      if (be is CrucibleBE) {
        be.addParticle(particle)
        particle.setBlockEntity(be)
      }

      return particle
    }
  }
}
