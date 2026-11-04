package com.wdiscute.echoes.aoe;

import com.wdiscute.utils.Utils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class GrowingCircleAOE extends AbstractAOE
{
    final int startTick;
    final List<Utils.Duo<Float, Float>> fullCircle;
    final float ticksUntilFull;
    final Entity entity;
    final Vec3 absolutePosition;
    final int color;
    final int segments;
    final BiConsumer<Entity, GrowingCircleAOE> onRemoved;

    public GrowingCircleAOE(Entity entity, Vec3 absolutePosition, int color, int segments, float size, float ticksUntilFull, BiConsumer<Entity, GrowingCircleAOE> onRemoved)
    {
        this.entity = entity;
        this.absolutePosition = absolutePosition;
        this.color = color;
        this.segments = segments;
        this.fullCircle = makeCircle(size, segments);
        this.startTick = entity.tickCount;
        this.ticksUntilFull = ticksUntilFull;
        this.onRemoved = onRemoved;
    }

    @Override
    public void tick(Entity aoeEntity)
    {
        super.tick(aoeEntity);
        if(Mth.clamp((aoeEntity.tickCount - startTick) / ticksUntilFull, 0f, 1f) == 1)
            removed = true;
    }

    @Override
    public void onRemoved(Entity entity)
    {
        super.onRemoved(entity);
        onRemoved.accept(entity, this);
    }

    @Override
    public AOERenderer getRenderer()
    {
        return new GrowingCircleAOERenderer();
    }

    public GrowingCircleAOE(Entity entity, int color, int segments, float size, float ticksUntilFull, BiConsumer<Entity, GrowingCircleAOE> onRemoved)
    {
        this(entity, Vec3.ZERO, color, segments, size, ticksUntilFull, onRemoved);
    }

    public static List<Utils.Duo<Float, Float>> makeCircle(float radius, int segments)
    {
        List<Utils.Duo<Float, Float>> points = new ArrayList<>(segments);
        for (int i = 0; i < segments; i++)
        {
            double angle = -2.0 * Math.PI * i / segments;
            points.add(new Utils.Duo<>(
                    (float) Math.cos(angle) * radius,
                    (float) Math.sin(angle) * radius));
        }
        return points;
    }
}
