package com.wdiscute.echoes.entity.heart;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wdiscute.echoes.entity.lantern.LanternModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.jspecify.annotations.NonNull;

public class SculkHeartRenderer extends EntityRenderer<SculkHeartEntity, SculkHeartRenderState>
{
    public HeartModel heartModel;

    public SculkHeartRenderer(EntityRendererProvider.Context context)
    {
        super(context);
        heartModel = new HeartModel(context.getModelSet().bakeLayer(HeartModel.LAYER_LOCATION));
    }

    @Override
    public void submit(@NonNull SculkHeartRenderState state, PoseStack poseStack, SubmitNodeCollector node, CameraRenderState camera)
    {
        super.submit(state, poseStack, node, camera);

        poseStack.translate(0, 1.3, 0);
        poseStack.mulPose(Axis.YP.rotationDegrees(state.rot));
        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.scale(1, 1, 1);


        node.submitModel(
                heartModel, state, poseStack, RenderTypes.entityCutout(HeartModel.TEXTURE_LOCATION),
                state.lightCoords, OverlayTexture.NO_OVERLAY,
                -1, null, state.outlineColor, null
        );
    }

    @Override
    public SculkHeartRenderState createRenderState()
    {
        return new SculkHeartRenderState();
    }

    @Override
    public void extractRenderState(SculkHeartEntity entity, SculkHeartRenderState state, float partialTicks)
    {
        super.extractRenderState(entity, state, partialTicks);
        state.rot = entity.getYRot();
    }
}