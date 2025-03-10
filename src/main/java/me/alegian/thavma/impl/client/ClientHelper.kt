package me.alegian.thavma.impl.client

import me.alegian.thavma.impl.init.registries.deferred.T7Attributes.REVEALING
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.Item

fun clientPlayerHasRevealing(): Boolean {
    return Minecraft.getInstance().player?.getAttribute(REVEALING)?.value == 1.0
}

fun getClientPlayerEquipmentItem(slot: EquipmentSlot): Item? {
    return Minecraft.getInstance().player?.getItemBySlot(slot)?.item
}

fun setClientScreen(screen: Screen){
    Minecraft.getInstance().setScreen(screen)
}
