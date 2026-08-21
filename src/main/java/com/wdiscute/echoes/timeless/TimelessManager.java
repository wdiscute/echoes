package com.wdiscute.echoes.timeless;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.echoes.Echoes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import javax.annotation.Nullable;
import java.util.*;

public class TimelessManager extends SavedData
{
    public static final Codec<TimelessManager> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TimelessInstance.CODEC.listOf().fieldOf("instances").forGetter(sd -> sd.instances.stream().toList())
    ).apply(instance, TimelessManager::new));

    public static final SavedDataType<TimelessManager> ID = new SavedDataType<>(
            Echoes.rl("timeless_instances"),
            TimelessManager::new,
            CODEC
    );

    public final Set<TimelessInstance> instances;

    public TimelessManager()
    {
        instances = new HashSet<>();
    }

    public TimelessManager(List<TimelessInstance> instances)
    {
        this.instances = new HashSet<>(instances);
    }

    public static TimelessManager getSavedData(MinecraftServer server)
    {
        return server.getLevel(Echoes.TIMELESS).getDataStorage().computeIfAbsent(ID);
    }

    public static TimelessInstance getClosest(MinecraftServer server, BlockPos pos)
    {
        TimelessInstance instanceToReturn = null;
        int dist = Integer.MAX_VALUE;

        for (TimelessInstance instance : getSavedData(server).instances)
        {
            int currentDist = Math.abs(instance.origin.getX() - pos.getX()) +
                              Math.abs(instance.origin.getY() - pos.getY()) +
                              Math.abs(instance.origin.getZ() - pos.getZ());

            if (dist > currentDist)
            {
                dist = currentDist;
                instanceToReturn = instance;
            }
        }

        return instanceToReturn;
    }

    public static TimelessInstance getOrCreate(MinecraftServer server, UUID instanceUUID)
    {
        TimelessManager savedData = getSavedData(server);
        return savedData.getOrCreate(instanceUUID);
    }

    public static void remove(MinecraftServer server, UUID instanceToRemove)
    {
        getSavedData(server).instances.removeIf(o -> o.uuid.equals(instanceToRemove));
    }

    public static @Nullable TimelessInstance getOrNull(MinecraftServer server, UUID instanceUUID)
    {
        TimelessManager savedData = getSavedData(server);
        return savedData.getOrNull(instanceUUID);
    }

    public @Nullable TimelessInstance getOrNull(UUID instanceUUID)
    {
        for (TimelessInstance instance : instances)
        {
            if (instance.uuid.equals(instanceUUID))
                return instance;
        }

        return null;
    }

    public TimelessInstance getOrCreate(UUID instanceUUID)
    {
        for (TimelessInstance instance : instances)
        {
            if (instance.uuid.equals(instanceUUID))
                return instance;
        }

        TimelessInstance timelessInstance = TimelessInstance.create(instanceUUID);
        instances.add(timelessInstance);
        return timelessInstance;
    }

    public void tick(ServerLevel sl)
    {
        sl.setRainLevel(sl.getRainLevel(0) - 0.01f);

        sl.getWeatherData().setDirty();

        //attempt close instance
        instances.forEach(o -> o.attemptClose(sl));

        instances.stream().filter(o -> o.shouldTick(sl)).forEach(o -> o.tick(sl));

        instances.removeIf(o -> o.phase.equals(TimelessInstance.Phase.CLOSED));

        this.setDirty();
    }
}
