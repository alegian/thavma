package me.alegian.thavma.impl.init.registries

import me.alegian.thavma.impl.common.research.Research
import me.alegian.thavma.impl.rl
import net.minecraft.resources.ResourceKey

object T7DatapackRegistries {
  val RESEARCH = ResourceKey.createRegistryKey<Research>(rl("research"))
}