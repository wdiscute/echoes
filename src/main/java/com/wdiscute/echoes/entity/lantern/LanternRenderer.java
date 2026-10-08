package com.wdiscute.echoes.entity.lantern;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class LanternRenderer extends EntityRenderer<LanternEntity>
{
    public LanternModel lanternModel;

    public LanternRenderer(EntityRendererProvider.Context context)
    {
        super(context);
        lanternModel = new LanternModel(context.getModelSet().bakeLayer(LanternModel.LAYER_LOCATION));
    }

    @Override
    public void render(LanternEntity entity, float entityYaw, float partialTick, PoseStack ps, MultiBufferSource bufferSource, int packedLight)
    {
        super.render(entity, entityYaw, partialTick, ps, bufferSource, packedLight);

        ps.pushPose();

        Entity attachedEntity;
        if (entity.level() instanceof ServerLevel sl)
            attachedEntity = sl.getEntity(entity.getEntityData().get(LanternEntity.UUID));
        else
            attachedEntity = entity.level().getEntities((Entity) null,
                    new AABB(entity.blockPosition()).inflate(1000), (p) -> p.getUUID().equals(entity.getEntityData().get(LanternEntity.UUID))).stream().findAny().orElse(null);

        Vec3 offset;

        if(attachedEntity instanceof Player)
        {
            Vec3 direction = new Vec3(
                    Math.sin(Math.toRadians(-attachedEntity.yRotO - 45)), // x
                    0,                 // y
                    Math.cos(Math.toRadians(-attachedEntity.yRotO - 45))  // z
            );

            offset = entity.renderOffset
                    .add(direction.multiply(1.6f, 1, 1.6f)).subtract(entity.position());
        }
        else
        {
            offset = entity.renderOffset.add(0, 0f, 0).subtract(entity.position());
        }

        double t = (System.nanoTime() + entity.renderTimeOffset) / 1_000_000_000.0;

        //bs math for wiggles straight out of chat ptbgtpbg
        double p1 = entity.renderTimeOffset * 0.000000011;
        double p2 = entity.renderTimeOffset * 0.000000017;
        double p3 = entity.renderTimeOffset * 0.000000023;

        double x = Math.sin(t * 0.80 + p1) * 0.043 + Math.sin(t * 1.60 + p2) * 0.015 + Math.sin(t * 2.91 + p3) * 0.005;
        double y = Math.sin(t * 1.10 + p2) * 0.021 + Math.sin(t * 2.40 + p1) * 0.006 + Math.cos(t * 3.63 + p3) * 0.003;
        double z = Math.cos(t * 0.70 + p3) * 0.034 + Math.sin(t * 1.30 + p1) * 0.011 + Math.sin(t * 2.74 + p2) * 0.004;

        Vec3 p = offset.add(x, y * 2, z);
        ps.translate(p.x, p.y, p.z);

        ps.mulPose(Axis.XP.rotationDegrees((float)(Math.sin(t * 0.65 + p1) * 1.25 + Math.sin(t * 1.45 + p2) * 0.35 + Math.cos(t * 2.61 + p3) * 0.12)));
        ps.mulPose(Axis.ZP.rotationDegrees((float)(Math.cos(t * 0.75 + p2) * 1.10 + Math.sin(t * 1.20 + p3) * 0.30 + Math.sin(t * 2.48 + p1) * 0.10)));
        ps.mulPose(Axis.YP.rotationDegrees((float)(Math.sin(t * 0.40 + p3) * 1.75 + Math.sin(t * 1.82 + p1) * 0.20)));

        lanternModel.renderToBuffer(ps, bufferSource.getBuffer(RenderType.entityCutoutNoCull(LanternModel.TEXTURE_LOCATION)),
                packedLight, OverlayTexture.NO_OVERLAY, -1);

        ps.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(LanternEntity entity)
    {
        return LanternModel.TEXTURE_LOCATION;
    }

    @Override
    public boolean shouldRender(LanternEntity entity, Frustum culler, double camX, double camY, double camZ)
    {
        return true;
    }
}