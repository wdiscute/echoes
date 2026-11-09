package com.wdiscute.echoes.entity.boss.sculkedtrader;

import com.wdiscute.echoes.aoe.donut.DonutAOE;
import com.wdiscute.echoes.aoe.growingcircle.GrowingCircleAOE;
import com.wdiscute.echoes.aoe.growingcircle.GrowingCircleAOERenderer;
import com.wdiscute.echoes.entity.boss.AttackHandler;
import com.wdiscute.echoes.entity.misc.aoe.AOEEntity;
import com.wdiscute.echoes.registry.ECEntities;
import com.wdiscute.utils.Utils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SculkedTraderEntity extends Mob implements Enemy
{
    public static final AttackHandler.Attack SPIN =
            new AttackHandler.Attack((byte) 167, 100, 4,
                    (mob) ->
                    {
                        Level level = mob.level();
                        AOEEntity aoe = ECEntities.AOE.get().create(level);
                        aoe.moveTo(mob.position());
                        DonutAOE donut = new DonutAOE(mob.getId(), 0xffcc4444, 8, 2, 5, 48, 20);
                        donut.onTrigger = (list) ->
                        {
                            Vec3 position = mob.position();
                            if (level instanceof ServerLevel sl)
                                sl.sendParticles(ParticleTypes.SWEEP_ATTACK, position.x(), position.y() + 1f, position.z(),
                                        20, 3, 0.3f, 3, 0);

                            level.playSound(null, mob.getX(), mob.getY(), mob.getZ(),
                                    SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.HOSTILE);

                            for (Entity entity : list)
                                if (!(entity instanceof SculkedTraderEntity))
                                    entity.hurt(entity.damageSources().mobAttack(mob), 6);
                        };
                        aoe.aoe = donut;
                        level.addFreshEntity(aoe);

                    }, Utils::nothing, Utils::alwaysTrue);

    public static final AttackHandler.Attack FRONT_SMACK = new AttackHandler.Attack((byte) 168, 100, 4,
            (mob) ->
            {
                Level level = mob.level();
                AOEEntity aoe = ECEntities.AOE.get().create(level);
                aoe.moveTo(mob.position());
                GrowingCircleAOE donut = new GrowingCircleAOE(mob.getId(), 0xffcc4444, 8, 5, 48);
                donut.onTrigger = (list) ->
                {
                    for (Entity entity : list)
                        if (!(entity instanceof SculkedTraderEntity))
                            entity.hurt(entity.damageSources().mobAttack(mob), 6);
                };
                aoe.aoe = donut;
                level.addFreshEntity(aoe);

            }, Utils::nothing, Utils::alwaysTrue);

    public static final AttackHandler.Attack FRONT_SLASH = new AttackHandler.Attack((byte) 169, 100, 4, Utils::nothing, Utils::nothing, Utils::alwaysTrue);
    public static final AttackHandler.Attack FRONT_STAB = new AttackHandler.Attack((byte) 170, 100, 4, Utils::nothing, Utils::nothing, Utils::alwaysTrue);

    public final AnimationState spinAS = new AnimationState();
    public final AnimationState frontSmackAS = new AnimationState();
    public final AnimationState frontSlashAS = new AnimationState();
    public final AnimationState frontStabAS = new AnimationState();

    public AttackHandler handler = new AttackHandler(SPIN, FRONT_SMACK/*, FRONT_SLASH, FRONT_STAB*/);

    @Override
    public void tick()
    {
        super.tick();
        if (!level().isClientSide())
            handler.tick(this);
    }

    @Override
    public void knockback(double power, double xd, double zd)
    {
    }

    @Override
    public boolean isPushable()
    {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 67)
                .add(Attributes.SCALE, 3.0)
                ;
    }

    public SculkedTraderEntity(EntityType<? extends Mob> type, Level level)
    {
        super(type, level);
    }

    @Override
    public void handleEntityEvent(byte id)
    {
        if (id == FRONT_SLASH.attackId())
            this.frontSlashAS.start(this.tickCount);

        if (id == FRONT_SMACK.attackId())
            this.frontSmackAS.start(this.tickCount);

        if (id == FRONT_STAB.attackId())
            this.frontStabAS.start(this.tickCount);

        if (id == SPIN.attackId())
            this.spinAS.start(this.tickCount);

        super.handleEntityEvent(id);
    }
}
