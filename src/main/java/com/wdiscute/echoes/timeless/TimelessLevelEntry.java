package com.wdiscute.echoes.timeless;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.registry.ECDataEntries;
import com.wdiscute.utils.Counter;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public record TimelessLevelEntry(Identifier id, int preferredLevel, int weight, int levelRange, int ticks, int maxUses)
{
    public static final TimelessLevelEntry HUB = new TimelessLevelEntry(Echoes.rl("timeless/hub"), 0, 0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
    public static final TimelessLevelEntry TUTORIAL = new TimelessLevelEntry(Echoes.rl("timeless/starter"), 0, 0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);

    public static final Codec<TimelessLevelEntry> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Identifier.CODEC.fieldOf("id").forGetter(TimelessLevelEntry::id),
                    Codec.INT.optionalFieldOf("preferred_level", 0).forGetter(TimelessLevelEntry::preferredLevel),
                    Codec.INT.fieldOf("weight").forGetter(TimelessLevelEntry::weight),
                    Codec.INT.optionalFieldOf("level_range", 0).forGetter(TimelessLevelEntry::levelRange),
                    Codec.INT.optionalFieldOf("ticks", 0).forGetter(TimelessLevelEntry::ticks),
                    Codec.INT.optionalFieldOf("max_uses_per_player", Integer.MAX_VALUE).forGetter(TimelessLevelEntry::ticks)
            ).apply(instance, TimelessLevelEntry::new)
    );

    public static @Nullable TimelessLevelEntry getRandomLevel(ServerLevel sl, Counter<Identifier> levelsCompleted, int level)
    {
        List<TimelessLevelEntry> entries = ECDataEntries.TIMELESS_LEVELS.get()
                .stream()
                .filter(o -> o.maxUses > levelsCompleted.get(o.id))
                .toList();

        if (entries.isEmpty())
            return null;

        float totalWeight = 0.0f;

        for (TimelessLevelEntry entry : entries)
        {
            //if current level is entry's preferred level and has no range
            if (entry.preferredLevel == level && entry.levelRange == 0)
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

    private static float getWeight(TimelessLevelEntry entry, int level)
    {
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
