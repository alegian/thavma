package me.alegian.thaumcraft7.impl.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import me.alegian.thaumcraft7.api.aspect.Aspect;
import me.alegian.thaumcraft7.impl.client.T7RenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public class VisRenderer {
  // triangle resolution
  public static int N = 100;
  // delta angle
  public static double da = 2 * Math.PI / N;

  public static void render(Vec3 playerPos, Vec3 blockPos, PoseStack poseStack, MultiBufferSource bufferSource, float partialTicks) {
    Vector3f a = playerPos.toVector3f();
    Vector3f b = blockPos.toVector3f();
    // delta vector (scaled b-a)
    Vector3f dx = b.sub(a).div(N);

    poseStack.pushPose();
    VertexConsumer vc = bufferSource.getBuffer(T7RenderTypes.DEBUG_TRIANGLE_STRIP);
    // start at one end
    poseStack.translate(a.x, a.y, a.z);

    // some useful axes
    Axis mainAxis = Axis.of(dx);
    // the perpendicular axis, that is not in the direction of Y
    Vector3f zBasis = new Vector3f(dx).cross(new Vector3f(0, 1, 0)).normalize();

    double phase = (partialTicks / 20 / 4 * 2 * Math.PI) % 2 * Math.PI;

    // rotation phase to "animate" the spiral
    poseStack.mulPose(mainAxis.rotation((float) (phase)));
    for (int i = 0; i <= N; i++) {
      float thicknessOffset = (float) Math.sin(da / 2 * i) / 8;

      // the 2 vertices render with opposite offsets to give thickness
      poseStack.pushPose();
      poseStack.translate(0, thicknessOffset, 0);
      renderVertex(vc, poseStack);
      poseStack.popPose();

      poseStack.pushPose();
      poseStack.translate(0, -thicknessOffset, 0);
      renderVertex(vc, poseStack);
      poseStack.popPose();

      // move towards goal
      poseStack.translate(dx.x, dx.y, dx.z);
      // offset on the Z axis to create spiral
      Vector3f translation = new Vector3f(zBasis).mul((float) (Math.sin(da) / 4f));
      poseStack.translate(translation.x, translation.y, translation.z);
      // spiral vertices should be drawn rotated
      poseStack.mulPose(mainAxis.rotation((float) (da)));
    }

    poseStack.popPose();
  }

  public static void renderVertex(
      VertexConsumer vc,
      PoseStack pose
  ) {
    // keep the praecantatio color with some alpha
    vc.addVertex(pose.last(), 0, 0, 0)
        .setColor(Aspect.PRAECANTATIO.getColor() & 0xFFFFFF | 0x88000000);
  }
}
