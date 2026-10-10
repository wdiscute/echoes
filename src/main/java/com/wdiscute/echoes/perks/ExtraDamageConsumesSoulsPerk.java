package com.wdiscute.echoes.perks;

import com.wdiscute.echoes.timeless.TimelessData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ExtraDamageConsumesSoulsPerk extends SimplePerk
{
    //first value = souls consumed
    //second value = damage added

    @Override
    public float addDamage(@NotNull Player player, @NotNull ItemStack weapon, @NotNull Entity entity, List<Float> value)
    {
        float damageToAdd = value.get(1);
        float soulsToConsume = value.get(0);

        if(TimelessData.consumeSouls(player, soulsToConsume))
            return damageToAdd;
        return 0;
    }

    @Override
    public List<MutableComponent> getTooltip(ItemStack stack, List<Float> value)
    {
        return List.of(Component.literal("Soulrend").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.BOLD));
    }

    @Override
    public List<MutableComponent> getShopExtendedTooltip(ItemStack stack, List<Float> amplifiers)
    {
        List<MutableComponent> list = new ArrayList<>();

        list.add(Component.literal("Soulrend").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.BOLD));
        list.add(Component.literal("Consumes " + formatAmplifier(amplifiers, 0) + " souls to do " + formatAmplifier(amplifiers, 1) + " damage"));

        return list;
    }
}
