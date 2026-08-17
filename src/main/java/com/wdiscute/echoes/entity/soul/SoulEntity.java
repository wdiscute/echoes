package com.wdiscute.echoes.entity.soul;

import com.wdiscute.echoes.registry.ECEntities;
import com.wdiscute.echoes.registry.ECEntityDataSerializers;
import com.wdiscute.echoes.timeless.TimelessData;
import com.wdiscute.echoes.timeless.TimelessHearts;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class SoulEntity extends Entity
{
    public static final EntityDataAccessor<UUID> UUID = SynchedEntityData.defineId(SoulEntity.class, ECEntityDataSerializers.UUID_HOLDER.get());
    int timeAlive = 0;
    public int extraSoulsToSpawn = 0;

    public Vec3 positionToRender;
    public Vec3 velocity;
    public double speed = 0.2;
    public double turnRate = 0.02;

    public SoulEntity(EntityType<?> type, Level level)
    {
        super(type, level);
    }

    public void setPosition()
    {
        RandomSource random = level().getRandom();
        positionToRender = position().offsetRandomXZ(random, 0.3f);
        if (random.nextBoolean())
        {
            Vec3 particlePos = positionToRender.offsetRandom(random, 1.5f);
            level().addParticle(ParticleTypes.SOUL, particlePos.x, particlePos.y, particlePos.z, 0, 0, 0);
        }
    }

    public void setVelocity()
    {
        if (level().getPlayerByUUID(getEntityData().get(SoulEntity.UUID)) instanceof Player player)
        {
            if (velocity == null)
            {
                Vec3 away = position().subtract(player.getEyePosition());

                Vec3 dir;

                if (away.lengthSqr() < 1.0E-8)
                {
                    double angle = random.nextDouble() * Math.PI * 2.0;
                    dir = new Vec3(Math.cos(angle), 0.0, Math.sin(angle));
                }
                else
                {
                    away = away.normalize();

                    dir = away.add(random.nextDouble() * 2.0 - 1.0, random.nextDouble() * 2.0 - 1.0, random.nextDouble() * 2.0 - 1.0)
                            .normalize();

                    if (dir.y < 0)
                        dir = new Vec3(dir.x, -dir.y, dir.z).normalize();

                    if (dir.y > Math.sin(Math.toRadians(75)))
                    {
                        double horizontal = Math.sqrt(dir.x * dir.x + dir.z * dir.z);

                        if (horizontal > 1.0E-8)
                        {
                            double y = Math.sin(Math.toRadians(75));

                            dir = new Vec3(
                                    dir.x / horizontal * Math.cos(Math.toRadians(75)), y,
                                    dir.z / horizontal * Math.cos(Math.toRadians(75))
                            );
                        }
                    }
                }

                double speed = 0.10 + random.nextDouble() * 0.20;
                velocity = dir.scale(speed);
            }
        }
    }

    @Override
    public void tick()
    {
        super.tick();

        if (level() instanceof ServerLevel sl && extraSoulsToSpawn > 0 /*&& level().getGameTime() % 2 == 0*/)
        {
            SoulEntity soul = ECEntities.SOUL.get().spawn(sl, blockPosition(), EntitySpawnReason.TRIGGERED);
            soul.getEntityData().set(SoulEntity.UUID, entityData.get(UUID));
            Vec3 pos = position();
            soul.snapTo(pos.x, pos.y, pos.z);
            sl.addFreshEntityWithPassengers(soul);

            extraSoulsToSpawn--;
        }

        if (level().isClientSide() && level().getPlayerByUUID(entityData.get(UUID)) instanceof Player player)
        {
            turnRate *= 1.04f;
            if (speed < 0.5f)
                speed += 0.03f;
            else
                turnRate *= 1.04f;


            if (false)
            {
                speed = 0;
                turnRate = 0;
                velocity = Vec3.ZERO;
            }

            if (positionToRender == null)
                setPosition();

            if (velocity == null)
                setVelocity();

            Vec3 toTarget = player.getEyePosition().add(0, -0.7, 0).subtract(positionToRender);
            Vec3 desiredVelocity = toTarget.normalize().scale(speed);

            Vec3 delta = desiredVelocity.subtract(velocity);

            double deltaLength = delta.length();

            if (deltaLength > turnRate)
                delta = delta.scale(turnRate / deltaLength);

            velocity = velocity.add(delta);

            positionToRender = positionToRender.add(velocity);

            if (positionToRender.distanceTo(player.getEyePosition().add(0, -0.7, 0)) < 0.2f)
                remove(RemovalReason.DISCARDED);

            return;
        }

        if (level().getPlayerByUUID(entityData.get(UUID)) == null)
        {
            Player nearestPlayer = level().getNearestPlayer(this, 1000);
            if (nearestPlayer != null)
                entityData.set(UUID, nearestPlayer.getUUID());
        }

        timeAlive++;
        if (timeAlive == 20)
        {
            Player playerByUUID = level().getPlayerByUUID(entityData.get(UUID));
            if (playerByUUID != null)
            {
                TimelessData.awardSoul(playerByUUID, 1);
                TimelessHearts.absorbSoul(playerByUUID);
            }
        }
        if (timeAlive > 1000 && extraSoulsToSpawn <= 0)
            remove(RemovalReason.DISCARDED);
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

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output)
    {

    }
}
