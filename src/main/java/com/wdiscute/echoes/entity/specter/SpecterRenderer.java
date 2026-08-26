package com.wdiscute.echoes.entity.specter;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wdiscute.echoes.entity.trader.SoulTraderModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec3;

public class SpecterRenderer extends EntityRenderer<SpecterEntity, SpecterRenderState>
{
    public SpecterModel soulTraderModel;

    public SpecterRenderer(EntityRendererProvider.Context context)
    {
        super(context);
        soulTraderModel = new SpecterModel(context.getModelSet().bakeLayer(SpecterModel.LAYER_LOCATION));
    }

    @Override
    public void submit(SpecterRenderState state, PoseStack poseStack, SubmitNodeCollector node, CameraRenderState camera)
    {
        super.submit(state, poseStack, node, camera);
        if (!state.shouldRender) return;
        poseStack.translate(0, 3.5, 0);

        double t = (System.nanoTime()) / 1_000_000_000.0;
        double y = Math.sin(t * 1.10);
        poseStack.translate(new Vec3(0, y / 5, 0));

        poseStack.scale(2, 2, 2);

        poseStack.mulPose(Axis.XP.rotationDegrees(180));

        poseStack.mulPose(Axis.YP.rotationDegrees(state.yrot));

        node.submitModel(
                soulTraderModel, state, poseStack, RenderTypes.entityTranslucent(SoulTraderModel.TEXTURE_LOCATION),
                state.lightCoords, OverlayTexture.NO_OVERLAY,
                -1, null, state.outlineColor, null
        );
    }

    @Override
    public SpecterRenderState createRenderState()
    {
        return new SpecterRenderState();
    }

    @Override
    public void extractRenderState(SpecterEntity entity, SpecterRenderState state, float partialTicks)
    {
        super.extractRenderState(entity, state, partialTicks);

        LocalPlayer player = Minecraft.getInstance().player;

        state.yrot = entity.yRotO;

        if (player != null)
        {
            if (entity.getEntityData().get(SpecterEntity.PLAYER_UUID).equals(player.getUUID()))
                state.shouldRender = false;
        }
    }
}
