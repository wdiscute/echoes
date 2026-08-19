package com.wdiscute.echoes.timeless;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;
import java.util.List;

public record TimelessEnemyEntry(Identifier id, int preferredLevel, int levelRange, int weight, float healthIncrease,
                                 float damageIncrease, float lootRolls)
{
    public static final Codec<TimelessEnemyEntry> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Identifier.CODEC.fieldOf("id").forGetter(TimelessEnemyEntry::id),
                    Codec.INT.fieldOf("preferred_level").forGetter(TimelessEnemyEntry::preferredLevel),
                    Codec.INT.fieldOf("level_range").forGetter(TimelessEnemyEntry::levelRange),
                    Codec.INT.fieldOf("weight").forGetter(TimelessEnemyEntry::weight),
                    Codec.FLOAT.fieldOf("health_increase_per_level").forGetter(TimelessEnemyEntry::healthIncrease),
                    Codec.FLOAT.fieldOf("damage_increase_per_level").forGetter(TimelessEnemyEntry::damageIncrease),
                    Codec.FLOAT.fieldOf("loot_rolls").forGetter(TimelessEnemyEntry::lootRolls)
            ).apply(instance, TimelessEnemyEntry::new)
    );


    public static @Nullable TimelessEnemyEntry getRandomEnemy(ServerLevel sl, List<TimelessEnemyEntry> allEnemies, int level)
    {
        if (allEnemies.isEmpty())
            return null;

        float totalWeight = 0.0f;

        for (TimelessEnemyEntry enemy : allEnemies)
            totalWeight += getWeight(enemy, level);

        if (totalWeight <= 0.0f)
            return null;

        float random = sl.getRandom().nextFloat() * totalWeight;

        for (TimelessEnemyEntry enemy : allEnemies)
        {
            random -= getWeight(enemy, level);

            if (random <= 0.0f)
                return enemy;
        }

        return allEnemies.getLast();
    }

    private static float getWeight(TimelessEnemyEntry enemy, int level)
    {
        int preferredLevel = enemy.preferredLevel();
        int range = enemy.levelRange();

        //if no preferred level, return 1
        if (preferredLevel == 0)
            return 1.0f;

        //if no range, return 1 if matching level
        if (range <= 0)
            return level == preferredLevel ? 1.0f : 0.0f;

        float distance = Math.abs(level - preferredLevel);

        if (distance >= range)
            return 0.0f;

        return 1.0f - (distance / range);
    }
}
