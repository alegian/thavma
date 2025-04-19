package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.client.gui.research_table.AspectWidget
import me.alegian.thavma.impl.client.gui.research_table.ButtonWidget
import me.alegian.thavma.impl.client.gui.research_table.CircleWidget
import me.alegian.thavma.impl.client.gui.research_table.ResearchScreen
import me.alegian.thavma.impl.common.block.WorkbenchBlock
import me.alegian.thavma.impl.common.book.TextPage
import me.alegian.thavma.impl.common.research.ResearchEntry
import me.alegian.thavma.impl.common.wand.WandCoreMaterial
import me.alegian.thavma.impl.common.wand.WandHandleMaterial
import me.alegian.thavma.impl.init.registries.deferred.*
import me.alegian.thavma.impl.init.registries.deferred.T7Attributes.REVEALING
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ARCANE_WORKBENCH
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ARCANUM_BLOCK
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.AURA_NODE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.CRUCIBLE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ELEMENTAL_STONE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ESSENTIA_CONTAINER
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_LEAVES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_LOG
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_PLANKS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_SAPLING
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.MATRIX
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ORICHALCUM_BLOCK
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.PEDESTAL
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.PILLAR
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.RESEARCH_TABLE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_LEAVES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_LOG
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_PLANKS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_SAPLING
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANUM_AXE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANUM_BOOTS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANUM_CHESTPLATE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANUM_HAMMER
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANUM_HANDLE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANUM_HELMET
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANUM_HOE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANUM_INGOT
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANUM_KATANA
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANUM_LEGGINGS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANUM_NUGGET
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANUM_PICKAXE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANUM_SHOVEL
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANUM_SWORD
import me.alegian.thavma.impl.init.registries.deferred.T7Items.COMPLETED_RESEARCH
import me.alegian.thavma.impl.init.registries.deferred.T7Items.CUSTOS_ARCANUM_BOOTS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.CUSTOS_ARCANUM_CHESTPLATE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.CUSTOS_ARCANUM_HELMET
import me.alegian.thavma.impl.init.registries.deferred.T7Items.CUSTOS_ARCANUM_LEGGINGS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.DAWN_CHARM
import me.alegian.thavma.impl.init.registries.deferred.T7Items.EYE_OF_WARDEN
import me.alegian.thavma.impl.init.registries.deferred.T7Items.GOGGLES
import me.alegian.thavma.impl.init.registries.deferred.T7Items.GOGGLES_CURIO
import me.alegian.thavma.impl.init.registries.deferred.T7Items.GOLD_HANDLE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.GREATWOOD_CORE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.IRON_HANDLE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.OCULUS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ORICHALCUM_HANDLE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ORICHALCUM_INGOT
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ORICHALCUM_NUGGET
import me.alegian.thavma.impl.init.registries.deferred.T7Items.RESEARCHER_BOOTS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.RESEARCHER_CHESTPLATE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.RESEARCHER_LEGGINGS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.RESEARCH_SCROLL
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ROTTEN_BRAIN
import me.alegian.thavma.impl.init.registries.deferred.T7Items.RUNE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.SIGIL
import me.alegian.thavma.impl.init.registries.deferred.T7Items.SILVERWOOD_CORE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAUMONOMICON
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ZEPHYR
import me.alegian.thavma.impl.init.registries.deferred.T7Items.wandOrThrow
import me.alegian.thavma.impl.init.registries.deferred.WandCoreMaterials.GREATWOOD
import me.alegian.thavma.impl.init.registries.deferred.WandCoreMaterials.SILVERWOOD
import me.alegian.thavma.impl.init.registries.deferred.WandCoreMaterials.WOOD
import me.alegian.thavma.impl.init.registries.deferred.WandHandleMaterials.ARCANUM
import me.alegian.thavma.impl.init.registries.deferred.WandHandleMaterials.GOLD
import me.alegian.thavma.impl.init.registries.deferred.WandHandleMaterials.IRON
import me.alegian.thavma.impl.init.registries.deferred.WandHandleMaterials.ORICHALCUM
import net.minecraft.Util
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.ai.attributes.Attribute
import net.neoforged.neoforge.common.data.LanguageProvider
import net.neoforged.neoforge.registries.DeferredHolder

