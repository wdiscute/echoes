package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.utils.item.BasicItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface ECItems
{

    DeferredRegister.Items ITEMS = DeferredRegister.createItems(Echoes.MOD_ID);

    DeferredItem<Item> SCULK_TISSUE = ITEMS.registerItem("sculk_tissue", Item::new);

    static void register(IEventBus modEventBus)
    {
        ITEMS.register(modEventBus);
    }
}
