package com.wdiscute.echoes.entity.totem;

import com.mojang.serialization.Codec;
import com.wdiscute.echoes.perk.totem.TotemPerk;
import com.wdiscute.echoes.perk.PerkInstance;
import com.wdiscute.utils.ValueHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class TotemEntity extends Entity
{
    public Player player;
    public PerkInstance perkInstance;
    public int cooldown;

    public TotemEntity(EntityType<?> entityType, Level level)
    {
        super(entityType, level);
    }

    public void setPerk(PerkInstance perk)
    {
        perkInstance = perk;
    }

    @Override
    public void tick()
    {
        if(perkInstance != null && perkInstance.perk() instanceof TotemPerk perk)
            perk.onTick(this, perkInstance);

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
        cooldown = ValueHelper.read("cooldown", Codec.INT, compoundTag).orElse(0);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag)
    {
        ValueHelper.store("perk", PerkInstance.CODEC, perkInstance, compoundTag);
        ValueHelper.store("cooldown", Codec.INT, cooldown, compoundTag);
    }
}
