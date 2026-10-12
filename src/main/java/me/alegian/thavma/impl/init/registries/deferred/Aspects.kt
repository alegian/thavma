package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.client.T7Colors
import me.alegian.thavma.impl.common.aspect.Aspect
import me.alegian.thavma.impl.init.registries.deferred.util.DeferredAspect
import me.alegian.thavma.impl.init.registries.deferred.util.T7DeferredRegister

object Aspects {
  val REGISTRAR: T7DeferredRegister.Aspects = T7DeferredRegister.createAspects(Thavma.MODID)

  // PRIMAL
  val TERRA = register("terra", 0xff56c000.toInt())
  val IGNIS = register("ignis", 0xffff5a01.toInt())
  val AQUA = register("aqua", 0xff3cd4fc.toInt())
  val AETHER = register("aether", T7Colors.PURPLE)

  // This array is used to datagen ores and other content. Use Aspect::isPrimal to check primality.
  val DATAGEN_PRIMALS = listOf(IGNIS, TERRA, AQUA, AETHER)

  // SECONDARY
  val LUX = register("lux", 0xffffffc0.toInt(), IGNIS)
  val VITREUS = register("vitreus", 0xff80ffff.toInt(), TERRA)
  val METALLUM = register("metallum", 0xffb5b5cd.toInt(), TERRA, IGNIS)
  val VICTUS = register("victus", 0xffde0005.toInt(), TERRA, AQUA)
  val HERBA = register("herba", 0xff01ac00.toInt(), TERRA, AQUA)

  // TERTIARY
  val ALKIMIA = register("alkimia", 0xff23ac9d.toInt(), AQUA, AETHER)
  val TENEBRAE = register("tenebrae", 0xff222222.toInt(), AETHER)
  val INSTRUMENTUM = register("instrumentum", 0xff4040ee.toInt(), VITREUS, METALLUM)
  val FABRICO = register("fabrico", 0xff809d80.toInt(), INSTRUMENTUM)
  val MACHINA = register("machina", 0xff8080a0.toInt(), METALLUM, INSTRUMENTUM)
  val VAS = register("vas", 0xff9a8080.toInt(), FABRICO)
  val COGNITIO = register("cognitio", 0xfff9967f.toInt(), AETHER, FABRICO)
  val ORNATUS = register("ornatus", 0xffc0ffc0.toInt(), VITREUS, FABRICO)
  val HOSTILIS = register("hostilis", 0xffc05050.toInt(), VICTUS, TENEBRAE)
  val CORPUS = register("corpus", 0xff9f6409.toInt(), VICTUS)
  val PRAEMUNIO = register("praemunio", 0xff00c0c0.toInt(), VITREUS, METALLUM, FABRICO)
  val CIVILIS = register("civilis", 0xffffd7c0.toInt(), VICTUS, COGNITIO, ORNATUS)

  private fun register(id: String, color: Int, vararg components: DeferredAspect<Aspect>): DeferredAspect<Aspect> {
    return REGISTRAR.registerAspect(id) {
      Aspect(id, color, if (components.isEmpty()) null else components.map { it.get() }.toSet())
    }
  }
}

fun <T> linkedMapWithPrimalKeys(mapper: (DeferredAspect<Aspect>) -> T): LinkedHashMap<DeferredAspect<Aspect>, T> {
  return linkedMapOf(*Aspects.DATAGEN_PRIMALS.map { Pair(it, mapper(it)) }.toTypedArray())
}

fun <T> listFromPrimals(mapper: (DeferredAspect<Aspect>) -> T): List<T> {
  return Aspects.DATAGEN_PRIMALS.map { mapper(it) }
}
