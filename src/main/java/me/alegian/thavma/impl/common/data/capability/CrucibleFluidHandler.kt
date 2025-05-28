package me.alegian.thavma.impl.common.data.capability

import me.alegian.thavma.impl.common.block.entity.CrucibleBE
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction
import net.neoforged.neoforge.fluids.capability.templates.FluidTank
import kotlin.math.min

class CrucibleFluidHandler(private val crucibleBE: CrucibleBE) : FluidTank(FluidType.BUCKET_VOLUME, { fluidStack -> fluidStack.`is`(Fluids.WATER) }) {
  override fun drain(maxDrain: Int, action: FluidAction) = FluidStack.EMPTY

  override fun onContentsChanged() {
    crucibleBE.setChanged()
  }

  // returns true if any water was drained
  fun catalystDrain(): Boolean {
    if (isEmpty) return false

    val maxDrain = FluidType.BUCKET_VOLUME / 4
    fluid.shrink(min(maxDrain, fluid.amount))
    onContentsChanged()
    return true
  }

  // returns true if any water was filled
  fun fillUp(): Boolean {
    if (space == 0) return false

    fluid = FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME)
    onContentsChanged()
    return true
  }
}
