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
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.client.model.generators.ModelFile.UncheckedModelFile
import net.neoforged.neoforge.common.data.ExistingFileHelper

class T7ItemModelProvider(output: PackOutput, existingFileHelper: ExistingFileHelper) : ItemModelProvider(output, Thavma.MODID, existingFileHelper) {
  override fun registerModels() {
    basicItem(IRON_PLATING)
    basicItem(GOLD_PLATING)
    basicItem(ORICHALCUM_PLATING)
    basicItem(THAVMITE_PLATING)

    basicItem(EYE_OF_WARDEN)
    basicItem(ROTTEN_BRAIN)
    basicItem(FABRIC)

    basicItem(GREATWOOD_CORE)
    basicItem(SILVERWOOD_CORE)

    basicItem(RUNE)
    basicItem(THAVMITE_INGOT)
    basicItem(THAVMITE_NUGGET)
    basicItem(ORICHALCUM_INGOT)
    basicItem(ORICHALCUM_NUGGET)

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

    basicItem(GOGGLES)
    basicItem(GOGGLES_CURIO)
    basicItem(DAWN_CHARM)
    basicItem(APPRENTICE_CHESTPLATE)
    basicItem(APPRENTICE_LEGGINGS)
    basicItem(APPRENTICE_BOOTS)

    basicItem(THAVMITE_HELMET)
    basicItem(THAVMITE_CHESTPLATE)
    basicItem(THAVMITE_LEGGINGS)
    basicItem(THAVMITE_BOOTS)

    basicItem(THAVMITE_VANGUARD_HELMET)
    basicItem(THAVMITE_VANGUARD_CHESTPLATE)
    basicItem(THAVMITE_VANGUARD_LEGGINGS)
    basicItem(THAVMITE_VANGUARD_BOOTS)

    handheldItem(THAVMITE_SWORD)
    handheldItem(THAVMITE_AXE)
    handheldItem(THAVMITE_PICKAXE)
    handheldItem(THAVMITE_HAMMER)
    handheldItem(THAVMITE_SHOVEL)
    handheldItem(THAVMITE_HOE)

    for (shard in SHARDS.values) item(shard, rl("item/shard"))

    for (wand in WANDS.values()) withExistingParent(wand.name, rl("wand"))

    withExistingParent(ANGRY_ZOMBIE_SPAWN_EGG.id.path, "template_spawn_egg")

    focus(T7Items.FOCUS_EMBERS)
    focus(T7Items.FOCUS_EXCAVATION)
    focus(T7Items.FOCUS_ENDERCHEST)
    focus(T7Items.FOCUS_LIGHT)
    focus(T7Items.FOCUS_HOLE)
    focus(T7Items.FOCUS_TELEPORT)
    focus(T7Items.FOCUS_EXCHANGE)
    focus(T7Items.FOCUS_LIGHTNING)
  }

  private fun basicItem(itemLike: ItemLike) = basicItem(itemLike.asItem())
  private fun handheldItem(itemLike: ItemLike) = handheldItem(itemLike.asItem())

  fun item(itemLike: ItemLike, textureRL: ResourceLocation): ItemModelBuilder {
    return getBuilder(BuiltInRegistries.ITEM.getKey(itemLike.asItem()).toString())
      .parent(UncheckedModelFile("item/generated"))
      .texture("layer0", textureRL)
  }

  fun focus(itemLike: ItemLike) =
    BuiltInRegistries.ITEM.getKey(itemLike.asItem()).also {
      withExistingParent(it.toString(), rl("focus")).texture("0", it.withPrefix("item/")).texture("particle", it.withPrefix("item/"))
    }
}
