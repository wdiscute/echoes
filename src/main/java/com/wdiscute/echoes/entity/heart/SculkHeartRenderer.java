package com.wdiscute.echoes.entity.heart;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class SculkHeartRenderer extends EntityRenderer<SculkHeartEntity>
{
    public HeartModel heartModel;

    public SculkHeartRenderer(EntityRendererProvider.Context context)
    {
        super(context);
        heartModel = new HeartModel(context.getModelSet().bakeLayer(HeartModel.LAYER_LOCATION));
    }

    @Override
    public void render(SculkHeartEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight)
    {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        poseStack.translate(0, 1.3, 0);
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.scale(1, 1, 1);


        heartModel.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityCutout(HeartModel.TEXTURE_LOCATION)),
                packedLight, OverlayTexture.NO_OVERLAY, -1);
    }

    @Override
    public ResourceLocation getTextureLocation(SculkHeartEntity entity)
    {
        return HeartModel.TEXTURE_LOCATION;
    }
}