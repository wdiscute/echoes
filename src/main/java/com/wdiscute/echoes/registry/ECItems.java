package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.item.EchoBladeItem;
import com.wdiscute.echoes.item.TimelessWeaponItem;
import com.wdiscute.echoes.item.RamattraItem;
import com.wdiscute.echoes.item.SoulHeartContainer;
import com.wdiscute.utils.item.BasicEquipableItem;
import com.wdiscute.utils.item.BasicItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface ECItems
{

    DeferredRegister.Items ITEMS = DeferredRegister.createItems(Echoes.MOD_ID);

    DeferredItem<Item> SOUL_HEART_CONTAINER = ITEMS.registerItem("soul_heart_container", SoulHeartContainer::new);
    DeferredItem<Item> SOUL = ITEMS.registerItem("soul", BasicItem::new);


    //
    //,--.   ,--.  ,---.   ,--,--.  ,---.   ,---.  ,--,--,   ,---.
    //|  |.'.|  | | .-. : ' ,-.  | | .-. | | .-. | |      \ (  .-'
    //|   .'.   | \   --. \ '-'  | | '-' ' ' '-' ' |  ||  | .-'  `)
    //'--'   '--'  `----'  `--`--' |  |-'   `---'  `--''--' `----'
    //                             `--'

    DeferredItem<Item> ECHO_BLADE = ITEMS.registerItem("echo_blade", (p) -> new EchoBladeItem(p, -2.4f));

    //sculk
    DeferredItem<Item> RAMATTRA = ITEMS.registerItem("ramattra", RamattraItem::new);
    DeferredItem<Item> TIME_REAPER = ITEMS.registerItem("time_reaper", (p) -> new TimelessWeaponItem(p, -2.4f));
    DeferredItem<Item> GLOOMBRINGER = ITEMS.registerItem("gloombringer", (p) -> new TimelessWeaponItem(p, -3.4F));

    //prisma
    DeferredItem<Item> LUCENT_WILL = ITEMS.registerItem("lucent_will", (p) -> new TimelessWeaponItem(p, -2.4f));
    DeferredItem<Item> TIME_KEEPER = ITEMS.registerItem("time_keeper", (p) -> new TimelessWeaponItem(p, -2.4f));


    //                      ,--.                   ,--.          ,--.
    //,--,--,--.  ,--,--. ,-'  '-.  ,---.  ,--.--. `--'  ,--,--. |  |  ,---.
    //|        | ' ,-.  | '-.  .-' | .-. : |  .--' ,--. ' ,-.  | |  | (  .-'
    //|  |  |  | \ '-'  |   |  |   \   --. |  |    |  | \ '-'  | |  | .-'  `)
    //`--`--`--'  `--`--'   `--'    `----' `--'    `--'  `--`--' `--' `----'
    //

    //sculk
    DeferredItem<Item> SCULK_SPAWN = ITEMS.registerItem("sculk_spawn", BasicItem::new);
    DeferredItem<Item> HOLLOWED_SPINE = ITEMS.registerItem("hollowed_spine", BasicItem::new);
    DeferredItem<Item> SCULKED_TEETH = ITEMS.registerItem("sculked_teeth", BasicItem::new);
    //it's a block so cant be here -> DeferredItem<Item> SCULKED_TENDRIL
    DeferredItem<Item> ECHOING_MARROW = ITEMS.registerItem("echoing_marrow", BasicItem::new);
    DeferredItem<Item> ROT_BRAIN = ITEMS.registerItem("rot_brain", BasicItem::new); // rare drop from sculked

    //prisma
    DeferredItem<Item> PRISMA_SHARD = ITEMS.registerItem("prisma_shard", BasicItem::new);
    DeferredItem<Item> LATTICE = ITEMS.registerItem("lattice", BasicItem::new);
    DeferredItem<Item> LUCENT_SHARD = ITEMS.registerItem("lucent_shard", BasicItem::new);
    DeferredItem<Item> CRYSTAL_CORE = ITEMS.registerItem("crystal_core", BasicItem::new);
    DeferredItem<Item> LUCENT_DIE = ITEMS.registerItem("lucent_die", BasicItem::new);

    //
    //
    // ,--,--. ,--.--. ,--,--,--.  ,---.  ,--.--.
    //' ,-.  | |  .--' |        | | .-. | |  .--'
    //\ '-'  | |  |    |  |  |  | ' '-' ' |  |
    // `--`--' `--'    `--`--`--'  `---'  `--'
    //

    DeferredItem<Item> TIMELOST_HELMET = ITEMS.registerItem("timelost_helmet", (p) -> new BasicEquipableItem(p, EquipmentSlot.HEAD, Echoes.rl("timelost")));
    DeferredItem<Item> TIMELOST_CHESTPLATE = ITEMS.registerItem("timelost_chestplate", (p) -> new BasicEquipableItem(p, EquipmentSlot.CHEST, Echoes.rl("timelost")));
    DeferredItem<Item> TIMELOST_LEGGINGS = ITEMS.registerItem("timelost_leggings", (p) -> new BasicEquipableItem(p, EquipmentSlot.LEGS, Echoes.rl("timelost")));
    DeferredItem<Item> TIMELOST_BOOTS = ITEMS.registerItem("timelost_boots", (p) -> new BasicEquipableItem(p, EquipmentSlot.FEET, Echoes.rl("timelost")));

    static void register(IEventBus modEventBus)
    {
        ITEMS.register(modEventBus);
    }
}
