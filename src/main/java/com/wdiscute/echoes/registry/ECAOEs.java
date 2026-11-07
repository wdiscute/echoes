package com.wdiscute.echoes.registry;

import com.mojang.serialization.Codec;
import com.wdiscute.echoes.aoe.AbstractAOE;
import com.wdiscute.echoes.aoe.donut.DonutAOE;
import com.wdiscute.echoes.aoe.growingcircle.GrowingCircleAOE;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface ECAOEs
{
    Map<ResourceLocation, Codec<? extends AbstractAOE>> MAP = new ConcurrentHashMap<>();

    static Codec<? extends AbstractAOE> get(ResourceLocation id)
    {
        return MAP.get(id);
    }

    static void register(IEventBus eventBus)
    {
        MAP.put(DonutAOE.ID, DonutAOE.CODEC);
        MAP.put(GrowingCircleAOE.ID, GrowingCircleAOE.CODEC);
    }
}
