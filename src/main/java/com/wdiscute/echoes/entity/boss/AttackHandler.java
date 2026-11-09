package com.wdiscute.echoes.entity.boss;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class AttackHandler
{
    public AttackHandler(Attack... attacks)
    {
        this.attacks = Arrays.stream(attacks).toList();
    }

    public List<Attack> attacks;
    public Attack current;
    public int ticks;
    private Attack pending;
    private float lockedYaw;

    public void tick(Mob mob)
    {
        if (current == null)
        {
            //roll once, keep it while walking/turning
            if (pending == null)
            {
                List<Attack> list = attacks.stream().filter(o -> o.canUse.apply(mob)).toList();
                if (list.isEmpty()) return;
                pending = list.get(mob.getRandom().nextInt(list.size()));
            }

            Player player = mob.level().getNearestPlayer(TargetingConditions.forCombat(), mob);
            if (player == null)
            {
                mob.getNavigation().stop();
                return;
            }

            //walk closer if out of range
            if (mob.distanceTo(player) > pending.range)
            {
                mob.getNavigation().moveTo(player, 0.4f);
                mob.getLookControl().setLookAt(player, 30, 30);
                return;
            }

            //in range: stop and face the player
            mob.getNavigation().stop();
            mob.getLookControl().setLookAt(player, 30, 30);

            double dx = player.getX() - mob.getX();
            double dz = player.getZ() - mob.getZ();
            float targetYaw = (float) (Mth.atan2(dz, dx) * (180 / Math.PI)) - 90f;
            if (Mth.degreesDifferenceAbs(mob.getYHeadRot(), targetYaw) > 5f)
                return; //still turning

            //start attack
            current = pending;
            pending = null;
            lockedYaw = mob.getYHeadRot();
            current.onAttackStart.accept(mob);
            mob.level().broadcastEntityEvent(mob, current.attackId);
            ticks = 0;
        }
        else
        {
            //freeze movement and rotation for the whole attack
            mob.getNavigation().stop();
            mob.setXxa(0);
            mob.setZza(0);
            mob.setYRot(lockedYaw);
            mob.setYHeadRot(lockedYaw);
            mob.setYBodyRot(lockedYaw);

            ticks++;
            if (ticks > current.cooldown)
            {
                current.onAttackEnd.accept(mob);
                current = null;
            }
        }
    }

    public record Attack(byte attackId, int cooldown, float range, Consumer<Mob> onAttackStart, Consumer<Mob> onAttackEnd, Function<Mob, Boolean> canUse)
    {

    }
}


