package com.wdiscute.echoes.blocks.marker;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.ClientHooks;
import org.joml.Matrix4f;

public class TimelessMarkerRenderer implements BlockEntityRenderer<TimelessMarkerBlockEntity>
{
    public TimelessMarkerRenderer(BlockEntityRendererProvider.Context context)
    {
    }

    @Override
    public void render(TimelessMarkerBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay)
    {
        Component text = Component.literal(
                blockEntity.getBlockState().getOptionalValue(TimelessMarkerBlock.TYPE)
                        .orElse(TimelessMarkerBlock.Type.SPAWN_POINT).getSerializedName());

        poseStack.pushPose();
        poseStack.translate(0.5D, 1.0D, 0.5D);

        renderNameTag(
                Minecraft.getInstance().player,
                text,
                poseStack,
                bufferSource,
                15728880,
                partialTick
        );

        poseStack.popPose();
    }

    protected void renderNameTag(Player entity, Component displayName, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, float partialTick)
    {
        LocalPlayer player = Minecraft.getInstance().player;
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        if(player == null || dispatcher == null)
            return;

        double d0 = dispatcher.distanceToSqr(entity);
        if (ClientHooks.isNameplateInRenderDistance(entity, d0))
        {
            Vec3 vec3 = entity.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, entity.getViewYRot(partialTick));
            if (vec3 != null)
            {
                boolean flag = !entity.isDiscrete();
                int i = "deadmau5".equals(displayName.getString()) ? -10 : 0;
                poseStack.pushPose();
                poseStack.translate(vec3.x, vec3.y + 0.5, vec3.z);
                poseStack.mulPose(dispatcher.cameraOrientation());
                poseStack.scale(0.025F, -0.025F, 0.025F);
                Matrix4f matrix4f = poseStack.last().pose();
                float f = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
                int j = (int) (f * 255.0F) << 24;
                Font font = Minecraft.getInstance().font;
                float f1 = (float) (-font.width(displayName) / 2);
                font.drawInBatch(
                        displayName, f1, (float) i, 553648127, false, matrix4f, bufferSource, flag ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, j, packedLight
                );
                if (flag)
                {
                    font.drawInBatch(displayName, f1, (float) i, -1, false, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, packedLight);
                }

                poseStack.popPose();
            }
        }
    }
}
