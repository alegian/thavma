package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.init.registries.deferred.T7ParticleTypes.CRUCIBLE_BUBBLE
import me.alegian.thavma.impl.init.registries.deferred.T7ParticleTypes.ETERNAL_FLAME
import me.alegian.thavma.impl.rl
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider

class T7ParticleDescriptionProvider(output: PackOutput, fileHelper: ExistingFileHelper) : ParticleDescriptionProvider(output, fileHelper) {
  override fun addDescriptions() {
    spriteSet(CRUCIBLE_BUBBLE.get(), ResourceLocation.withDefaultNamespace("bubble"))
    spriteSet(ETERNAL_FLAME.get(), rl("eternal_flame_red"), rl("eternal_flame_orange"), rl("eternal_flame_yellow"))
  }
}
