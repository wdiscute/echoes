package com.wdiscute.echoes.registry.entity.lantern;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public class LanternRenderer extends EntityRenderer<LanternEntity, LanternRenderState>
{
    public static final List<Identifier> skins = new ArrayList<>();

    public LanternRenderer(EntityRendererProvider.Context context)
    {
        super(context);
    }

    public void submit(LanternRenderState state, PoseStack poseStack, SubmitNodeCollector node, CameraRenderState camera)
    {
        super.submit(state, poseStack, node, camera);
    }

    public LanternRenderState createRenderState()
    {
        return new LanternRenderState();
    }

    public void extractRenderState(LanternEntity entity, LanternRenderState state, float partialTicks)
    {
        super.extractRenderState(entity, state, partialTicks);
    }
}