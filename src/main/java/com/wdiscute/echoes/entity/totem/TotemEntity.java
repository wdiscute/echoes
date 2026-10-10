package com.wdiscute.echoes.entity.totem;

import com.wdiscute.echoes.perks.TotemPerk;
import com.wdiscute.echoes.upgrades.PerkInstance;
import com.wdiscute.utils.ValueHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class TotemEntity extends Entity
{
    PerkInstance perkInstance;

    public TotemEntity(EntityType<?> entityType, Level level)
    {
        super(entityType, level);
    }

    @Override
    public void tick()
    {
        if(perkInstance != null && perkInstance.perk() instanceof TotemPerk perk)
            perk.onTick(this, perkInstance.amplifiers());

        super.tick();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder)
    {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag)
    {
        perkInstance = ValueHelper.read("perk", PerkInstance.CODEC, compoundTag).orElse(null);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag)
    {
        ValueHelper.store("perl", PerkInstance.CODEC, perkInstance, compoundTag);
    }
}
