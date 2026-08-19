package com.wdiscute.echoes.entity.enemy.hollowed;

import com.wdiscute.echoes.SculkAura;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public class HollowedEntity extends AbstractSkeleton implements SculkAura
{
    public HollowedEntity(EntityType<? extends HollowedEntity> type, Level level)
    {
        super(type, level);
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.STRAY_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source)
    {
        return SoundEvents.STRAY_HURT;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.STRAY_DEATH;
    }

    @Override
    protected SoundEvent getStepSound()
    {
        return SoundEvents.STRAY_STEP;
    }

    @Override
    protected AbstractArrow getArrow(ItemStack projectile, float power, @Nullable ItemStack firingWeapon)
    {
        AbstractArrow arrow = super.getArrow(projectile, power, firingWeapon);
        if (arrow instanceof Arrow)
        {
            ((Arrow) arrow).addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 600));
        }

        return arrow;
    }

    @Override
    public float getSculkAura(@Nullable ServerLevel sl)
    {
        return 3;
    }
}