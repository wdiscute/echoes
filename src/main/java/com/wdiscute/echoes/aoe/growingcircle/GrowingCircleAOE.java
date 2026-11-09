package com.wdiscute.echoes.aoe.growingcircle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.aoe.AOERenderer;
import com.wdiscute.echoes.aoe.AbstractAOE;
import com.wdiscute.utils.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class GrowingCircleAOE extends AbstractAOE<GrowingCircleAOE>
{
    //synced to client
    int color;
    float size;
    int segments;
    int duration;

    //not synced
    List<Utils.Duo<Float, Float>> fullCircle;

    public static final Codec<GrowingCircleAOE> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("entity").forGetter(o -> o.entityID),
                    Codec.INT.fieldOf("color").forGetter(o -> o.color),
                    Codec.INT.fieldOf("segments").forGetter(o -> o.segments),
                    Codec.FLOAT.fieldOf("size").forGetter(o -> o.size),
                    Codec.INT.fieldOf("duration").forGetter(o -> o.duration)
            ).apply(instance, GrowingCircleAOE::new)
    );

    public GrowingCircleAOE(int entityId, int color, int segments, float size, int duration)
    {
        super(entityId, duration);
        this.color = color;
        this.segments = segments;
        this.size = size;

        this.fullCircle = makeCircle(size, segments);
    }

    @Override
    public void tick(Entity aoeEntity)
    {
        super.tick(aoeEntity);

        if (aoeEntity.tickCount >= duration + 3)
            removed = true;

        if (!triggered && aoeEntity.tickCount > duration)
        {
            triggered = true;
            onTrigger.accept(getEntitiesInAOE(aoeEntity));
        }
    }

    @Override
    public List<Entity> getEntitiesInAOE(Entity aoe)
    {
        AABB box = aoe.getBoundingBox().inflate(size, 2, size);
        List<Entity> entitiesInside = new ArrayList<>();

        for (Entity entity : aoe.level().getEntities(aoe, box))
        {
            double dx = entity.getX() - aoe.getX();
            double dz = entity.getZ() - aoe.getZ();
            double distSq = dx * dx + dz * dz;

            if (distSq <= size)
                entitiesInside.add(entity);
        }

        return entitiesInside;
    }

    @Override
    public void onRemoved(Entity entity)
    {
        super.onRemoved(entity);
        onRemoved.accept(entity, this);
    }

    public static final ResourceLocation ID = Echoes.rl("circle");

    @Override
    public ResourceLocation getId()
    {
        return ID;
    }

    @Override
    public Codec<GrowingCircleAOE> getCodec()
    {
        return CODEC;
    }

    public static final AOERenderer RENDERER = new GrowingCircleAOERenderer();
    @Override
    public AOERenderer getRenderer()
    {
        return RENDERER;
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
