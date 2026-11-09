package com.wdiscute.echoes.aoe.donut;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.aoe.AOERenderer;
import com.wdiscute.echoes.aoe.AbstractAOE;
import com.wdiscute.echoes.aoe.growingcircle.GrowingCircleAOE;
import com.wdiscute.echoes.entity.misc.aoe.AOEEntity;
import com.wdiscute.utils.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DonutAOE extends AbstractAOE<DonutAOE>
{
    //synced to client
    int color;
    int segments;
    float inner;
    float size;
    int linger;

    //not synced
    final List<Utils.Duo<Float, Float>> innerRing;
    final List<Utils.Duo<Float, Float>> outerRing;

    public static final Codec<DonutAOE> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("entity").forGetter(o -> o.entityID),
                    Codec.INT.fieldOf("color").forGetter(o -> o.color),
                    Codec.INT.fieldOf("segments").forGetter(o -> o.segments),
                    Codec.FLOAT.fieldOf("inner").forGetter(o -> o.inner),
                    Codec.FLOAT.fieldOf("size").forGetter(o -> o.size),
                    Codec.INT.fieldOf("duration").forGetter(o -> o.duration),
                    Codec.INT.fieldOf("linger").forGetter(o -> o.linger)
            ).apply(instance, DonutAOE::new)
    );

    public DonutAOE(int entityId, int color, int segments, float inner, float size, int duration, int linger)
    {
        super(entityId, duration);
        this.color = color;
        this.segments = segments;
        this.inner = inner;
        this.size = size;
        this.linger = linger;

        this.innerRing = GrowingCircleAOE.makeCircle(inner, segments);
        this.outerRing = GrowingCircleAOE.makeCircle(inner + size, segments);
    }

    @Override
    public void tick(Entity aoeEntity)
    {
        super.tick(aoeEntity);

        if (aoeEntity.tickCount >= duration + linger + 3)
            removed = true;
    }

    @Override
    public List<Entity> getEntitiesInAOE(Entity aoe)
    {
        float outer = inner + size;
        double innerSq = inner * inner;
        double outerSq = outer * outer;

        AABB box = aoe.getBoundingBox().inflate(outer, 2, outer);
        List<Entity> entitiesInside = new ArrayList<>();

        for (Entity entity : aoe.level().getEntities(aoe, box))
        {
            double dx = entity.getX() - aoe.getX();
            double dz = entity.getZ() - aoe.getZ();
            double distSq = dx * dx + dz * dz;

            if (distSq >= innerSq && distSq <= outerSq)
                entitiesInside.add(entity);
        }

        return entitiesInside;
    }

    public static final ResourceLocation ID = Echoes.rl("donut");

    @Override
    public ResourceLocation getId()
    {
        return ID;
    }

    @Override
    public Codec<DonutAOE> getCodec()
    {
        return CODEC;
    }

    public static final AOERenderer RENDERER = new DonutAOERenderer();

    @Override
    public AOERenderer getRenderer()
    {
        return RENDERER;
    }
}
