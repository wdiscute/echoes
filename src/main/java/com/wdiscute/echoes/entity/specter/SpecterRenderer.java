package com.wdiscute.echoes.entity.specter;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wdiscute.echoes.entity.trader.SoulTraderModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.checkerframework.checker.nullness.qual.NonNull;

public class SpecterRenderer extends EntityRenderer<SpecterEntity>
{
    public SpecterModel soulTraderModel;

    public SpecterRenderer(EntityRendererProvider.Context context)
    {
        super(context);

        soulTraderModel = new SpecterModel(
                context.getModelSet().bakeLayer(SpecterModel.LAYER_LOCATION)
        );
    }

    @Override
    public void render(SpecterEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight)
    {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        LocalPlayer player = Minecraft.getInstance().player;

        boolean shouldRender = true;

        if (player != null
            && entity.getEntityData().get(SpecterEntity.PLAYER_UUID).equals(player.getUUID())
            && Minecraft.getInstance().options.getCameraType().isFirstPerson()
        )
        {
            return;
        }

        float scale = (float) Math.min((entity.tickCount + partialTick) / 20.0, 1.0);

        Vec3 renderPosition = entity.getRenderPosition(partialTick);
        Vec3 networkPosition = entity.getNetworkPosition(partialTick);

        if (!shouldRender)
            return;

        Vec3 offset = entity.renderPosition.subtract(networkPosition);

        poseStack.translate(offset.x, offset.y, offset.z);

        poseStack.translate(0, 3.5, 0);

        double t = System.nanoTime() / 1_000_000_000.0;
        double y = Math.sin(t * 1.10);

        poseStack.translate(0, y / 5 - 1.1, 0);

        poseStack.scale(scale, scale, scale);

        poseStack.mulPose(Axis.XP.rotationDegrees(180));

        soulTraderModel.renderToBuffer(
                poseStack,
                bufferSource.getBuffer(RenderType.entityTranslucent(SoulTraderModel.TEXTURE_LOCATION)),
                packedLight,
                OverlayTexture.NO_OVERLAY,
                -1
        );
    }

    @Override
    public ResourceLocation getTextureLocation(SpecterEntity entity)
    {
        return SoulTraderModel.TEXTURE_LOCATION;
    }
}