package com.wdiscute.echoes.blocks.display;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class DisplayRenderer implements BlockEntityRenderer<DisplayBlockEntity>
{
    public DisplayRenderer(BlockEntityRendererProvider.Context context)
    {
    }

    @Override
    public void render(DisplayBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay)
    {
        //do not render anything if there's no trade
        if(blockEntity.trade == null)
            return;

        ItemStack stack = blockEntity.trade.first().item().toStack();

        if (stack.isEmpty())
            return;

        Direction facing = blockEntity.getBlockState().getOptionalValue(DisplayBlock.FACING).orElse(Direction.NORTH);

        float rotationOffset = switch (facing)
        {
            case NORTH -> 0.0F;
            case SOUTH -> 180.0F;
            case WEST -> 90.0F;
            case EAST -> 270.0F;
            default -> 0.0F;
        };

        Minecraft minecraft = Minecraft.getInstance();

        poseStack.pushPose();

        poseStack.scale(0.7F, 0.7F, 0.7F);

        poseStack.translate(
                0.7F,
                1.7F + (Math.sin(Util.getMillis() / 555.0F) / 60.0F),
                0.7F
        );

        poseStack.translate(0.0F, 0.4F, 0.0F);

        float x = (float) (Math.sin(Util.getMillis() / 2000.0F + 323.0F) * 20.0F);
        float y = (float) (Math.sin(Util.getMillis() / 2000.0F) * 20.0F);

        poseStack.mulPose(Axis.XP.rotationDegrees(x));
        poseStack.mulPose(Axis.YP.rotationDegrees(y + rotationOffset));

        poseStack.mulPose(Axis.ZP.rotationDegrees((float) (Math.toRadians(Util.getMillis() % 360.0F) / 600.0F)));

        minecraft.getItemRenderer().renderStatic(
                stack,
                ItemDisplayContext.FIXED,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,
                minecraft.level,
                0
        );

        poseStack.popPose();
    }
}