package me.alegian.thavma.impl.client

import net.minecraft.client.renderer.RenderStateShard.DepthTestStateShard
import net.minecraft.client.renderer.RenderStateShard.ShaderStateShard
import net.minecraft.client.renderer.ShaderInstance
import org.lwjgl.opengl.GL11

object T7RenderStateShards {
  val NOT_EQUAL_DEPTH_TEST = DepthTestStateShard("!=", GL11.GL_NOTEQUAL)

  // TODO: move this elsewhere
  var auraNodeShader: ShaderInstance? = null
  val AURA_NODE_SHADER: ShaderStateShard = ShaderStateShard { auraNodeShader }
}
