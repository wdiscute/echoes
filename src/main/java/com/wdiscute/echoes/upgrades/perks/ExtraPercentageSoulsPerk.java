package com.wdiscute.echoes.upgrades.perks;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.upgrades.Perk;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class ExtraPercentageSoulsPerk extends Perk
{
    @Override
    public float addSouls(Player player, ItemStack weapon, LivingEntity entityKilled, float amplifier, float currentSouls)
    {
        return currentSouls * amplifier;
    }

    @Override
    public @Nullable MutableComponent getTooltip(float value)
    {
        //if value is 1.5 => +50% souls gathered
        //if value is 0.7 => -30% souls gathered
        if (value == 1)
            return null;

        if (value > 1)
            return Component.literal("+" + Echoes.FORMAT.format((value - 1) * 100) + "% souls gathered");
        else
            return Component.literal("-" + Echoes.FORMAT.format((1 - value) * 100) + "% souls gathered");
    }
}
