package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.blocks.portal.PortalFrameBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface ECBlockEntities
{
    DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Echoes.MOD_ID);

    Supplier<BlockEntityType<PortalFrameBlockEntity>> PORTAL = BLOCK_ENTITIES.register("portal",
            () -> new BlockEntityType<>(PortalFrameBlockEntity::new, ECBlocks.PORTAL_FRAME.get()));

    static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
