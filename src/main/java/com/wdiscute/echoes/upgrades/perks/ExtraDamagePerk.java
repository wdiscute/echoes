package com.wdiscute.echoes.upgrades.perks;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.upgrades.Perk;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExtraDamagePerk extends SimplePerk
{
    @Override
    public float addDamage(@NotNull Player player, @NotNull ItemStack weapon, @NotNull Entity entity, List<Float> value)
    {
        return value.getFirst();
    }

    @Override
    public List<MutableComponent> getTooltip(ItemStack stack, List<Float> value)
    {
        return List.of(Component.literal("+" + Echoes.FORMAT.format(value.getFirst()) + " damage"));
    }
}
