package com.wdiscute.echoes.perks;

import com.wdiscute.echoes.upgrades.PerkInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ExtraFlatSoulsPerk extends SimplePerk
{
    @Override
    public float addFlatSouls(Player player, ItemStack weapon, LivingEntity entityKilled, List<Float> amplifier, float currentSouls)
    {
        return amplifier.getFirst();
    }

    @Override
    public List<MutableComponent> getTooltip(ItemStack stack, List<Float> amplifiers)
    {
        return List.of(Component.literal(formatAmplifier(amplifiers, 0) + " souls per kill"));
    }
}
