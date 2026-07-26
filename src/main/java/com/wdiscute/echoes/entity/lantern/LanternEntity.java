package com.wdiscute.echoes.entity.lantern;

import com.wdiscute.echoes.SculkAura;
import com.wdiscute.echoes.TimelessInstance;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class LanternEntity extends Entity implements SculkAura
{
    public Vec3 lerpedPosition = Vec3.ZERO;
    public double lerpSpeed = 0;
    TimelessInstance instance;
    int ringCooldown = 0;

    public boolean isLocked = false;
    boolean canDeattach = false;
    boolean canAttach = false;
    Player player = null;

    public LanternEntity(EntityType<?> type, Level level)
    {
        super(type, level);
    }

    public void setInstance(TimelessInstance instance)
    {
        this.instance = instance;
    }

    @Override
    public void tick()
    {
        super.tick();

        if (level().isClientSide()) return;

        ringCooldown--;

        if(instance != null && ringCooldown < 0 && level().getRandom().nextFloat() > 0.95f)
        {
            ringCooldown = 100;
            instance.submitRing(position(), getSculkAura((ServerLevel) level()) + instance.globalAuraBoost, 0.4f);
        }

        if(isLocked) return;

        //if not attached
        if (player == null)
        {
            Player nearestPlayer = level().getNearestPlayer(this, 2);
            if (nearestPlayer == null) return;

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
        isLocked = input.getBooleanOr("locked", false);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output)
    {
        output.putBoolean("locked", isLocked);
    }

    @Override
    public float getSculkAura(ServerLevel sl)
    {
        return 6;
    }
}
