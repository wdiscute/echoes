package com.wdiscute.echoes.upgrades.perks;

import com.wdiscute.echoes.upgrades.Perk;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ExtraDamagePerk implements Perk
{

    @Override
    public float addDamage(Player player, ItemStack weapon, Entity entity, float value)
    {
        return value;
    }

    @Override
    public MutableComponent getTooltip(float value)
    {
        return Component.literal("+" + value + " damage");
    }
}
