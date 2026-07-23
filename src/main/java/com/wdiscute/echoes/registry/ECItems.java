package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.utils.item.BasicItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface ECItems
{

    DeferredRegister.Items ITEMS = DeferredRegister.createItems(Echoes.MOD_ID);

    static void register(IEventBus modEventBus)
    {
        ITEMS.register(modEventBus);
    }
}
