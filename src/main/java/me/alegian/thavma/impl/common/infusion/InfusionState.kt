package me.alegian.thavma.impl.common.infusion

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.common.block.entity.MatrixBE
import me.alegian.thavma.impl.common.codec.listOfNullable
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import java.util.Optional
import kotlin.collections.ArrayDeque
import kotlin.jvm.optionals.getOrNull

data class InfusionState(
  val remainingInputs: RemainingInputs = RemainingInputs(listOf(), AspectMap()),
  val flyingAspects: ArrayDeque<ArrivingAspectStack?> = ArrayDeque(),
  val prevSourcePos: BlockPos? = null,
  val prevPedestalPos: BlockPos? = null,
  val aspectDelay: Int = MatrixBE.MAX_ASPECT_DELAY,
  val itemDelay: Int = MatrixBE.MAX_ITEM_DELAY,
  val active: Boolean = false,
  val isOpen: Boolean = false,
  val result: ItemStack = ItemStack.EMPTY
) {
  companion object {
    val CODEC = RecordCodecBuilder.create<InfusionState> {
      it.group(
        RemainingInputs.CODEC.fieldOf("remainingInputs").forGetter { it.remainingInputs },
        FLYING_ASPECTS_CODEC.fieldOf("flyingAspects").forGetter { it.flyingAspects },
        BlockPos.CODEC.optionalFieldOf("prevSourcePos").forGetter { Optional.ofNullable(it.prevSourcePos) },
        BlockPos.CODEC.optionalFieldOf("prevPedestalPos").forGetter { Optional.ofNullable(it.prevPedestalPos) },
        Codec.INT.fieldOf("aspectDelay").forGetter { it.aspectDelay },
        Codec.INT.fieldOf("itemDelay").forGetter { it.itemDelay },
        Codec.BOOL.fieldOf("active").forGetter { it.active },
        Codec.BOOL.fieldOf("isOpen").forGetter { it.isOpen },
        ItemStack.OPTIONAL_CODEC.fieldOf("result").forGetter { it.result }
      ).apply(it) { r1, f, p1, p2, a1, i, a2, o, r2 ->
        InfusionState(r1, f, p1.getOrNull(), p2.getOrNull(), a1, i, a2, o, r2)
      }
    }
  }
}

private val FLYING_ASPECTS_CODEC = ArrivingAspectStack.CODEC.listOfNullable("arrivingAspectStack").xmap(
  ::ArrayDeque,
  { deque -> deque.toList() }
)
