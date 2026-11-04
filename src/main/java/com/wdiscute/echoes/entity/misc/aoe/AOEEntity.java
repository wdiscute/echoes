package com.wdiscute.echoes.entity.misc.aoe;

import com.wdiscute.echoes.aoe.AbstractAOE;
import com.wdiscute.echoes.aoe.GrowingCircleAOE;
import com.wdiscute.utils.Utils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class AOEEntity extends Entity
{
    public AbstractAOE aoe;

    public AOEEntity(EntityType<?> entityType, Level level)
    {
        super(entityType, level);
        aoe = new GrowingCircleAOE(this, 0xFFcc5555, 80, 3.5f, 40, Utils::nothing);
    }

    @Override
    public void tick()
    {
        super.tick();
        if(aoe != null)
            aoe.tick(this);
    }

    @Override
    public boolean canCollideWith(Entity entity)
    {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount)
    {
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder)
    {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound)
    {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound)
    {

    }
}
