package com.wdiscute.echoes.entity.trader;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wdiscute.echoes.entity.heart.HeartModel;
import com.wdiscute.echoes.entity.heart.SculkHeartEntity;
import com.wdiscute.echoes.entity.heart.SculkHeartRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.jspecify.annotations.NonNull;

public class SoulTraderRenderer extends EntityRenderer<SoulTraderEntity, SoulTraderRenderState>
{
    public SoulTraderModel soulTraderModel;

    public SoulTraderRenderer(EntityRendererProvider.Context context)
    {
        super(context);
        soulTraderModel = new SoulTraderModel(context.getModelSet().bakeLayer(SoulTraderModel.LAYER_LOCATION));
    }

    @Override
    public void submit(@NonNull SoulTraderRenderState state, PoseStack poseStack, SubmitNodeCollector node, CameraRenderState camera)
    {
        super.submit(state, poseStack, node, camera);

        poseStack.translate(0, 1.3, 0);
        poseStack.scale(1, -1, 1);

        node.submitModel(
                soulTraderModel, state, poseStack, RenderTypes.entityCutout(HeartModel.TEXTURE_LOCATION),
                state.lightCoords, OverlayTexture.NO_OVERLAY,
                -1, null, state.outlineColor, null
        );
    }

    @Override
    public SoulTraderRenderState createRenderState()
    {
        return new SoulTraderRenderState();
    }

    @Override
    public void extractRenderState(SoulTraderEntity entity, SoulTraderRenderState state, float partialTicks)
    {
        super.extractRenderState(entity, state, partialTicks);
    }
}