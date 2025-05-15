package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.init.registries.T7ItemProperties
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ANGRY_ZOMBIE_SPAWN_EGG
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANUM_AXE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANUM_BOOTS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANUM_CHESTPLATE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANUM_HAMMER
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANUM_HANDLE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANUM_HELMET
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANUM_HOE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANUM_INGOT
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANUM_LEGGINGS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANUM_NUGGET
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANUM_PICKAXE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANUM_SHOVEL
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANUM_SWORD
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
import me.alegian.thavma.impl.init.registries.deferred.T7Items.TESTAS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.WANDS
import me.alegian.thavma.impl.rl
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredItem

class T7ItemModelProvider(output: PackOutput, existingFileHelper: ExistingFileHelper) : ItemModelProvider(output, Thavma.MODID, existingFileHelper) {
  override fun registerModels() {
    basicItem(IRON_HANDLE.get())
    basicItem(GOLD_HANDLE.get())
    basicItem(ORICHALCUM_HANDLE.get())
    basicItem(ARCANUM_HANDLE.get())

    basicItem(EYE_OF_WARDEN.get())
    basicItem(ROTTEN_BRAIN.get())
    basicItem(SIGIL.get())

    basicItem(GREATWOOD_CORE.get())
    basicItem(SILVERWOOD_CORE.get())

    basicItem(RUNE.get())
    basicItem(ARCANUM_INGOT.get())
    basicItem(ARCANUM_NUGGET.get())
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
    basicItem(RESEARCHER_CHESTPLATE.get())
    basicItem(RESEARCHER_LEGGINGS.get())
    basicItem(RESEARCHER_BOOTS.get())

    basicItem(ARCANUM_HELMET.get())
    basicItem(ARCANUM_CHESTPLATE.get())
    basicItem(ARCANUM_LEGGINGS.get())
    basicItem(ARCANUM_BOOTS.get())

    basicItem(CUSTOS_ARCANUM_HELMET.get())
    basicItem(CUSTOS_ARCANUM_CHESTPLATE.get())
    basicItem(CUSTOS_ARCANUM_LEGGINGS.get())
    basicItem(CUSTOS_ARCANUM_BOOTS.get())

    handheldItem(ARCANUM_SWORD)
    handheldItem(ARCANUM_AXE)
    handheldItem(ARCANUM_PICKAXE)
    handheldItem(ARCANUM_HAMMER)
    handheldItem(ARCANUM_SHOVEL)
    handheldItem(ARCANUM_HOE)

    for (testa in TESTAS.values) withVanillaParent(testa.id.path, "testa", "generated")

    for (wand in WANDS.values()) withExistingParent(wand.name, rl("wand"))

    withExistingParent(ANGRY_ZOMBIE_SPAWN_EGG.id.path, "template_spawn_egg")
  }

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
