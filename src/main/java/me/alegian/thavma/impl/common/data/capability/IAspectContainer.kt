package me.alegian.thavma.impl.common.data.capability

import me.alegian.thavma.impl.common.aspect.Aspect
import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.common.aspect.AspectMap.Companion.builder

/**
 * WARNING: this differs from the old Thaumcraft API
 * used by any Block, Item, Entity that contains Aspects
 * (not to be confused with the Aspect DataMap which
 * holds the scannable aspects)
 */
interface IAspectContainer {
  var aspects: AspectMap

  /**
   * Useful when spawning Aura Nodes. Null means "not yet generated"
   */
  fun areAspectsNull(): Boolean

  /**
   * Returns: the amount that was inserted (or would have been, if simulated)
   */
  fun insert(aspect: Aspect, amount: Int, simulate: Boolean): Int

  /**
   * Returns: an AspectMap representing the aspects inserted (or those that would have been inserted, if simulated)
   */
  fun insert(toInsert: AspectMap): AspectMap {
    if (toInsert == null) return AspectMap()

    val insertedBuilder = builder()

    for ((aspect, amount) in toInsert) {
      if (amount == 0) continue
      val simulatedAmount = insert(aspect, amount, true)
      if (simulatedAmount == 0) continue
      insertedBuilder.add(aspect, simulatedAmount)
    }

    val insertedAspects = insertedBuilder.build()
    aspects = aspects.add(insertedAspects)

    return insertedAspects
  }

  /**
   * Returns: the amount that was extracted (or would have been, if simulated)
   */
  fun extract(aspect: Aspect, amount: Int, simulate: Boolean): Int

  /**
   * Returns: an AspectMap representing the aspects extracted (or those that would have been extracted, if simulated)
   */
  fun extract(toExtract: AspectMap): AspectMap {
    val extractedBuilder = builder()

    for ((aspect, amount) in toExtract) {
      if (amount == 0) continue
      val simulatedAmount = extract(aspect, amount, true)
      if (simulatedAmount == 0) continue
      extractedBuilder.add(aspect, simulatedAmount)
    }

    val extractedAspects = extractedBuilder.build()
    aspects = aspects.subtract(extractedAspects)

    return extractedAspects
  }

  /**
   * Returns: the maximum amount that can be held, per Aspect
   */
  val capacity: Int

  val isVisSource: Boolean

  val isEssentiaSource: Boolean
}
