package me.alegian.thavma.impl.common.entity;

import me.alegian.thavma.impl.init.registries.deferred.T7EntityTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

/**
 * Helper Entity mainly used for spawning Entity Renderers.
 * Does not tick like normal entities, but may be overridden to add side effects.
 */
public abstract class RendererEntity extends Entity {
  // position is used to determine if the render section is active, otherwise the entity is culled
  public RendererEntity(Level pLevel, Vec3 pos) {
    super(T7EntityTypes.INSTANCE.getVIS().get(), pLevel);
    this.setPos(pos);
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.@NotNull Builder pBuilder) {
  }

  @Override
  public void tick() {
  }
}
