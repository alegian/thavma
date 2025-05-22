package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.init.registries.T7Tags.CATALYST
import me.alegian.thavma.impl.init.registries.T7Tags.SHARD
import me.alegian.thavma.impl.init.registries.T7Tags.WAND_CORE
import me.alegian.thavma.impl.init.registries.T7Tags.WAND_HANDLE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.APPRENTICE_BOOTS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.APPRENTICE_CHESTPLATE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.APPRENTICE_LEGGINGS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.GOGGLES
import me.alegian.thavma.impl.init.registries.deferred.T7Items.GOLD_HANDLE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.GREATWOOD_CORE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.IRON_HANDLE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ORICHALCUM_HANDLE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ORICHALCUM_INGOT
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ORICHALCUM_NUGGET
import me.alegian.thavma.impl.init.registries.deferred.T7Items.SHARDS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.SILVERWOOD_CORE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_AXE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_BOOTS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_CHESTPLATE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_HAMMER
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_HANDLE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_HELMET
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_HOE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_INGOT
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_KATANA
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_LEGGINGS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_NUGGET
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_PICKAXE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_SHOVEL
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_SWORD
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_VANGUARD_BOOTS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_VANGUARD_CHESTPLATE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_VANGUARD_HELMET
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_VANGUARD_LEGGINGS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ZEPHYR
import me.alegian.thavma.impl.integration.curios.CuriosIntegration
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class T7ItemTagProvider(pOutput: PackOutput, pLookupProvider: CompletableFuture<HolderLookup.Provider?>, pBlockTags: CompletableFuture<TagLookup<Block?>?>, pExistingFileHelper: ExistingFileHelper?) : ItemTagsProvider(pOutput, pLookupProvider, pBlockTags, Thavma.MODID, pExistingFileHelper) {
  override fun addTags(lookupProvider: HolderLookup.Provider) {
    tag(WAND_HANDLE).add(
      IRON_HANDLE.get(),
      GOLD_HANDLE.get(),
      ORICHALCUM_HANDLE.get(),
      THAVMITE_HANDLE.get()
    )

    tag(WAND_CORE)
      .addTag(Tags.Items.RODS_WOODEN)
      .add(
        GREATWOOD_CORE.get(),
        SILVERWOOD_CORE.get()
      )

    for (shard in SHARDS.values) tag(SHARD).add(shard.get())

    tag(Tags.Items.INGOTS).add(
      THAVMITE_INGOT.get(),
      ORICHALCUM_INGOT.get()
    )

    tag(Tags.Items.NUGGETS).add(
      THAVMITE_NUGGET.get(),
      ORICHALCUM_NUGGET.get()
    )

    tag(ItemTags.SWORDS).add(
      THAVMITE_SWORD.get(),
      THAVMITE_KATANA.get(),
      ZEPHYR.get()
    )
    tag(ItemTags.AXES).add(THAVMITE_AXE.get())
    tag(ItemTags.PICKAXES).add(THAVMITE_PICKAXE.get())
    tag(ItemTags.MINING_ENCHANTABLE).add(THAVMITE_HAMMER.get())
    tag(ItemTags.MINING_LOOT_ENCHANTABLE).add(THAVMITE_HAMMER.get())
    tag(Tags.Items.MINING_TOOL_TOOLS).add(THAVMITE_HAMMER.get())
    tag(ItemTags.SHOVELS).add(THAVMITE_SHOVEL.get())
    tag(ItemTags.HOES).add(THAVMITE_HOE.get())

    tag(ItemTags.FOOT_ARMOR).add(
      THAVMITE_BOOTS.get(),
      THAVMITE_VANGUARD_BOOTS.get(),
      APPRENTICE_BOOTS.get()
    )
    tag(ItemTags.LEG_ARMOR).add(
      THAVMITE_LEGGINGS.get(),
      THAVMITE_VANGUARD_LEGGINGS.get(),
      APPRENTICE_LEGGINGS.get()
    )
    tag(ItemTags.CHEST_ARMOR).add(
      THAVMITE_CHESTPLATE.get(),
      THAVMITE_VANGUARD_CHESTPLATE.get(),
      APPRENTICE_CHESTPLATE.get()
    )
    tag(ItemTags.HEAD_ARMOR).add(
      GOGGLES.get(),
      THAVMITE_HELMET.get(),
      THAVMITE_VANGUARD_HELMET.get()
    )

    tag(CATALYST).add(Items.DRAGON_EGG)

    CuriosIntegration.get().addTags(this, lookupProvider)
  }

  // override visibility
  public override fun tag(tag: TagKey<Item>): IntrinsicTagAppender<Item> {
    return super.tag(tag)
  }
}
