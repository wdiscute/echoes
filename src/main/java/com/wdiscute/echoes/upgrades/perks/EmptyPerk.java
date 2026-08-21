package com.wdiscute.echoes.upgrades.perks;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class EmptyPerk extends SimplePerk
{
    @Override
    public List<MutableComponent> getTooltip(ItemStack stack, List<Float> value)
    {
        return List.of();
    }
}
