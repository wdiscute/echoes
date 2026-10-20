package com.wdiscute.echoes.perk.generic;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.perk.Perk;
import com.wdiscute.echoes.perk.PerkInstance;
import com.wdiscute.echoes.perk.SimplePerk;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class FlatReduceDamageTakenPerk extends SimplePerk
{
    @Override
    public float reduceDamage(Player player, PerkInstance instance)
    {
        return instance.getValue();
    }

    @Override
    public List<String> getIds()
    {
        return List.of(Perk.VALUE);
    }

    @Override
    public List<MutableComponent> getTooltip(ItemStack stack, PerkInstance value)
    {
        return List.of(Component.literal("-" + Echoes.FORMAT.format(value.getValue()) + " damage taken"));
    }
}
