package com.wdiscute.echoes.entity.unleashedsoul;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class UnleashedSoulRenderer extends EntityRenderer<UnleashedSoulEntity, UnleashedSoulRenderState>
{

    private final UnleashedSoulModel model;

    public UnleashedSoulRenderer(EntityRendererProvider.Context context)
    {
        super(context);

        this.model = new UnleashedSoulModel(
                context.bakeLayer(UnleashedSoulModel.LAYER_LOCATION)
        );
    }

    @Override
    public UnleashedSoulRenderState createRenderState()
    {
        return new UnleashedSoulRenderState();
    }

    @Override
    public void extractRenderState(UnleashedSoulEntity entity, UnleashedSoulRenderState state, float partialTick)
    {
        super.extractRenderState(entity, state, partialTick);

        state.maxTicks = entity.getEntityData().get(UnleashedSoulEntity.MAX_TICKS);
        state.ticksAlive = entity.tickCount + partialTick;
        state.yRot = entity.getYRot(partialTick);
        state.xRot = entity.getXRot(partialTick);
    }

    @Override
    public void submit(UnleashedSoulRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState)
    {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(-state.yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(state.xRot));

        float scale = 1.0f;

        if (state.ticksAlive < 5.0f) {
            scale = state.ticksAlive / 5.0f;
        } else if (state.ticksAlive > state.maxTicks - 5.0f) {
            scale = 1.0f - (
                    (state.ticksAlive - (state.maxTicks - 5.0f)) / 5.0f
            );
        }

        poseStack.scale(scale, scale, scale);

        poseStack.translate(0.0F, -0.9F, -0.3F);

        collector.submitModel(model, state, poseStack, RenderTypes.entityTranslucent(UnleashedSoulModel.TEXTURE_LOCATION), 0x00ffffff, OverlayTexture.NO_OVERLAY, -1, null, 0, null);

        poseStack.popPose();
    }
}