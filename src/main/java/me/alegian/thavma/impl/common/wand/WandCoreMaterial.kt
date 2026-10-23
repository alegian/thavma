package me.alegian.thavma.impl.common.wand

class WandCoreMaterial(val registerCombinations: Boolean, val capacity: Int) {
  constructor(capacity: Int) : this(true, capacity)
}
