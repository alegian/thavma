package me.alegian.thaumcraft7.blockentity.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import me.alegian.thaumcraft7.blockentity.AuraNodeBE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class AuraNodeBER implements BlockEntityRenderer<AuraNodeBE> {

    @Override
    public void render(AuraNodeBE be, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        // what type of data our vertices contain
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tesselator t = Tesselator.getInstance();
        BufferBuilder buff = t.getBuilder();
        poseStack.pushPose();

        // at the center of the block
        poseStack.translate(0.5f, 0.5f, 0.5f);

        // follows the camera like a particle
        Quaternionf rotation = Minecraft.getInstance().gameRenderer.getMainCamera().rotation();
        poseStack.mulPose(rotation);

        // origin
        Matrix4f matrix = poseStack.last().pose();

        buff.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        buff.vertex(matrix, -0.5f, -0.5f, 0).color(0, 0, 1, 0.2f).endVertex();
        buff.vertex(matrix, -0.5f, 0.5f, 0).color(0, 0, 1, 0.2f).endVertex();
        buff.vertex(matrix, 0.5f, 0.5f, 0).color(0, 0, 1, 0.2f).endVertex();
        buff.vertex(matrix, 0.5f, -0.5f, 0).color(0, 0, 1, 0.2f).endVertex();
        t.end();

        poseStack.popPose();
    }
}
