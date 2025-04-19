package me.alegian.thavma.impl.common.research

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import net.minecraft.Util
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack

class ResearchCategory(
  val title: Component,
  val index: Float,
  val icon: ItemStack,
) {
  companion object{
    val CODEC = RecordCodecBuilder.create {
      it.group(
        ComponentSerialization.CODEC.fieldOf("title").forGetter(ResearchCategory::title),
        Codec.FLOAT.fieldOf("index").forGetter(ResearchCategory::index),
        ItemStack.STRICT_CODEC.fieldOf("icon").forGetter(ResearchCategory::icon),
      ).apply(it, ::ResearchCategory)
    }

    fun translationId(key: ResourceKey<ResearchCategory>) = Util.makeDescriptionId(T7DatapackRegistries.RESEARCH_CATEGORY.location().path, key.location())
  }
}
