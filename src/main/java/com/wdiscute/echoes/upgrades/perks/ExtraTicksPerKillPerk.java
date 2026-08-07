package com.wdiscute.echoes.upgrades.perks;

import com.wdiscute.echoes.timeless.TimelessInstance;
import com.wdiscute.echoes.timeless.TimelessManager;
import com.wdiscute.echoes.upgrades.Perk;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ExtraTicksPerKillPerk extends Perk
{
    @Override
    public void onEntityKilled(@NotNull Player killer, @NotNull ItemStack weapon, @NotNull Entity entityKilled, float value)
    {
        TimelessInstance closest = TimelessManager.getClosest(killer.level().getServer(), killer.blockPosition());
        if(closest != null && closest.timeToExit != Long.MAX_VALUE)
            closest.timeToExit += (long) value;
        super.onEntityKilled(killer, weapon, entityKilled, value);
    }

    @Override
    public MutableComponent getTooltip(float value)
    {
        return Component.literal("+" + value / 20 + " seconds on each kill");
    }
}
