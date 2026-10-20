package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.client.T7Colors
import me.alegian.thavma.impl.common.aspect.Aspect
import me.alegian.thavma.impl.init.registries.deferred.util.DeferredAspect
import me.alegian.thavma.impl.init.registries.deferred.util.T7DeferredRegister

object Aspects {
  val REGISTRAR: T7DeferredRegister.Aspects = T7DeferredRegister.createAspects(Thavma.MODID)

  // PRIMAL
  val TERRA = register("terra", 0xff56c000.toInt(), false)
  val IGNIS = register("ignis", 0xffff5a01.toInt(), false)
  val AQUA = register("aqua", 0xff3cd4fc.toInt(), false)
  val AETHER = register("aether", T7Colors.PURPLE, false)

  // This array is used to datagen ores and other content. Use Aspect::isPrimal to check primality.
  val DATAGEN_PRIMALS = listOf(IGNIS, TERRA, AQUA, AETHER)

  // SECONDARY
  val LUX = register("lux", 0xffffffc0.toInt(), false, IGNIS)
  val VITREUS = register("vitreus", 0xff80ffff.toInt(), false, TERRA)
  val METALLUM = register("metallum", 0xffb5b5cd.toInt(), false, TERRA, IGNIS)
  val VICTUS = register("victus", 0xffde0005.toInt(), false, TERRA, AQUA)
  val HERBA = register("herba", 0xff01ac00.toInt(), false, TERRA, AQUA)

  // TERTIARY
  val ALKIMIA = register("alkimia", 0xff23ac9d.toInt(), false, AQUA, AETHER)
  val TENEBRAE = register("tenebrae", 0xff222222.toInt(), false, AETHER)
  val INSTRUMENTUM = register("instrumentum", 0xff4040ee.toInt(), false, VITREUS, METALLUM)
  val FABRICO = register("fabrico", 0xff809d80.toInt(), false, INSTRUMENTUM)
  val MACHINA = register("machina", 0xff8080a0.toInt(), false, METALLUM, INSTRUMENTUM)
  val VAS = register("vas", 0xff9a8080.toInt(), false, FABRICO)
  val COGNITIO = register("cognitio", 0xfff9967f.toInt(), false, AETHER, FABRICO)
  val ORNATUS = register("ornatus", 0xffc0ffc0.toInt(), false, VITREUS, FABRICO)
  val HOSTILIS = register("hostilis", 0xffc05050.toInt(), false, VICTUS, TENEBRAE)
  val CORPUS = register("corpus", 0xff9f6409.toInt(), false, VICTUS)
  val PRAEMUNIO = register("praemunio", 0xff00c0c0.toInt(), false, VITREUS, METALLUM, FABRICO)
  val CIVILIS = register("civilis", 0xffffd7c0.toInt(), false, VICTUS, COGNITIO, ORNATUS)

  private fun register(id: String, color: Int, latent: Boolean, vararg components: DeferredAspect<Aspect>): DeferredAspect<Aspect> {
    return REGISTRAR.registerAspect(id) {
      Aspect(id, color, latent, if (components.isEmpty()) null else components.map { it.get() }.toSet())
    }
  }
}

fun <T> linkedMapWithPrimalKeys(mapper: (DeferredAspect<Aspect>) -> T): LinkedHashMap<DeferredAspect<Aspect>, T> {
  return linkedMapOf(*Aspects.DATAGEN_PRIMALS.map { Pair(it, mapper(it)) }.toTypedArray())
}

fun <T> listFromPrimals(mapper: (DeferredAspect<Aspect>) -> T): List<T> {
  return Aspects.DATAGEN_PRIMALS.map { mapper(it) }
}
