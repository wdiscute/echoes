package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.item.EchoBladeItem;
import com.wdiscute.echoes.item.RamattraItem;
import com.wdiscute.echoes.item.SoulHeartContainer;
import com.wdiscute.echoes.upgrades.PerkInstance;
import com.wdiscute.utils.item.BasicItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public interface ECItems
{

    DeferredRegister.Items ITEMS = DeferredRegister.createItems(Echoes.MOD_ID);

    DeferredItem<Item> SCULK_TISSUE = ITEMS.registerItem("sculk_tissue", BasicItem::new);
    DeferredItem<Item> SOUL_HEART_CONTAINER = ITEMS.registerItem("soul_heart_container", SoulHeartContainer::new);


    //weapons
    DeferredItem<Item> ECHO_BLADE = ITEMS.registerItem("echo_blade", EchoBladeItem::new);

    DeferredItem<Item> RAMATTRA = ITEMS.registerItem("ramattra", RamattraItem::new);


    static void register(IEventBus modEventBus)
    {
        ITEMS.register(modEventBus);
    }
}