class T7LanguageProvider(output: PackOutput, locale: String) : LanguageProvider(output, Thavma.MODID, locale) {
  private val aspectTranslations by lazy {
    Aspects.REGISTRAR.entries.associateBy({ it }, { it.id.path.replaceFirstChar { c -> c.uppercase() } })
  }

  override fun addTranslations() {
    for ((aspect, translation) in aspectTranslations) add(aspect.get().translationId, translation)

    add(Thavma.MODID, "Thavma")

    add(IRON_HANDLE.get(), "Iron Wand Handle")
    add(GOLD_HANDLE.get(), "Gold Wand Handle")
    add(ORICHALCUM_HANDLE.get(), "Orichalcum Wand Handle")
    add(ARCANUM_HANDLE.get(), "Arcanum Wand Handle")

    add(EYE_OF_WARDEN.get(), "Eye of Warden")
    add(ROTTEN_BRAIN.get(), "Rotten Brain")
    add(SIGIL.get(), "Sigil")

    add(GREATWOOD_CORE.get(), "Greatwood Wand Core")
    add(SILVERWOOD_CORE.get(), "Silverwood Wand Core")

    add(RUNE.get(), "Rune")
    add(ARCANUM_INGOT.get(), "Arcanum Ingot")
    add(ARCANUM_NUGGET.get(), "Arcanum Nugget")
    add(ORICHALCUM_INGOT.get(), "Orichalcum Ingot")
    add(ORICHALCUM_NUGGET.get(), "Orichalcum Nugget")
    add(RESEARCH_SCROLL.get(), "Research Scroll")
    add(COMPLETED_RESEARCH.get(), "Completed Research")
    add(OCULUS.get(), "Oculus")
    add(THAUMONOMICON.get(), "Thaumonomicon")

    add(GOGGLES.get(), "Goggles Of Revealing")
    add(GOGGLES_CURIO.get(), "Goggles Of Revealing (Curio)")
    add(DAWN_CHARM.get(), "Charm of the Dawn")
    add(RESEARCHER_BOOTS.get(), "Researcher Boots")
    add(RESEARCHER_CHESTPLATE.get(), "Researcher Chestplate")
    add(RESEARCHER_LEGGINGS.get(), "Researcher Leggings")

    add(ARCANUM_BOOTS.get(), "Arcanum Boots")
    add(ARCANUM_HELMET.get(), "Arcanum Helmet")
    add(ARCANUM_CHESTPLATE.get(), "Arcanum Chestplate")
    add(ARCANUM_LEGGINGS.get(), "Arcanum Leggings")

    add(CUSTOS_ARCANUM_BOOTS.get(), "Custos Arcanum Boots")
    add(CUSTOS_ARCANUM_HELMET.get(), "Custos Arcanum Helmet")
    add(CUSTOS_ARCANUM_CHESTPLATE.get(), "Custos Arcanum Chestplate")
    add(CUSTOS_ARCANUM_LEGGINGS.get(), "Custos Arcanum Leggings")

    for ((aspect, testa) in T7Items.TESTAS)
      add(testa.get(), aspectTranslations[aspect]!! + " Testa")

    add(ARCANUM_SWORD.get(), "Arcanum Sword")
    add(ARCANUM_AXE.get(), "Arcanum Axe")
    add(ARCANUM_PICKAXE.get(), "Arcanum Pickaxe")
    add(ARCANUM_HAMMER.get(), "Arcanum Hammer")
    add(ARCANUM_SHOVEL.get(), "Arcanum Shovel")
    add(ARCANUM_HOE.get(), "Arcanum Hoe")
    add(ARCANUM_KATANA.get(), "Arcanum Katana")
    add(ZEPHYR.get(), "Zephyr")

    val handleNames: MutableMap<WandHandleMaterial, String> = HashMap()
    handleNames[IRON.get()] = "Iron Handle"
    handleNames[GOLD.get()] = "Gold Handle"
    handleNames[ORICHALCUM.get()] = "Orichalcum Handle"
    handleNames[ARCANUM.get()] = "Arcanum Handle"

    val coreNames: MutableMap<WandCoreMaterial, String> = HashMap()
    coreNames[WOOD.get()] = "Wooden"
    coreNames[GREATWOOD.get()] = "Greatwood"
    coreNames[SILVERWOOD.get()] = "Silverwood"

    for ((key, value) in handleNames) for ((key1, value1) in coreNames) {
      val wand = wandOrThrow(key, key1)
      add(wand, "$value $value1 Wand")
    }

    add(AURA_NODE.get(), "Aura Node")
    add(CRUCIBLE.get(), "Crucible")
    add(ARCANE_WORKBENCH.get(), "Arcane Workbench")
    add(MATRIX.get(), "Infusion Matrix")
    add(PILLAR.get(), "Infusion Pillar")
    add(PEDESTAL.get(), "Infusion Pedestal")
    add(RESEARCH_TABLE.get(), "Research Table")
    add(ELEMENTAL_STONE.get(), "Elemental Stone")

    for ((aspect, infusedStone) in T7Blocks.INFUSED_STONES)
      add(infusedStone.get(), aspectTranslations[aspect]!! + " Infused Stone")
    for ((aspect, infusedDeepslate) in T7Blocks.INFUSED_DEEPSLATES)
      add(infusedDeepslate.get(), aspectTranslations[aspect]!! + " Infused Deepslate")

    add(ARCANUM_BLOCK.get(), "Arcanum Block")
    add(ORICHALCUM_BLOCK.get(), "Orichalcum Block")

    add(GREATWOOD_LOG.get(), "Greatwood Log")
    add(GREATWOOD_LEAVES.get(), "Greatwood Leaves")
    add(GREATWOOD_PLANKS.get(), "Greatwood Planks")
    add(GREATWOOD_SAPLING.get(), "Greatwood Sapling")
    add(SILVERWOOD_LOG.get(), "Silverwood Log")
    add(SILVERWOOD_LEAVES.get(), "Silverwood Leaves")
    add(SILVERWOOD_PLANKS.get(), "Silverwood Planks")
    add(SILVERWOOD_SAPLING.get(), "Silverwood Sapling")

    add(ESSENTIA_CONTAINER.get(), "Essentia Container")

    add(WorkbenchBlock.CONTAINER_TITLE, "Arcane Workbench")
    add(ResearchScreen.translationId, "Research Table")
    add(AspectWidget.descriptionTranslationId, "Click and drag to use")
    add(AspectWidget.costTranslationId, "Rune Cost:")
    add(CircleWidget.removeTranslationId, "Click to remove")
    add(ButtonWidget.leftTranslationId, "Previous Page")
    add(ButtonWidget.rightTranslationId, "Next Page")

    add(REVEALING, "Revealing")
    add(T7EntityTypes.ANGRY_ZOMBIE.get(), "Angry Zombie")
    add(T7Items.ANGRY_ZOMBIE_SPAWN_EGG.get(), "Angry Zombie Spawn Egg")

    simpleTextPage(
      ResearchEntries.WELCOME,
      "Welcome",
      """
        I was merely toying with that wand -if it can even be called that- when the book
        flew into my hands. The cover reads "Thavma", but on the inside most pages
        appear blank, sealed by some magic.
      """,
      """
        I believe I’ve stumbled upon something of
        great significance. If I am to unlock the book's secrets, I will first need to
        break that seal. It won't be easy... but I am certain it is worth my efforts.
      """
    )

    simpleTextPage(
      ResearchEntries.OCCULUS,
      "The Oculus",
      """
        The part of the book I can read describes an arcane tool that "allows the user
        to see", whatever that might mean. I have a feeling that crafting it could assist
        my work in unsealing the other pages.
      """,
      """
        I should look at the world through its lens,
        in hopes of uncovering something useful.
      """
    )
  }

  private fun add(attributeHolder: DeferredHolder<Attribute, Attribute>, name: String) {
    add(Util.makeDescriptionId(Registries.ATTRIBUTE.location().path, attributeHolder.id), name)
  }

  private fun simpleTextPage(entryKey: ResourceKey<ResearchEntry>, title: String, vararg paragraphs: String) {
    val baseId = ResearchEntry.translationId(entryKey)
    add(TextPage.titleTranslationId(baseId), title)
    for (i in paragraphs.indices)
      add(TextPage.paragraphTranslationId(baseId, i), paragraphs[i].trimIndent().replace("\n", " "))
  }
}
