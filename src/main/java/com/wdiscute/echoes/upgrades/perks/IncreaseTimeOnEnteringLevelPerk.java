package com.wdiscute.echoes.upgrades.perks;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.timeless.TimelessInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class IncreaseTimeOnEnteringLevelPerk extends SimplePerk
{
    @Override
    public void onNewInstanceEntered(ServerPlayer player, List<Float> amplifiers, TimelessInstance instance)
    {
        instance.addTime(player.level(), amplifiers.getFirst().longValue());
    }

    @Override
    public List<MutableComponent> getTooltip(ItemStack stack, List<Float> value)
    {
        return List.of(Component.literal("+" + Echoes.FORMAT.format(value.getFirst() / 20) + " seconds"));
    }
}
