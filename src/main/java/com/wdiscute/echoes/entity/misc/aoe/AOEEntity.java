package com.wdiscute.echoes.entity.misc.aoe;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.aoe.AbstractAOE;
import com.wdiscute.echoes.registry.ECAOEs;
import com.wdiscute.utils.ValueHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

public class AOEEntity extends Entity implements IEntityWithComplexSpawn
{
    public AbstractAOE aoe;

    public AOEEntity(EntityType<?> entityType, Level level)
    {
        super(entityType, level);
    }

    @Override
    public void tick()
    {
        super.tick();
        if (aoe != null)
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
    public boolean shouldRender(double x, double y, double z)
    {
        return true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder)
    {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound)
    {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound)
    {

    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer)
    {
        CompoundTag tag = new CompoundTag();
        if (aoe != null)
        {
            ValueHelper.store("aoe_type", ResourceLocation.CODEC, aoe.getId(), tag);
            ValueHelper.store("aoe", aoe.getCodec(), aoe, tag);
            buffer.writeNbt(tag);
        }
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf additionalData)
    {
        CompoundTag tag = additionalData.readNbt();

        ResourceLocation id = ValueHelper.read("aoe_type", ResourceLocation.CODEC, tag).orElse(Echoes.MISSINGNO);

        if (ECAOEs.MAP.get(id) != null)
            aoe = ValueHelper.read("aoe", ECAOEs.MAP.get(id), tag).orElse(null);
    }
}
