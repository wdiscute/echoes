package com.wdiscute.echoes.aoe.donut;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.aoe.AOERenderer;
import com.wdiscute.echoes.aoe.AbstractAOE;
import com.wdiscute.echoes.aoe.growingcircle.GrowingCircleAOE;
import com.wdiscute.utils.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.List;
import java.util.function.BiConsumer;

public class DonutAOE extends AbstractAOE<DonutAOE>
{
    //synced to client
    int entityID;
    int color;
    int segments;
    float inner;
    float size;
    int duration;

    //not synced
    final List<Utils.Duo<Float, Float>> innerRing;
    final List<Utils.Duo<Float, Float>> outerRing;
    final BiConsumer<Entity, DonutAOE> onRemoved;

    public static final Codec<DonutAOE> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("entity").forGetter(o -> o.entityID),
                    Codec.INT.fieldOf("color").forGetter(o -> o.color),
                    Codec.INT.fieldOf("segments").forGetter(o -> o.segments),
                    Codec.FLOAT.fieldOf("inner").forGetter(o -> o.inner),
                    Codec.FLOAT.fieldOf("size").forGetter(o -> o.size),
                    Codec.INT.fieldOf("ticks").forGetter(o -> o.duration)
            ).apply(instance, DonutAOE::new)
    );

    public DonutAOE(int entityId, int color, int segments, float inner, float size, int ticks)
    {
        this.entityID = entityId;
        this.color = color;
        this.segments = segments;
        this.inner = inner;
        this.size = size;
        this.duration = ticks;

        this.innerRing = GrowingCircleAOE.makeCircle(inner, segments);
        this.outerRing = GrowingCircleAOE.makeCircle(inner + size, segments);
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
