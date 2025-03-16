package me.alegian.thavma.impl.common.block.entity

import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities.WORKBENCH
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ARCANE_WORKBENCH
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import software.bernie.geckolib.animatable.GeoBlockEntity
import software.bernie.geckolib.animatable.SingletonGeoAnimatable
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar
import software.bernie.geckolib.animation.AnimationController
import software.bernie.geckolib.animation.RawAnimation
import software.bernie.geckolib.util.GeckoLibUtil

/**
 * Default values used for rendering Item form
 */
class WorkbenchBE(
    pos: BlockPos = BlockPos(0, 0, 0),
    blockState: BlockState = ARCANE_WORKBENCH.get().defaultBlockState()
) : BlockEntity(WORKBENCH.get(), pos, blockState), GeoBlockEntity {
    private var inBEWLR = false
    private var playerInRange: Player? = null
    private val cache = GeckoLibUtil.createInstanceCache(this)
    val CLOSED_ANIM = RawAnimation.begin().thenLoop("closed")
    val OPEN_ANIM = RawAnimation.begin().thenLoop("rotating")
    private val ANIM_CONTROLLER = AnimationController(
        this, "main", 20
    ) { state ->
        var animation: RawAnimation? = state.controller.currentRawAnimation
        if (animation == null) {
            animation = if(inBEWLR || isPlayerNearby()) OPEN_ANIM else CLOSED_ANIM
        }
        state.setAndContinue(animation)
    }
        .triggerableAnim("closed", CLOSED_ANIM)
        .triggerableAnim("rotating", OPEN_ANIM)

    init {
        SingletonGeoAnimatable.registerSyncedAnimatable(this)
    }

    override fun registerControllers(controllers: ControllerRegistrar) {
        controllers.add(this.ANIM_CONTROLLER)
    }

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache {
        return this.cache
    }

    fun withDefaultAnimations(): WorkbenchBE {
        inBEWLR = true
        return this
    }

    private fun isPlayerNearby(): Boolean {
        level.let {
            if(it == null) return false
            return getNearbyPlayer(it, blockPos) != null
        }
    }

    companion object {
        fun serverTick(level: Level, pos: BlockPos, state: BlockState, be: WorkbenchBE) {
            val player = getNearbyPlayer(level, pos)
            if (player != null && be.playerInRange == null) {
                be.triggerAnim("main", "rotating")
            } else if (player == null && be.playerInRange != null) {
                be.triggerAnim("main", "closed")
            }
            be.playerInRange = player
        }
    }
}

private fun getNearbyPlayer(level: Level, pos: BlockPos): Player? {
    return level.getNearestPlayer(pos.x.toDouble() + 0.5, pos.y.toDouble() + 0.5, pos.z.toDouble() + 0.5, 4.0, false)
}
