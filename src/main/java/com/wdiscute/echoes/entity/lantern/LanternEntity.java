package com.wdiscute.echoes.entity.lantern;

import com.wdiscute.echoes.SculkAura;
import com.wdiscute.echoes.registry.ECDataAttachments;
import com.wdiscute.echoes.registry.ECEntityDataSerializers;
import com.wdiscute.utils.Utils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

public class LanternEntity extends Entity implements SculkAura
{
    public boolean isLocked = false;
    public int renderTimeOffset = Utils.r.nextInt();
    public Vec3 renderOffset = Vec3.ZERO;
    public int pickupCooldown = 0;

    public LanternEntity(EntityType<?> type, Level level)
    {
        super(type, level);
    }

    public static final EntityDataAccessor<UUID> UUID = SynchedEntityData.defineId(LanternEntity.class, ECEntityDataSerializers.UUID_HOLDER.get());

    @Override
    public void setPos(double x, double y, double z)
    {
        this.setBoundingBox(this.makeBoundingBox());
    }

    @Override
    public void tick()
    {
        super.tick();

        pickupCooldown--;

        //if attached entity doesn't exist any more, clear uuid
        Entity attachedEntity = level().getEntity(entityData.get(UUID));
        if (attachedEntity == null)
        {
            entityData.set(UUID, null);
        }
        else
        {
            //if attached, move to entity attached every tick
            setPosRaw(attachedEntity.position().x, attachedEntity.position().y, attachedEntity.position().z);
        }

        //if client set offset for render and return
        if (level().isClientSide())
        {
            Vec3 position = position();

            if (renderOffset.distanceTo(position) > 10f)
                renderOffset = position;
            else
                renderOffset = position.add(0, attachedEntity == null ? 0 : attachedEntity.getEyeHeight() - 1, 0);

            return;
        }

        //return if is locked as attach logic doesn't apply
        if (isLocked) return;

        //if pickup cooldown
        if (pickupCooldown > 0) return;

        //if not attached to an entity
        if (attachedEntity == null)
        {
            List<Entity> entitiesClose = level().getEntities((Entity) null, new AABB(blockPosition()), o -> o instanceof LivingEntity);

            Entity closest = null;
            float distance = 10210;

            //find closest
            for (Entity entity : entitiesClose)
            {
                //if entity already holding lanter, skip it
                if(entity.getData(ECDataAttachments.HAS_LANTERN)) continue;

                float dist = entity.distanceTo(this);
                if (dist < distance)
                {
                    closest = entity;
                    distance = dist;
                }
            }

            if (closest == null) return;

            closest.setData(ECDataAttachments.HAS_LANTERN, true);
            entityData.set(UUID, closest.getUUID());
        }
        //if attached to a player
        else
        {
            if (attachedEntity instanceof Player player)
            {
                if (player.isCrouching())
                {
                    player.removeData(ECDataAttachments.HAS_LANTERN);
                    entityData.set(UUID, null);
                    pickupCooldown = 40;
                }
            }
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
        entityData.define(UUID, java.util.UUID.randomUUID());
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
