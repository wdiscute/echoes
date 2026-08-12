package com.wdiscute.echoes.entity.unleashedsoul;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class UnleashedSoulEntity extends Projectile
{
    public UnleashedSoulEntity(EntityType<? extends UnleashedSoulEntity> type, Level level)
    {
        super(type, level);
    }

    public static final EntityDataAccessor<Integer> MAX_TICKS = SynchedEntityData.defineId(UnleashedSoulEntity.class, EntityDataSerializers.INT);

    public float damage = 2;

    public UnleashedSoulEntity(
            EntityType<? extends UnleashedSoulEntity> type,
            LivingEntity owner,
            Vec3 direction,
            Level level,
            int lastingTicks,
            float damage
    )
    {
        this(type, level);

        entityData.set(MAX_TICKS,lastingTicks);
        this.damage = damage;
        this.setOwner(owner);

        Vec3 velocity = direction.normalize();

        Vec3 look = owner.getViewVector(1.0F).normalize();
        Vec3 right = look.cross(new Vec3(0, 1, 0)).normalize();

        Vec3 spawnOffset = right.scale(0.5)
                .add(look.scale(0.9));

        Vec3 randomOffset = new Vec3(
                (random.nextDouble() - 0.5) * 0.5,
                (random.nextDouble() - 0.5) * 0.5,
                (random.nextDouble() - 0.5) * 0.5
        );

        this.setPos(
                owner.getX() + spawnOffset.x + randomOffset.x,
                owner.getEyeY() - 0.3F + spawnOffset.y + randomOffset.y,
                owner.getZ() + spawnOffset.z + randomOffset.z
        );

        this.setDeltaMovement(velocity.offsetRandom(random, 0.02f));

        this.setYRot(owner.getYRot());
        this.setXRot(owner.getXRot());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData)
    {
        entityData.define(MAX_TICKS, 0);
    }

    @Override
    public void tick()
    {
        super.tick();

        Vec3 movement = this.getDeltaMovement();

        HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitResult.getType() != HitResult.Type.MISS)
        {
            this.onHit(hitResult);
            return;
        }

        this.setPos(this.position().add(movement));

        updateRotation();

        if (this.tickCount > entityData.get(MAX_TICKS))
        {
            level().addParticle(ParticleTypes.SOUL, getX(), getY(), getZ(), 0, 0, 0);
            this.discard();
        }
    }

    @Override
    protected void updateRotation()
    {
        Vec3 velocity = this.getDeltaMovement();

        double horizontalLength = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);

        float yaw = (float) (Math.atan2(velocity.z, velocity.x) * (180.0D / Math.PI)) - 90.0F;

        float pitch = (float) -(Math.atan2(velocity.y, horizontalLength) * (180.0D / Math.PI));

        this.setYRot(yaw);
        this.setXRot(pitch);
    }

    @Override
    protected void onHit(HitResult result)
    {
        super.onHit(result);

        switch (result.getType())
        {
            case ENTITY -> onHitEntity((EntityHitResult) result);
            case BLOCK -> onHitBlock((BlockHitResult) result);

            default ->
            {
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result)
    {
        if (!this.level().isClientSide())
        {
            if (result.getEntity() instanceof LivingEntity entity)
            {
                entity.hurt(this.damageSources().generic(), damage);
                entity.invulnerableTime = 0;
            }

            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result)
    {
        if (!this.level().isClientSide())
            this.discard();
    }
}