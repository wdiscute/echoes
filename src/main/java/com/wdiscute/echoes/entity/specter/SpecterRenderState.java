package com.wdiscute.echoes.entity.specter;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.phys.Vec3;

public class SpecterRenderState extends EntityRenderState
{
    public Vec3 renderPosition;
    public Vec3 networkPosition;
    float scale;

    public float yrot;
    public float xrot;

    public boolean shouldRender;
}
