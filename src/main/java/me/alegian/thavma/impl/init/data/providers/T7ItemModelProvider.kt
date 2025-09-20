package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.init.registries.T7ItemProperties
import me.alegian.thavma.impl.init.registries.deferred.T7Items
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ANGRY_ZOMBIE_SPAWN_EGG
import me.alegian.thavma.impl.init.registries.deferred.T7Items.APPRENTICE_BOOTS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.APPRENTICE_CHESTPLATE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.APPRENTICE_LEGGINGS
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
import me.alegian.thavma.impl.init.registries.deferred.T7Items.SHARDS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.SILVERWOOD_CORE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_AXE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_BOOTS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_CHESTPLATE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_HAMMER
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_HELMET
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_HOE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_INGOT
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
import me.alegian.thavma.impl.init.registries.deferred.T7Items.WANDS
import me.alegian.thavma.impl.rl
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredItem

class T7ItemModelProvider(output: PackOutput, existingFileHelper: ExistingFileHelper) : ItemModelProvider(output, Thavma.MODID, existingFileHelper) {
  override fun registerModels() {
    basicItem(IRON_PLATING.get())
    basicItem(GOLD_PLATING.get())
    basicItem(ORICHALCUM_PLATING.get())
    basicItem(THAVMITE_PLATING.get())

    basicItem(EYE_OF_WARDEN.get())
    basicItem(ROTTEN_BRAIN.get())
    basicItem(FABRIC.get())

    basicItem(GREATWOOD_CORE.get())
    basicItem(SILVERWOOD_CORE.get())

    basicItem(RUNE.get())
    basicItem(THAVMITE_INGOT.get())
    basicItem(THAVMITE_NUGGET.get())
    basicItem(ORICHALCUM_INGOT.get())
    basicItem(ORICHALCUM_NUGGET.get())

    val scrollRL = BuiltInRegistries.ITEM.getKey(RESEARCH_SCROLL.get())
    val completedRL = scrollRL.withSuffix("_completed")
    basicItem(completedRL)
    basicItem(scrollRL)
      .override()
      .predicate(T7ItemProperties.COMPLETED, 1f)
      .model(
        ModelFile.ExistingModelFile(
          completedRL.withPrefix("item/"),
          existingFileHelper
        )
      ).end()

    basicItem(GOGGLES.get())
    basicItem(GOGGLES_CURIO.get())
    basicItem(DAWN_CHARM.get())
    basicItem(APPRENTICE_CHESTPLATE.get())
    basicItem(APPRENTICE_LEGGINGS.get())
    basicItem(APPRENTICE_BOOTS.get())

    basicItem(THAVMITE_HELMET.get())
    basicItem(THAVMITE_CHESTPLATE.get())
    basicItem(THAVMITE_LEGGINGS.get())
    basicItem(THAVMITE_BOOTS.get())

    basicItem(THAVMITE_VANGUARD_HELMET.get())
    basicItem(THAVMITE_VANGUARD_CHESTPLATE.get())
    basicItem(THAVMITE_VANGUARD_LEGGINGS.get())
    basicItem(THAVMITE_VANGUARD_BOOTS.get())

    handheldItem(THAVMITE_SWORD)
    handheldItem(THAVMITE_AXE)
    handheldItem(THAVMITE_PICKAXE)
    handheldItem(THAVMITE_HAMMER)
    handheldItem(THAVMITE_SHOVEL)
    handheldItem(THAVMITE_HOE)

    for (shard in SHARDS.values) withVanillaParent(shard.id.path, "shard", "generated")

    for (wand in WANDS.values()) withExistingParent(wand.name, rl("wand"))

    withExistingParent(ANGRY_ZOMBIE_SPAWN_EGG.id.path, "template_spawn_egg")


    basicItem(T7Items.FOCUS_EMBERS)
    basicItem(T7Items.FOCUS_EXCAVATION)
    basicItem(T7Items.FOCUS_ENDERCHEST)
    basicItem(T7Items.FOCUS_LIGHT)
    basicItem(T7Items.FOCUS_HOLE)
    basicItem(T7Items.FOCUS_TELEPORT)
    basicItem(T7Items.FOCUS_EXCHANGE)
    basicItem(T7Items.FOCUS_LIGHTNING)
  }

  private fun basicItem(itemLike: ItemLike) = basicItem(itemLike.asItem())

  private fun handheldItem(deferredItem: DeferredItem<*>) {
    withVanillaParent(deferredItem, "handheld")
  }

  private fun withVanillaParent(deferredItem: DeferredItem<*>, parent: String) {
    val path = deferredItem.id.path
    withVanillaParent(path, parent)
  }

  private fun withVanillaParent(itemPath: String, parent: String) {
    withVanillaParent(itemPath, itemPath, parent)
  }

  private fun withVanillaParent(itemPath: String, texturePath: String, parent: String) {
    withExistingParent(itemPath, parent)
      .texture("layer0", rl(texturePath).withPrefix("item/"))
  }
}
