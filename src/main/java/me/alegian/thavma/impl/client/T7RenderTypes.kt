package me.alegian.thavma.impl.client

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.rl
import net.minecraft.Util
import net.minecraft.client.renderer.RenderStateShard
import net.minecraft.client.renderer.RenderStateShard.TextureStateShard
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.RenderType.CompositeState
import net.minecraft.resources.ResourceLocation
import java.util.function.Function

object T7RenderTypes {
  val AURA_NODE: RenderType = RenderType.create(
    Thavma.MODID + "_aura_node", T7VertexFormats.AURA_NODE, VertexFormat.Mode.TRIANGLES, RenderType.SMALL_BUFFER_SIZE, false, true, auraNodeState()
  )

  val TRANSLUCENT_TRIANGLES: RenderType = RenderType.create(
    Thavma.MODID + "_vis",
    DefaultVertexFormat.POSITION_COLOR,
    VertexFormat.Mode.TRIANGLE_STRIP,
    1536,
    false,
    true,
    CompositeState.builder()
      .setShaderState(RenderStateShard.POSITION_COLOR_SHADER)
      .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
      .setCullState(RenderStateShard.NO_CULL)
      .setWriteMaskState(RenderStateShard.COLOR_WRITE)
      .createCompositeState(false)
  )

  /**
   * named after the similar EYES vanilla RenderType, used for emissive entity eyes
   * used in Thavma for infusion green effects, because Gecko deletes the original pixels
   * so we have to write depth to avoid bugs
   */
  val EYES_WITH_DEPTH: Function<ResourceLocation, RenderType> = Util.memoize { textureLoc ->
    val textureStateShard = TextureStateShard(textureLoc, false, false)
    RenderType.create(
      "eyes",
      DefaultVertexFormat.NEW_ENTITY,
      VertexFormat.Mode.QUADS,
      1536,
      false,
      true,
      CompositeState.builder()
        .setShaderState(RenderStateShard.RENDERTYPE_EYES_SHADER)
        .setTextureState(textureStateShard)
        .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
        .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
        .createCompositeState(false)
    )
  }

  val ANGRY_ZOMBIE_EYES: RenderType = RenderType.eyes(rl("textures/entity/angry_zombie_eyes.png"))

  private fun auraNodeState(): CompositeState {
    return CompositeState.builder()
      .setShaderState(T7RenderStateShards.AURA_NODE_SHADER)
      .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
      .setTextureState(RenderStateShard.NO_TEXTURE)
      .setDepthTestState(T7RenderStateShards.NOT_EQUAL_DEPTH_TEST) // alpha colors do not stack in aura node layers, and aura nodes can be seen through blocks
      .setOutputState(RenderStateShard.TRANSLUCENT_TARGET)
      .setCullState(RenderStateShard.NO_CULL)
      .createCompositeState(false)
  }
}
