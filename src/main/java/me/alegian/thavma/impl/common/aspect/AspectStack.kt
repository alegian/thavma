package me.alegian.thavma.impl.common.aspect

/**
 * Used when converting AspectMaps to lists.
 * The element of these lists is the AspectStack.
 */
data class AspectStack(val aspect: Aspect, val amount: Int) {
  companion object {
    fun of(aspect: Aspect, amount: Int): AspectStack {
      return AspectStack(aspect, amount)
    }
  }
}
