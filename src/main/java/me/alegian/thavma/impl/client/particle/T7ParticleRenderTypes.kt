package me.alegian.thavma.impl.client.particle

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.renderer.texture.TextureAtlas

object T7ParticleRenderTypes {
  val ETERNAL_FLAME = ParticleRenderType { tesselator, textureManager ->
    RenderSystem.enableBlend()
    RenderSystem.depthMask(false)
    // additive blending
    RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE.value)
    RenderSystem.enableCull()
    RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES)
    RenderSystem.enableDepthTest()

    tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE)
  }
}