package com.wdiscute.echoes.blocks.portal;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.compat.IrisCompat;
import com.wdiscute.echoes.registry.ECRenderPipelines;
import com.wdiscute.utils.Utils;
import com.wdiscute.utils.screen.ScreenUtils;
import net.irisshaders.iris.api.v0.IrisApi;
import net.minecraft.client.renderer.FaceInfo;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PortalRenderer implements BlockEntityRenderer<PortalBlockEntity, PortalRenderState>
{
    public PortalRenderer(BlockEntityRendererProvider.Context context)
    {
    }

    public static final Identifier SKY = Echoes.rl("textures/environment/end_sky.png");
    public static final Identifier PORTAL_TEXTURE = Echoes.rl("textures/environment/end_portal.png");

    private static final RenderType PORTAL = RenderType.create(
            "end_portal",
            RenderSetup.builder(ECRenderPipelines.PORTAL)
                    .withTexture("Sampler0", SKY)
                    .withTexture("Sampler1", PORTAL_TEXTURE)
                    .createRenderSetup()
    );

    @Override
    public void submit(PortalRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        //poseStack.pushPose();
        //poseStack.translate(0, 1, 0);
        //poseStack.scale(1, 2, 1);
        //submitSpecial(PORTAL, poseStack, submitNodeCollector);
        //poseStack.popPose();
//
        //poseStack.pushPose();
        //poseStack.translate(
        //        Utils.smooth(0, 0.3f, 15),
        //        Utils.smooth(1.3f, 1.5f, 36),
        //        Utils.smooth(1.0f, 1.5f, 63)
        //);
        //poseStack.scale(0.1f, 0.5f, 0.1f);
        //submitSpecial(PORTAL, poseStack, submitNodeCollector);
        //poseStack.popPose();
    }

    @Override
    public PortalRenderState createRenderState()
    {
        return new PortalRenderState();
    }

    @Override
    public void extractRenderState(PortalBlockEntity blockEntity, PortalRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress)
    {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.pos = blockEntity.getBlockPos().getCenter();
    }

    private static final Vector3fc FROM = new Vector3f(0.0F, 0.0F, 0.0F);
    private static final Vector3fc TO = new Vector3f(1.0F, 1.0F, 1.0F);
    private static final Map<Direction, List<Vector3fc>> FACES = Util.makeEnumMap(
            Direction.class,
            direction ->
            {
                FaceInfo faceInfo = FaceInfo.fromFacing(direction);
                return List.of(
                        faceInfo.getVertexInfo(0).select(FROM, TO),
                        faceInfo.getVertexInfo(1).select(FROM, TO),
                        faceInfo.getVertexInfo(2).select(FROM, TO),
                        faceInfo.getVertexInfo(3).select(FROM, TO)
                );
            }
    );

    protected static void submitCube(Collection<Direction> facesToShow, RenderType renderType, PoseStack poseStack, SubmitNodeCollector submitNodeCollector)
    {
        if (!facesToShow.isEmpty())
        {
            if (ModList.get().isLoaded("iris"))
                if (IrisCompat.isShaderPackInUse()) return;

            submitNodeCollector.submitCustomGeometry(poseStack, renderType, (pose, buffer) ->
            {
                for (Direction direction : facesToShow)
                {
                    for (Vector3fc faceVertex : FACES.get(direction))
                    {
                        buffer.addVertex(pose, faceVertex);
                    }
                }
            });
        }
    }

    private static final List<Direction> ALL_FACES = List.of(Direction.values());

    public static void submitSpecial(RenderType renderType, PoseStack poseStack, SubmitNodeCollector submitNodeCollector)
    {
        submitCube(ALL_FACES, renderType, poseStack, submitNodeCollector);
    }
}
