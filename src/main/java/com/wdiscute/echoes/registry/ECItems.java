package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.upgrades.PerkInstance;
import com.wdiscute.utils.Utils;
import com.wdiscute.utils.item.BasicItem;
import com.wdiscute.utils.item.SingleStackBasicItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public interface ECItems
{

    DeferredRegister.Items ITEMS = DeferredRegister.createItems(Echoes.MOD_ID);

    DeferredItem<Item> SCULK_TISSUE = ITEMS.registerItem("sculk_tissue", BasicItem::new);

    //weapons
    DeferredItem<Item> ECHO_BLADE = ITEMS.registerItem("echo_blade", (p) -> new SingleStackBasicItem(
            p.component(ECDataComponents.PERKS, List.of(
                    new PerkInstance(ECPerks.EXTRA_DAMAGE, 3),
                    new PerkInstance(ECPerks.EXTRA_PERCENTAGE_SOULS, 1.6f)
            )
    )));

    static void register(IEventBus modEventBus)
    {
        ITEMS.register(modEventBus);
    }
}
