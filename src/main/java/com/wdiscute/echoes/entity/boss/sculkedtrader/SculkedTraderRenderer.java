package com.wdiscute.echoes.entity.boss.sculkedtrader;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wdiscute.echoes.Echoes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class SculkedTraderRenderer extends MobRenderer<SculkedTraderEntity, SculkedTraderModel>
{
    public SculkedTraderRenderer(EntityRendererProvider.Context context)
    {
        super(context, new SculkedTraderModel(context.getModelSet().bakeLayer(SculkedTraderModel.LAYER_LOCATION)), 0);
    }

    @Override
    public ResourceLocation getTextureLocation(SculkedTraderEntity entity)
    {
        return SculkedTraderModel.TEXTURE_LOCATION;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void render(SculkedTraderEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight)
    {
        //float scale = 3;
        //poseStack.scale(scale, scale, scale);
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }
}