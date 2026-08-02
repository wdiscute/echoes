package com.wdiscute.echoes.upgrades.perks;

import com.wdiscute.echoes.upgrades.Perk;
import net.minecraft.network.chat.Component;

public class ExtraSculkDamagePerk implements Perk
{
    @Override
    public Component getTooltip(float value)
    {
        return Component.literal("+" + value + " sculk-aligned damage");
    }
}
