package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.Thavma
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
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.INFUSED_DEEPSLATES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.INFUSED_STONES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.MATRIX
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ORICHALCUM_BLOCK
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.PEDESTAL
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.PILLAR
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.RESEARCH_TABLE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_LEAVES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_LOG
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_PLANKS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_SAPLING
import me.alegian.thavma.impl.init.registries.deferred.WandCoreMaterials.GREATWOOD
import me.alegian.thavma.impl.init.registries.deferred.WandCoreMaterials.SILVERWOOD
import me.alegian.thavma.impl.init.registries.deferred.WandCoreMaterials.WOOD
import me.alegian.thavma.impl.init.registries.deferred.WandHandleMaterials.ARCANUM
import me.alegian.thavma.impl.init.registries.deferred.WandHandleMaterials.GOLD
import me.alegian.thavma.impl.init.registries.deferred.WandHandleMaterials.IRON
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.neoforged.neoforge.registries.DeferredRegister

object T7CreativeModeTabs {
    val REGISTRAR = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Thavma.MODID)

    init {
        REGISTRAR.register(
            Thavma.MODID
        ) { ->
            CreativeModeTab
                .builder()
                .title(Component.translatable(Thavma.MODID))
                .icon { T7Items.THAUMONOMICON.get().defaultInstance }
                .displayItems { _, output ->
                    output.accept(AURA_NODE.get())
                    output.accept(ESSENTIA_CONTAINER.get())

                    output.accept(CRUCIBLE.get())
                    output.accept(ARCANE_WORKBENCH.get())
                    output.accept(RESEARCH_TABLE.get())
                    output.accept(MATRIX.get())
                    output.accept(PILLAR.get())
                    output.accept(PEDESTAL.get())
                    output.accept(PEDESTAL.get())

                    output.accept(ELEMENTAL_STONE.get())

                    for(infusedStone in INFUSED_STONES.values) output.accept(infusedStone.get())
                    for(infusedStone in INFUSED_DEEPSLATES.values) output.accept(infusedStone.get())

                    output.accept(ARCANUM_BLOCK.get())
                    output.accept(ORICHALCUM_BLOCK.get())

                    output.accept(GREATWOOD_LEAVES.get())
                    output.accept(GREATWOOD_LOG.get())
                    output.accept(GREATWOOD_PLANKS.get())
                    output.accept(GREATWOOD_SAPLING.get())

                    output.accept(SILVERWOOD_LEAVES.get())
                    output.accept(SILVERWOOD_SAPLING.get())
                    output.accept(SILVERWOOD_LOG.get())
                    output.accept(SILVERWOOD_PLANKS.get())

                    output.accept(T7Items.IRON_HANDLE)
                    output.accept(T7Items.GOLD_HANDLE)
                    output.accept(T7Items.ORICHALCUM_HANDLE)
                    output.accept(T7Items.ARCANUM_HANDLE)

                    output.accept(T7Items.EYE_OF_WARDEN)
                    output.accept(T7Items.ROTTEN_BRAIN)
                    output.accept(T7Items.SIGIL)

                    output.accept(T7Items.GREATWOOD_CORE)
                    output.accept(T7Items.SILVERWOOD_CORE)

                    output.accept(T7Items.RUNE)
                    output.accept(T7Items.ARCANUM_INGOT)
                    output.accept(T7Items.ARCANUM_NUGGET)
                    output.accept(T7Items.ORICHALCUM_INGOT)
                    output.accept(T7Items.ORICHALCUM_NUGGET)
                    output.accept(T7Items.RESEARCH_SCROLL)
                    output.accept(T7Items.COMPLETED_RESEARCH)
                    output.accept(T7Items.OCULUS)
                    output.accept(T7Items.THAUMONOMICON)

                    output.accept(T7Items.wandOrThrow(IRON.get(), WOOD.get()))
                    output.accept(
                        T7Items.wandOrThrow(
                            GOLD.get(),
                            GREATWOOD.get()
                        )
                    )
                    output.accept(
                        T7Items.wandOrThrow(
                            ARCANUM.get(),
                            SILVERWOOD.get()
                        )
                    )

                    output.accept(T7Items.GOGGLES)
                    output.accept(T7Items.GOGGLES_CURIO)
                    output.accept(T7Items.DAWN_CHARM)
                    output.accept(T7Items.RESEARCHER_CHESTPLATE)
                    output.accept(T7Items.RESEARCHER_LEGGINGS)
                    output.accept(T7Items.RESEARCHER_BOOTS)

                    output.accept(T7Items.ARCANUM_HELMET)
                    output.accept(T7Items.ARCANUM_CHESTPLATE)
                    output.accept(T7Items.ARCANUM_LEGGINGS)
                    output.accept(T7Items.ARCANUM_BOOTS)

                    output.accept(T7Items.CUSTOS_ARCANUM_HELMET)
                    output.accept(T7Items.CUSTOS_ARCANUM_CHESTPLATE)
                    output.accept(T7Items.CUSTOS_ARCANUM_LEGGINGS)
                    output.accept(T7Items.CUSTOS_ARCANUM_BOOTS)

                    for(testa in T7Items.TESTAS.values) output.accept(testa)

                    output.accept(T7Items.ARCANUM_AXE)
                    output.accept(T7Items.ARCANUM_PICKAXE)
                    output.accept(T7Items.ARCANUM_HAMMER)
                    output.accept(T7Items.ARCANUM_SHOVEL)
                    output.accept(T7Items.ARCANUM_HOE)

                    output.accept(T7Items.ARCANUM_SWORD)
                    output.accept(T7Items.ARCANUM_KATANA)
                    output.accept(T7Items.ZEPHYR)

                    output.accept(T7Items.ANGRY_ZOMBIE_SPAWN_EGG)
                }
                .build()
        }
    }
}
