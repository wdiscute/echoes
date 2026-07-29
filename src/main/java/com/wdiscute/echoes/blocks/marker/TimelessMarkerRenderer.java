package com.wdiscute.echoes.blocks.marker;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class TimelessMarkerRenderer implements BlockEntityRenderer<TimelessMarkerBlockEntity, TimelessMarkerRenderState>
{
    public TimelessMarkerRenderer(BlockEntityRendererProvider.Context context)
    {
    }

    @Override
    public TimelessMarkerRenderState createRenderState()
    {
        return new TimelessMarkerRenderState();
    }

    @Override
    public void extractRenderState(TimelessMarkerBlockEntity blockEntity, TimelessMarkerRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress)
    {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.type = blockEntity.getBlockState().getValueOrElse(TimelessMarkerBlock.TYPE, TimelessMarkerBlock.Type.SPAWN_POINT);
        state.pos = blockEntity.getBlockPos().getCenter();
    }

    @Override
    public void submit(TimelessMarkerRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        submitNodeCollector.submitNameTag(
                poseStack, new Vec3(0.5f, 1 , 0.5f), 0, Component.literal(state.type.getSerializedName()),
                true, 15728880, camera.pos.distanceToSqr(state.pos), camera
        );
    }
}
