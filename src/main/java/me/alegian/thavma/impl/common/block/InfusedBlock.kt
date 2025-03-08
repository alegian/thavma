package me.alegian.thavma.impl.common.block

import me.alegian.thavma.impl.common.aspect.Aspect
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.DropExperienceBlock
import java.util.function.Supplier

class InfusedBlock(private val aspect: Supplier<Aspect>, properties: Properties, private val baseBlock: Supplier<Block>) : DropExperienceBlock(UniformInt.of(2, 5), properties) {
  fun getAspect(): Aspect {
    return aspect.get()
  }

  fun getBaseBlock(): Block {
    return baseBlock.get()
  }
}
