package me.alegian.thavma.impl.common.infusion

import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.init.registries.deferred.Aspects.IGNIS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.TERRA
import net.minecraft.world.item.crafting.Ingredient

data class RemainingInputs(
  val ingredients: List<Ingredient>, val aspects: AspectMap =
    // todo: remove after testing
    AspectMap.builder().add(IGNIS.get(), 40).add(TERRA.get(), 50).build()
) {
  companion object {
    val CODEC = RecordCodecBuilder.create {
      it.group(
        Ingredient.LIST_CODEC_NONEMPTY.fieldOf("ingredients").forGetter(RemainingInputs::ingredients),
        AspectMap.CODEC.fieldOf("aspects").forGetter(RemainingInputs::aspects),
      ).apply(it, ::RemainingInputs)
    }
  }
}