package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.common.feature.NodeFeature
import net.minecraft.core.registries.BuiltInRegistries
import net.neoforged.neoforge.registries.DeferredRegister

object T7Features {
  val REGISTRAR = DeferredRegister.create(BuiltInRegistries.FEATURE, Thavma.MODID)

  val NODE = REGISTRAR.register("node", ::NodeFeature)
}