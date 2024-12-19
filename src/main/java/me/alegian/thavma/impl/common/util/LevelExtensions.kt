package me.alegian.thavma.impl.common.util

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType

fun ServerLevel.updateBlockEntityS2C(blockPos: BlockPos) {
    getBlockEntity(blockPos)?.let { be ->
        be.setChanged()
        sendBlockUpdated(blockPos, be.blockState, be.blockState, Block.UPDATE_CLIENTS)
    }
}

fun <T : BlockEntity> Level.getBE(blockPos: BlockPos, type: BlockEntityType<T>): T? {
    return getBlockEntity(blockPos, type).orElse(null)
}
