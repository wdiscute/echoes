package com.wdiscute.echoes.upgrades.perks;

import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public class EmptyPerk extends SimplePerk
{
    @Override
    public List<MutableComponent> getTooltip(List<Float> value)
    {
        return List.of();
    }
}
