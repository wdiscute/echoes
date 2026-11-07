package com.wdiscute.echoes.aoe;

import com.mojang.serialization.Codec;
import com.wdiscute.echoes.aoe.donut.DonutAOE;
import com.wdiscute.utils.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public abstract class AbstractAOE<T extends AbstractAOE<T>>
{
    public static final Utils.Duo<Float, Float> CENTER = new Utils.Duo<>(0f, 0f);
    public boolean removed = false;
    public int tickCount;

    public void onRemoved(Entity entity)
    {

    }

    public abstract Codec<T> getCodec();

    public abstract ResourceLocation getId();

    public abstract AOERenderer getRenderer();

    public void tick(Entity aoeEntity)
    {
        tickCount++;
        if(removed)
            aoeEntity.discard();
    }
}
