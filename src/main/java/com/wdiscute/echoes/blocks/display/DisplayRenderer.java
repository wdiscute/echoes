package com.wdiscute.echoes.blocks.display;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Util;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class DisplayRenderer implements BlockEntityRenderer<DisplayBlockEntity, DisplayRenderState>
{
    private final ItemModelResolver itemModelResolver;

    public DisplayRenderer(BlockEntityRendererProvider.Context context)
    {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public DisplayRenderState createRenderState()
    {
        return new DisplayRenderState();
    }

    @Override
    public void extractRenderState(DisplayBlockEntity blockEntity, DisplayRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress)
    {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        state.stack = blockEntity.trade.stack().toStack();
        this.itemModelResolver.updateForNonLiving(state.item, state.stack, ItemDisplayContext.FIXED, Minecraft.getInstance().player);
    }

    @Override
    public void submit(DisplayRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        poseStack.pushPose();

        poseStack.scale(0.7f, 0.7f, 0.7f);
        poseStack.translate(0.7f, 1.7f + (Math.sin(Util.getMillis() / 555f) / 60), 0.7f);
        poseStack.translate(0f, 0.4f, 0f);

        float x = (float) (Math.sin(Util.getMillis() / 2000f + 323) * 20f);
        float y = (float) (Math.sin(Util.getMillis() / 2000f) * 20f);

        poseStack.mulPose(Axis.XP.rotationDegrees(x));
        poseStack.mulPose(Axis.YP.rotationDegrees(y));
        poseStack.mulPose(Axis.ZP.rotationDegrees((float) (Math.toRadians(Util.getMillis() % 360f) / 600f)));

        if (!state.stack.isEmpty())
        {
            int lightVal = state.lightCoords;
            state.item.submit(poseStack, submitNodeCollector, lightVal, OverlayTexture.NO_OVERLAY, 0);
        }

        poseStack.popPose();
    }
}
