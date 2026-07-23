package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.registry.blocks.portal.PortalBlock;
import com.wdiscute.echoes.registry.blocks.portal.PortalFrameBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public interface ECBlocks
{
    DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Echoes.MOD_ID);

    DeferredBlock<Block> PORTAL_FRAME = register("portal_frame", PortalFrameBlock::new);
    DeferredBlock<Block> PORTAL = register("portal", PortalBlock::new);

    static DeferredBlock<Block> register(String name, Function<BlockBehaviour.Properties, ? extends Block> suplier)
    {
        DeferredBlock<Block> block = BLOCKS.registerBlock(name, suplier);
        ECItems.ITEMS.registerSimpleBlockItem(block);
        return block;
    }

    static void register(IEventBus modEventBus)
    {
        BLOCKS.register(modEventBus);;
    }
}
