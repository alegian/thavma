package me.alegian.thavma.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.alegian.thavma.impl.common.item.HammerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.extensions.IBlockExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = IBlockExtension.class, remap = false)
public interface IBlockExtensionMixin {
  @WrapMethod(method = "onDestroyedByPlayer(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;ZLnet/minecraft/world/level/material/FluidState;)Z")
  private boolean thavma_onDestroyedByPlayerWrapper(
      BlockState state,
      Level level,
      BlockPos pos,
      Player player,
      boolean willHarvest,
      FluidState fluid,
      Operation<Boolean> original
  ) {
    var out = original.call(state, level, pos, player, willHarvest, fluid);
    if (out) HammerItem.Companion.destroyBlockMixin(player, level, pos);
    return out;
  }
}
