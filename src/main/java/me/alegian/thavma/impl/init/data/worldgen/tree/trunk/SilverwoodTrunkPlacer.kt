package me.alegian.thavma.impl.init.data.worldgen.tree.trunk

import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.init.registries.deferred.T7TrunkPlacerTypes.SILVERWOOD
import net.minecraft.core.BlockPos
import net.minecraft.core.BlockPos.MutableBlockPos
import net.minecraft.core.Direction
import net.minecraft.tags.BlockTags
import net.minecraft.util.RandomSource
import net.minecraft.world.level.LevelSimulatedReader
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer.FoliageAttachment
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType
import java.util.function.BiConsumer

class SilverwoodTrunkPlacer(pBaseHeight: Int, pHeightRandA: Int, pHeightRandB: Int) : TrunkPlacer(pBaseHeight, pHeightRandA, pHeightRandB) {
    override fun type(): TrunkPlacerType<*> {
        return SILVERWOOD.get()
    }

    override fun placeTrunk(
        level: LevelSimulatedReader,
        blockSetter: BiConsumer<BlockPos, BlockState>,
        random: RandomSource,
        freeTreeHeight: Int,
        pos: BlockPos, config:
        TreeConfiguration
    ): List<FoliageAttachment> {
        val mutableBlockPos = MutableBlockPos()

        for (i in 0..<freeTreeHeight) {
            place3x3Slice(level, blockSetter, random, mutableBlockPos, config, pos, i, i < 2)
        }
        placeLegs(level, blockSetter, random, mutableBlockPos, config, pos)

        return listOf(
            leavesBubble(pos, freeTreeHeight, 0, 0),
            leavesBubble(pos, freeTreeHeight, -1, 0),
            leavesBubble(pos, freeTreeHeight, 1, 0),
            leavesBubble(pos, freeTreeHeight, 0, -1),
            leavesBubble(pos, freeTreeHeight, 0, 1),
        )
    }

    private fun leavesBubble(pos: BlockPos, freeTreeHeight: Int, offsetX: Int, offsetZ: Int): FoliageAttachment {
        return FoliageAttachment(pos.offset(offsetX, freeTreeHeight - 3, offsetZ), 0, false)
    }

    private fun place3x3Slice(
        level: LevelSimulatedReader,
        blockSetter: BiConsumer<BlockPos, BlockState>,
        random: RandomSource,
        mutableBlockPos: MutableBlockPos,
        config: TreeConfiguration,
        pos: BlockPos,
        offsetY: Int,
        placeCorners: Boolean, // whether to place the corners of the 3x3 cross
    ) {
        for (x in -1..1) {
            for (z in -1..1) {
                if (x * z != 0 && !placeCorners) continue
                placeLogIfFreeWithOffset(level, blockSetter, random, mutableBlockPos, config, pos, x, offsetY, z)
            }
        }
    }

    private fun placeLegs(
        level: LevelSimulatedReader,
        blockSetter: BiConsumer<BlockPos, BlockState>,
        random: RandomSource,
        mutableBlockPos: MutableBlockPos,
        config: TreeConfiguration,
        pos: BlockPos,
    ) {
        placeLogWithOffsetAlongAxis(level, blockSetter, random, mutableBlockPos, config, pos, -2, 0, 0, Direction.Axis.X)
        placeLogIfFreeWithOffset(level, blockSetter, random, mutableBlockPos, config, pos, -2, -1, 0)

        placeLogWithOffsetAlongAxis(level, blockSetter, random, mutableBlockPos, config, pos, 2, 0, 0, Direction.Axis.X)
        placeLogIfFreeWithOffset(level, blockSetter, random, mutableBlockPos, config, pos, 2, -1, 0)

        placeLogWithOffsetAlongAxis(level, blockSetter, random, mutableBlockPos, config, pos, 0, 0, -2, Direction.Axis.Z)
        placeLogIfFreeWithOffset(level, blockSetter, random, mutableBlockPos, config, pos, 0, -1, -2)

        placeLogWithOffsetAlongAxis(level, blockSetter, random, mutableBlockPos, config, pos, 0, 0, 2, Direction.Axis.Z)
        placeLogIfFreeWithOffset(level, blockSetter, random, mutableBlockPos, config, pos, 0, -1, 2)
    }

    private fun placeLogIfFreeWithOffset(
        level: LevelSimulatedReader,
        blockSetter: BiConsumer<BlockPos, BlockState>,
        random: RandomSource,
        pos: MutableBlockPos,
        config: TreeConfiguration,
        offsetPos: BlockPos,
        offsetX: Int,
        offsetY: Int,
        offsetZ: Int
    ) {
        pos.setWithOffset(offsetPos, offsetX, offsetY, offsetZ)
        placeLogIfFree(level, blockSetter, random, pos, config)
    }

    private fun placeLogWithOffsetAlongAxis(
        level: LevelSimulatedReader,
        blockSetter: BiConsumer<BlockPos, BlockState>,
        random: RandomSource,
        pos: MutableBlockPos,
        config: TreeConfiguration,
        offsetPos: BlockPos,
        offsetX: Int,
        offsetY: Int,
        offsetZ: Int,
        axis: Direction.Axis
    ) {
        pos.setWithOffset(offsetPos, offsetX, offsetY, offsetZ)
        placeLog(level, blockSetter, random, pos, config) { bs -> bs.trySetValue(RotatedPillarBlock.AXIS, axis) }
    }

    /**
     * silverwood logs can overwrite dirt
     */
    override fun validTreePos(level: LevelSimulatedReader, pos: BlockPos): Boolean {
        return super.validTreePos(level, pos) || level.isStateAtPosition(pos) { bs -> bs.`is`(BlockTags.DIRT) }
    }

    companion object {
        val CODEC = RecordCodecBuilder.mapCodec { builder -> trunkPlacerParts(builder).apply(builder, ::SilverwoodTrunkPlacer) }
    }
}
