package com.wdiscute.echoes.perk.unique;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.perk.Perk;
import com.wdiscute.echoes.perk.PerkInstance;
import com.wdiscute.echoes.perk.SimplePerk;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RamattraPerk extends SimplePerk
{
    @Override
    public List<MutableComponent> getTooltip(ItemStack stack, PerkInstance instance)
    {
        return List.of(Component.literal("Soul Unleash").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.BOLD));
    }

    @Override
    public List<MutableComponent> getShopExtendedTooltip(ItemStack stack, PerkInstance instance)
    {
        List<MutableComponent> list = new ArrayList<>();

        list.add(Component.literal("Soul Unleash").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.BOLD));
        list.add(Component.literal("[use] to unleash souls,"));
        list.add(Component.literal("each damaging for " + Echoes.FORMAT.format(instance.getDamage())));

        return list;
    }

    @Override
    public List<String> getIds()
    {
        return List.of(Perk.DAMAGE);
    }
}
