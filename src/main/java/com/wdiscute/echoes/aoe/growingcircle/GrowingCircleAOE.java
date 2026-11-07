package com.wdiscute.echoes.aoe.growingcircle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.aoe.AOERenderer;
import com.wdiscute.echoes.aoe.AbstractAOE;
import com.wdiscute.utils.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class GrowingCircleAOE extends AbstractAOE
{
    //synced to client
    int entityID;
    int color;
    float size;
    int segments;
    int duration;

    //not synced
    List<Utils.Duo<Float, Float>> fullCircle;
    BiConsumer<Entity, GrowingCircleAOE> onRemoved;

    public static final Codec<GrowingCircleAOE> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("entity").forGetter(o -> o.entityID),
                    Codec.INT.fieldOf("color").forGetter(o -> o.color),
                    Codec.INT.fieldOf("segments").forGetter(o -> o.segments),
                    Codec.FLOAT.fieldOf("size").forGetter(o -> o.size),
                    Codec.INT.fieldOf("ticks").forGetter(o -> o.duration)
            ).apply(instance, GrowingCircleAOE::new)
    );

    public GrowingCircleAOE(int entityId, int color, int segments, float size, int ticks)
    {
        this.entityID = entityId;
        this.color = color;
        this.segments = segments;
        this.size = size;
        this.duration = ticks;

        this.fullCircle = makeCircle(size, segments);
        this.onRemoved = Utils::nothing;
    }

    @Override
    public void tick(Entity aoeEntity)
    {
        super.tick(aoeEntity);
        if (aoeEntity.tickCount >= duration + 3)
            removed = true;
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
