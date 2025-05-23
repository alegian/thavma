package me.alegian.thavma.impl.client.extension;

import com.mojang.blaze3d.vertex.PoseStack;
import me.alegian.thavma.impl.common.enumextension.ArcaneLensArmPose;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

@OnlyIn(Dist.CLIENT)
public class ArcaneLensItemExtensions implements IClientItemExtensions {
  @Override
  public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
    return ArcaneLensArmPose.value();
  }

  @Override
  public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
    // pretty much nullifies the default right click bob
    if (player.getUseItem() == itemInHand && player.isUsingItem()) {
      int i = arm == HumanoidArm.RIGHT ? 1 : -1;
      poseStack.translate(i * 0.56F, -0.53F, -0.72F);
      return true;
    }
    return false;
  }
}
