package com.wdiscute.echoes.swappers;

import com.wdiscute.utils.MaybeStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public abstract class Swapper
{
    public abstract void handle(Player player,
                                Map<ResourceLocation, MaybeStack> inventoryToStore,
                                Map<ResourceLocation, MaybeStack> storedInventory);
}
