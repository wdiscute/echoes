package com.wdiscute.echoes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.*;

public class TimelessInstancesSD extends SavedData
{
    public static final Codec<TimelessInstancesSD> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TimelessInstance.CODEC.listOf().fieldOf("instances").forGetter(sd -> sd.instances.stream().toList())
    ).apply(instance, TimelessInstancesSD::new));

    public static final SavedDataType<TimelessInstancesSD> ID = new SavedDataType<>(
            Echoes.rl("timeless_instances"),
            TimelessInstancesSD::new,
            CODEC
    );



    public final Set<TimelessInstance> instances;

    public TimelessInstancesSD()
    {
        instances = new HashSet<>();
    }

    public TimelessInstancesSD(List<TimelessInstance> instances)
    {
        this.instances = new HashSet<>(instances);
    }

    public static TimelessInstancesSD getSavedData(MinecraftServer server)
    {
        return server.getLevel(Echoes.TIMELESS).getDataStorage().computeIfAbsent(ID);
    }

    public static TimelessInstance getOrCreate(MinecraftServer server, UUID instanceUUID)
    {
        TimelessInstancesSD savedData = getSavedData(server);
        return savedData.getOrCreate(instanceUUID);
    }

    public TimelessInstance getOrCreate(UUID instanceUUID)
    {
        for (TimelessInstance instance : instances)
        {
            if(instance.uuid.equals(instanceUUID))
                return instance;
        }

        TimelessInstance timelessInstance = TimelessInstance.create();
        instances.add(timelessInstance);
        return timelessInstance;
    }

    public void tick(ServerLevel sl)
    {
        instances.stream().filter(o -> o.phase != TimelessInstance.Phase.NEW).forEach(o -> o.tick(sl));
        this.setDirty();
    }
}
