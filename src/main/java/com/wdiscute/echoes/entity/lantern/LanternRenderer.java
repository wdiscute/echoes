package com.wdiscute.echoes.entity.lantern;

import com.mojang.blaze3d.vertex.PoseStack;
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
        ps.pushPose();

        Identifier rl = Echoes.rl("textures/entity/lantern.png");

        //absolute world position where you want to render
        Vec3 renderPos = state.positionToRenderAt;

        //offset from the normal render position
        Vec3 renderOrigin = new Vec3(
                Mth.lerp(state.partialTick, state.entity.xOld, state.entity.getX()),
                Mth.lerp(state.partialTick, state.entity.yOld, state.entity.getY()),
                Mth.lerp(state.partialTick, state.entity.zOld, state.entity.getZ())
        );

        Vec3 offset = renderPos.subtract(renderOrigin);

        ps.translate(offset.x, offset.y, offset.z);

        node.submitModel(
                lanternModel, state, ps, RenderTypes.entityCutout(rl),
                state.lightCoords, OverlayTexture.NO_OVERLAY,
                -1, null, state.outlineColor, null
        );

        ps.popPose();

        super.submit(state, ps, node, camera);
    }

    public void extractRenderState(LanternEntity entity, LanternRenderState state, float partialTicks)
    {
        super.extractRenderState(entity, state, partialTicks);
        state.positionToRenderAt = lerpPosition(entity);
        state.entity = entity;
    }

    public Vec3 lerpPosition(LanternEntity entity)
    {
        if(entity.lerpedPosition.equals(Vec3.ZERO))
            entity.lerpedPosition = entity.position();


        entity.lerpedPosition = entity.lerpedPosition.lerp(entity.position().add(new Vec3(0.7f, 0.8, 0.7f)), 0.03f);

        return entity.lerpedPosition;
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