package me.alegian.thavma.impl.common.research

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.client.clientRegistry
import me.alegian.thavma.impl.common.util.T7ExtraCodecs
import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import net.minecraft.resources.ResourceKey
import org.joml.Vector2i

class ResearchEntry(val category: ResourceKey<ResearchCategory>, val position: Vector2i, val preferX: Boolean, val children: List<ResourceKey<ResearchEntry>>) {
  private var resolvedChildren: List<ResearchEntry>? = null

  companion object {
    val CODEC = RecordCodecBuilder.create {
      it.group(
        ResourceKey.codec(T7DatapackRegistries.RESEARCH_CATEGORY).fieldOf("category").forGetter(ResearchEntry::category),
        T7ExtraCodecs.VECTOR2I.fieldOf("position").forGetter(ResearchEntry::position),
        Codec.BOOL.fieldOf("preferX").forGetter(ResearchEntry::preferX),
        ResourceKey.codec(T7DatapackRegistries.RESEARCH_ENTRY).listOf().fieldOf("children").forGetter(ResearchEntry::children),
      ).apply(it, ::ResearchEntry)
    }
  }

  fun resolveChildren(): List<ResearchEntry> {
    resolvedChildren?.let { return it }

    val registry = clientRegistry(T7DatapackRegistries.RESEARCH_ENTRY)
    if (registry == null) return listOf()

    resolvedChildren = children.map { registry.getOrThrow(it) }.also { return it }
  }
}
