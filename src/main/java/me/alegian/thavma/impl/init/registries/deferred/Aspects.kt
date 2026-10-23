package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.common.aspect.Aspect
import me.alegian.thavma.impl.init.registries.deferred.util.DeferredAspect
import me.alegian.thavma.impl.init.registries.deferred.util.T7DeferredRegister

object Aspects {
  val REGISTRAR: T7DeferredRegister.Aspects = T7DeferredRegister.createAspects(Thavma.MODID)

  // PRIMAL
  val TERRA = register("terra", 0xff56c000.toInt(), false)
  val IGNIS = register("ignis", 0xffff5a01.toInt(), false)
  val AQUA = register("aqua", 0xff3cd4fc.toInt(), false)
  val AER = register("aer", 0xffffff7e.toInt(), false)
  val ORDO = register("ordo", 0xffd5d4ec.toInt(), true)
  val PERDITIO = register("perditio", 0xff404040.toInt(), true)

  // This array is used to datagen ores and other content. Use Aspect::isPrimal to check primality.
  val DATAGEN_PRIMALS = listOf(IGNIS, TERRA, AQUA, AER, ORDO, PERDITIO)

  // COMPOUND (components must be registered first)
  val PERMUTATIO = register("permutatio", 0xff578357.toInt(), false, ORDO, PERDITIO)
  val VACUOS = register("vacuos", 0xff888888.toInt(), false, PERDITIO, AER)
  val ALIENIS = register("alienis", 0xff805080.toInt(), false, PERMUTATIO, VACUOS)
  val PULVIS = register("pulvis", 0xff6f3318.toInt(), false, PERDITIO, TERRA)
  val VICTUS = register("victus", 0xffde0005.toInt(), true, AQUA, TERRA)
  val HERBA = register("herba", 0xff01ac00.toInt(), false, PULVIS, VICTUS)
  val MOTUS = register("motus", 0xff00d65b.toInt(), false, TERRA, ORDO)
  val CRESCENTIA = register("crescentia", 0xff9f6409.toInt(), false, VICTUS, MOTUS)
  val ARBOR = register("arbor", 0xff876531.toInt(), false, HERBA, CRESCENTIA)
  val CORPUS = register("corpus", 0xffee478d.toInt(), false, PERMUTATIO, VICTUS)
  val LUX = register("lux", 0xfffff663.toInt(), false, IGNIS, AER)
  val SPIRITUS = register("spiritus", 0xffebebfb.toInt(), true, LUX, PERMUTATIO)
  val HUMANUS = register("humanus", 0xffffd7c0.toInt(), true, CORPUS, SPIRITUS)
  val VENENUM = register("venenum", 0xff89f000.toInt(), false, AQUA, PERDITIO)
  val MORTUUS = register("mortuus", 0xff887788.toInt(), true, CORPUS, VENENUM)
  val EXANIMIS = register("exanimis", 0xff3a4000.toInt(), false, HUMANUS, MORTUUS)
  val FAMES = register("fames", 0xff9a0305.toInt(), false, VACUOS, CORPUS)
  val IMPETUS = register("impetus", 0xffe67d42.toInt(), false, IGNIS, PERDITIO)
  val IDEA = register("idea", 0xfffb8dff.toInt(), true, SPIRITUS, MOTUS)
  val METALLUM = register("metallum", 0xffb5b5cd.toInt(), false, TERRA, IGNIS)
  val INSTRUMENTUM = register("instrumentum", 0xff4040ee.toInt(), false, HUMANUS, METALLUM)
  val TEMPUS = register("tempus", 0xffcc668e.toInt(), true, MOTUS, PERMUTATIO)
  val FABRICO = register("fabrico", 0xff809d80.toInt(), false, HUMANUS, IDEA)
  val MACHINA = register("machina", 0xff5373d2.toInt(), false, TEMPUS, FABRICO)
  val MESSIS = register("messis", 0xffe1b371.toInt(), false, HUMANUS, HERBA)
  val POTENTIA = register("potentia", 0xffc0ffff.toInt(), false, IGNIS, ORDO)
  val PRAECANTATIO = register("praecantatio", 0xff9700c0.toInt(), false, SPIRITUS, POTENTIA)
  val LUCRUM = register("lucrum", 0xffe6be44.toInt(), false, FAMES, HUMANUS)
  val ROSAURA = register("rosaura", 0xffb468c0.toInt(), false, ALIENIS, LUCRUM)
  val SONORUS = register("sonorus", 0xff22c5b2.toInt(), false, ORDO, AER)
  val SENSUS = register("sensus", 0xff0fd9ff.toInt(), false, LUX, SONORUS)
  val TENEBRAE = register("tenebrae", 0xff222222.toInt(), false, LUX, VACUOS)
  val SOMNIUM = register("somnium", 0xff8c6dcc.toInt(), false, SENSUS, TENEBRAE)
  val TEXTURA = register("textura", 0xffe3d06d.toInt(), false, CORPUS, INSTRUMENTUM)
  val ORBIS = register("orbis", 0xff43c5f1.toInt(), true, AER, AQUA)
  val TUTAMEN = register("tutamen", 0xff00c0c0.toInt(), false, HUMANUS, ORBIS)
  val VITREUS = register("vitreus", 0xff80ffff.toInt(), false, ORDO, AQUA)
  val VITIUM = register("vitium", 0xff800080.toInt(), false, HUMANUS, PRAECANTATIO)
  val DENSITAS = register("densitas", 0xffcacaca.toInt(), false, VITREUS, METALLUM)
  val ACULEUS = register("aculeus", 0xffc7bb1e.toInt(), false, IMPETUS, ORBIS)
  val MEMORIA = register("memoria", 0xffff4747.toInt(), false, SPIRITUS, TUTAMEN)
  val COGNITIO = register("cognitio", 0xffffc2b3.toInt(), false, HUMANUS, SENSUS)

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
