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

public class ExtraDamageConsumesTimePerk extends SimplePerk
{
    @Override
    public float addDamage(@NotNull Player player, @NotNull ItemStack weapon, @NotNull Entity entity, List<Float> value)
    {
        TimelessInstance closest = TimelessManager.getClosest(player.level().getServer(), player.blockPosition());
        if(closest != null && closest.timeToExit != Long.MAX_VALUE && player.level() instanceof ServerLevel sl)
        {
            closest.setTime(sl, closest.timeToExit - value.getFirst().longValue());
            return value.get(1);
        }
        return 0;
    }

    @Override
    public List<MutableComponent> getTooltip(List<Float> value)
    {
        return List.of(Component.literal("Temporal Decay").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.BOLD));
    }

    @Override
    public List<MutableComponent> getShopExtendedTooltip(List<Float> value)
    {
        List<MutableComponent> list = new ArrayList<>();

        list.add(Component.literal("Temporal Decay").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.BOLD));
        list.add(Component.literal("Consumes " + Echoes.FORMAT.format(value.getFirst() / 20) + " seconds"));
        list.add(Component.literal("to do " + Echoes.FORMAT.format(value.get(1)) + " damage"));

        return list;
    }
}
