package com.wdiscute.echoes.perk.generic;

import com.wdiscute.echoes.perk.Perk;
import com.wdiscute.echoes.perk.PerkInstance;
import com.wdiscute.echoes.perk.SimplePerk;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ExtraFlatSoulsPerk extends SimplePerk
{
    @Override
    public float addFlatSouls(Player player, ItemStack weapon, LivingEntity entityKilled, PerkInstance instance, float currentSouls)
    {
        return instance.getValue();
    }

    @Override
    public List<String> getIds()
    {
        return List.of(Perk.VALUE);
    }

    @Override
    public List<MutableComponent> getTooltip(ItemStack stack, PerkInstance insance)
    {
        return List.of(Component.literal(format(insance, Perk.VALUE) + " souls per kill"));
    }
}
