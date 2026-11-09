package com.wdiscute.echoes.aoe.donut;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wdiscute.echoes.aoe.AOERenderer;
import com.wdiscute.echoes.compat.IrisCompat;
import com.wdiscute.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class DonutAOERenderer extends AOERenderer<DonutAOE>
{
    @Override
    public void render(DonutAOE aoe, PoseStack ps, MultiBufferSource bufferSource, Vec3 entityPos, float partial, int packedLight)
    {
        ps.pushPose();
        //move up to avoid z fighting
        ps.translate(0, 0.01f, 0);
        PoseStack.Pose pose = ps.last();

        Entity attached = Minecraft.getInstance().level.getEntity(aoe.entityID);
        if (attached != null)
        {
            Vec3 target = attached.getPosition(partial);
            ps.translate(target.x - entityPos.x, target.y - entityPos.y, target.z - entityPos.z);
        }

        VertexConsumer buffer = bufferSource.getBuffer(RenderType.translucent());

        int r = Utils.intToRed(aoe.color);
        int g = Utils.intToGreen(aoe.color);
        int b = Utils.intToBlue(aoe.color);
        int n = aoe.innerRing.size();
        float progress = Mth.clamp((aoe.tickCount + partial) / aoe.duration, 0f, 1f);

        //render background donut
        if(progress < 1)
        {
            int backgroundAlpha = IrisCompat.isShaderPackInUse() ? 172 : 72;
            for (int i = 0; i < n; i++)
            {
                int next = (i + 1) % n;
                Utils.Duo<Float, Float> inA = aoe.innerRing.get(i);
                Utils.Duo<Float, Float> inB = aoe.innerRing.get(next);
                Utils.Duo<Float, Float> outA = aoe.outerRing.get(i);
                Utils.Duo<Float, Float> outB = aoe.outerRing.get(next);

                vertex(buffer, pose, inA.first(), inA.second(), r, g, b, backgroundAlpha, packedLight);
                vertex(buffer, pose, outA.first(), outA.second(), r, g, b, backgroundAlpha, packedLight);
                vertex(buffer, pose, outB.first(), outB.second(), r, g, b, backgroundAlpha, packedLight);
                vertex(buffer, pose, inB.first(), inB.second(), r, g, b, backgroundAlpha, packedLight);
            }
        }

        //move up to avoid z fighting between background and filling
        ps.translate(0, 0.01f, 0);

        //filling donut
        int fadeTicks = 3;
        float fadeProgress = Mth.clamp((aoe.tickCount - aoe.duration - aoe.linger + partial) / (float) fadeTicks, 0f, 1f);
        int alpha = (int) (255 * (1f - fadeProgress));
        for (int i = 0; i < n; i++)
        {
            int next = (i + 1) % n;
            Utils.Duo<Float, Float> inA = aoe.innerRing.get(i);
            Utils.Duo<Float, Float> inB = aoe.innerRing.get(next);
            Utils.Duo<Float, Float> outA = aoe.outerRing.get(i);
            Utils.Duo<Float, Float> outB = aoe.outerRing.get(next);

            float fillAx = Mth.lerp(progress, inA.first(), outA.first());
            float fillAz = Mth.lerp(progress, inA.second(), outA.second());
            float fillBx = Mth.lerp(progress, inB.first(), outB.first());
            float fillBz = Mth.lerp(progress, inB.second(), outB.second());

            vertex(buffer, pose, inA.first(), inA.second(), r, g, b, alpha, packedLight);
            vertex(buffer, pose, fillAx, fillAz, r, g, b, alpha, packedLight);
            vertex(buffer, pose, fillBx, fillBz, r, g, b, alpha, packedLight);
            vertex(buffer, pose, inB.first(), inB.second(), r, g, b, alpha, packedLight);
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
