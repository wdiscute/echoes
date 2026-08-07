package com.wdiscute.echoes.upgrades.perks;

import com.wdiscute.echoes.upgrades.Perk;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ExtraFlatSoulsPerk extends Perk
{
    @Override
    public float addSouls(Player player, ItemStack weapon, LivingEntity entityKilled, float amplifier, float currentSouls)
    {
        return amplifier;
    }

    @Override
    public MutableComponent getTooltip(float value)
    {
        return Component.literal((value > 0 ? "+" : "-") + value + " souls per kill");
    }
}
