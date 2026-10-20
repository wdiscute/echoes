package com.wdiscute.echoes.anomaly;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class Anomaly
{
    public boolean canApply(ItemStack stack)
    {
        return true;
    }

    public void onDamageTaken(Player player, ItemStack stack)
    {
    }

    public void onDeath(Player player, ItemStack stack)
    {
    }

    public void onSoulCollected(Player player, ItemStack stack)
    {
    }

    public void onSoulSpent(Player player, ItemStack stack)
    {
    }

    public void onEnemyKilled(Player player, ItemStack stack)
    {
    }

    public void onEnemyDamaged(Player player, ItemStack stack)
    {
    }
}
