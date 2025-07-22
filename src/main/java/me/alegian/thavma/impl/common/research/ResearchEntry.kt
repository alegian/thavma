package me.alegian.thavma.impl.common.research

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.client.clientPlayer
import me.alegian.thavma.impl.client.clientRegistry
import me.alegian.thavma.impl.common.book.Page
import me.alegian.thavma.impl.common.entity.knowsResearch
import me.alegian.thavma.impl.common.util.T7ExtraCodecs
import me.alegian.thavma.impl.common.util.registry
import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import net.minecraft.Util
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import org.joml.Vector2i

private val parentsMap = mutableMapOf<ResearchEntry, List<ResourceKey<ResearchEntry>>>()

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
  val clientResolvedChildren by lazy {
    val registry = clientRegistry(T7DatapackRegistries.RESEARCH_ENTRY)
    if (registry == null)
      listOf()
    else
      children.map { registry.getOrThrow(it) }
  }
  val clientKnown by lazy {
    val registry = clientRegistry(T7DatapackRegistries.RESEARCH_ENTRY)
    if (registry == null)
      false
    else
      clientPlayer()?.knowsResearch(this) ?: false
  }

  fun parents(level: Level) =
    parentsMap.computeIfAbsent(this) { _ ->
      val registry = level.registry(T7DatapackRegistries.RESEARCH_ENTRY)
      registry.filter { e -> e.children.contains(registry.getResourceKey(this).get()) }
        .map { e -> registry.getResourceKey(e).get() }
    }

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
}
