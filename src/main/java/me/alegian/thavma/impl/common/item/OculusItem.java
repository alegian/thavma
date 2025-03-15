package me.alegian.thavma.impl.common.item;

import me.alegian.thavma.impl.common.aspect.AspectHelperKt;
import me.alegian.thavma.impl.common.block.AuraNodeBlock;
import me.alegian.thavma.impl.init.registries.T7AttributeModifiers;
import me.alegian.thavma.impl.init.registries.deferred.T7Attributes;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

import static me.alegian.thavma.impl.ThavmaKt.rl;

public class OculusItem extends Item implements GeoItem {
  private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

  public OculusItem(Properties props) {
    super(props.attributes(
        ItemAttributeModifiers.builder().add(
            T7Attributes.INSTANCE.getREVEALING(),
            T7AttributeModifiers.Revealing.INSTANCE.getOCULUS(),
            EquipmentSlotGroup.MAINHAND
        ).build()
    ).stacksTo(1));
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    var level = context.getLevel();
    var block = level.getBlockState(context.getClickedPos()).getBlock();
    if (block instanceof AuraNodeBlock) {
      var player = context.getPlayer();
      if (player != null)
        if (level.isClientSide)
          player.sendSystemMessage(Component.literal(AspectHelperKt.getAspects(block).toString()));
    }
    return InteractionResult.PASS;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    player.startUsingItem(hand);
    return InteractionResultHolder.success(player.getItemInHand(hand));
  }

  @Override
  public UseAnim getUseAnimation(ItemStack itemStack) {
    return UseAnim.CUSTOM;
  }

  @Override
  public int getUseDuration(ItemStack pStack, LivingEntity pEntity) {
    return 72000;
  }

  @Override
  public void releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int someDuration) {
    if (level.isClientSide() && entity instanceof Player) entity.sendSystemMessage(Component.literal("release using"));
  }

  @Override
  public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

  }

  @Override
  public AnimatableInstanceCache getAnimatableInstanceCache() {
    return this.cache;
  }

  @Override
  public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
    consumer.accept(new GeoRenderProvider() {
      private GeoItemRenderer<?> renderer;

      @Override
      public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
        if (this.renderer == null)
          this.renderer = new GeoItemRenderer<>(new DefaultedItemGeoModel<OculusItem>(rl("oculus")) {
            @Override
            public RenderType getRenderType(OculusItem animatable, ResourceLocation texture) {
              return RenderType.entityTranslucent(texture);
            }
          });

        return this.renderer;
      }
    });
  }
}
