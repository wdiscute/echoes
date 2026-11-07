package com.wdiscute.echoes.blocks.floater;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.model.data.ModelData;

public class FloaterBlockRenderer implements BlockEntityRenderer<FloaterBlockEntity>
{
    BlockRenderDispatcher blockRenderer;

    public FloaterBlockRenderer(BlockEntityRendererProvider.Context context)
    {
        blockRenderer = context.getBlockRenderDispatcher();
    }

    @Override
    public boolean shouldRenderOffScreen(FloaterBlockEntity blockEntity)
    {
        return true;
    }

    @Override
    public boolean shouldRender(FloaterBlockEntity blockEntity, Vec3 cameraPos)
    {
        return true;
    }

    @Override
    public void render(FloaterBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay)
    {
        Level level = blockEntity.getLevel();

        poseStack.pushPose();

        double sin = Math.sin((level.getGameTime() + blockEntity.offset + partialTick) / 100d);
        poseStack.translate(0, (sin+1) * 2, 0);

        for (var entry : blockEntity.blocks)
        {
            BlockPos pos = entry.first();
            BlockState state = entry.second();
            if (state.isAir()) continue;

            BlockPos worldPos = blockEntity.getBlockPos().offset(pos);

            poseStack.pushPose();
            poseStack.translate(pos.getX(), pos.getY(), pos.getZ());

            var model = this.blockRenderer.getBlockModel(state);
            RandomSource random = RandomSource.create(state.getSeed(worldPos));

            for (var renderType : model.getRenderTypes(state, random, ModelData.EMPTY))
            {
                this.blockRenderer.getModelRenderer().tesselateBlock(
                        level,
                        model,
                        state,
                        worldPos,                       // real position for light/AO/tint
                        poseStack,
                        bufferSource.getBuffer(RenderTypeHelper.getMovingBlockRenderType(renderType)),
                        false,
                        random,
                        state.getSeed(worldPos),
                        OverlayTexture.NO_OVERLAY,
                        ModelData.EMPTY,
                        renderType
                );
            }
            poseStack.popPose();
        }

        poseStack.popPose();
    }
}
