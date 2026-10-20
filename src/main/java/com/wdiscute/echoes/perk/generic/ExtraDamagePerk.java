package com.wdiscute.echoes.perk.generic;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.perk.Perk;
import com.wdiscute.echoes.perk.PerkInstance;
import com.wdiscute.echoes.perk.SimplePerk;
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
    public float addDamage(@NotNull Player player, @NotNull ItemStack weapon, @NotNull Entity entity, PerkInstance instance)
    {
        return instance.getDamage();
    }

    @Override
    public List<String> getIds()
    {
        return List.of(Perk.DAMAGE);
    }

    @Override
    public List<MutableComponent> getTooltip(ItemStack stack, PerkInstance value)
    {
        return List.of(Component.literal("+" + Echoes.FORMAT.format(value.getDamage()) + " damage"));
    }
}
