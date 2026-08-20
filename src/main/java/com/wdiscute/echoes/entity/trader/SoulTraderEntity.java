package com.wdiscute.echoes.entity.trader;

import com.wdiscute.echoes.SculkAura;
import com.wdiscute.echoes.timeless.TimelessInstance;
import com.wdiscute.echoes.timeless.TimelessManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public class SoulTraderEntity extends Mob
{
    public SoulTraderEntity(EntityType<? extends Mob> type, Level level)
    {
        super(type, level);
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

    private static final ProjectileDeflection PROJECTILE_DEFLECTION = (projectile, entity, random) ->
    {
        entity.level().playSound(null, entity, SoundEvents.SCULK_SHRIEKER_SHRIEK, entity.getSoundSource(), 1.0F, 1.0F);
        ProjectileDeflection.REVERSE.deflect(projectile, entity, random);
    };

    @Override
    public ProjectileDeflection deflection(Projectile projectile)
    {
        return PROJECTILE_DEFLECTION;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage)
    {
        if(source.getEntity() instanceof Player player)
            player.hurt(player.damageSources().source(DamageTypes.MAGIC), 1);

        return false;
    }

    @Override
    public void tick()
    {
        super.tick();
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 67);
    }
}
