package com.wdiscute.echoes.swappers;

import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.Map;

public class CuriosSwapper extends Swapper
{
    @Override
    public void handle(Player player, Map<ResourceLocation, MaybeStack> inventoryToStore, Map<ResourceLocation, MaybeStack> storedInventory)
    {
        CuriosApi.getCuriosInventory(player).ifPresent(handler ->
        {
            for (Map.Entry<String, ICurioStacksHandler> entry : handler.getCurios().entrySet())
            {
                String slotId = entry.getKey();
                ICurioStacksHandler stacksHandler = entry.getValue();

                swap(slotId, false, stacksHandler.getStacks(), inventoryToStore, storedInventory);
                swap(slotId, true, stacksHandler.getCosmeticStacks(), inventoryToStore, storedInventory);
            }
        });
    }

    private static void swap(String slotId, boolean cosmetic, IDynamicStackHandler stacks, Map<ResourceLocation, MaybeStack> inventoryToStore, Map<ResourceLocation, MaybeStack> storedInventory)
    {
        for (int i = 0; i < stacks.getSlots(); i++)
        {
            ResourceLocation key = key(slotId, cosmetic, i);

            //store what is currently equipped
            ItemStack current = stacks.getStackInSlot(i);
            if (!current.isEmpty())
                inventoryToStore.put(key, new MaybeStack(current));

            //load what was previously stored (or clear the slot)
            stacks.setStackInSlot(i, storedInventory.getOrDefault(key, MaybeStack.EMPTY).toStack());
        }
    }

    private static ResourceLocation key(String slotId, boolean cosmetic, int index)
    {
        return Utils.rl("curios", slotId + (cosmetic ? "/cosmetic/" : "/") + index);
    }
}