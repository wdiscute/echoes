package com.wdiscute.echoes.upgrades.perks;

import com.wdiscute.echoes.timeless.TimelessInstance;
import com.wdiscute.echoes.timeless.TimelessManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExtraTicksPerKillPerk extends SimplePerk
{
    @Override
    public void onEntityKilled(@NotNull Player killer, @NotNull ItemStack weapon, @NotNull Entity entityKilled, List<Float> value)
    {
        TimelessInstance closest = TimelessManager.getClosest(killer.level().getServer(), killer.blockPosition());
        if(closest != null && closest.timeToExit != Long.MAX_VALUE)
            closest.timeToExit += value.getFirst().longValue();
        super.onEntityKilled(killer, weapon, entityKilled, value);
    }

    @Override
    public List<MutableComponent> getTooltip(List<Float> value)
    {
        return List.of(Component.literal("+" + value.getFirst() / 20 + " seconds on each kill"));
    }
}
