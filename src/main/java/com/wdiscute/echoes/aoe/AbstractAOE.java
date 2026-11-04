package com.wdiscute.echoes.aoe;

import com.wdiscute.utils.Utils;
import net.minecraft.world.entity.Entity;

public abstract class AbstractAOE
{
    public static final Utils.Duo<Float, Float> CENTER = new Utils.Duo<>(0f, 0f);
    public boolean removed = false;

    public void onRemoved(Entity entity)
    {

    }

    public abstract AOERenderer getRenderer();

    public void tick(Entity aoeEntity)
    {
        if(removed)
            aoeEntity.discard();
    }
}
