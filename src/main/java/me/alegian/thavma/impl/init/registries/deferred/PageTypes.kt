package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.common.book.CraftingPage
import me.alegian.thavma.impl.common.book.PageType
import me.alegian.thavma.impl.common.book.TextPage
import me.alegian.thavma.impl.init.registries.T7Registries
import me.alegian.thavma.impl.rl
import net.neoforged.neoforge.registries.DeferredRegister

object PageTypes {
  val REGISTRAR = DeferredRegister.create(T7Registries.PAGE_TYPE.key(), Thavma.MODID)

  val TEXT = REGISTRAR.register("text") { -> PageType<TextPage>(rl("text"), TextPage.CODEC) }
  val CRAFTING = REGISTRAR.register("crafting") { -> PageType<CraftingPage>(rl("crafting"), CraftingPage.CODEC) }
}