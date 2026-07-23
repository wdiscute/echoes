package com.wdiscute.echoes.registry.entity.lantern;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class LanternEntity extends Entity
{

    boolean canDeattach = false;
    boolean canAttach = false;
    Player player = null;

    public LanternEntity(EntityType<?> type, Level level)
    {
        super(type, level);
    }

    @Override
    public void tick()
    {
        super.tick();

        if (level().isClientSide()) return;

        //if not attached
        if (player == null)
        {
            Player nearestPlayer = level().getNearestPlayer(this, 2);
            if (nearestPlayer == null) return;

            //we know there's a vehicle
            if(!nearestPlayer.isCrouching()) canAttach = true;

            if(nearestPlayer.isCrouching() && canAttach)
            {
                if (nearestPlayer.isCrouching())
                {
                    canDeattach = false;
                    player = nearestPlayer;
                }
            }
        }
        //if attached
        else
        {
            //we know there's a vehicle
            if(!player.isCrouching()) canDeattach = true;

            if(player.isCrouching() && canDeattach)
            {
                player = null;
                canAttach = false;
                return;
            }

            moveOrInterpolateTo(player.position());
        }
    }

    @Override
    protected boolean canRide(Entity vehicle)
    {
        return true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData)
    {

    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage)
    {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input)
    {

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output)
    {

    }
}
