package com.wdiscute.echoes.upgrades.perks;

import com.wdiscute.echoes.upgrades.Perk;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.List;

public abstract class SimplePerk extends Perk
{
    public abstract List<MutableComponent> getTooltip(ItemStack stack, List<Float> value);

    @Override
    public List<MutableComponent> getItemTooltip(ItemStack stack,List<Float> value)
    {
        return getTooltip(stack, value);
    }

    @Override
    public @Nullable List<MutableComponent> getShopTooltip(ItemStack stack,List<Float> value)
    {
        return getTooltip(stack, value);
    }
}
