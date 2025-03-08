package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.common.aspect.Aspect
import me.alegian.thavma.impl.init.registries.deferred.util.DeferredAspect
import me.alegian.thavma.impl.init.registries.deferred.util.T7DeferredRegister
import java.util.function.Supplier

object Aspects {
  val REGISTRAR: T7DeferredRegister.Aspects = T7DeferredRegister.createAspects(Thavma.MODID)

  // PRIMAL
  val AER = register("aer", 0xffffff7e.toInt(), listOf())
  val TERRA = register("terra", 0xff56c000.toInt(), listOf())
  val IGNIS = register("ignis", 0xffff5a01.toInt(), listOf())
  val AQUA = register("aqua", 0xff3cd4fc.toInt(), listOf())
  val ORDO = register("ordo", 0xffd5d4ec.toInt(), listOf())
  val PERDITIO = register("perditio", 0xff404040.toInt(), listOf())

  // DO NOT use this to check if an aspect is primal. This array is used to auto-generate ores and other content. Instead, use Aspect::isPrimal
  val PRIMAL_ASPECTS = listOf(IGNIS, AER, TERRA, AQUA, ORDO, PERDITIO)

  // SECONDARY
  val VACUOS = register("vacuos", 0xff888888.toInt(), listOf(AER, PERDITIO))
  val LUX = register("lux", 0xffffffc0.toInt(), listOf(AER, IGNIS))
  val MOTUS = register("motus", 0xffcdccf4.toInt(), listOf(AER, ORDO))
  val GELUM = register("gelum", 0xffe1ffff.toInt(), listOf(IGNIS, PERDITIO))
  val VITREUS = register("vitreus", 0xff80ffff.toInt(), listOf(TERRA, AER))
  val METALLUM = register("metallum", 0xffb5b5cd.toInt(), listOf(TERRA, ORDO))
  val VICTUS = register("victus", 0xffde0005.toInt(), listOf(TERRA, AQUA))
  val MORTUUS = register("mortuus", 0xff6a0005.toInt(), listOf(AQUA, PERDITIO))
  val POTENTIA = register("potentia", 0xffc0ffff.toInt(), listOf(ORDO, IGNIS))
  val PERMUTATIO = register("permutatio", 0xff578357.toInt(), listOf(PERDITIO, ORDO))

  // TERTIARY
  val PRAECANTATIO = register("praecantatio", 0xffcf00ff.toInt(), listOf(POTENTIA, AER))
  val AURAM = register("auram", 0xffffc0ff.toInt(), listOf(PRAECANTATIO, AER))
  val ALKIMIA = register("alkimia", 0xff23ac9d.toInt(), listOf(PRAECANTATIO, AQUA))
  val VITIUM = register("vitium", 0xff800080.toInt(), listOf(PERDITIO, PRAECANTATIO))
  val TENEBRAE = register("tenebrae", 0xff222222.toInt(), listOf(VACUOS, LUX))
  val ALIENIS = register("alienis", 0xff805080.toInt(), listOf(VACUOS, TENEBRAE))
  val VOLATUS = register("volatus", 0xffe7e7d7.toInt(), listOf(AER, MOTUS))
  val HERBA = register("herba", 0xff01ac00.toInt(), listOf(VICTUS, TERRA))
  val INSTRUMENTUM = register("instrumentum", 0xff4040ee.toInt(), listOf(METALLUM, POTENTIA))
  val FABRICO = register("fabrico", 0xff809d80.toInt(), listOf(PERMUTATIO, INSTRUMENTUM))
  val MACHINA = register("machina", 0xff8080a0.toInt(), listOf(MOTUS, INSTRUMENTUM))
  val VINCULUM = register("vinculum", 0xff9a8080.toInt(), listOf(MOTUS, PERDITIO))
  val SPIRITUS = register("spiritus", 0xffebebfb.toInt(), listOf(VICTUS, MORTUUS))
  val COGNITIO = register("cognitio", 0xfff9967f.toInt(), listOf(IGNIS, SPIRITUS))
  val SENSUS = register("sensus", 0xffc0ffc0.toInt(), listOf(AER, SPIRITUS))
  val AVERSIO = register("aversio", 0xffc05050.toInt(), listOf(SPIRITUS, PERDITIO))
  val PRAEMUNIO = register("praemunio", 0xff00c0c0.toInt(), listOf(SPIRITUS, TERRA))
  val DESIDERIUM = register("desiderium", 0xffe6be44.toInt(), listOf(SPIRITUS, VACUOS))
  val EXANIMIS = register("exanimis", 0xff3a4000.toInt(), listOf(MOTUS, MORTUUS))
  val BESTIA = register("bestia", 0xff9f6409.toInt(), listOf(MOTUS, VICTUS))
  val HUMANUS = register("humanus", 0xffffd7c0.toInt(), listOf(SPIRITUS, VICTUS))

  private fun register(id: String, color: Int, components: List<Supplier<Aspect>>): DeferredAspect<Aspect> {
    return REGISTRAR.registerAspect(id) { Aspect(id, color, components) }
  }
}

fun <T> linkedMapWithPrimalKeys(mapper: (DeferredAspect<Aspect>) -> T): LinkedHashMap<DeferredAspect<Aspect>, T> {
  return linkedMapOf(*Aspects.PRIMAL_ASPECTS.map { Pair(it, mapper(it)) }.toTypedArray())
}

fun <T> listFromPrimals(mapper: (DeferredAspect<Aspect>) -> T): List<T> {
  return Aspects.PRIMAL_ASPECTS.map { mapper(it) }
}
