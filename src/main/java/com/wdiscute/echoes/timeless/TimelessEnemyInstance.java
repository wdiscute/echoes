package com.wdiscute.echoes.timeless;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;
import java.util.List;

public record TimelessEnemyInstance(Identifier id, int preferredLevel, int weight, int levelRange, float healthIncrease,
                                    float damageIncrease)
{
    public static final Codec<TimelessEnemyInstance> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Identifier.CODEC.fieldOf("id").forGetter(TimelessEnemyInstance::id),
                    Codec.INT.optionalFieldOf("preferred_level", 0).forGetter(TimelessEnemyInstance::preferredLevel),
                    Codec.INT.fieldOf("weight").forGetter(TimelessEnemyInstance::weight),
                    Codec.INT.optionalFieldOf("level_range", 0).forGetter(TimelessEnemyInstance::levelRange),
                    Codec.FLOAT.fieldOf("health_increase_per_level").forGetter(TimelessEnemyInstance::healthIncrease),
                    Codec.FLOAT.fieldOf("damage_increase_per_level").forGetter(TimelessEnemyInstance::damageIncrease)
            ).apply(instance, TimelessEnemyInstance::new)
    );


    public static @Nullable TimelessEnemyInstance getRandomEnemy(
            ServerLevel sl,
            List<TimelessEnemyInstance> allEnemies,
            int level)
    {
        if (allEnemies.isEmpty())
            return null;

        float totalWeight = 0.0f;

        for (TimelessEnemyInstance enemy : allEnemies)
            totalWeight += getWeight(enemy, level);

        if (totalWeight <= 0.0f)
            return null;

        float random = sl.getRandom().nextFloat() * totalWeight;

        for (TimelessEnemyInstance enemy : allEnemies)
        {
            random -= getWeight(enemy, level);

            if (random <= 0.0f)
                return enemy;
        }

        return allEnemies.getLast();
    }

    private static float getWeight(TimelessEnemyInstance enemy, int level)
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
