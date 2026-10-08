package com.wdiscute.echoes.entity.corpse;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.wdiscute.echoes.Echoes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;

public class TimelessCorpseRenderer extends EntityRenderer<TimelessCorpseEntity>
{
    final TimelessCorpseModel model;
    final TimelessCorpseModelSlim modelSlim;

    public TimelessCorpseRenderer(EntityRendererProvider.Context context)
    {
        super(context);
        model = new TimelessCorpseModel(context.bakeLayer(TimelessCorpseModel.LAYER_LOCATION));
        modelSlim = new TimelessCorpseModelSlim(context.bakeLayer(TimelessCorpseModelSlim.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getTextureLocation(TimelessCorpseEntity entity)
    {
        return Echoes.MISSINGNO;
    }

    @Override
    public void render(TimelessCorpseEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight)
    {
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);

        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYRot()));

        poseStack.translate(0, 1.7, 0);
        poseStack.scale(-1.0F, -1.0F, 1.0F);

        //render player model with local skin
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null)
        {
            var model = Minecraft.getInstance().player.getSkin().model().equals(PlayerSkin.Model.SLIM) ? modelSlim : this.model;
            {
                VertexConsumer vertexConsumer = buffer.getBuffer(model.renderType(Minecraft.getInstance().player.getSkin().texture()));

                model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
            }

            //sculk overlay
            {
                poseStack.scale(1.0001f, 1f, 1.001f);
                poseStack.translate(-0.0001f, 0f, -0.001f);
                VertexConsumer vertexConsumer = buffer.getBuffer(model.renderType(Echoes.rl("textures/entity/corpse_sculk_overlay.png")));
                model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
            }

            //render sword
            if (!entity.getStack().isEmpty())
            {
                poseStack.pushPose();

                poseStack.scale(0.7f, 0.7f, 0.7f);
                poseStack.translate(-0.3f, 1.4f, -0.3f);

                poseStack.mulPose(Axis.YP.rotationDegrees(120));
                poseStack.mulPose(Axis.XP.rotationDegrees(30));
                poseStack.mulPose(Axis.ZP.rotationDegrees(30));

                Minecraft.getInstance().getItemRenderer().renderStatic(
                        entity.getStack(),
                        ItemDisplayContext.FIXED,
                        packedLight,
                        OverlayTexture.NO_OVERLAY,
                        poseStack,
                        buffer,
                        Minecraft.getInstance().level,
                        0
                );

                poseStack.popPose();
            }
        }
    }
}