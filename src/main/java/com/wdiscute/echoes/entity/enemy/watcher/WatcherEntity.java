package com.wdiscute.echoes.entity.enemy.watcher;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class WatcherEntity extends Monster
{
    protected WatcherEntity(EntityType<? extends Monster> type, Level level)
    {
        super(type, level);
    }
}
