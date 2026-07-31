package com.wdiscute.echoes.entity.enemy.sculked;

import com.wdiscute.echoes.registry.ECEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;

public class SculkedEntity extends Zombie
{
    public SculkedEntity(EntityType<? extends Zombie> type, Level level)
    {
        super(type, level);
        xpReward = 0;
    }

    

}
