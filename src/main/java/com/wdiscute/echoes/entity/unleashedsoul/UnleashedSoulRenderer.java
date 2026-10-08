package com.wdiscute.echoes.entity.unleashedsoul;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class UnleashedSoulRenderer extends EntityRenderer<UnleashedSoulEntity>
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
    public void render(UnleashedSoulEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight)
    {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        int maxTicks = entity.getEntityData().get(UnleashedSoulEntity.MAX_TICKS);
        float ticksAlive = entity.tickCount + partialTick;
        float yRot = entity.getViewYRot(partialTick);
        float xRot = entity.getViewXRot(partialTick);

        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(-yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));

        float scale = 1.0f;

        if (ticksAlive < 5.0f)
            scale = ticksAlive / 5.0f;
        else if (ticksAlive > maxTicks - 5.0f)
            scale = 1.0f - ((ticksAlive - (maxTicks - 5.0f)) / 5.0f);

        poseStack.scale(scale, scale, scale);

        poseStack.translate(0.0F, -0.9F, -0.3F);

        model.renderToBuffer(poseStack,
                bufferSource.getBuffer(RenderType.entityTranslucent(UnleashedSoulModel.TEXTURE_LOCATION)),
                0x00ffffff, OverlayTexture.NO_OVERLAY, -1);

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(UnleashedSoulEntity entity)
    {
        return UnleashedSoulModel.TEXTURE_LOCATION;
    }
}