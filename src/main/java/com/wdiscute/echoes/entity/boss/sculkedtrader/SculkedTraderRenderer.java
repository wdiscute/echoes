package com.wdiscute.echoes.entity.boss.sculkedtrader;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.entity.misc.aoe.AOEEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SculkedTraderRenderer extends EntityRenderer<SculkedTraderEntity>
{
    public SculkedTraderRenderer(EntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(SculkedTraderEntity entity)
    {
        return Echoes.MISSINGNO;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void render(SculkedTraderEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight)
    {
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }
}