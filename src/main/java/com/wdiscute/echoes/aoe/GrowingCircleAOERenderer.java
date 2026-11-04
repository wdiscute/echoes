package com.wdiscute.echoes.aoe;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wdiscute.echoes.compat.IrisCompat;
import com.wdiscute.utils.Utils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class GrowingCircleAOERenderer extends AOERenderer<GrowingCircleAOE>
{
    @Override
    public void render(GrowingCircleAOE aoe, PoseStack ps, MultiBufferSource bufferSource, Vec3 entityPos, float partial, int packedLight)
    {
        ps.pushPose();
        if (!aoe.absolutePosition.equals(Vec3.ZERO))
            ps.translate(aoe.absolutePosition.x - entityPos.x, aoe.absolutePosition.y - entityPos.y, aoe.absolutePosition.z - entityPos.z);
        //move 0.1f up to avoid z fighting
        ps.translate(0, 0.01f, 0);
        PoseStack.Pose pose = ps.last();

        VertexConsumer buffer = bufferSource.getBuffer(RenderType.translucent());

        //render full circle
        for (int i = 0; i < aoe.fullCircle.size(); i++)
        {
            Utils.Duo<Float, Float> a = aoe.fullCircle.get(i);
            Utils.Duo<Float, Float> b = aoe.fullCircle.get((i + 1) % aoe.fullCircle.size());

            for (Utils.Duo<Float, Float> vec : List.of(AbstractAOE.CENTER, a, b, b))
            {
                buffer.addVertex(pose, vec.first(), 0f, vec.second())
                        .setColor(Utils.intToRed(aoe.color), Utils.intToGreen(aoe.color), Utils.intToBlue(aoe.color), IrisCompat.isShaderPackInUse() ? 172 : 72)
                        .setUv(0, 0)
                        .setOverlay(OverlayTexture.NO_OVERLAY)
                        .setLight(packedLight)
                        .setNormal(pose, 0f, 1f, 0f);
            }
        }

        float progress = Mth.clamp((aoe.entity.tickCount + partial - aoe.startTick) / aoe.ticksUntilFull, 0f, 1f);
        final List<Utils.Duo<Float, Float>> fillingCircle = aoe.fullCircle.stream().map(o -> new Utils.Duo<>(o.first() * progress, o.second() * progress)).toList();
        //move 0.1f up to avoid z fighting
        ps.translate(0, 0.01f, 0);
        for (int i = 0; i < fillingCircle.size(); i++)
        {
            Utils.Duo<Float, Float> a = fillingCircle.get(i);
            Utils.Duo<Float, Float> b = fillingCircle.get((i + 1) % fillingCircle.size());

            for (Utils.Duo<Float, Float> vec : List.of(AbstractAOE.CENTER, a, b, b))
            {
                buffer.addVertex(pose, vec.first(), 0f, vec.second())
                        .setColor(Utils.intToRed(aoe.color), Utils.intToGreen(aoe.color), Utils.intToBlue(aoe.color), 255)
                        .setUv(0, 0)
                        .setOverlay(OverlayTexture.NO_OVERLAY)
                        .setLight(packedLight)
                        .setNormal(pose, 0f, 1f, 0f);
            }
        }

        ps.popPose();

    }
}
