package com.wdiscute.echoes.upgrades.perks;

import com.wdiscute.echoes.upgrades.Perk;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ExtraTicksPerKillPerk implements Perk
{
    @Override
    public MutableComponent getTooltip(float value)
    {
        return Component.literal("+" + value + " seconds on each kill");
    }
}
