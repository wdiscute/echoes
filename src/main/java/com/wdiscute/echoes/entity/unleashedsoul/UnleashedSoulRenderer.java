package com.wdiscute.echoes.entity.unleashedsoul;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wdiscute.echoes.entity.soul.SoulModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

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

        state.yRot = entity.getYRot(partialTick);
        state.xRot = entity.getXRot(partialTick);
    }

    @Override
    public void submit(UnleashedSoulRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState)
    {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(-state.yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(state.xRot));

        poseStack.translate(0.0F, -0.9F, -0.3F);

        collector.submitModel(model, state, poseStack, RenderTypes.entityTranslucent(UnleashedSoulModel.TEXTURE_LOCATION), state.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, 0, null);

        poseStack.popPose();
    }
}