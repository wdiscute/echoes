package com.wdiscute.echoes.timeless;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.echoes.registry.ECDataEntries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;
import java.util.List;

public record TimelessLevelEntry(Identifier id, int preferredLevel, int weight, int levelRange, int ticks, boolean isHub)
{
    public static final Codec<TimelessLevelEntry> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Identifier.CODEC.fieldOf("id").forGetter(TimelessLevelEntry::id),
                    Codec.INT.optionalFieldOf("preferred_level", 0).forGetter(TimelessLevelEntry::preferredLevel),
                    Codec.INT.fieldOf("weight").forGetter(TimelessLevelEntry::weight),
                    Codec.INT.optionalFieldOf("level_range", 0).forGetter(TimelessLevelEntry::levelRange),
                    Codec.INT.optionalFieldOf("ticks", 0).forGetter(TimelessLevelEntry::ticks),
                    Codec.BOOL.optionalFieldOf("is_hub", false).forGetter(TimelessLevelEntry::isHub)
            ).apply(instance, TimelessLevelEntry::new)
    );

    public static @Nullable TimelessLevelEntry getRandomLoot(ServerLevel sl, int level)
    {
        List<TimelessLevelEntry> entries = ECDataEntries.TIMELESS_LEVELS.get();

        if (entries.isEmpty())
            return null;

        float totalWeight = 0.0f;

        for (TimelessLevelEntry entry : entries)
        {
            //if current level is entry's preferred level and has no range
            if (entry.preferredLevel == level && entry.levelRange == 0 && entry.canUse(level))
                return entry;

            totalWeight += getWeight(entry, level);
        }

        if (totalWeight <= 0.0f)
            return null;

        float random = sl.getRandom().nextFloat() * totalWeight;

        for (TimelessLevelEntry enemy : entries)
        {
            random -= getWeight(enemy, level);

            if (random <= 0.0f)
                return enemy;
        }

        return entries.getLast();
    }

    private boolean canUse(int level)
    {
        //return false if current level is hub but entry is NOT hub
        //return false if current level is NOT hub but entry is hub
        return (level % 5 == 0 && this.isHub) || (level % 5 != 0 && !this.isHub);
    }

    private static float getWeight(TimelessLevelEntry entry, int level)
    {
        //hub check
        if(!entry.canUse(level)) return 0;

        int preferredLevel = entry.preferredLevel();
        int range = entry.levelRange();

        //if no preferred level, return 1
        if (preferredLevel == 0)
            return 1.0f;

        //if no range but does have a preferred level, return 1 if matching level
        if (range <= 0)
            return level == preferredLevel ? 1.0f : 0.0f;

        float distance = Math.abs(level - preferredLevel);

        if (distance >= range)
            return 0.0f;

        return 1.0f - (distance / range);
    }
}
