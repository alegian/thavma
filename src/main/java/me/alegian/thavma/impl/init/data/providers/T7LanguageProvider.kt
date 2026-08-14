package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.client.T7KeyMappings
import me.alegian.thavma.impl.client.gui.book.PageTurningWidget
import me.alegian.thavma.impl.client.gui.layer.ArcaneLensLayer
import me.alegian.thavma.impl.client.gui.research_table.AspectWidget
import me.alegian.thavma.impl.client.gui.research_table.ButtonWidget
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

    add(T7Items.IRON_PLATING.get(), "Iron Plating")
    add(T7Items.GOLD_PLATING.get(), "Gold Plating")
    add(T7Items.ORICHALCUM_PLATING.get(), "Orichalcum Plating")
    add(T7Items.THAVMITE_PLATING.get(), "Thavmite Plating")

    add(T7Items.EYE_OF_WARDEN.get(), "Eye of Warden")
    add(T7Items.ROTTEN_BRAIN.get(), "Rotten Brain")
    add(T7Items.FABRIC.get(), "Infused Fabric")

    add(T7Items.GREATWOOD_CORE.get(), "Greatwood Wand Core")
    add(T7Items.SILVERWOOD_CORE.get(), "Silverwood Wand Core")

    add(T7Items.RUNE.get(), "Rune")
    add(T7Items.THAVMITE_INGOT.get(), "Thavmite Ingot")
    add(T7Items.THAVMITE_NUGGET.get(), "Thavmite Nugget")
    add(T7Items.ORICHALCUM_INGOT.get(), "Orichalcum Ingot")
    add(T7Items.ORICHALCUM_NUGGET.get(), "Orichalcum Nugget")
    add(T7Items.RESEARCH_SCROLL.get(), "Research Scroll")
    add(T7Items.ARCANE_LENS.get(), "Arcane Lens")
    add(T7Items.BOOK.get(), "Elements of Thavma")

    add(T7Items.BASIC_AMULET.get(), "Basic Amulet")
    add(T7Items.BASIC_BELT.get(), "Basic Belt")
    add(T7Items.BASIC_RING.get(), "Basic Ring")

    add(T7Items.GOGGLES.get(), "Goggles Of Revealing")
    add(T7Items.GOGGLES_CURIO.get(), "Goggles Of Revealing (Curio)")
    add(T7Items.DAWN_CHARM.get(), "Charm of the Dawn")
    add(T7Items.APPRENTICE_BOOTS.get(), "[WIP]Apprentice Boots")
    add(T7Items.APPRENTICE_CHESTPLATE.get(), "[WIP]Apprentice Robes")
    add(T7Items.APPRENTICE_LEGGINGS.get(), "[WIP]Apprentice Pants")

    add(T7Items.THAVMITE_BOOTS.get(), "Thavmite Boots")
    add(T7Items.THAVMITE_HELMET.get(), "Thavmite Helmet")
    add(T7Items.THAVMITE_CHESTPLATE.get(), "Thavmite Chestplate")
    add(T7Items.THAVMITE_LEGGINGS.get(), "Thavmite Leggings")

    add(T7Items.THAVMITE_VANGUARD_BOOTS.get(), "[WIP]Thavmite Vanguard Boots")
    add(T7Items.THAVMITE_VANGUARD_HELMET.get(), "[WIP]Thavmite Vanguard Helmet")
    add(T7Items.THAVMITE_VANGUARD_CHESTPLATE.get(), "[WIP]Thavmite Vanguard Chestplate")
    add(T7Items.THAVMITE_VANGUARD_LEGGINGS.get(), "[WIP]Thavmite Vanguard Leggings")

    for ((aspect, shard) in T7Items.SHARDS)
      add(shard.get(), aspectTranslations[aspect]!! + " Shard")

    add(T7Items.THAVMITE_SWORD.get(), "Thavmite Sword")
    add(T7Items.THAVMITE_AXE.get(), "Thavmite Axe")
    add(T7Items.THAVMITE_PICKAXE.get(), "Thavmite Pickaxe")
    add(T7Items.THAVMITE_HAMMER.get(), "Thavmite Hammer")
    add(T7Items.THAVMITE_SHOVEL.get(), "Thavmite Shovel")
    add(T7Items.THAVMITE_HOE.get(), "Thavmite Hoe")
    add(T7Items.THAVMITE_KATANA.get(), "[WIP]Thavmite Katana")
    add(T7Items.ZEPHYR.get(), "[WIP]Zephyr")
    add(T7Items.AXE_OF_THE_FOREST.get(), "Axe of the Forest")

    val platingNames: MutableMap<WandPlatingMaterial, String> = HashMap()
    platingNames[WandPlatingMaterials.IRON.get()] = "Iron Plated"
    platingNames[WandPlatingMaterials.GOLD.get()] = "Gold Plated"
    platingNames[WandPlatingMaterials.ORICHALCUM.get()] = "Orichalcum Plated"
    platingNames[WandPlatingMaterials.THAVMITE.get()] = "Thavmite Plated"

    val coreNames: MutableMap<WandCoreMaterial, String> = HashMap()
    coreNames[WandCoreMaterials.WOOD.get()] = "Wooden"
    coreNames[WandCoreMaterials.GREATWOOD.get()] = "Greatwood"
    coreNames[WandCoreMaterials.SILVERWOOD.get()] = "Silverwood"

    for ((pKey, pName) in platingNames) for ((cKey, cName) in coreNames) {
      val wand = T7Items.wandOrThrow(pKey, cKey)
      add(wand, "$pName $cName Wand")
    }

    add(T7Items.FOCUS_EMBERS.get(), "[WIP]Focus: Embers")
    add(T7Items.FOCUS_EXCAVATION.get(), "Focus: Excavation")
    add(T7Items.FOCUS_ENDERCHEST.get(), "Focus: Enderchest")
    add(T7Items.FOCUS_LIGHT.get(), "Focus: Light")
    add(T7Items.FOCUS_HOLE.get(), "[WIP]Focus: Hole")
    add(T7Items.FOCUS_ENDERPEARL.get(), "Focus: Enderpearl")
    add(T7Items.FOCUS_EXCHANGE.get(), "Focus: Exchange")
    add(T7Items.FOCUS_LIGHTNING.get(), "[WIP]Focus: Lightning")

    add(T7Blocks.AURA_NODE.get(), "Aura Node")
    add(T7Blocks.CRUCIBLE.get(), "Crucible")
    add(T7Blocks.ARCANE_WORKBENCH.get(), "Arcane Workbench")
    add(T7Blocks.MATRIX.get(), "Infusion Matrix")
    add(T7Blocks.PILLAR.get(), "Infusion Pillar")
    add(T7Blocks.PEDESTAL.get(), "Infusion Pedestal")
    add(T7Blocks.RESEARCH_TABLE.get(), "Research Table")
    add(T7Blocks.TABLE.get(), "Table")
    add(T7Blocks.ITEM_HATCH.get(), "Item Hatch")
    add(T7Blocks.ELEMENTAL_STONE.get(), "Elemental Stone")
    add(T7Blocks.ELEMENTAL_STONE_STAIRS.get(), "Elemental Stone Stairs")
    add(T7Blocks.ELEMENTAL_STONE_SLAB.get(), "Elemental Stone Slab")
    add(T7Blocks.ELEMENTAL_CORE.get(), "Elemental Core")
    add(T7Blocks.CRACKED_ELEMENTAL_STONE.get(), "Cracked Elemental Stone")
    add(T7Blocks.ELEMENTAL_STONE_BRICKS.get(), "Elemental Stone Bricks")

    add(T7Blocks.ARCANE_LEVITATOR.get(), "Arcane Levitator")
    add(T7Blocks.LEVITATOR_COLUMN.get(), "Arcane Levitator Column")

    for ((aspect, infusedStone) in T7Blocks.INFUSED_STONES)
      add(infusedStone.get(), aspectTranslations[aspect]!! + " Infused Stone")
    for ((aspect, infusedDeepslate) in T7Blocks.INFUSED_DEEPSLATES)
      add(infusedDeepslate.get(), aspectTranslations[aspect]!! + " Infused Deepslate")

    add(T7Blocks.THAVMITE_BLOCK.get(), "Thavmite Block")
    add(T7Blocks.ORICHALCUM_BLOCK.get(), "Orichalcum Block")

    add(T7Blocks.GREATWOOD_LOG.get(), "Greatwood Log")
    add(T7Blocks.GREATWOOD_LEAVES.get(), "Greatwood Leaves")
    add(T7Blocks.GREATWOOD_PLANKS.get(), "Greatwood Planks")
    add(T7Blocks.GREATWOOD_STAIRS.get(), "Greatwood Stairs")
    add(T7Blocks.GREATWOOD_SLAB.get(), "Greatwood Slab")
    add(T7Blocks.GREATWOOD_SAPLING.get(), "Greatwood Sapling")
    add(T7Blocks.SILVERWOOD_LOG.get(), "Silverwood Log")
    add(T7Blocks.SILVERWOOD_LEAVES.get(), "Silverwood Leaves")
    add(T7Blocks.SILVERWOOD_PLANKS.get(), "Silverwood Planks")
    add(T7Blocks.SILVERWOOD_SAPLING.get(), "Silverwood Sapling")

    add(T7Blocks.SEALING_JAR.get(), "Sealing Jar")
    add(T7Items.NODE_JAR.get(), "Node in a Jar")
    add(T7Blocks.ETERNAL_FLAME.get(), "Eternal Flame")
    add(T7Blocks.HUNGRY_CHEST.get(), "Hungry Chest")
    add(T7Blocks.HOLE.get(), "Hole Block")

    add(WorkbenchBlock.CONTAINER_TITLE, "Arcane Workbench")
    add(HungryChestBlock.CONTAINER_TITLE, "Hungry Chest")

    add(ResearchTableBlock.CONTAINER_TITLE, "Research Table")
    add(AspectWidget.descriptionTranslationId, "Click and drag to use")
    add(AspectWidget.costTranslationId, "Rune Cost:")
    add(SocketWidget.removeTranslationId, "Click to remove")
    add(ButtonWidget.leftTranslationId, "Previous Page")
    add(ButtonWidget.rightTranslationId, "Next Page")
    add(PageTurningWidget.leftTranslationId, "Previous Page")
    add(PageTurningWidget.rightTranslationId, "Next Page")

    add(T7Attributes.REVEALING, "Revealing")
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

    addCategory(ResearchCategories.STORY, "???")
    addEntry(ResearchEntries.Story.STORY1, "A Courtesy Call")

    addTextPage(
      ResearchEntries.Story.STORY1, 0,
      "A Courtesy Call 1",
      "Lorem ipsum %s 1 sit amet,",
      "this story a great meaning haveth."
    )

    addTextPage(
      ResearchEntries.Story.STORY1, 1,
      "A Courtesy Call 2",
      "Lorem dolor 2 sit amet,",
      "this story a great meaning haveth."
    )

    addTextPage(
      ResearchEntries.Story.STORY1, 2,
      "A Courtesy Call 3",
      "Lorem lotrumatum dolor 3 sit amet,",
      "this story a great meaning haveth."
    )

    addTextPage(
      ResearchEntries.Story.STORY2, 0,
      "An epic poem from times of old", "Do you feel that in the distance?", "It is of your pomp an instance!"
    )

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

    add(T7Items.RESEARCH_SCROLL.get().completedTranslation(), "Completed Research")
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
    add(
      RecipeViewerDescriptions.ARCANE_WORKBENCH,
      "Right click a crafting table with a wand to convert it to an arcane workbench."
    )
    add(RecipeViewerDescriptions.CRUCIBLE, "Right click a cauldron with a wand to convert it to a crucible.")
    add(
      RecipeViewerDescriptions.RESEARCH_TABLE,
      "Can be formed by placing 2 tables next to each other, and right clicking one with a wand."
    )
    add(
      RecipeViewerDescriptions.INFUSED_STONES,
      "A piece of stone, infused with a primal element. Found in the overworld, at any height."
    )
    add(
      RecipeViewerDescriptions.GREATWOOD,
      "Greatwoods are very tall, ancient trees. They are somewhat rare, but they can spawn in all overworld biomes."
    )
    add(
      RecipeViewerDescriptions.SILVERWOOD,
      "Silverwoods are magical trees, with uniquely blue leaves. They are very rare, but they can spawn in all overworld biomes."
    )
    add(
      RecipeViewerDescriptions.PILLAR,
      "Formed by right clicking the Infusion Matrix, after completing the Infusion Multiblock."
    )
    add(
      RecipeViewerDescriptions.RESEARCH_SCROLL,
      "Obtained by clicking any unknown entry in the \"Elements of Thavma\""
    )

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

  private fun addTextPage(
    entryKey: ResourceKey<ResearchEntry>,
    pageIndex: Int,
    title: String?,
    vararg paragraphs: String
  ) {
    val baseId = ResearchEntry.translationId(entryKey)
    if (title != null) add(TextPage.titleTranslationId(baseId, pageIndex), title)
    for (parIndex in paragraphs.indices)
      add(
        TextPage.paragraphTranslationId(baseId, pageIndex, parIndex),
        paragraphs[parIndex].trimIndent().replace("\n", " ")
      )
  }
}
