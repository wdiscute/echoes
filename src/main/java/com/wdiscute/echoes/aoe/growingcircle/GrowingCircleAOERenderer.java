package com.wdiscute.echoes.aoe.growingcircle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wdiscute.echoes.aoe.AOERenderer;
import com.wdiscute.echoes.aoe.AbstractAOE;
import com.wdiscute.echoes.compat.IrisCompat;
import com.wdiscute.utils.Utils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GrowingCircleAOERenderer extends AOERenderer<GrowingCircleAOE>
{
    @Override
    public void render(GrowingCircleAOE aoe, PoseStack ps, MultiBufferSource bufferSource, Vec3 entityPos, float partial, int packedLight)
    {
        ps.pushPose();
        //move 0.1f up to avoid z fighting
        ps.translate(0, 0.01f, 0);
        PoseStack.Pose pose = ps.last();

        float progress = Mth.clamp((aoe.tickCount + partial) / aoe.duration, 0f, 1f);
        int r = Utils.intToRed(aoe.color);
        int g = Utils.intToGreen(aoe.color);
        int b = Utils.intToBlue(aoe.color);

        VertexConsumer buffer = bufferSource.getBuffer(RenderType.translucent());

        //render full circle
        if (progress < 1)
        {
            for (int i = 0; i < aoe.fullCircle.size(); i++)
            {
                Utils.Duo<Float, Float> a = aoe.fullCircle.get(i);
                Utils.Duo<Float, Float> a2 = aoe.fullCircle.get((i + 1) % aoe.fullCircle.size());

                for (Utils.Duo<Float, Float> vec : List.of(AbstractAOE.CENTER, a, a2, a2))
                {
                    buffer.addVertex(pose, vec.first(), 0f, vec.second())
                            .setColor(r, g, b, IrisCompat.isShaderPackInUse() ? 172 : 72)
                            .setUv(0, 0)
                            .setOverlay(OverlayTexture.NO_OVERLAY)
                            .setLight(packedLight)
                            .setNormal(pose, 0f, 1f, 0f);
                }
            }
        }

        int fadeTicks = 3;
        float fadeProgress = Mth.clamp((aoe.tickCount - aoe.duration + partial) / (float) fadeTicks, 0f, 1f);
        int alpha = (int) (255 * (1f - fadeProgress));

        ps.translate(0, 0.01f, 0);

        int n = aoe.fullCircle.size();
        for (int i = 0; i < n; i++)
        {
            Utils.Duo<Float, Float> a = aoe.fullCircle.get(i);
            Utils.Duo<Float, Float> next = aoe.fullCircle.get((i + 1) % n);

            float ax = a.first() * progress;
            float az = a.second() * progress;
            float bx = next.first() * progress;
            float bz = next.second() * progress;

            vertex(buffer, pose, AbstractAOE.CENTER.first(), AbstractAOE.CENTER.second(), r, g, b, alpha, packedLight);
            vertex(buffer, pose, ax, az, r, g, b, alpha, packedLight);
            vertex(buffer, pose, bx, bz, r, g, b, alpha, packedLight);
            vertex(buffer, pose, bx, bz, r, g, b, alpha, packedLight); // quad format needs 4 verts, so repeat the last
        }

        ps.popPose();
    }

    private static void vertex(VertexConsumer buffer, PoseStack.Pose pose, float x, float z, int r, int g, int b, int a, int packedLight)
    {
        buffer.addVertex(pose, x, 0f, z)
                .setColor(r, g, b, a)
                .setUv(0, 0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(pose, 0f, 1f, 0f);
    }
}
