package com.wdiscute.echoes.entity.specter;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.phys.Vec3;

public class SpecterRenderState extends EntityRenderState
{
    public Vec3 renderPosition;
    public Vec3 networkPosition;
    float scale;

    public final AnimationState spinAnimationState = new AnimationState();
    public final AnimationState sixSevenAnimationState = new AnimationState();
    public final AnimationState headExplodeAnimationState = new AnimationState();
    public final AnimationState pointAnimationState = new AnimationState();

    public float yrot;
    public float xrot;

    public boolean shouldRender;
}
