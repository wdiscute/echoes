package com.wdiscute.echoes.perk.generic;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.perk.Perk;
import com.wdiscute.echoes.perk.PerkInstance;
import com.wdiscute.echoes.perk.SimplePerk;
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
    public void onEntityKilled(@NotNull Player killer, @NotNull ItemStack weapon, @NotNull Entity entityKilled, PerkInstance instance)
    {
        TimelessInstance closest = TimelessManager.getClosest(killer.level().getServer(), killer.blockPosition());
        if(closest != null && closest.timeToExit != Long.MAX_VALUE && killer.level() instanceof ServerLevel sl)
            closest.setTime(sl, closest.timeToExit + (long) instance.getValue());
        super.onEntityKilled(killer, weapon, entityKilled, instance);
    }

    @Override
    public List<MutableComponent> getTooltip(ItemStack stack, PerkInstance value)
    {
        return List.of(Component.literal("Timeless Souls").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.BOLD));
    }

    @Override
    public String format(PerkInstance instance, String id)
    {
        return "[" + Echoes.FORMAT.format(instance.get(id) / 20) + "]";
    }

    @Override
    public List<MutableComponent> getShopExtendedTooltip(ItemStack stack, PerkInstance amplifiers)
    {
        List<MutableComponent> list = new ArrayList<>();

        list.add(Component.literal("Timeless Souls").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.BOLD));
        list.add(Component.literal("Killing enemies extends"));
        list.add(Component.literal("the rift's duration for " + format(amplifiers, Perk.VALUE) + " seconds"));

        return list;
    }

    @Override
    public List<String> getIds()
    {
        return List.of(Perk.VALUE);
    }
}
