package com.wdiscute.echoes.upgrades.perks;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.timeless.TimelessData;
import com.wdiscute.echoes.upgrades.Perk;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ExtraDamageConsumesSoulsPerk extends SimplePerk
{
    @Override
    public float addDamage(@NotNull Player player, @NotNull ItemStack weapon, @NotNull Entity entity, List<Float> value)
    {
        float damageToAdd = value.getFirst();
        float soulsToConsume = value.get(1);

        if(TimelessData.consumeSouls(player, soulsToConsume))
            return damageToAdd;
        return 0;
    }

    @Override
    public List<MutableComponent> getTooltip(List<Float> value)
    {
        return List.of(Component.literal("Soulrend").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.BOLD));
    }

    @Override
    public List<MutableComponent> getShopExtendedTooltip(List<Float> value)
    {
        List<MutableComponent> list = new ArrayList<>();

        list.add(Component.literal("Soulrend").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.BOLD));
        list.add(Component.literal("Consumes " + Echoes.FORMAT.format(value.get(1)) + " souls"));
        list.add(Component.literal("to do " + Echoes.FORMAT.format(value.getFirst()) + " damage"));

        return list;
    }
}
