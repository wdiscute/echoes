package com.wdiscute.echoes.entity.heart;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;

public class SculkHeartRenderer extends EntityRenderer<SculkHeartEntity, SculkHeartRenderState>
{
    public SculkHeartRenderer(EntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void submit(SculkHeartRenderState state, PoseStack poseStack, SubmitNodeCollector node, CameraRenderState camera)
    {
        super.submit(state, poseStack, node, camera);
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
    }
}