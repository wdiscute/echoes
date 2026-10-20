package com.wdiscute.echoes.perk;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public abstract class SimplePerk extends Perk
{
    public abstract List<MutableComponent> getTooltip(ItemStack stack, PerkInstance instance);

    @Override
    public List<MutableComponent> getItemTooltip(ItemStack stack, PerkInstance instance)
    {
        return getTooltip(stack, instance);
    }

    @Override
    public List<MutableComponent> getShopTooltip(ItemStack stack, PerkInstance instance)
    {
        return getTooltip(stack, instance);
    }
}
