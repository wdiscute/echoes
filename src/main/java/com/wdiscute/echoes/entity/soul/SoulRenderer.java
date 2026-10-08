package com.wdiscute.echoes.entity.soul;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class SoulRenderer extends EntityRenderer<SoulEntity>
{
    public SoulModel soul;

    public SoulRenderer(EntityRendererProvider.Context context)
    {
        super(context);
        soul = new SoulModel(context.getModelSet().bakeLayer(SoulModel.LAYER_LOCATION));
    }

    @Override
    public void render(SoulEntity entity, float entityYaw, float partialTick, PoseStack ps, MultiBufferSource bufferSource, int packedLight)
    {
        super.render(entity, entityYaw, partialTick, ps, bufferSource, packedLight);

        Vec3 offset = Vec3.ZERO;
        Vec3 velocity = Vec3.ZERO;

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

            var fakeVelocity = entity.velocity.add(delta).scale(partialTick);

            var fakePos = entity.positionToRender.add(fakeVelocity);

            velocity = fakeVelocity;
            offset = fakePos.subtract(entity.position());
        }

        if(velocity.equals(Vec3.ZERO))
            return;

        ps.pushPose();

        ps.translate(offset.x, offset.y, offset.z);

        Vec3 dir = velocity.normalize();

        float yaw = (float) Math.toDegrees(Math.atan2(dir.x, dir.z));
        float pitch = (float) -Math.toDegrees(Math.asin(dir.y));

        ps.mulPose(Axis.YP.rotationDegrees(yaw));
        ps.mulPose(Axis.XP.rotationDegrees(pitch));

        ps.translate(0f, -1f, -0.22f);
        soul.renderToBuffer(ps, bufferSource.getBuffer(RenderType.entityTranslucent(SoulModel.TEXTURE_LOCATION)),
                0x00ffffff, OverlayTexture.NO_OVERLAY, -1);

        ps.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(SoulEntity entity)
    {
        return SoulModel.TEXTURE_LOCATION;
    }

    @Override
    public boolean shouldRender(SoulEntity entity, Frustum culler, double camX, double camY, double camZ)
    {
        return true;
    }
}