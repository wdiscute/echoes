package com.wdiscute.echoes.perk.generic;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.perk.Perk;
import com.wdiscute.echoes.perk.PerkInstance;
import com.wdiscute.echoes.perk.SimplePerk;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ExtraPercentageSoulsPerk extends SimplePerk
{
    @Override
    public float addPercentSouls(Player player, ItemStack weapon, LivingEntity entityKilled, PerkInstance instance, float currentSouls)
    {
        return instance.getValue();
    }

    @Override
    public List<String> getIds()
    {
        return List.of(Perk.VALUE);
    }

    @Override
    public List<MutableComponent> getTooltip(ItemStack stack, PerkInstance instance)
    {
        //if value is 1.5 => +50% souls gathered
        //if value is 0.7 => -30% souls gathered
        float value = instance.getValue();
        if (value == 1)
            return null;

        if (value > 1)
            return List.of(Component.literal("+" + Echoes.FORMAT.format((value - 1) * 100) + "% souls gathered"));
        else
            return List.of(Component.literal("-" + Echoes.FORMAT.format((1 - value) * 100) + "% souls gathered"));
    }
}
