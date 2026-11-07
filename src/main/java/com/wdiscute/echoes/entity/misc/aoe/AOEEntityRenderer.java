package com.wdiscute.echoes.entity.misc.aoe;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wdiscute.echoes.Echoes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class AOEEntityRenderer extends EntityRenderer<AOEEntity>
{
    public AOEEntityRenderer(EntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(AOEEntity entity)
    {
        return Echoes.MISSINGNO;
    }

    @Override
    public boolean shouldRender(AOEEntity livingEntity, Frustum camera, double camX, double camY, double camZ)
    {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void render(AOEEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight)
    {
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
        if(entity.aoe != null)
            entity.aoe.getRenderer().render(entity.aoe, poseStack, buffer, entity.position(), partialTick, packedLight);
    }
}