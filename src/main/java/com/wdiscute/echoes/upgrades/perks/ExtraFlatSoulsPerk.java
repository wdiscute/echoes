package com.wdiscute.echoes.upgrades.perks;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ExtraFlatSoulsPerk extends SimplePerk
{
    @Override
    public float addSouls(Player player, ItemStack weapon, LivingEntity entityKilled, List<Float> amplifier, float currentSouls)
    {
        return amplifier.getFirst();
    }

    @Override
    public List<MutableComponent> getTooltip(List<Float> value)
    {
        return List.of(Component.literal((value.getFirst() > 0 ? "+" : "-") + value.getFirst() + " souls per kill"));
    }
}
