package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.item.EchoWeaponItem;
import com.wdiscute.echoes.item.RamattraItem;
import com.wdiscute.echoes.item.SoulHeartContainer;
import com.wdiscute.utils.item.BasicItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface ECItems
{

    DeferredRegister.Items ITEMS = DeferredRegister.createItems(Echoes.MOD_ID);

    DeferredItem<Item> SOUL_HEART_CONTAINER = ITEMS.registerItem("soul_heart_container", SoulHeartContainer::new);


    //weapons
    //sculk
    DeferredItem<Item> ECHO_BLADE = ITEMS.registerItem("echo_blade", (p) -> new EchoWeaponItem(p, -2.4f));
    DeferredItem<Item> RAMATTRA = ITEMS.registerItem("ramattra", RamattraItem::new);
    DeferredItem<Item> TIME_REAPER = ITEMS.registerItem("time_reaper", (p) -> new EchoWeaponItem(p, -2.4f));
    DeferredItem<Item> GLOOMBRINGER = ITEMS.registerItem("gloombringer", (p) -> new EchoWeaponItem(p, -3.4F));

    //prisma
    DeferredItem<Item> PRISMA_SWORD = ITEMS.registerItem("prisma_sword", (p) -> new EchoWeaponItem(p, -2.4f));
    DeferredItem<Item> LUCENT_WILL = ITEMS.registerItem("lucent_will", (p) -> new EchoWeaponItem(p, -2.4f));
    DeferredItem<Item> TIME_KEEPER = ITEMS.registerItem("time_keeper", (p) -> new EchoWeaponItem(p, -2.4f));


    //materials
    //sculk
    DeferredItem<Item> SCULK_TISSUE = ITEMS.registerItem("sculk_tissue", BasicItem::new);
    DeferredItem<Item> HOLLOWED_SPINE = ITEMS.registerItem("hollowed_spine", BasicItem::new);
    DeferredItem<Item> SCULKED_TEETH = ITEMS.registerItem("sculked_teeth", BasicItem::new);
    DeferredItem<Item> SCULK_TENDRIL = ITEMS.registerItem("sculk_tendril", BasicItem::new);
    DeferredItem<Item> ECHOING_MARROW = ITEMS.registerItem("echoing_marrow", BasicItem::new);
    DeferredItem<Item> ROT_BRAIN = ITEMS.registerItem("rot_brain", BasicItem::new); // rare drop from sculked

    //prisma
    DeferredItem<Item> PRISMA_SHARD = ITEMS.registerItem("prisma_shard", BasicItem::new);
    DeferredItem<Item> LATTICE = ITEMS.registerItem("lattice", BasicItem::new);
    DeferredItem<Item> LUCENT_SHARD = ITEMS.registerItem("lucent_shard", BasicItem::new);
    DeferredItem<Item> CRYSTAL_CORE = ITEMS.registerItem("crystal_core", BasicItem::new);
    DeferredItem<Item> LUCENT_DIE = ITEMS.registerItem("lucent_die", BasicItem::new);

    static void register(IEventBus modEventBus)
    {
        ITEMS.register(modEventBus);
    }
}
