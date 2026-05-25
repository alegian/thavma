package me.alegian.thavma.config

import net.neoforged.neoforge.common.ModConfigSpec

object Config {
  private val BUILDER = ModConfigSpec.Builder()

  val SPEC: ModConfigSpec
  var FONT_SIZE_MULTIPLIER: ModConfigSpec.DoubleValue


  init {
    BUILDER.push("general_settings")

    FONT_SIZE_MULTIPLIER = BUILDER
      .comment("A number to multiply font size with")
      .defineInRange("fontSizeMultiplier", 1.0, 0.1, 2.0)

    BUILDER.pop()
    SPEC = BUILDER.build()
  }
}