package com.wdiscute.echoes.entity.enemy.sculked;

import com.wdiscute.echoes.SculkAura;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public class SculkedEntity extends Zombie implements SculkAura
{
    public SculkedEntity(EntityType<? extends Zombie> type, Level level)
    {
        super(type, level);
        xpReward = 0;
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0)
                .add(Attributes.MOVEMENT_SPEED, 0.23F)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.ARMOR, 2.0)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE)
                ;
    }

    @Override
    public float getSculkAura(@Nullable ServerLevel sl)
    {
        return 7;
    }
}
