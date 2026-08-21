package com.wdiscute.echoes.timeless;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.echoes.registry.ECDataEntries;
import com.wdiscute.echoes.upgrades.BlacksmithTrade;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;

import javax.annotation.Nullable;
import java.util.List;

public record TimelessLootEntry(MaybeStack stack, BlacksmithTrade.Rarity rarity, int preferredLevel, int weight, int levelRange)
{
    public static final Codec<TimelessLootEntry> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    MaybeStack.CODEC.fieldOf("stack").forGetter(TimelessLootEntry::stack),
                    BlacksmithTrade.Rarity.CODEC.fieldOf("rarity").forGetter(TimelessLootEntry::rarity),
                    Codec.INT.optionalFieldOf("preferred_level", 0).forGetter(TimelessLootEntry::preferredLevel),
                    Codec.INT.fieldOf("weight").forGetter(TimelessLootEntry::weight),
                    Codec.INT.optionalFieldOf("level_range", 0).forGetter(TimelessLootEntry::levelRange)
            ).apply(instance, TimelessLootEntry::new)
    );

    public static @Nullable TimelessLootEntry getRandomLoot(RandomSource randomSource, int level)
    {
        List<TimelessLootEntry> entries = ECDataEntries.TIMELESS_LOOT.get();

        if (entries.isEmpty())
            return null;

        float totalWeight = 0.0f;

        for (TimelessLootEntry entry : entries)
        {
            //if current level is entry's preferred level and has no range
            if (entry.preferredLevel == level && entry.levelRange == 0 && entry.canUse(level))
                return entry;

            totalWeight += getWeight(entry, level);
        }

        if (totalWeight <= 0.0f)
            return null;

        float random = randomSource.nextFloat() * totalWeight;

        for (TimelessLootEntry entry : entries)
        {
            random -= getWeight(entry, level);

            if (random <= 0.0f)
                return entry;
        }

        return entries.getLast();
    }

    private boolean canUse(int level)
    {
        return (level % 5 != 0);
    }

    private static float getWeight(TimelessLootEntry entry, int level)
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
