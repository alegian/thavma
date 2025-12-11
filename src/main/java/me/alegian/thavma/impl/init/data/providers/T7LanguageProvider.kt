package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.client.T7KeyMappings
import me.alegian.thavma.impl.client.gui.layer.ArcaneLensLayer
import me.alegian.thavma.impl.client.gui.research_table.AspectWidget
import me.alegian.thavma.impl.client.gui.research_table.ButtonWidget
import me.alegian.thavma.impl.client.gui.research_table.ResearchScreen
import me.alegian.thavma.impl.client.gui.research_table.SocketWidget
import me.alegian.thavma.impl.client.gui.tooltip.AspectClientTooltipComponent
import me.alegian.thavma.impl.common.block.HungryChestBlock
import me.alegian.thavma.impl.common.block.ResearchTableBlock
import me.alegian.thavma.impl.common.block.WorkbenchBlock
import me.alegian.thavma.impl.common.book.TextPage
import me.alegian.thavma.impl.common.recipe.translationId
import me.alegian.thavma.impl.common.research.ResearchCategory
import me.alegian.thavma.impl.common.research.ResearchEntry
import me.alegian.thavma.impl.common.wand.WandCoreMaterial
import me.alegian.thavma.impl.common.wand.WandPlatingMaterial
import me.alegian.thavma.impl.init.registries.T7Tags
import me.alegian.thavma.impl.init.registries.deferred.*
import me.alegian.thavma.impl.init.registries.deferred.T7Attributes.REVEALING
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ARCANE_LEVITATOR
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ARCANE_WORKBENCH
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.AURA_NODE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.CRACKED_ELEMENTAL_STONE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.CRUCIBLE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ELEMENTAL_CORE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ELEMENTAL_STONE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ELEMENTAL_STONE_BRICKS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ELEMENTAL_STONE_SLAB
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ELEMENTAL_STONE_STAIRS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ETERNAL_FLAME
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_LEAVES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_LOG
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_PLANKS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_SAPLING
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_SLAB
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_STAIRS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.HUNGRY_CHEST
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ITEM_HATCH
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.LEVITATOR_COLUMN
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.MATRIX
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ORICHALCUM_BLOCK
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.PEDESTAL
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.PILLAR
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.RESEARCH_TABLE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SEALING_JAR
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_LEAVES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_LOG
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_PLANKS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_SAPLING
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.TABLE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.THAVMITE_BLOCK
import me.alegian.thavma.impl.init.registries.deferred.T7Items.APPRENTICE_BOOTS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.APPRENTICE_CHESTPLATE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.APPRENTICE_LEGGINGS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANE_LENS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.BOOK
import me.alegian.thavma.impl.init.registries.deferred.T7Items.DAWN_CHARM
import me.alegian.thavma.impl.init.registries.deferred.T7Items.EYE_OF_WARDEN
import me.alegian.thavma.impl.init.registries.deferred.T7Items.FABRIC
import me.alegian.thavma.impl.init.registries.deferred.T7Items.GOGGLES
import me.alegian.thavma.impl.init.registries.deferred.T7Items.GOGGLES_CURIO
import me.alegian.thavma.impl.init.registries.deferred.T7Items.GOLD_PLATING
import me.alegian.thavma.impl.init.registries.deferred.T7Items.GREATWOOD_CORE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.IRON_PLATING
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ORICHALCUM_INGOT
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ORICHALCUM_NUGGET
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ORICHALCUM_PLATING
import me.alegian.thavma.impl.init.registries.deferred.T7Items.RESEARCH_SCROLL
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ROTTEN_BRAIN
import me.alegian.thavma.impl.init.registries.deferred.T7Items.RUNE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.SILVERWOOD_CORE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_BOOTS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_CHESTPLATE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_HAMMER
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_HELMET
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_HOE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_INGOT
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_KATANA
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_LEGGINGS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_NUGGET
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_PICKAXE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_PLATING
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_SHOVEL
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_SWORD
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_VANGUARD_BOOTS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_VANGUARD_CHESTPLATE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_VANGUARD_HELMET
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_VANGUARD_LEGGINGS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ZEPHYR
import me.alegian.thavma.impl.init.registries.deferred.T7Items.wandOrThrow
import me.alegian.thavma.impl.init.registries.deferred.WandCoreMaterials.GREATWOOD
import me.alegian.thavma.impl.init.registries.deferred.WandCoreMaterials.SILVERWOOD
import me.alegian.thavma.impl.init.registries.deferred.WandCoreMaterials.WOOD
import me.alegian.thavma.impl.init.registries.deferred.WandPlatingMaterials.GOLD
import me.alegian.thavma.impl.init.registries.deferred.WandPlatingMaterials.IRON
import me.alegian.thavma.impl.init.registries.deferred.WandPlatingMaterials.ORICHALCUM
import me.alegian.thavma.impl.init.registries.deferred.WandPlatingMaterials.THAVMITE
import me.alegian.thavma.impl.integration.RecipeViewerAliases
import me.alegian.thavma.impl.integration.RecipeViewerDescriptions
import net.minecraft.Util
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.common.data.LanguageProvider
import net.neoforged.neoforge.registries.DeferredHolder

