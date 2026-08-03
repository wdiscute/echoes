package com.wdiscute.echoes.upgrades.perks;

import com.wdiscute.echoes.upgrades.Perk;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class EmptyPerk implements Perk
{
    @Override
    public MutableComponent getTooltip(float value)
    {
        return Component.literal("nothing");
    }
}
