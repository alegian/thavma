package me.alegian.thavma.impl.common.payload

import me.alegian.thavma.impl.common.codec.nullable
import me.alegian.thavma.impl.common.item.WandItem
import me.alegian.thavma.impl.init.registries.T7Tags
import me.alegian.thavma.impl.init.registries.deferred.T7DataComponents
import me.alegian.thavma.impl.rl
import net.minecraft.core.NonNullList
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemContainerContents
import net.neoforged.neoforge.items.ItemHandlerHelper.giveItemToPlayer
import net.neoforged.neoforge.network.handling.IPayloadContext

// C2S
class FocusPayload(val itemStack: ItemStack?) : CustomPacketPayload {
  override fun type() = TYPE

  companion object {
    val TYPE = CustomPacketPayload.Type<FocusPayload>(rl("focus"))

    val STREAM_CODEC = StreamCodec.composite(
      ItemStack.STREAM_CODEC.nullable(),
      FocusPayload::itemStack,
      ::FocusPayload
    )

    // null payload means unequip focus
    fun handle(payload: FocusPayload, context: IPayloadContext) {
      val player = context.player()
      val wand = player.mainHandItem
      if (wand.item !is WandItem) return
      val oldFocus = wand.get(T7DataComponents.FOCUS)?.nonEmptyItems()?.firstOrNull()
      val newFocus = payload.itemStack

      if (newFocus == null) {
        if (oldFocus != null) giveItemToPlayer(player, oldFocus)
        wand.set(T7DataComponents.FOCUS, ItemContainerContents.EMPTY)
        return
      }

      if (!newFocus.`is`(T7Tags.Items.FOCI)) return
      player.inventory.run {
        for (i in 0..<containerSize) {
          val stack = getItem(i)
          if (ItemStack.isSameItemSameComponents(stack, newFocus)) {
            if (oldFocus != null) giveItemToPlayer(player, oldFocus)
            val removed = removeItem(i, 1)
            wand.set(T7DataComponents.FOCUS, ItemContainerContents.fromItems(NonNullList.copyOf(listOf(removed))))
            return
          }
        }
      }
    }
  }
}