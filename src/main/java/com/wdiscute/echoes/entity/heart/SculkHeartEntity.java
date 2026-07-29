package com.wdiscute.echoes.entity.heart;

import com.wdiscute.echoes.SculkAura;
import com.wdiscute.echoes.TimelessInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public class SculkHeartEntity extends Mob implements SculkAura
{
    TimelessInstance instance = null;


    public SculkHeartEntity(EntityType<? extends Mob> type, Level level)
    {
        super(type, level);
    }

    public void setInstance(TimelessInstance instance)
    {
        this.instance = instance;
    }

    @Override
    public boolean canBeCollidedWith(@Nullable Entity other)
    {
        return false;
    }

    @Override
    public boolean isPushable()
    {
        return false;
    }

    @Override
    public boolean canCollideWith(Entity entity)
    {
        return false;
    }

    @Override
    public boolean canBeLeashed()
    {
        return false;
    }

    @Override
    public boolean canBeHitByProjectile()
    {
        return false;
    }

    @Override
    public void knockback(double power, double xd, double zd)
    {
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage)
    {
        if (instance != null)
            instance.onHeartHit(level);
        return true;
    }

    @Override
    public void tick()
    {
        super.tick();
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0F);
    }

    @Override
    public float getSculkAura(ServerLevel sl)
    {
        return 4.5f;
    }
}
