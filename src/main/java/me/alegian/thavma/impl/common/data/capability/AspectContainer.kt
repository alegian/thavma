package me.alegian.thavma.impl.common.data.capability

import me.alegian.thavma.impl.common.aspect.Aspect
import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.common.aspect.AspectStack
import me.alegian.thavma.impl.common.aspect.AspectStack.Companion.of
import me.alegian.thavma.impl.init.registries.T7Capabilities.AspectContainer.BLOCK
import me.alegian.thavma.impl.init.registries.T7Capabilities.AspectContainer.ITEM
import me.alegian.thavma.impl.init.registries.deferred.Aspects.PRIMAL_ASPECTS
import me.alegian.thavma.impl.init.registries.deferred.T7DataComponents.ASPECTS
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.common.MutableDataComponentHolder
import kotlin.math.min

open class AspectContainer(
  private val holder: MutableDataComponentHolder,
  override val capacity: Int = Int.MAX_VALUE,
  val slots: Int = Int.MAX_VALUE
) : IAspectContainer {
  override var aspects = holder.get(ASPECTS) ?: AspectMap()
    set(a) {
      holder.set(ASPECTS, a)
    }

  override fun areAspectsNull(): Boolean {
    return holder.get(ASPECTS) == null
  }

  override fun insert(aspect: Aspect, amount: Int, simulate: Boolean): Int {
    if (amount == 0) return 0
    if (aspects.size >= slots && !aspects.has(aspect)) return 0

    val current = this.aspects
    val maxInsert = this.capacity - current[aspect]
    val cappedInsert = min(amount.toDouble(), maxInsert.toDouble()).toInt()

    if (!simulate) this.aspects = current.add(aspect, cappedInsert)

    return cappedInsert
  }

  override fun extract(aspect: Aspect, amount: Int, simulate: Boolean): Int {
    if (amount == 0) return 0
    val maxSubtract = this.aspects[aspect]
    val cappedSubtract = min(amount.toDouble(), maxSubtract.toDouble()).toInt()

    if (!simulate) this.aspects = this.aspects.subtract(aspect, amount)

    return cappedSubtract
  }

  open class Pair(private val source: IAspectContainer, private val sink: IAspectContainer) {
    private fun simulateTransfer(a: Aspect, idealAmount: Int): Int {
      val maxInsert = sink.insert(a, idealAmount, true)
      return source.extract(a, maxInsert, true)
    }

    open fun canTransferPrimals(): Boolean {
      return PRIMAL_ASPECTS.stream()
        .anyMatch { a -> simulateTransfer(a.get(), 1) > 0 }
    }

    open fun transferAnyAspect(): AspectStack? {
      for (a in source.aspects.map { it.aspect }) {
        val amount = simulateTransfer(a, source.aspects[a])
        if (amount == 0) continue
        sink.insert(a, amount, false)
        source.extract(a, amount, false)
        return of(a, amount)
      }
      return null
    }

    open fun transferAllAspects() {
      for (a in source.aspects.map { it.aspect }) {
        val amount = simulateTransfer(a, source.aspects[a])
        if (amount == 0) continue
        sink.insert(a, amount, false)
        source.extract(a, amount, false)
      }
    }

    open fun transferPrimal(indexOffset: Int, idealAmount: Int): AspectStack? {
      val primals = PRIMAL_ASPECTS.size
      for (i in 0..<primals) {
        val a = PRIMAL_ASPECTS[(i + indexOffset) % primals].get()
        val amount = simulateTransfer(a, idealAmount)
        if (amount == 0) continue
        sink.insert(a, amount, false)
        source.extract(a, amount, false)
        return of(a, amount)
      }
      return null
    }
  }

  companion object {
    fun at(level: Level?, pos: BlockPos): IAspectContainer? {
      return level?.getCapability(BLOCK, pos)
    }

    fun from(itemStack: ItemStack): IAspectContainer? {
      return itemStack.getCapability(ITEM)
    }

    fun from(be: BlockEntity): IAspectContainer? {
      return be.level?.getCapability(BLOCK, be.blockPos, null, be)
    }

    fun isAspectContainer(level: Level, blockPos: BlockPos): Boolean {
      return level.getCapability(BLOCK, blockPos) != null
    }

    fun blockSourceItemSink(level: Level, blockPos: BlockPos, itemStack: ItemStack): Pair? {
      val sink = from(itemStack)
      val source = at(level, blockPos)
      if (sink == null || source == null) return null
      return Pair(source, sink)
    }

    fun itemSourceBlockSink(level: Level, blockPos: BlockPos, itemStack: ItemStack): Pair? {
      val sink = at(level, blockPos)
      val source = from(itemStack)
      if (sink == null || source == null) return null
      return Pair(source, sink)
    }
  }
}
