package com.wdiscute.echoes.upgrades.perks;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.timeless.TimelessInstance;
import com.wdiscute.echoes.timeless.TimelessManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ExtraTicksPerKillPerk extends SimplePerk
{
    @Override
    public void onEntityKilled(@NotNull Player killer, @NotNull ItemStack weapon, @NotNull Entity entityKilled, List<Float> value)
    {
        TimelessInstance closest = TimelessManager.getClosest(killer.level().getServer(), killer.blockPosition());
        if(closest != null && closest.timeToExit != Long.MAX_VALUE && killer.level() instanceof ServerLevel sl)
            closest.setTime(sl, closest.timeToExit + value.getFirst().longValue());
        super.onEntityKilled(killer, weapon, entityKilled, value);
    }

    @Override
    public List<MutableComponent> getTooltip(List<Float> value)
    {
        return List.of(Component.literal("Timeless Souls").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.BOLD));
    }


    @Override
    public List<MutableComponent> getShopExtendedTooltip(List<Float> value)
    {
        List<MutableComponent> list = new ArrayList<>();

        list.add(Component.literal("Timeless Souls").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.BOLD));
        list.add(Component.literal("Harvesting sculk enemies extends"));
        list.add(Component.literal("the rift's duration for " + Echoes.FORMAT.format(value.getFirst() / 20) + " seconds"));

        return list;
    }
}
