package me.alegian.thavma.impl.common.block.entity

import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class HoleBE(pos: BlockPos, state: BlockState) : BlockEntity(T7BlockEntities.HOLE.get(), pos, state)
