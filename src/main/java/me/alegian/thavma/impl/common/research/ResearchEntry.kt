package me.alegian.thavma.impl.common.research

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.client.clientRegistry
import me.alegian.thavma.impl.common.book.Page
import me.alegian.thavma.impl.common.util.T7ExtraCodecs
import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import net.minecraft.Util
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import org.joml.Vector2i

class ResearchEntry(
  val category: ResourceKey<ResearchCategory>,
  val position: Vector2i,
  val preferX: Boolean,
  val children: List<ResourceKey<ResearchEntry>>,
  val pages: List<Page>,
  val icon: ItemStack,
  val title: Component,
  val defaultResearchState: List<SocketState>,
  val defaultKnown: Boolean
) {
  private var resolvedChildren: List<ResearchEntry>? = null

  companion object {
    val CODEC = RecordCodecBuilder.create {
      it.group(
        ResourceKey.codec(T7DatapackRegistries.RESEARCH_CATEGORY).fieldOf("category").forGetter(ResearchEntry::category),
        T7ExtraCodecs.VECTOR2I.fieldOf("position").forGetter(ResearchEntry::position),
        Codec.BOOL.fieldOf("preferX").forGetter(ResearchEntry::preferX),
        ResourceKey.codec(T7DatapackRegistries.RESEARCH_ENTRY).listOf().fieldOf("children").forGetter(ResearchEntry::children),
        Page.CODEC.listOf().fieldOf("pages").forGetter(ResearchEntry::pages),
        ItemStack.STRICT_CODEC.fieldOf("icon").forGetter(ResearchEntry::icon),
        ComponentSerialization.CODEC.fieldOf("title").forGetter(ResearchEntry::title),
        SocketState.CODEC.listOf().fieldOf("defaultResearchState").forGetter(ResearchEntry::defaultResearchState),
        Codec.BOOL.fieldOf("defaultKnown").forGetter(ResearchEntry::defaultKnown),
      ).apply(it, ::ResearchEntry)
    }

    fun translationId(entryKey: ResourceKey<ResearchEntry>) = Util.makeDescriptionId(T7DatapackRegistries.RESEARCH_ENTRY.location().path, entryKey.location())

    val TOAST_TRANSLATION = "research." + Thavma.MODID + ".toast"
    val SCROLL_GIVEN_TRANSLATION = "research." + Thavma.MODID + ".scroll_given"
  }

  fun resolveChildren(): List<ResearchEntry> {
    resolvedChildren?.let { return it }

    val registry = clientRegistry(T7DatapackRegistries.RESEARCH_ENTRY)
    if (registry == null) return listOf()

    val resolved = children.map { registry.getOrThrow(it) }
    resolvedChildren = resolved

    return resolved
  }
}
