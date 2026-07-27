package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.blocks.portal.PortalBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public interface ECBlocks
{
    DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Echoes.MOD_ID);

    DeferredBlock<Block> PORTAL = register("portal", PortalBlock::new);

    DeferredBlock<Block> GLEEMSLATE_PILLAR = register("gleemslate_pillar", RotatedPillarBlock::new);
    DeferredBlock<Block> GLEEMSLATE_TILES = register("gleemslate_tiles", Block::new);

    DeferredBlock<Block> SCULK_PILLAR = register("sculk_pillar", RotatedPillarBlock::new);
    DeferredBlock<Block> SCULK_SLAB = register("sculk_slab", SlabBlock::new);

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
