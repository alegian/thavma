package me.alegian.thavma.impl.client.renderer.blockentity

import me.alegian.thavma.impl.common.block.entity.HungryChestBE
import me.alegian.thavma.impl.rl
import net.minecraft.client.renderer.Sheets
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.ChestRenderer
import net.minecraft.client.resources.model.Material
import net.minecraft.world.level.block.state.properties.ChestType

class HungryChestBER(ctx: BlockEntityRendererProvider.Context) : ChestRenderer<HungryChestBE>(ctx) {
  override fun getMaterial(blockEntity: HungryChestBE, chestType: ChestType) = Material(Sheets.CHEST_SHEET, rl("entity/chest/hungry"))
}