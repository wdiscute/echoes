package com.wdiscute.echoes.entity.specter;

import com.wdiscute.echoes.entity.lantern.LanternEntity;
import com.wdiscute.echoes.registry.ECEntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.UUID;

public class SpecterEntity extends Entity
{
    public static final EntityDataAccessor<UUID> PLAYER_UUID = SynchedEntityData.defineId(SpecterEntity.class, ECEntityDataSerializers.UUID_HOLDER.get());

    Player player;

    public SpecterEntity(EntityType<?> type, Level level)
    {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData)
    {
        entityData.define(PLAYER_UUID, UUID.randomUUID());
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

    @Override
    public void tick()
    {
        super.tick();
        if (level().isClientSide()) return;

        if (player != null)
        {
            moveOrInterpolateTo(player.position());

            setYRot(player.getYRot());
            setXRot(player.getXRot());

            if (!player.isSpectator())
                remove(RemovalReason.DISCARDED);
        }
        else
            remove(RemovalReason.DISCARDED);
    }

    public void setPlayer(ServerPlayer sp)
    {
        player = sp;
        entityData.set(PLAYER_UUID, sp.getUUID());
    }
}
