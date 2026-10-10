package com.wdiscute.echoes.perks;

import com.wdiscute.echoes.upgrades.Perk;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public abstract class SimplePerk extends Perk
{
    public abstract List<MutableComponent> getTooltip(ItemStack stack, List<Float> amplifiers);

    @Override
    public List<MutableComponent> getItemTooltip(ItemStack stack,List<Float> amplifiers)
    {
        return getTooltip(stack, amplifiers);
    }

    @Override
    public List<MutableComponent> getShopTooltip(ItemStack stack,List<Float> amplifiers)
    {
        return getTooltip(stack, amplifiers);
    }
}
