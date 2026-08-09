package com.wdiscute.echoes.entity.soul;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class SoulRenderer extends EntityRenderer<SoulEntity, SoulRenderState>
{
    public SoulModel soul;

    public SoulRenderer(EntityRendererProvider.Context context)
    {
        super(context);
        soul = new SoulModel(context.getModelSet().bakeLayer(SoulModel.LAYER_LOCATION));
    }

    @Override
    public void submit(SoulRenderState state, PoseStack ps, SubmitNodeCollector node, CameraRenderState camera)
    {
        super.submit(state, ps, node, camera);

        if(state.velocity.equals(Vec3.ZERO))
            return;

        ps.pushPose();

        ps.translate(state.offset);

        Vec3 dir = state.velocity.normalize();

        float yaw = (float) Math.toDegrees(Math.atan2(dir.x, dir.z));
        float pitch = (float) -Math.toDegrees(Math.asin(dir.y));

        ps.mulPose(Axis.YP.rotationDegrees(yaw));
        ps.mulPose(Axis.XP.rotationDegrees(pitch));

        ps.translate(0f, -1f, -0.22f);
        node.submitModel(
                soul, state, ps, RenderTypes.entityTranslucent(SoulModel.TEXTURE_LOCATION),
                state.lightCoords, OverlayTexture.NO_OVERLAY,
                -1, null, state.outlineColor, null
        );

        ps.popPose();
    }

    @Override
    public boolean shouldRender(SoulEntity entity, Frustum culler, double camX, double camY, double camZ)
    {
        return true;
    }

    @Override
    protected boolean affectedByCulling(SoulEntity entity)
    {
        return false;
    }

    @Override
    public void extractRenderState(SoulEntity entity, SoulRenderState state, float partialTicks)
    {
        super.extractRenderState(entity, state, partialTicks);

        state.offset = Vec3.ZERO;
        state.velocity = Vec3.ZERO;

        if (entity.level().getPlayerByUUID(entity.getEntityData().get(SoulEntity.UUID)) instanceof Player player)
        {
            if (entity.positionToRender == null)
                entity.setPosition();

            if (entity.velocity == null)
                entity.setVelocity();

            Vec3 toTarget = player.position().subtract(entity.positionToRender);
            Vec3 desiredVelocity = toTarget.normalize().scale(entity.speed);

            Vec3 delta = desiredVelocity.subtract(entity.velocity);

            double deltaLength = delta.length();

            if (deltaLength > entity.turnRate)
                delta = delta.scale(entity.turnRate / deltaLength);

            var fakeVelocity = entity.velocity.add(delta).scale(partialTicks);

            var fakePos = entity.positionToRender.add(fakeVelocity);

            state.velocity = fakeVelocity;
            state.offset = fakePos.subtract(entity.position());
        }
    }

    public SoulRenderState createRenderState()
    {
        return new SoulRenderState();
    }
}