package com.wdiscute.echoes.upgrades.perks;

import com.wdiscute.echoes.upgrades.Perk;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ExtraSculkDamagePerk implements Perk
{
    @Override
    public MutableComponent getTooltip(float value)
    {
        return Component.literal("+" + value + " sculk-aligned damage");
    }
}
