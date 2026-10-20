package com.wdiscute.echoes.perk.generic;

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

public class ExtraDamageConsumesTimePerk extends SimplePerk
{
    @Override
    public float addDamage(@NotNull Player player, @NotNull ItemStack weapon, @NotNull Entity entity, PerkInstance instance)
    {
        TimelessInstance closest = TimelessManager.getClosest(player.level().getServer(), player.blockPosition());
        if(closest != null && closest.timeToExit != Long.MAX_VALUE && player.level() instanceof ServerLevel sl)
        {
            closest.setTime(sl, closest.timeToExit - (long) instance.getCost());
            return instance.getDamage();
        }
        return 0;
    }

    @Override
    public List<MutableComponent> getTooltip(ItemStack stack, PerkInstance instance)
    {
        return List.of(Component.literal("Temporal Decay").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.BOLD));
    }

    @Override
    public List<String> getIds()
    {
        return List.of(Perk.COST, Perk.DAMAGE);
    }

    @Override
    public List<MutableComponent> getShopExtendedTooltip(ItemStack stack, PerkInstance instance)
    {
        List<MutableComponent> list = new ArrayList<>();

        list.add(Component.literal("Temporal Decay").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.BOLD));
        list.add(Component.literal("Consumes " + format(instance, Perk.COST) + " seconds"));
        list.add(Component.literal("to do " + format(instance, Perk.DAMAGE) + " damage"));

        return list;
    }
}
