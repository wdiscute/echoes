package com.wdiscute.echoes.timeless;

import com.wdiscute.echoes.perk.Perk;
import com.wdiscute.echoes.perk.PerkInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SoulsApi
{
    public static int calculateSouls(Player player, ItemStack weapon, float baseSouls)
    {
        float soulsToSpawn = baseSouls;
        float percentSouls = 1;

        List<PerkInstance> activePerks = Perk.getActivePerks(player, weapon);
        for (PerkInstance activePerk : activePerks)
            soulsToSpawn += activePerk.perk().addFlatSouls(player, ItemStack.EMPTY, null, activePerk, baseSouls);

        for (PerkInstance activePerk : activePerks)
            percentSouls += activePerk.perk().addPercentSouls(player, ItemStack.EMPTY, null, activePerk, soulsToSpawn);

        soulsToSpawn *= percentSouls;
        return (int) (player.level().getRandom().nextFloat() < soulsToSpawn % 1 ? soulsToSpawn + 1 : soulsToSpawn);
    }
}
