package com.wdiscute.echoes.upgrades.perks;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.upgrades.Perk;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public class ExtraPercentageSoulsPerk extends SimplePerk
{
    @Override
    public float addSouls(Player player, ItemStack weapon, LivingEntity entityKilled, List<Float> amplifier, float currentSouls)
    {
        return currentSouls * amplifier.getFirst();
    }

    @Override
    public List<MutableComponent> getTooltip(List<Float> value)
    {
        //if value is 1.5 => +50% souls gathered
        //if value is 0.7 => -30% souls gathered
        Float value1 = value.getFirst();
        if (value1 == 1)
            return null;

        if (value1 > 1)
            return List.of(Component.literal("+" + Echoes.FORMAT.format((value1 - 1) * 100) + "% souls gathered"));
        else
            return List.of(Component.literal("-" + Echoes.FORMAT.format((1 - value1) * 100) + "% souls gathered"));
    }
}
