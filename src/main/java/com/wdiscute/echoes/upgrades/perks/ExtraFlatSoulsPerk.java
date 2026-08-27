package com.wdiscute.echoes.upgrades.perks;

import com.wdiscute.echoes.Echoes;
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
    public List<MutableComponent> getTooltip(ItemStack stack, List<Float> value)
    {
        return List.of(Component.literal((value.getFirst() > 0 ? "+" : "-") + Echoes.FORMAT.format(value.getFirst()) + " souls per kill"));
    }
}
