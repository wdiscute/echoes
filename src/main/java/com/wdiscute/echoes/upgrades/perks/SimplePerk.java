package com.wdiscute.echoes.upgrades.perks;

import com.wdiscute.echoes.upgrades.Perk;
import net.minecraft.network.chat.MutableComponent;
import org.jspecify.annotations.Nullable;

import java.util.List;

public abstract class SimplePerk extends Perk
{
    public abstract List<MutableComponent> getTooltip(List<Float> value);

    @Override
    public List<MutableComponent> getItemTooltip(List<Float> value)
    {
        return getTooltip(value);
    }

    @Override
    public @Nullable List<MutableComponent> getShopTooltip(List<Float> value)
    {
        return getTooltip(value);
    }
}
