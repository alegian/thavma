package me.alegian.thaumcraft7.impl.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import me.alegian.thaumcraft7.impl.client.renderer.VisRenderer;
import me.alegian.thaumcraft7.impl.common.entity.RendererEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class RendererRenderer extends EntityRenderer<RendererEntity> {

  public RendererRenderer(EntityRendererProvider.Context pContext) {
    super(pContext);
  }

  @Override
  public void render(RendererEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight) {
    // general purpose useful stuff
    var minecraft = Minecraft.getInstance();
    if (minecraft.level == null) return;
    var hitResult = minecraft.hitResult;
    if (hitResult == null || hitResult.getType() != HitResult.Type.BLOCK) return;
    var blockPos = ((BlockHitResult) hitResult).getBlockPos();
    var player = minecraft.player;
    if (player == null) return;

    Vec3 playerPos = player.getPosition(pPartialTick).add(0, 1.5, 0);
    pPoseStack.pushPose();
    pPoseStack.translate(-pEntity.getX(), -pEntity.getY(), -pEntity.getZ());
    VisRenderer.render(playerPos, blockPos.getCenter(), pPoseStack, pBufferSource, pEntity.tickCount + pPartialTick);
    pPoseStack.popPose();
  }

  @Override
  public ResourceLocation getTextureLocation(RendererEntity pEntity) {
    return InventoryMenu.BLOCK_ATLAS;
  }

  @Override
  public boolean shouldRender(RendererEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
    return true;
  }
}
