package me.alegian.thavma.impl.common.payload

import me.alegian.thavma.impl.common.menu.ResearchMenu
import me.alegian.thavma.impl.common.research.SocketState
import me.alegian.thavma.impl.init.registries.deferred.T7DataComponents
import me.alegian.thavma.impl.init.registries.deferred.T7Items
import me.alegian.thavma.impl.rl
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext

// C2S
class SocketStatePayload(val state: SocketState) : CustomPacketPayload {
  override fun type() = TYPE

  companion object {
    val TYPE = CustomPacketPayload.Type<SocketStatePayload>(rl("socket_state"))

    val STREAM_CODEC = StreamCodec.composite(
      SocketState.STREAM_CODEC,
      SocketStatePayload::state,
      ::SocketStatePayload
    )

    // TODO: allow only aspects scanned (prevent hacks)
    fun handle(payload: SocketStatePayload, context: IPayloadContext) {
      val menu = context.player().containerMenu
      if (menu !is ResearchMenu) return

      val itemStack = menu.scrollContainer.getItem(0).copy()
      if (itemStack.item != T7Items.RESEARCH_SCROLL.get()) return

      val newState = itemStack.get(T7DataComponents.RESEARCH_STATE)?.toMutableList() ?: return
      newState.removeIf { it.indices == payload.state.indices }
      newState.add(payload.state)

      itemStack.set(T7DataComponents.RESEARCH_STATE, newState)
      menu.scrollContainer.setItem(0, itemStack)
    }
  }
}