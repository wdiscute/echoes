package com.wdiscute.echoes.upgrades;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface Perk
{
    default void onEntityKilled(Player killer, ItemStack weapon, Entity entityKilled, float value)
    {

    }

    default float addDamage(Player player, ItemStack weapon, Entity entity, float value)
    {
        return 0;
    }

    Component getTooltip(float value);
}
