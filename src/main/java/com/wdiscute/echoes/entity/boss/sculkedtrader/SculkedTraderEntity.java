package com.wdiscute.echoes.entity.boss.sculkedtrader;

import com.wdiscute.echoes.aoe.donut.DonutAOE;
import com.wdiscute.echoes.aoe.growingcircle.GrowingCircleAOE;
import com.wdiscute.echoes.entity.misc.aoe.AOEEntity;
import com.wdiscute.echoes.registry.ECEntities;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

public class SculkedTraderEntity extends Mob implements Enemy
{
    public SculkedTraderEntity(EntityType<? extends Mob> type, Level level)
    {
        super(type, level);
    }

    @Override
    protected void actuallyHurt(DamageSource damageSource, float damageAmount)
    {
        super.actuallyHurt(damageSource, damageAmount);
        AOEEntity aoe = ECEntities.AOE.get().create(level());
        aoe.moveTo(position());
        aoe.aoe = new GrowingCircleAOE(-1, 0xffcc4444, 8, 4, 50);
        level().addFreshEntity(aoe);
    }

    @Override
    public boolean canBeLeashed()
    {
        return false;
    }

    @Override
    public void knockback(double power, double xd, double zd)
    {
        if (true)
            super.knockback(power, xd, zd);
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
