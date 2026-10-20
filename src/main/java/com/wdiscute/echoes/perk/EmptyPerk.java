package com.wdiscute.echoes.perk;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class EmptyPerk extends SimplePerk
{
    @Override
    public List<MutableComponent> getTooltip(ItemStack stack, PerkInstance instance)
    {
        return List.of();
    }

    @Override
    public List<String> getIds()
    {
        return List.of();
    }
}
