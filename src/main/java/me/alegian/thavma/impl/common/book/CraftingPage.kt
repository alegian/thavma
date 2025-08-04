package me.alegian.thavma.impl.common.book

import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.init.registries.deferred.PageTypes
import net.minecraft.resources.ResourceLocation

class CraftingPage(
  val recipeRL: ResourceLocation,
) : Page {
  override val type: PageType<*>
    get() = PageTypes.CRAFTING.get()

  companion object {
    val CODEC = RecordCodecBuilder.mapCodec { builder ->
      builder.group(
        ResourceLocation.CODEC.fieldOf("recipeRL").forGetter(CraftingPage::recipeRL),
      ).apply(builder, ::CraftingPage)
    }
  }
}