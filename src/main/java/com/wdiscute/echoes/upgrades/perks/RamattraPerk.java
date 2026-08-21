package com.wdiscute.echoes.upgrades.perks;

import com.wdiscute.echoes.Echoes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RamattraPerk extends SimplePerk
{
    @Override
    public List<MutableComponent> getTooltip(ItemStack stack, List<Float> value)
    {
        return List.of(Component.literal("Soul Unleash").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.BOLD));
    }

    @Override
    public List<MutableComponent> getShopExtendedTooltip(ItemStack stack, List<Float> value)
    {
        List<MutableComponent> list = new ArrayList<>();

        list.add(Component.literal("Soul Unleash").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.BOLD));
        list.add(Component.literal("[use] to unleash souls,"));
        list.add(Component.literal("each damaging for " + Echoes.FORMAT.format(value.getFirst())));

        return list;
    }
}
