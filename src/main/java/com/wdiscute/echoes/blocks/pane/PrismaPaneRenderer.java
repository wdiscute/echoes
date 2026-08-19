package com.wdiscute.echoes.blocks.pane;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.blocks.PrismaPaneBlock;
import com.wdiscute.echoes.compat.IrisCompat;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PrismaPaneRenderer implements BlockEntityRenderer<PrismaPaneBlockEntity, PrismaPaneRenderState>
{
    public PrismaPaneRenderer(BlockEntityRendererProvider.Context context)
    {
    }

    @Override
    public PrismaPaneRenderState createRenderState()
    {
        return new PrismaPaneRenderState();
    }

    @Override
    public void extractRenderState(PrismaPaneBlockEntity blockEntity, PrismaPaneRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress)
    {
        BlockEntityRenderer.super.extractRenderState(
                blockEntity,
                state,
                partialTicks,
                cameraPosition,
                breakProgress
        );

        state.facing = blockEntity.getBlockState().getValue(PrismaPaneBlock.FACING);
    }

    private static float[] rotateAroundBlockCenter(float x, float z, PrismaPaneBlock.Facing facing)
    {
        float cx = x - 0.5F;
        float cz = z - 0.5F;

        float rx, rz;
        switch (facing)
        {
            case SOUTH ->
            {
                rx = -cx;
                rz = -cz;
            }  // 180°
            case WEST ->
            {
                rx = cz;
                rz = -cx;
            }  // 90°
            case EAST ->
            {
                rx = -cz;
                rz = cx;
            }  // -90°
            default ->
            {
                rx = cx;
                rz = cz;
            }  // NORTH, 0°
        }

        return new float[]{rx + 0.5F, rz + 0.5F};
    }

    private static int[] getVertexColor(double worldX, double worldY, double worldZ, int color1, int color2)
    {
        double time = Util.getMillis() / 60000.0d;

        double value = time
                       + worldX * 0.05F
                       + worldY * 0.05F
                       + worldZ * 0.05F;

        double progress = Mth.abs(Mth.sin(value * Mth.PI));

        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;

        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;

        int r = (int) Mth.lerp(progress, r1, r2);
        int g = (int) Mth.lerp(progress, g1, g2);
        int b = (int) Mth.lerp(progress, b1, b2);

        return new int[]{r, g, b};
    }

    private static final float ATLAS_SIZE = 128.0F;
    private static final float CROP_SIZE = 16.0F;
    private static final float CROP_SCALE = CROP_SIZE / ATLAS_SIZE; // 0.25
    private static final int ATLAS_CELLS = 8; // 64 / 16

    private static float cellOffset(int worldCoord)
    {
        return Math.floorMod(worldCoord, ATLAS_CELLS) * CROP_SCALE;
    }

    private static float fract(float x)
    {
        return x - Mth.floor(x);
    }

    // direction: +1 or -1. speedMs: ms per full cycle.
    private static float drift(float speedMs, float direction)
    {
        if (speedMs <= 0f) return 0f;
        return direction * fract(Util.getMillis() / speedMs);
    }

    private static void submitFace(double offset, RenderType renderType, PoseStack poseStack, SubmitNodeCollector collector,
                                   Vec3 blockCenter, int color1, int color2,
                                   float uBase, float vBase, PrismaPaneBlock.Facing facing, int light)
    {
        double worldX = blockCenter.x - 0.5;
        double worldY = blockCenter.y - 0.5;
        double worldZ = blockCenter.z - 0.5;
        float z = (float) (0.5F + offset);

        collector.submitCustomGeometry(poseStack, renderType, (pose, buffer) ->
        {
            addVertex(pose, buffer, 0.0F, 0.0F, z, worldX, worldY, worldZ, color1, color2, uBase, vBase, facing, light);
            addVertex(pose, buffer, 1.0F, 0.0F, z, worldX, worldY, worldZ, color1, color2, uBase, vBase, facing, light);
            addVertex(pose, buffer, 1.0F, 1.0F, z, worldX, worldY, worldZ, color1, color2, uBase, vBase, facing, light);
            addVertex(pose, buffer, 0.0F, 1.0F, z, worldX, worldY, worldZ, color1, color2, uBase, vBase, facing, light);
        });
    }

    private static void addVertex(PoseStack.Pose pose, VertexConsumer buffer, float x, float y, float z,
                                  double worldX, double worldY, double worldZ,
                                  int color1, int color2, float uBase, float vBase, PrismaPaneBlock.Facing facing, int light)
    {
        float[] rotated = rotateAroundBlockCenter(x, z, facing);


        double vertexWorldX = worldX + rotated[0];
        double vertexWorldY = worldY + y;
        double vertexWorldZ = worldZ + rotated[1];


        int[] color = getVertexColor(vertexWorldX, vertexWorldY, vertexWorldZ, color1, color2);


        float textureX = switch (facing)
        {
            case SOUTH, WEST -> 1.0F - x;
            default -> x;
        };


        float u = uBase + textureX * CROP_SCALE;
        float v = vBase + y * CROP_SCALE;


        buffer.addVertex(pose, x, y, z)
                .setColor(color[0], color[1], color[2], 255)
                .setUv(u, v)
                .setUv1(0, 10)
                .setUv2(light, 0)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public boolean shouldRender(PrismaPaneBlockEntity blockEntity, Vec3 cameraPosition)
    {
        return true;
    }

    @Override
    public void submit(PrismaPaneRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        poseStack.pushPose();

        poseStack.translate(0.5F, 0.0F, 0.5F);

        switch (state.facing)
        {
            case NORTH -> poseStack.mulPose(Axis.YP.rotationDegrees(0F));
            case SOUTH -> poseStack.mulPose(Axis.YP.rotationDegrees(180F));
            case WEST -> poseStack.mulPose(Axis.YP.rotationDegrees(90F));
            case EAST -> poseStack.mulPose(Axis.YP.rotationDegrees(-90F));
        }

        poseStack.translate(-0.5F, 0.0F, -0.3F);

        int color1 = 0xFFbeb0eb;
        int color2 = 0xFF5d8fd4;

        int color21 = 0xFFdecced;
        int color22 = 0xFFd9c0ed;

        int horizontalCoord = switch (state.facing)
        {
            case NORTH, SOUTH -> state.blockPos.getX();
            case WEST, EAST -> state.blockPos.getZ();
            default -> state.blockPos.getX();
        };

        final float LAYER1_V_SPEED = 30000f, LAYER1_V_DIR = -1f;
        final float LAYER1_U_SPEED = 86000f, LAYER1_U_DIR = 1f;

        final float LAYER2_V_SPEED = 44000f, LAYER2_V_DIR = 1f;
        final float LAYER2_U_SPEED = 53000f, LAYER2_U_DIR = 1f;

        final float LAYER3_V_SPEED = 94000f, LAYER3_V_DIR = -1f;
        final float LAYER3_U_SPEED = 93000f, LAYER3_U_DIR = 1f;

        final float LAYER4_V_SPEED = 114000f, LAYER4_V_DIR = -1f;
        final float LAYER4_U_SPEED = 113000f, LAYER4_U_DIR = -1f;

        float uBaseBase = cellOffset(horizontalCoord);
        float vBaseBase = cellOffset(state.blockPos.getY());

        float uBaseLayer1 = uBaseBase + drift(LAYER1_U_SPEED, LAYER1_U_DIR);
        float vBaseLayer1 = vBaseBase + drift(LAYER1_V_SPEED, LAYER1_V_DIR);

        float uBaseLayer2 = uBaseBase + drift(LAYER2_U_SPEED, LAYER2_U_DIR);
        float vBaseLayer2 = vBaseBase + drift(LAYER2_V_SPEED, LAYER2_V_DIR);

        float uBaseLayer3 = uBaseBase + drift(LAYER3_U_SPEED, LAYER3_U_DIR);
        float vBaseLayer3 = vBaseBase + drift(LAYER3_V_SPEED, LAYER3_V_DIR);

        float uBaseLayer4 = uBaseBase + drift(LAYER4_U_SPEED, LAYER4_U_DIR);
        float vBaseLayer4 = vBaseBase + drift(LAYER4_V_SPEED, LAYER4_V_DIR);

        //if iris, use lower light so it doesn't look as bright, otherwise use block light
        int light = ModList.get().isLoaded("iris") && IrisCompat.isShaderPackInUse() ? 0xF000D8 : state.lightCoords;

        submitFace(0, RenderTypes.entityCutout(Echoes.rl("textures/pane_base.png")), poseStack, submitNodeCollector,
                state.blockPos.getCenter(), color1, color2, uBaseBase, vBaseBase, state.facing, light);

        submitFace(0.002d, RenderTypes.entityCutout(Echoes.rl("textures/pane_layer_1.png")), poseStack, submitNodeCollector,
                state.blockPos.getCenter(), color21, color22, uBaseLayer1, vBaseLayer1, state.facing, light);

        submitFace(0.004d, RenderTypes.entityTranslucent(Echoes.rl("textures/pane_layer_2.png")), poseStack, submitNodeCollector,
                state.blockPos.getCenter(), color21, color22, uBaseLayer2, vBaseLayer2, state.facing, light);

        submitFace(0.004d, RenderTypes.entityTranslucent(Echoes.rl("textures/pane_layer_3.png")), poseStack, submitNodeCollector,
                state.blockPos.getCenter(), color21, color22, uBaseLayer3, vBaseLayer3, state.facing, light);

        submitFace(0.004d, RenderTypes.entityTranslucent(Echoes.rl("textures/pane_layer_4.png")), poseStack, submitNodeCollector,
                state.blockPos.getCenter(), color21, color22, uBaseLayer4, vBaseLayer4, state.facing, light);

        poseStack.popPose();
    }

}