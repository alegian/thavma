package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.common.wand.WandCoreMaterial
import me.alegian.thavma.impl.common.wand.WandHandleMaterial
import me.alegian.thavma.impl.init.registries.deferred.Aspects
import me.alegian.thavma.impl.init.registries.deferred.T7Attributes.REVEALING
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
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
import me.alegian.thavma.impl.init.registries.deferred.T7EntityTypes
import me.alegian.thavma.impl.init.registries.deferred.T7Items
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
import net.minecraft.world.entity.ai.attributes.Attribute
import net.neoforged.neoforge.common.data.LanguageProvider
import net.neoforged.neoforge.registries.DeferredHolder

private val ASPECT_TRANSLATIONS = Aspects.PRIMAL_ASPECTS.associateBy({ it }, { it.id.path.replaceFirstChar { c -> c.uppercase() } })

class T7LanguageProvider(output: PackOutput, locale: String) : LanguageProvider(output, Thavma.MODID, locale) {
    override fun addTranslations() {
        this.add(Thavma.MODID, "Thavma")

        this.add(IRON_HANDLE.get(), "Iron Wand Handle")
        this.add(GOLD_HANDLE.get(), "Gold Wand Handle")
        this.add(ORICHALCUM_HANDLE.get(), "Orichalcum Wand Handle")
        this.add(ARCANUM_HANDLE.get(), "Arcanum Wand Handle")

        this.add(EYE_OF_WARDEN.get(), "Eye of Warden")
        this.add(ROTTEN_BRAIN.get(), "Rotten Brain")
        this.add(SIGIL.get(), "Sigil")

        this.add(GREATWOOD_CORE.get(), "Greatwood Wand Core")
        this.add(SILVERWOOD_CORE.get(), "Silverwood Wand Core")

        this.add(RUNE.get(), "Rune")
        this.add(ARCANUM_INGOT.get(), "Arcanum Ingot")
        this.add(ARCANUM_NUGGET.get(), "Arcanum Nugget")
        this.add(ORICHALCUM_INGOT.get(), "Orichalcum Ingot")
        this.add(ORICHALCUM_NUGGET.get(), "Orichalcum Nugget")
        this.add(RESEARCH_SCROLL.get(), "Research Scroll")
        this.add(COMPLETED_RESEARCH.get(), "Completed Research")
        this.add(OCULUS.get(), "Oculus")
        this.add(THAUMONOMICON.get(), "Thaumonomicon")

        this.add(GOGGLES.get(), "Goggles Of Revealing")
        this.add(GOGGLES_CURIO.get(), "Goggles Of Revealing (Curio)")
        this.add(DAWN_CHARM.get(), "Charm of the Dawn")
        this.add(RESEARCHER_BOOTS.get(), "Researcher Boots")
        this.add(RESEARCHER_CHESTPLATE.get(), "Researcher Chestplate")
        this.add(RESEARCHER_LEGGINGS.get(), "Researcher Leggings")

        this.add(ARCANUM_BOOTS.get(), "Arcanum Boots")
        this.add(ARCANUM_HELMET.get(), "Arcanum Helmet")
        this.add(ARCANUM_CHESTPLATE.get(), "Arcanum Chestplate")
        this.add(ARCANUM_LEGGINGS.get(), "Arcanum Leggings")

        this.add(CUSTOS_ARCANUM_BOOTS.get(), "Custos Arcanum Boots")
        this.add(CUSTOS_ARCANUM_HELMET.get(), "Custos Arcanum Helmet")
        this.add(CUSTOS_ARCANUM_CHESTPLATE.get(), "Custos Arcanum Chestplate")
        this.add(CUSTOS_ARCANUM_LEGGINGS.get(), "Custos Arcanum Leggings")

        for ((aspect, testa) in T7Items.TESTAS)
            add(testa.get(), ASPECT_TRANSLATIONS[aspect]!! + " Testa")

        this.add(ARCANUM_SWORD.get(), "Arcanum Sword")
        this.add(ARCANUM_AXE.get(), "Arcanum Axe")
        this.add(ARCANUM_PICKAXE.get(), "Arcanum Pickaxe")
        this.add(ARCANUM_HAMMER.get(), "Arcanum Hammer")
        this.add(ARCANUM_SHOVEL.get(), "Arcanum Shovel")
        this.add(ARCANUM_HOE.get(), "Arcanum Hoe")
        this.add(ARCANUM_KATANA.get(), "Arcanum Katana")
        this.add(ZEPHYR.get(), "Zephyr")

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
            this.add(wand, "$value $value1 Wand")
        }

        this.add(AURA_NODE.get(), "Aura Node")
        this.add(CRUCIBLE.get(), "Crucible")
        this.add(ARCANE_WORKBENCH.get(), "Arcane Workbench")
        this.add(MATRIX.get(), "Infusion Matrix")
        this.add(PILLAR.get(), "Infusion Pillar")
        this.add(PEDESTAL.get(), "Infusion Pedestal")
        this.add(RESEARCH_TABLE.get(), "Research Table")
        this.add(ELEMENTAL_STONE.get(), "Elemental Stone")

        for ((aspect, infusedStone) in T7Blocks.INFUSED_STONES)
            add(infusedStone.get(), ASPECT_TRANSLATIONS[aspect]!! + " Infused Stone")
        for ((aspect, infusedDeepslate) in T7Blocks.INFUSED_DEEPSLATES)
            add(infusedDeepslate.get(), ASPECT_TRANSLATIONS[aspect]!! + " Infused Deepslate")

        this.add(ARCANUM_BLOCK.get(), "Arcanum Block")
        this.add(ORICHALCUM_BLOCK.get(), "Orichalcum Block")

        this.add(GREATWOOD_LOG.get(), "Greatwood Log")
        this.add(GREATWOOD_LEAVES.get(), "Greatwood Leaves")
        this.add(GREATWOOD_PLANKS.get(), "Greatwood Planks")
        this.add(GREATWOOD_SAPLING.get(), "Greatwood Sapling")
        this.add(SILVERWOOD_LOG.get(), "Silverwood Log")
        this.add(SILVERWOOD_LEAVES.get(), "Silverwood Leaves")
        this.add(SILVERWOOD_PLANKS.get(), "Silverwood Planks")
        this.add(SILVERWOOD_SAPLING.get(), "Silverwood Sapling")

        this.add(ESSENTIA_CONTAINER.get(), "Essentia Container")

        this.add("container." + Thavma.MODID + ".arcane_workbench", "Arcane Workbench")

        this.add(REVEALING, "Revealing")
        this.add(T7EntityTypes.ANGRY_ZOMBIE.get(), "Angry Zombie")
        this.add(T7Items.ANGRY_ZOMBIE_SPAWN_EGG.get(), "Angry Zombie Spawn Egg")
    }

    private fun add(attributeHolder: DeferredHolder<Attribute, Attribute>, name: String) {
        this.add(Util.makeDescriptionId(Registries.ATTRIBUTE.location().path, attributeHolder.id), name)
    }
}
