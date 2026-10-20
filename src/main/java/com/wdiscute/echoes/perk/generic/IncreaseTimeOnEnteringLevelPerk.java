package com.wdiscute.echoes.perk.generic;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.perk.Perk;
import com.wdiscute.echoes.perk.PerkInstance;
import com.wdiscute.echoes.perk.SimplePerk;
import com.wdiscute.echoes.timeless.TimelessInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class IncreaseTimeOnEnteringLevelPerk extends SimplePerk
{
    @Override
    public void onNewInstanceEntered(ServerPlayer player, PerkInstance amplifiers, TimelessInstance instance)
    {
        instance.addTime(player.serverLevel(), (long) amplifiers.getValue());
    }

    @Override
    public List<String> getIds()
    {
        return List.of(Perk.VALUE);
    }

    @Override
    public List<MutableComponent> getTooltip(ItemStack stack, PerkInstance value)
    {
        return List.of(Component.literal("+" + Echoes.FORMAT.format(value.getValue() / 20) + " seconds"));
    }
}
