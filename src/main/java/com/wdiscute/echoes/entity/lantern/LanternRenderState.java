package com.wdiscute.echoes.entity.lantern;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class LanternRenderState extends EntityRenderState
{
    public Vec3 positionToRenderAt;
    public Entity entity;
}
