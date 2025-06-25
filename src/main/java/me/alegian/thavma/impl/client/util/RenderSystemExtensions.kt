package me.alegian.thavma.impl.client.util

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.util.FastColor.ARGB32.*

fun setRenderSystemColor(color: Int) =
  RenderSystem.setShaderColor(red(color) / 255f, green(color) / 255f, blue(color) / 255f, alpha(color) / 255f)

fun resetRenderSystemColor() =
  RenderSystem.setShaderColor(1f, 1f, 1f, 1f)