class T7LanguageProvider(output: PackOutput, locale: String) : LanguageProvider(output, Thavma.MODID, locale) {
  private val aspectTranslations by lazy {
    Aspects.REGISTRAR.entries.associateBy({ it }, { it.id.path.replaceFirstChar { c -> c.uppercase() } })
  }

  override fun addTranslations() {
    for ((aspect, translation) in aspectTranslations) add(aspect.get().translationId, translation)

    add(Thavma.MODID, "Thavma")

    add(IRON_PLATING.get(), "Iron Plating")
    add(GOLD_PLATING.get(), "Gold Plating")
    add(ORICHALCUM_PLATING.get(), "Orichalcum Plating")
    add(THAVMITE_PLATING.get(), "Thavmite Plating")

    add(EYE_OF_WARDEN.get(), "Eye of Warden")
    add(ROTTEN_BRAIN.get(), "Rotten Brain")
    add(FABRIC.get(), "Infused Fabric")

    add(GREATWOOD_CORE.get(), "Greatwood Wand Core")
    add(SILVERWOOD_CORE.get(), "Silverwood Wand Core")

    add(RUNE.get(), "Rune")
    add(THAVMITE_INGOT.get(), "Thavmite Ingot")
    add(THAVMITE_NUGGET.get(), "Thavmite Nugget")
    add(ORICHALCUM_INGOT.get(), "Orichalcum Ingot")
    add(ORICHALCUM_NUGGET.get(), "Orichalcum Nugget")
    add(RESEARCH_SCROLL.get(), "Research Scroll")
    add(ARCANE_LENS.get(), "Arcane Lens")
    add(BOOK.get(), "Elements of Thavma")

    add(T7Items.BASIC_AMULET.get(), "Basic Amulet")
    add(T7Items.BASIC_BELT.get(), "Basic Belt")
    add(T7Items.BASIC_RING.get(), "Basic Ring")

    add(GOGGLES.get(), "Goggles Of Revealing")
    add(GOGGLES_CURIO.get(), "Goggles Of Revealing (Curio)")
    add(DAWN_CHARM.get(), "Charm of the Dawn")
    add(APPRENTICE_BOOTS.get(), "[WIP]Apprentice Boots")
    add(APPRENTICE_CHESTPLATE.get(), "[WIP]Apprentice Robes")
    add(APPRENTICE_LEGGINGS.get(), "[WIP]Apprentice Pants")

    add(THAVMITE_BOOTS.get(), "Thavmite Boots")
    add(THAVMITE_HELMET.get(), "Thavmite Helmet")
    add(THAVMITE_CHESTPLATE.get(), "Thavmite Chestplate")
    add(THAVMITE_LEGGINGS.get(), "Thavmite Leggings")

    add(THAVMITE_VANGUARD_BOOTS.get(), "[WIP]Thavmite Vanguard Boots")
    add(THAVMITE_VANGUARD_HELMET.get(), "[WIP]Thavmite Vanguard Helmet")
    add(THAVMITE_VANGUARD_CHESTPLATE.get(), "[WIP]Thavmite Vanguard Chestplate")
    add(THAVMITE_VANGUARD_LEGGINGS.get(), "[WIP]Thavmite Vanguard Leggings")

    for ((aspect, shard) in T7Items.SHARDS)
      add(shard.get(), aspectTranslations[aspect]!! + " Shard")

    add(THAVMITE_SWORD.get(), "Thavmite Sword")
    add(T7Items.THAVMITE_AXE.get(), "Thavmite Axe")
    add(THAVMITE_PICKAXE.get(), "Thavmite Pickaxe")
    add(THAVMITE_HAMMER.get(), "Thavmite Hammer")
    add(THAVMITE_SHOVEL.get(), "Thavmite Shovel")
    add(THAVMITE_HOE.get(), "Thavmite Hoe")
    add(THAVMITE_KATANA.get(), "[WIP]Thavmite Katana")
    add(ZEPHYR.get(), "[WIP]Zephyr")
    add(T7Items.AXE_OF_THE_FOREST.get(), "Axe of the Forest")

    val platingNames: MutableMap<WandPlatingMaterial, String> = HashMap()
    platingNames[IRON.get()] = "Iron Plated"
    platingNames[GOLD.get()] = "Gold Plated"
    platingNames[ORICHALCUM.get()] = "Orichalcum Plated"
    platingNames[THAVMITE.get()] = "Thavmite Plated"

    val coreNames: MutableMap<WandCoreMaterial, String> = HashMap()
    coreNames[WOOD.get()] = "Wooden"
    coreNames[GREATWOOD.get()] = "Greatwood"
    coreNames[SILVERWOOD.get()] = "Silverwood"

    for ((pKey, pName) in platingNames) for ((cKey, cName) in coreNames) {
      val wand = wandOrThrow(pKey, cKey)
      add(wand, "$pName $cName Wand")
    }

    add(T7Items.FOCUS_EMBERS.get(), "[WIP]Focus: Embers")
    add(T7Items.FOCUS_EXCAVATION.get(), "[WIP]Focus: Excavation")
    add(T7Items.FOCUS_ENDERCHEST.get(), "Focus: Enderchest")
    add(T7Items.FOCUS_LIGHT.get(), "Focus: Light")
    add(T7Items.FOCUS_HOLE.get(), "[WIP]Focus: Hole")
    add(T7Items.FOCUS_ENDERPEARL.get(), "Focus: Enderpearl")
    add(T7Items.FOCUS_EXCHANGE.get(), "Focus: Exchange")
    add(T7Items.FOCUS_LIGHTNING.get(), "[WIP]Focus: Lightning")

    add(AURA_NODE.get(), "Aura Node")
    add(CRUCIBLE.get(), "Crucible")
    add(ARCANE_WORKBENCH.get(), "Arcane Workbench")
    add(MATRIX.get(), "Infusion Matrix")
    add(PILLAR.get(), "Infusion Pillar")
    add(PEDESTAL.get(), "Infusion Pedestal")
    add(RESEARCH_TABLE.get(), "Research Table")
    add(TABLE.get(), "Table")
    add(ITEM_HATCH.get(), "Item Hatch")
    add(ELEMENTAL_STONE.get(), "Elemental Stone")
    add(ELEMENTAL_STONE_STAIRS.get(), "Elemental Stone Stairs")
    add(ELEMENTAL_STONE_SLAB.get(), "Elemental Stone Slab")
    add(ELEMENTAL_CORE.get(), "Elemental Core")
    add(CRACKED_ELEMENTAL_STONE.get(), "Cracked Elemental Stone")
    add(ELEMENTAL_STONE_BRICKS.get(), "Elemental Stone Bricks")

    add(ARCANE_LEVITATOR.get(), "Arcane Levitator")
    add(LEVITATOR_COLUMN.get(), "Arcane Levitator Column")

    for ((aspect, infusedStone) in T7Blocks.INFUSED_STONES)
      add(infusedStone.get(), aspectTranslations[aspect]!! + " Infused Stone")
    for ((aspect, infusedDeepslate) in T7Blocks.INFUSED_DEEPSLATES)
      add(infusedDeepslate.get(), aspectTranslations[aspect]!! + " Infused Deepslate")

    add(THAVMITE_BLOCK.get(), "Thavmite Block")
    add(ORICHALCUM_BLOCK.get(), "Orichalcum Block")

    add(GREATWOOD_LOG.get(), "Greatwood Log")
    add(GREATWOOD_LEAVES.get(), "Greatwood Leaves")
    add(GREATWOOD_PLANKS.get(), "Greatwood Planks")
    add(GREATWOOD_STAIRS.get(), "Greatwood Stairs")
    add(GREATWOOD_SLAB.get(), "Greatwood Slab")
    add(GREATWOOD_SAPLING.get(), "Greatwood Sapling")
    add(SILVERWOOD_LOG.get(), "Silverwood Log")
    add(SILVERWOOD_LEAVES.get(), "Silverwood Leaves")
    add(SILVERWOOD_PLANKS.get(), "Silverwood Planks")
    add(SILVERWOOD_SAPLING.get(), "Silverwood Sapling")

    add(SEALING_JAR.get(), "Sealing Jar")
    add(T7Items.NODE_JAR.get(), "Node in a Jar")
    add(ETERNAL_FLAME.get(), "Eternal Flame")
    add(HUNGRY_CHEST.get(), "Hungry Chest")

    add(WorkbenchBlock.CONTAINER_TITLE, "Arcane Workbench")
    add(HungryChestBlock.CONTAINER_TITLE, "Hungry Chest")

    add(ResearchTableBlock.CONTAINER_TITLE, "Research Table")
    add(AspectWidget.descriptionTranslationId, "Click and drag to use")
    add(AspectWidget.costTranslationId, "Rune Cost:")
    add(SocketWidget.removeTranslationId, "Click to remove")
    add(ButtonWidget.leftTranslationId, "Previous Page")
    add(ButtonWidget.rightTranslationId, "Next Page")

    add(REVEALING, "Revealing")
    add(T7EntityTypes.ANGRY_ZOMBIE.get(), "Angry Zombie")
    add(T7Items.ANGRY_ZOMBIE_SPAWN_EGG.get(), "Angry Zombie Spawn Egg")

    addCategory(ResearchCategories.THAVMA, "Thavma")
    addEntry(ResearchEntries.Thavma.THAVMA, "Thavma")
    addEntry(ResearchEntries.Thavma.ORES, "Ores")
    addEntry(ResearchEntries.Thavma.TREES, "Trees")
    addEntry(ResearchEntries.Thavma.ARCANE_LENS, "The Arcane Lens")
    addEntry(ResearchEntries.Thavma.RESEARCH_TABLE, "Research Table")
    addEntry(ResearchEntries.Thavma.RESEARCH_PROFICIENCY, "Research Proficiency")
    addEntry(ResearchEntries.Thavma.ALCHEMY, "Alchemy")
    addEntry(ResearchEntries.Thavma.INFUSION, "Infusion")
    addEntry(ResearchEntries.Thavma.WANDS, "Wands")
    addEntry(ResearchEntries.Thavma.TECHNOLOGY, "Technology")

    addCategory(ResearchCategories.ALCHEMY, "Alchemy")
    addEntry(ResearchEntries.Alchemy.ALCHEMY, "Alchemy")

    addTextPage(
      ResearchEntries.Thavma.THAVMA, 0,
      "Thavma",
      """
        I was merely toying with that wand -if it can even be called that- when this tome
        flew into my hands! I can sense great power within it.
      """,
      """
        The cover reads "Elements of Thavma", but a lot of its pages appear blank, sealed by some magic.
      """,
      """
        To read them, I will first need to break that seal. It won't be easy... but
        I have a feeling it will be worth my efforts.
      """
    )

    addTextPage(
      ResearchEntries.Thavma.THAVMA, 1,
      null,
      """
        I will document all my findings inside the book, so that I can recall them later.
      """
    )

    addTextPage(
      ResearchEntries.Thavma.ARCANE_LENS, 0,
      "The Arcane Lens",
      """
        The part of the book I can read describes an arcane tool that "allows the user
        to see", whatever that might mean. I have a feeling that crafting it could assist
        my work in unsealing the other pages.
      """,
      """
        The blueprint describes a hexagonal device, much like a prism,
        made with those colorful crystals I found lying in a cave.
      """,
      """
        I should look at the world through its lens, maybe it will uncover something useful.
      """
    )

    add(RESEARCH_SCROLL.get().completedTranslation(), "Completed Research")
    add(ResearchEntry.TOAST_TRANSLATION, "Research Complete!")
    add(ResearchEntry.SCROLL_GIVEN_TRANSLATION, "You have received a research scroll")
    add(ResearchEntry.PARENTS_UNKNOWN_TRANSLATION, "You are missing required knowledge for this research")

    add(AspectClientTooltipComponent.I18n.NOT_SCANNED, "Not Scanned")

    add(RecipeType.CRAFTING.translationId, "Crafting")
    add(T7RecipeTypes.WORKBENCH.get().translationId, "Arcane Workbench")
    add(T7RecipeTypes.INFUSION.get().translationId, "Infusion")
    add(T7RecipeTypes.CRUCIBLE.get().translationId, "Crucible")

    add(RecipeViewerDescriptions.ROTTEN_BRAIN, "Sometimes dropped by angry zombies.")
    add(RecipeViewerDescriptions.BOOK, "Right click a bookcase with a wand to obtain!")
    add(RecipeViewerDescriptions.ARCANE_WORKBENCH, "Right click a crafting table with a wand to convert it to an arcane workbench.")
    add(RecipeViewerDescriptions.CRUCIBLE, "Right click a cauldron with a wand to convert it to a crucible.")
    add(RecipeViewerDescriptions.RESEARCH_TABLE, "Can be formed by placing 2 tables next to each other, and right clicking one with a wand.")
    add(RecipeViewerDescriptions.INFUSED_STONES, "A piece of stone, infused with a primal element. Found in the overworld, at any height.")
    add(RecipeViewerDescriptions.GREATWOOD, "Greatwoods are very tall, ancient trees. They are somewhat rare, but they can spawn in all overworld biomes.")
    add(RecipeViewerDescriptions.SILVERWOOD, "Silverwoods are magical trees, with uniquely blue leaves. They are very rare, but they can spawn in all overworld biomes.")
    add(RecipeViewerDescriptions.PILLAR, "Formed by right clicking the Infusion Matrix, after completing the Infusion Multiblock.")
    add(RecipeViewerDescriptions.RESEARCH_SCROLL, "Obtained by clicking any unknown entry in the \"Elements of Thavma\"")

    add(RecipeViewerAliases.BOOK, "Book")
    add(RecipeViewerAliases.ORE, "Ore")

    add(T7KeyMappings.FOCI.name, "Wand Foci Wheel")

    add(T7Tags.Blocks.INFUSED_STONES, "Infused Stones")
    add(T7Tags.Items.INFUSED_STONES, "Infused Stones")
    add(T7Tags.Blocks.CRUCIBLE_HEAT_SOURCES, "Crucible Block Heat Sources")
    add(T7Tags.Fluids.CRUCIBLE_HEAT_SOURCES, "Crucible Fluid Heat Sources")
    add(T7Tags.Items.GOGGLES, "Goggles of Revealing")
    add(T7Tags.Items.SHARDS, "Elemental Shards")
    add(T7Tags.Items.WAND_CORES, "Wand Cores")
    add(T7Tags.Items.WAND_PLATINGS, "Wand Platings")
    add(T7Tags.Items.CATALYSTS, "Crucible Catalysts")
    add(T7Tags.Items.STEP_HEIGHT, "Items that increase Step Height")
    add(T7Tags.Items.TREE_FELLING, "Items that Fell Trees")

    add(ArcaneLensLayer.NO_ASPECTS, "Nothing can be learned from this")
  }

  private fun add(attributeHolder: DeferredHolder<Attribute, Attribute>, name: String) {
    add(Util.makeDescriptionId(Registries.ATTRIBUTE.location().path, attributeHolder.id), name)
  }

  private fun addEntry(key: ResourceKey<ResearchEntry>, name: String) {
    add(ResearchEntry.translationId(key), name)
  }

  private fun addCategory(key: ResourceKey<ResearchCategory>, name: String) {
    add(ResearchCategory.translationId(key), name)
  }

  private fun addTextPage(entryKey: ResourceKey<ResearchEntry>, pageIndex: Int, title: String?, vararg paragraphs: String) {
    val baseId = ResearchEntry.translationId(entryKey)
    if (title != null) add(TextPage.titleTranslationId(baseId, pageIndex), title)
    for (parIndex in paragraphs.indices)
      add(TextPage.paragraphTranslationId(baseId, pageIndex, parIndex), paragraphs[parIndex].trimIndent().replace("\n", " "))
  }
}
