package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.client.T7Colors
import me.alegian.thavma.impl.common.aspect.Aspect
import me.alegian.thavma.impl.init.registries.deferred.util.DeferredAspect
import me.alegian.thavma.impl.init.registries.deferred.util.T7DeferredRegister

object Aspects {
  val REGISTRAR: T7DeferredRegister.Aspects = T7DeferredRegister.createAspects(Thavma.MODID)

  // PRIMAL
  val TERRA = register("terra", 0xff56c000.toInt(), true)
  val IGNIS = register("ignis", 0xffff5a01.toInt(), true)
  val AQUA = register("aqua", 0xff3cd4fc.toInt(), true)
  val AETHER = register("aether", T7Colors.PURPLE, true)

  // DO NOT use this to check if an aspect is primal. This array is used to datagen ores and other content. Instead, use Aspect::isPrimal
  val DATAGEN_PRIMALS = listOf(IGNIS, TERRA, AQUA, AETHER)

  // SECONDARY
  val LUX = register("lux", 0xffffffc0.toInt())
  val VITREUS = register("vitreus", 0xff80ffff.toInt())
  val METALLUM = register("metallum", 0xffb5b5cd.toInt())
  val VICTUS = register("victus", 0xffde0005.toInt())
  val HERBA = register("herba", 0xff01ac00.toInt())

  // TERTIARY
  val ALKIMIA = register("alkimia", 0xff23ac9d.toInt())
  val TENEBRAE = register("tenebrae", 0xff222222.toInt())
  val INSTRUMENTUM = register("instrumentum", 0xff4040ee.toInt())
  val FABRICO = register("fabrico", 0xff809d80.toInt())
  val MACHINA = register("machina", 0xff8080a0.toInt())
  val VAS = register("vas", 0xff9a8080.toInt())
  val COGNITIO = register("cognitio", 0xfff9967f.toInt())
  val ORNATUS = register("ornatus", 0xffc0ffc0.toInt())
  val HOSTILIS = register("hostilis", 0xffc05050.toInt())
  val CORPUS = register("corpus", 0xff9f6409.toInt())
  val PRAEMUNIO = register("praemunio", 0xff00c0c0.toInt())
  val CIVILIS = register("civilis", 0xffffd7c0.toInt())

  private fun register(id: String, color: Int, isPrimal: Boolean = false): DeferredAspect<Aspect> {
    return REGISTRAR.registerAspect(id) { Aspect(id, color, isPrimal) }
  }
}

fun <T> linkedMapWithPrimalKeys(mapper: (DeferredAspect<Aspect>) -> T): LinkedHashMap<DeferredAspect<Aspect>, T> {
  return linkedMapOf(*Aspects.DATAGEN_PRIMALS.map { Pair(it, mapper(it)) }.toTypedArray())
}

fun <T> listFromPrimals(mapper: (DeferredAspect<Aspect>) -> T): List<T> {
  return Aspects.DATAGEN_PRIMALS.map { mapper(it) }
}
