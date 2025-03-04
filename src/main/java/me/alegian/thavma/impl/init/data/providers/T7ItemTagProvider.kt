package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.init.registries.T7Tags.CATALYST
import me.alegian.thavma.impl.init.registries.T7Tags.TESTA
import me.alegian.thavma.impl.init.registries.T7Tags.WAND_CORE
import me.alegian.thavma.impl.init.registries.T7Tags.WAND_HANDLE
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
import me.alegian.thavma.impl.init.registries.deferred.T7Items.CUSTOS_ARCANUM_BOOTS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.CUSTOS_ARCANUM_CHESTPLATE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.CUSTOS_ARCANUM_HELMET
import me.alegian.thavma.impl.init.registries.deferred.T7Items.CUSTOS_ARCANUM_LEGGINGS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.GOGGLES
import me.alegian.thavma.impl.init.registries.deferred.T7Items.GOLD_HANDLE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.GREATWOOD_CORE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.IRON_HANDLE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ORICHALCUM_HANDLE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ORICHALCUM_INGOT
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ORICHALCUM_NUGGET
import me.alegian.thavma.impl.init.registries.deferred.T7Items.RESEARCHER_BOOTS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.RESEARCHER_CHESTPLATE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.RESEARCHER_LEGGINGS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.SILVERWOOD_CORE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.TESTAS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ZEPHYR
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class T7ItemTagProvider(pOutput: PackOutput, pLookupProvider: CompletableFuture<HolderLookup.Provider?>, pBlockTags: CompletableFuture<TagLookup<Block?>?>, pExistingFileHelper: ExistingFileHelper?) : ItemTagsProvider(pOutput, pLookupProvider, pBlockTags, Thavma.MODID, pExistingFileHelper) {
  override fun addTags(pProvider: HolderLookup.Provider) {
    tag(WAND_HANDLE).add(
      IRON_HANDLE.get(),
      GOLD_HANDLE.get(),
      ORICHALCUM_HANDLE.get(),
      ARCANUM_HANDLE.get()
    )

    tag(WAND_CORE)
      .addTag(Tags.Items.RODS_WOODEN)
      .add(
        GREATWOOD_CORE.get(),
        SILVERWOOD_CORE.get()
      )

    for (testa in TESTAS.values) tag(TESTA).add(testa.get())

    tag(Tags.Items.INGOTS).add(
      ARCANUM_INGOT.get(),
      ORICHALCUM_INGOT.get()
    )

    tag(Tags.Items.NUGGETS).add(
      ARCANUM_NUGGET.get(),
      ORICHALCUM_NUGGET.get()
    )

    tag(ItemTags.SWORDS).add(
      ARCANUM_SWORD.get(),
      ARCANUM_KATANA.get(),
      ZEPHYR.get()
    )
    tag(ItemTags.AXES).add(ARCANUM_AXE.get())
    tag(ItemTags.PICKAXES).add(ARCANUM_PICKAXE.get())
    tag(ItemTags.MINING_ENCHANTABLE).add(ARCANUM_HAMMER.get())
    tag(ItemTags.MINING_LOOT_ENCHANTABLE).add(ARCANUM_HAMMER.get())
    tag(Tags.Items.MINING_TOOL_TOOLS).add(ARCANUM_HAMMER.get())
    tag(ItemTags.SHOVELS).add(ARCANUM_SHOVEL.get())
    tag(ItemTags.HOES).add(ARCANUM_HOE.get())

    tag(ItemTags.FOOT_ARMOR).add(
      ARCANUM_BOOTS.get(),
      CUSTOS_ARCANUM_BOOTS.get(),
      RESEARCHER_BOOTS.get()
    )
    tag(ItemTags.LEG_ARMOR).add(
      ARCANUM_LEGGINGS.get(),
      CUSTOS_ARCANUM_LEGGINGS.get(),
      RESEARCHER_LEGGINGS.get()
    )
    tag(ItemTags.CHEST_ARMOR).add(
      ARCANUM_CHESTPLATE.get(),
      CUSTOS_ARCANUM_CHESTPLATE.get(),
      RESEARCHER_CHESTPLATE.get()
    )
    tag(ItemTags.HEAD_ARMOR).add(
      GOGGLES.get(),
      ARCANUM_HELMET.get(),
      CUSTOS_ARCANUM_HELMET.get()
    )

    tag(CATALYST).add(Items.DRAGON_EGG)
  }
}
