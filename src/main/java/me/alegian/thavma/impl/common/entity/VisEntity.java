package me.alegian.thavma.impl.common.entity;

import me.alegian.thavma.impl.common.data.capability.AspectContainer;
import me.alegian.thavma.impl.common.util.LevelExtensionsKt;
import me.alegian.thavma.impl.init.registries.deferred.Aspects;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Entity that renders as a spiral beam between the Player and an Aura Node.
 * Transfers vis from the target block to the held wand by ticking.
 */
public class VisEntity extends RendererEntity {
  public static final String PLAYER_TAG = "player";
  private static final int PERIOD_TICKS = 5;
  @Nullable
  private UUID playerUUID; // save player UUID and not entire Player, because when deserializing, level.players is not yet populated

  public VisEntity(Level pLevel, Player player) {
    this(pLevel, player, new BlockPos(0, 0, 0));
  }

  public VisEntity(Level pLevel, @Nullable Player player, @NotNull BlockPos blockPos) {
    super(pLevel, blockPos.getCenter());
    if (player != null) this.playerUUID = player.getUUID();
  }

  /**
   * Ticks once every 5 ticks. Kills itself if the player is not using the Wand.
   * Charges the held aspect container (wand).
   * Interrupts item use when the aspect container is full.
   */
  @Override
  public void tick() {
    if (this.tickCount % VisEntity.PERIOD_TICKS != 0) return;
    var player = this.getPlayer();
    var optionalPair = AspectContainer.blockSourceItemSink(this.level(), this.blockPosition(), player.getUseItem());
    boolean canTransfer = optionalPair.map(AspectContainer.Pair::canTransferPrimals).orElse(false);
    if (!canTransfer) player.stopUsingItem();

    this.serverTick(player, optionalPair.orElse(null));
  }

  private void serverTick(Player player, @Nullable AspectContainer.Pair pair) {
    if (this.level().isClientSide()) return;
    if (player == null || !player.isUsingItem() || pair == null) {
      this.kill();
      return;
    }
    var transferred = pair.transferPrimal((this.tickCount / VisEntity.PERIOD_TICKS) % Aspects.INSTANCE.getPRIMAL_ASPECTS().size(), 5);
    if (transferred != null && transferred.getAmount() > 0 && this.level() instanceof ServerLevel serverLevel)
      LevelExtensionsKt.updateBlockEntityS2C(serverLevel, this.blockPosition());
  }

  public @Nullable Player getPlayer() {
    if (this.playerUUID == null) return null;
    return this.level().getPlayerByUUID(this.playerUUID);
  }

  public @Nullable UUID getPlayerUUID() {
    return this.playerUUID;
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag pCompound) {
    if (pCompound.hasUUID(VisEntity.PLAYER_TAG)) this.playerUUID = pCompound.getUUID(VisEntity.PLAYER_TAG);
  }

  @Override
  protected void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
    if (this.playerUUID == null) return;
    pCompound.putUUID(VisEntity.PLAYER_TAG, this.playerUUID);
  }

  @Override
  public void restoreFrom(@NotNull Entity pEntity) {
    super.restoreFrom(pEntity);
    this.playerUUID = ((VisEntity) pEntity).getPlayerUUID();
  }

  @Override
  public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket(@NotNull ServerEntity pEntity) {
    Entity player = this.getPlayer();
    return new ClientboundAddEntityPacket(this, pEntity, player == null ? 0 : player.getId());
  }

  @Override
  public void recreateFromPacket(@NotNull ClientboundAddEntityPacket pPacket) {
    super.recreateFromPacket(pPacket);
    Entity entity = this.level().getEntity(pPacket.getData());
    if (entity instanceof Player player) this.playerUUID = player.getUUID();
  }
}
