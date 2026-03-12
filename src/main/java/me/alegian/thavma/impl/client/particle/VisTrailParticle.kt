package me.alegian.thavma.impl.client.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.TextureSheetParticle
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.renderer.LightTexture
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

private const val STRETCH_RATIO = 5.0f
private const val MIN_HALF_LENGTH = 0.06f

class VisTrailParticle private constructor(
  level: ClientLevel,
  x: Double,
  y: Double,
  z: Double,
  private val target: Vec3,
  color: Int,
  private val phase: Float,
  scale: Float,
  private val spriteSet: SpriteSet
) : TextureSheetParticle(level, x, y, z, .0, .0, .0) {
  private val initialSize: Float

  companion object {
    var spriteSet: SpriteSet? = null

    fun spawn(start: Vec3, target: Vec3, color: Int, phase: Int, scale: Float, progress: Float = 0f) {
      val mc = Minecraft.getInstance()
      val level = mc.level as? ClientLevel ?: return
      val spriteSet = spriteSet ?: return

      val spawnPos = start.lerp(target, progress.toDouble())
      val jitter = 0.015
      val x = spawnPos.x + (level.random.nextDouble() - 0.5) * jitter
      val y = spawnPos.y + (level.random.nextDouble() - 0.5) * jitter
      val z = spawnPos.z + (level.random.nextDouble() - 0.5) * jitter
      runCatching {
        mc.particleEngine.add(VisTrailParticle(level, x, y, z, target, color, phase.toFloat(), scale, spriteSet))
      }
    }
  }

  init {
    val toTarget = target.subtract(x, y, z)
    val distance = toTarget.length().coerceAtLeast(0.001)
    val forward = toTarget.normalize()
    val tempUp = if (abs(forward.y) > 0.9) Vec3(1.0, 0.0, 0.0) else Vec3(0.0, 1.0, 0.0)
    val side = forward.cross(tempUp).normalize()
    val up = side.cross(forward).normalize()

    val colorVariance = 0.05f
    val red = (color shr 16 and 0xFF) / 255f
    val green = (color shr 8 and 0xFF) / 255f
    val blue = (color and 0xFF) / 255f
    setColor(
      red - red * colorVariance + random.nextFloat() * red * colorVariance,
      green - green * colorVariance + random.nextFloat() * green * colorVariance,
      blue - blue * colorVariance + random.nextFloat() * blue * colorVariance
    )

    alpha = 0.6f
    hasPhysics = false
    gravity = 0f
    lifetime = (distance * 22.0).roundToInt().coerceIn(14, 50)
    quadSize = scale * (0.92f + random.nextFloat() * 0.16f)
    initialSize = quadSize
    pickSprite(spriteSet)

    val swirl1 = sin(phase * 0.43f).toDouble() * 0.006
    val swirl2 = cos(phase * 0.31f).toDouble() * 0.004
    xd = forward.x * 0.04 + side.x * swirl1 + up.x * swirl2 + random.nextGaussian() * 0.0004
    yd = forward.y * 0.04 + side.y * swirl1 + up.y * swirl2 + random.nextGaussian() * 0.0004
    zd = forward.z * 0.04 + side.z * swirl1 + up.z * swirl2 + random.nextGaussian() * 0.0004
  }

  override fun render(buffer: VertexConsumer, camera: Camera, partialTick: Float) {
    val px = Mth.lerp(partialTick.toDouble(), xo, x) - camera.position.x
    val py = Mth.lerp(partialTick.toDouble(), yo, y) - camera.position.y
    val pz = Mth.lerp(partialTick.toDouble(), zo, z) - camera.position.z

    val vel = Vec3(xd, yd, zd)
    val speed = vel.length()
    val stretchDir = if (speed > 1.0E-6) vel.normalize() else (target.subtract(x, y, z)).let { if (it.lengthSqr() > 1.0E-6) it.normalize() else Vec3(1.0, 0.0, 0.0) }
    val camToParticle = Vec3(px, py, pz)
    if (camToParticle.lengthSqr() < 1.0E-6) { super.render(buffer, camera, partialTick); return }
    val camDir = camToParticle.normalize()

    var sideVec = stretchDir.cross(camDir)
    if (sideVec.lengthSqr() < 1.0E-6) { super.render(buffer, camera, partialTick); return }
    sideVec = sideVec.normalize()

    val halfWidth = quadSize * 0.28f
    val halfLength = max(quadSize * STRETCH_RATIO * 0.5f, MIN_HALF_LENGTH)

    val sx = sideVec.x.toFloat() * halfWidth
    val sy = sideVec.y.toFloat() * halfWidth
    val sz = sideVec.z.toFloat() * halfWidth
    val vx = stretchDir.x.toFloat() * halfLength
    val vy = stretchDir.y.toFloat() * halfLength
    val vz = stretchDir.z.toFloat() * halfLength

    val cx = px.toFloat()
    val cy = py.toFloat()
    val cz = pz.toFloat()

    val u0 = getU0()
    val u1 = getU1()
    val v0 = getV0()
    val v1 = getV1()
    val light = getLightColor(partialTick)

    buffer.addVertex(cx - sx + vx, cy - sy + vy, cz - sz + vz)
      .setUv(u1, v0).setColor(rCol, gCol, bCol, alpha).setLight(light)
    buffer.addVertex(cx + sx + vx, cy + sy + vy, cz + sz + vz)
      .setUv(u1, v1).setColor(rCol, gCol, bCol, alpha).setLight(light)
    buffer.addVertex(cx + sx - vx, cy + sy - vy, cz + sz - vz)
      .setUv(u0, v1).setColor(rCol, gCol, bCol, alpha).setLight(light)
    buffer.addVertex(cx - sx - vx, cy - sy - vy, cz - sz - vz)
      .setUv(u0, v0).setColor(rCol, gCol, bCol, alpha).setLight(light)
  }

  override fun tick() {
    xo = x
    yo = y
    zo = z

    if (age++ >= lifetime) {
      remove()
      return
    }

    val dx = target.x - x
    val dy = target.y - y
    val dz = target.z - z
    val distance = sqrt(dx * dx + dy * dy + dz * dz)
    if (distance < 0.1) {
      remove()
      return
    }

    val invDistance = 1.0 / distance
    val attraction = 0.02 + 0.035 / max(1.0, distance)
    xd = Mth.clamp(xd * 0.96 + dx * invDistance * attraction, -0.09, 0.09)
    yd = Mth.clamp(yd * 0.96 + dy * invDistance * attraction, -0.09, 0.09)
    zd = Mth.clamp(zd * 0.96 + dz * invDistance * attraction, -0.09, 0.09)
    move(xd, yd, zd)

    val pulse = 0.94f + 0.08f * sin((age + phase) / 4.5f)
    val shrinkNearEnd = Mth.clamp((distance / 1.7).toFloat(), 0.45f, 1.0f)
    quadSize = initialSize * pulse * shrinkNearEnd
    alpha = 0.3f + 0.5f * min(1f, distance.toFloat())
  }

  override fun getLightColor(partialTick: Float) = LightTexture.FULL_BRIGHT

  override fun getRenderType() = T7ParticleRenderTypes.ETERNAL_FLAME

  class Provider(private val sprites: SpriteSet) : ParticleProvider<SimpleParticleType> {
    init {
      spriteSet = sprites
    }

    override fun createParticle(type: SimpleParticleType, level: ClientLevel, x: Double, y: Double, z: Double, xSpeed: Double, ySpeed: Double, zSpeed: Double) =
      VisTrailParticle(level, x, y, z, Vec3(x + xSpeed, y + ySpeed, z + zSpeed), 0xFFFFFF, 0f, 0.1f, sprites)
  }
}

object VisTrailParticles {
  fun spawnBurst(start: Vec3, target: Vec3, colors: List<Int>, seed: Int, count: Int, scale: Float) {
    if (colors.isEmpty()) return
    val safeCount = count.coerceAtLeast(1)
    repeat(safeCount) { index ->
      val color = colors[(seed + index).mod(colors.size)]
      val progress = if (safeCount == 1) 0f else index.toFloat() / safeCount * 0.28f
      VisTrailParticle.spawn(start, target, color, seed * 7 + index * 13, scale, progress)
    }
  }
}
