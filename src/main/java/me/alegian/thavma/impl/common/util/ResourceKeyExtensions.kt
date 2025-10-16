package me.alegian.thavma.impl.common.util

import net.minecraft.resources.ResourceKey

fun ResourceKey<*>.serialize() =
  registry().toString() + ":" + location().toString()