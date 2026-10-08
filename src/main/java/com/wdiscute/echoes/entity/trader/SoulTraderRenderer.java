package com.wdiscute.echoes.entity.trader;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class SoulTraderRenderer extends EntityRenderer<SoulTraderEntity>
{
    public SoulTraderModel soulTraderModel;

    public SoulTraderRenderer(EntityRendererProvider.Context context)
    {
        super(context);
        soulTraderModel = new SoulTraderModel(context.getModelSet().bakeLayer(SoulTraderModel.LAYER_LOCATION));
    }

    @Override
    public void render(SoulTraderEntity p_entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight)
    {
        super.render(p_entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        poseStack.translate(0, 3.5, 0);

        double t = (System.nanoTime()) / 1_000_000_000.0;
        double y = Math.sin(t * 1.10);
        Vec3 p = new Vec3(0, y / 5, 0);
        poseStack.translate(p.x, p.y, p.z);

        poseStack.scale(2, 2, 2);

        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        poseStack.mulPose(Axis.YP.rotationDegrees(90));

        //todo fix light coords so it doesn't darken when entity is inside a block
        soulTraderModel.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityTranslucent(SoulTraderModel.TEXTURE_LOCATION)),
                packedLight, OverlayTexture.NO_OVERLAY, -1);
    }

    @Override
    public ResourceLocation getTextureLocation(SoulTraderEntity entity)
    {
        return SoulTraderModel.TEXTURE_LOCATION;
    }
}