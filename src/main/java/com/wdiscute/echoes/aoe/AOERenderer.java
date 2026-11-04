package com.wdiscute.echoes.aoe;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;

public abstract class AOERenderer<T extends AbstractAOE>
{
    public abstract void render(T aoe, PoseStack ps, MultiBufferSource bufferSource, Vec3 entityPos, float partial, int packedLight);
}
