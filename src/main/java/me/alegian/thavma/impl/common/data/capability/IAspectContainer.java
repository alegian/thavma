package me.alegian.thavma.impl.common.data.capability;

import me.alegian.thavma.impl.common.aspect.Aspect;
import me.alegian.thavma.impl.common.aspect.AspectMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * WARNING: this differs from the old Thaumcraft API
 * used by any Block, Item, Entity that contains Aspects
 * (not to be confused with the Aspect DataMap which
 * holds the scannable aspects)
 */
public interface IAspectContainer {
  @Nonnull
  AspectMap getAspects();

  void setAspects(AspectMap aspects);

  /**
   * Useful when spawning Aura Nodes. Null means "not yet generated"
   */
  boolean areAspectsNull();

  /**
   * Returns: the amount that was inserted (or would have been, if simulated)
   */
  int insert(Aspect aspect, int amount, boolean simulate);

  /**
   * Returns: an AspectMap representing the aspects inserted (or those that would have been inserted, if simulated)
   */
  default @Nonnull AspectMap insert(@Nullable AspectMap toInsert) {
    if (toInsert == null) return new AspectMap();

    var insertedBuilder = AspectMap.Companion.builder();

    for (var stack : toInsert) {
      if (stack.getAmount() == 0) continue;
      var simulatedAmount = this.insert(stack.getAspect(), stack.getAmount(), true);
      if (simulatedAmount == 0) continue;
      insertedBuilder.add(stack.getAspect(), simulatedAmount);
    }

    var insertedAspects = insertedBuilder.build();
    this.setAspects(this.getAspects().add(insertedAspects));

    return insertedAspects;
  }

  /**
   * Returns: the amount that was extracted (or would have been, if simulated)
   */
  int extract(Aspect aspect, int amount, boolean simulate);

  /**
   * Returns: an AspectMap representing the aspects extracted (or those that would have been extracted, if simulated)
   */
  default @Nonnull AspectMap extract(@Nullable AspectMap toExtract) {
    if (toExtract == null) return new AspectMap();

    var extractedBuilder = AspectMap.Companion.builder();

    for (var stack : toExtract) {
      if (stack.getAmount() == 0) continue;
      var simulatedAmount = this.extract(stack.getAspect(), stack.getAmount(), true);
      if (simulatedAmount == 0) continue;
      extractedBuilder.add(stack.getAspect(), simulatedAmount);
    }

    var extractedAspects = extractedBuilder.build();
    this.setAspects(this.getAspects().subtract(extractedAspects));

    return extractedAspects;
  }

  /**
   * Returns: the maximum amount that can be held, per Aspect
   */
  int getCapacity();

  boolean isVisSource();

  boolean isEssentiaSource();
}
