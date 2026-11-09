package com.wdiscute.echoes.aoe;

import com.mojang.serialization.Codec;
import com.wdiscute.echoes.aoe.donut.DonutAOE;
import com.wdiscute.utils.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class AbstractAOE<T extends AbstractAOE<T>>
{
    public static final Utils.Duo<Float, Float> CENTER = new Utils.Duo<>(0f, 0f);
    public final int entityID;
    public final int duration;
    public boolean triggered = false;
    public boolean removed = false;
    public int tickCount;

    public BiConsumer<Entity, T> onRemoved = Utils::nothing;
    public Consumer<List<Entity>> onTrigger = Utils::nothing;

    public AbstractAOE(int entityID, int duration)
    {
        this.entityID = entityID;
        this.duration = duration;
    }

    public abstract List<Entity> getEntitiesInAOE(Entity entity);

    public void onRemoved(Entity entity)
    {
        onRemoved.accept(entity, (T) this);
    }

    public abstract Codec<T> getCodec();

    public abstract ResourceLocation getId();

    public abstract AOERenderer getRenderer();

    public void tick(Entity aoeEntity)
    {
        tickCount++;
        Entity entity = aoeEntity.level().getEntity(entityID);
        if(entity != null)
            aoeEntity.moveTo(entity.getPosition(0));

        if (!triggered && aoeEntity.tickCount > duration)
        {
            triggered = true;
            onTrigger.accept(getEntitiesInAOE(aoeEntity));
        }

        if(removed)
        {
            onRemoved(aoeEntity);
            aoeEntity.discard();
        }
    }
}
