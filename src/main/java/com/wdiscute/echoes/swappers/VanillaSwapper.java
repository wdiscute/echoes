package com.wdiscute.echoes.swappers;

import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class VanillaSwapper extends Swapper
{
    @Override
    public void handle(Player player, Map<ResourceLocation, MaybeStack> inventoryToStore, Map<ResourceLocation, MaybeStack> storedInventory)
    {
        //for 100 slots in vanilla inventory
        for (int i = 0; i < 100; i++)
        {
            //get item currently in inventory and store it in the map
            ItemStack item = player.getInventory().getItem(i);
            inventoryToStore.put(Utils.rl("" + i), new MaybeStack(item));

            //get the item previously stored, and set the player inventory to that
            MaybeStack storedStack = storedInventory.getOrDefault(Utils.rl("" + i), MaybeStack.EMPTY);
            player.getInventory().setItem(i, storedStack.toStack());
        }
    }
}
