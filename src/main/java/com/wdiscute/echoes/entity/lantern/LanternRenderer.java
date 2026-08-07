package com.wdiscute.echoes.entity.lantern;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wdiscute.echoes.Echoes;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class LanternRenderer extends EntityRenderer<LanternEntity, LanternRenderState>
{
    public LanternModel lanternModel;

    public LanternRenderer(EntityRendererProvider.Context context)
    {
        super(context);
        lanternModel = new LanternModel(context.getModelSet().bakeLayer(LanternModel.LAYER_LOCATION));
    }

    public void submit(LanternRenderState state, PoseStack ps, SubmitNodeCollector node, CameraRenderState camera)
    {
        super.submit(state, ps, node, camera);

        ps.pushPose();

        double t = (System.nanoTime() + state.timeOffset) / 1_000_000_000.0;

        //bs math for wiggles straight out of chat ptbgtpbg
        double p1 = state.timeOffset * 0.000000011;
        double p2 = state.timeOffset * 0.000000017;
        double p3 = state.timeOffset * 0.000000023;

        double x = Math.sin(t * 0.80 + p1) * 0.043 + Math.sin(t * 1.60 + p2) * 0.015 + Math.sin(t * 2.91 + p3) * 0.005;
        double y = Math.sin(t * 1.10 + p2) * 0.021 + Math.sin(t * 2.40 + p1) * 0.006 + Math.cos(t * 3.63 + p3) * 0.003;
        double z = Math.cos(t * 0.70 + p3) * 0.034 + Math.sin(t * 1.30 + p1) * 0.011 + Math.sin(t * 2.74 + p2) * 0.004;

        ps.translate(state.offset.add(x, y * 2, z));

        ps.mulPose(Axis.XP.rotationDegrees((float)(Math.sin(t * 0.65 + p1) * 1.25 + Math.sin(t * 1.45 + p2) * 0.35 + Math.cos(t * 2.61 + p3) * 0.12)));
        ps.mulPose(Axis.ZP.rotationDegrees((float)(Math.cos(t * 0.75 + p2) * 1.10 + Math.sin(t * 1.20 + p3) * 0.30 + Math.sin(t * 2.48 + p1) * 0.10)));
        ps.mulPose(Axis.YP.rotationDegrees((float)(Math.sin(t * 0.40 + p3) * 1.75 + Math.sin(t * 1.82 + p1) * 0.20)));

        node.submitModel(
                lanternModel, state, ps, RenderTypes.entityCutout(LanternModel.TEXTURE_LOCATION),
                state.lightCoords, OverlayTexture.NO_OVERLAY,
                -1, null, state.outlineColor, null
        );

        ps.popPose();
    }

    public void extractRenderState(LanternEntity entity, LanternRenderState state, float partialTicks)
    {
        super.extractRenderState(entity, state, partialTicks);

        Entity attachedEntity = entity.level().getEntity(entity.getEntityData().get(LanternEntity.UUID));

        state.timeOffset = entity.renderTimeOffset;

        if(attachedEntity == null)
        {
            state.offset = Vec3.ZERO;
            return;
        }

        if(attachedEntity instanceof Player)
        {
            Vec3 direction = new Vec3(
                    Math.sin(Math.toRadians(-attachedEntity.yRotO - 45)), // x
                    0,                 // y
                    Math.cos(Math.toRadians(-attachedEntity.yRotO - 45))  // z
            );

            state.offset = entity.renderOffset
                    .add(direction.multiply(1.6f, 1, 1.6f)).subtract(entity.position());
        }
        else
        {
            state.offset = entity.renderOffset.add(0, 0.8f, 0).subtract(entity.position());
        }
    }

    @Override
    protected boolean affectedByCulling(LanternEntity entity)
    {
        return false;
    }

    @Override
    public boolean shouldRender(LanternEntity entity, Frustum culler, double camX, double camY, double camZ)
    {
        return true;
    }

    public LanternRenderState createRenderState()
    {
        return new LanternRenderState();
    }
}