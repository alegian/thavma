package me.alegian.thavma.impl.common.payload

import me.alegian.thavma.impl.common.item.WandItem
import me.alegian.thavma.impl.rl
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.network.handling.IPayloadContext

// C2S
class FocusPayload(val itemStack: ItemStack) : CustomPacketPayload {
  override fun type() = TYPE

  companion object {
    val TYPE = CustomPacketPayload.Type<FocusPayload>(rl("focus"))

    val STREAM_CODEC = StreamCodec.composite(
      ItemStack.STREAM_CODEC, FocusPayload::itemStack,
      ::FocusPayload
    )

    fun handle(payload: FocusPayload, context: IPayloadContext) {
      val player = context.player()
      val wand = player.mainHandItem
      if (wand.item !is WandItem) return

      player.inventory.run {
        for (i in 0..<containerSize) {
          val stack = getItem(i)
          if (ItemStack.isSameItemSameComponents(stack, payload.itemStack)) {
            removeItem(i, 1)
            return
          }
        }
      }
    }
  }
}