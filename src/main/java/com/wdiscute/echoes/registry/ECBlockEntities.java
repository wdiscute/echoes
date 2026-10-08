package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.blocks.PrismaPaneBlock;
import com.wdiscute.echoes.blocks.display.DisplayBlockEntity;
import com.wdiscute.echoes.blocks.marker.TimelessMarkerBlock;
import com.wdiscute.echoes.blocks.marker.TimelessMarkerBlockEntity;
import com.wdiscute.echoes.blocks.pane.PrismaPaneBlockEntity;
import com.wdiscute.echoes.blocks.portal.PortalBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface ECBlockEntities
{
    DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Echoes.MOD_ID);

    Supplier<BlockEntityType<PortalBlockEntity>> PORTAL = BLOCK_ENTITIES.register("portal",
            () -> BlockEntityType.Builder.of(PortalBlockEntity::new, ECBlocks.PORTAL.get()).build(null));

    Supplier<BlockEntityType<TimelessMarkerBlockEntity>> TIMELESS_MARKER = BLOCK_ENTITIES.register("timeless_marker",
            () -> BlockEntityType.Builder.of(TimelessMarkerBlockEntity::new, ECBlocks.TIMELESS_MARKER.get()).build(null));

    Supplier<BlockEntityType<PrismaPaneBlockEntity>> PRISMA_PANE = BLOCK_ENTITIES.register("prisma_pane",
            () -> BlockEntityType.Builder.of(PrismaPaneBlockEntity::new, ECBlocks.PRISMA_PANE.get()).build(null));


    Supplier<BlockEntityType<DisplayBlockEntity>> DISPLAY = BLOCK_ENTITIES.register("display",
            () -> BlockEntityType.Builder.of(DisplayBlockEntity::new, ECBlocks.DISPLAY.get()).build(null));

    static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
