package com.wdiscute.echoes.entity.corpse;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wdiscute.echoes.Echoes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.item.ItemDisplayContext;

public class TimelessCorpseRenderer extends EntityRenderer<TimelessCorpseEntity, TimelessCorpseRenderState>
{
    final TimelessCorpseModel model;
    final TimelessCorpseModelSlim modelSlim;
    private final ItemModelResolver itemModelResolver;

    public TimelessCorpseRenderer(EntityRendererProvider.Context context)
    {
        super(context);
        itemModelResolver = context.getItemModelResolver();
        model = new TimelessCorpseModel(context.bakeLayer(TimelessCorpseModel.LAYER_LOCATION));
        modelSlim = new TimelessCorpseModelSlim(context.bakeLayer(TimelessCorpseModelSlim.LAYER_LOCATION));
    }

    @Override
    public void submit(TimelessCorpseRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        super.submit(state, poseStack, submitNodeCollector, camera);

        poseStack.mulPose(Axis.YP.rotationDegrees(state.rot));

        poseStack.translate(0, 1.7, 0);
        poseStack.scale(-1.0F, -1.0F, 1.0F);

        //render player model with local skin
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null)
            submitNodeCollector.submitModel(state.isSlim ? modelSlim : model,
                    state,
                    poseStack,
                    player.getSkin().body().texturePath(),
                    state.lightCoords,
                    OverlayTexture.NO_OVERLAY,
                    state.outlineColor,
                    null);

        //sculk overlay
        poseStack.scale(1.0001f, 1f, 1.001f);
        poseStack.translate(-0.0001f, 0f, -0.001f);
        submitNodeCollector.submitModel(state.isSlim ? modelSlim : model,
                state,
                poseStack,
                Echoes.rl("textures/entity/corpse_sculk_overlay.png"),
                state.lightCoords,
                OverlayTexture.NO_OVERLAY,
                state.outlineColor,
                null);

        //render sword
        if (!state.stack.isEmpty())
        {
            poseStack.pushPose();

            poseStack.scale(0.7f, 0.7f, 0.7f);
            poseStack.translate(-0.3f, 1.4f, -0.3f);

            poseStack.mulPose(Axis.YP.rotationDegrees(120));
            poseStack.mulPose(Axis.XP.rotationDegrees(30));
            poseStack.mulPose(Axis.ZP.rotationDegrees(30));

            state.item.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

            poseStack.popPose();
        }
    }

    @Override
    public void extractRenderState(TimelessCorpseEntity entity, TimelessCorpseRenderState state, float partialTicks)
    {
        super.extractRenderState(entity, state, partialTicks);
        if (Minecraft.getInstance().player != null)
            state.isSlim = Minecraft.getInstance().player.getSkin().model().equals(PlayerModelType.SLIM);

        state.stack = entity.getStack();
        state.rot = entity.getYRot();

        this.itemModelResolver.updateForNonLiving(state.item, state.stack, ItemDisplayContext.FIXED, Minecraft.getInstance().player);

    }

    @Override
    public TimelessCorpseRenderState createRenderState()
    {
        return new TimelessCorpseRenderState();
    }
}