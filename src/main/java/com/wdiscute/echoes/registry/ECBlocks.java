package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.blocks.*;
import com.wdiscute.echoes.blocks.display.DisplayBlock;
import com.wdiscute.echoes.blocks.marker.TimelessMarkerBlock;
import com.wdiscute.echoes.blocks.portal.PortalBlock;
import net.minecraft.data.BlockFamily;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public interface ECBlocks
{
    DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Echoes.MOD_ID);

    //timeless
    DeferredBlock<Block> PORTAL = register("portal", PortalBlock::new);
    DeferredBlock<Block> TIMELESS_MARKER = register("timeless_marker", TimelessMarkerBlock::new);
    DeferredBlock<Block> DISPLAY = register("display", DisplayBlock::new);


    //prisma
    DeferredBlock<Block> PRISMA_PANE = register("prisma_pane", PrismaPaneBlock::new);

    //gleemslate
    DeferredBlock<Block> GLEEMSLATE_PILLAR = register("gleemslate_pillar", (p) -> new RotatedPillarBlock(p.sound(SoundType.AMETHYST).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    DeferredBlock<Block> TRIMMED_GLEEMSLATE = register("trimmed_gleemslate", (p) -> new RotatedPillarBlock(p.sound(SoundType.AMETHYST).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    DeferredBlock<Block> CHISELED_GLEEMSLATE = register("chiseled_gleemslate", GleemslateBlock::new);
    DeferredBlock<Block> GLEEMSLATE_GRASS = register("gleemslate_grass", GleemslateBlock::new);

    //gleemslate
    DeferredBlock<Block> GLEEMSLATE = register("gleemslate", GleemslateBlock::new);
    DeferredBlock<Block> GLEEMSLATE_SLAB = register("gleemslate_slab", GleemslateSlabBlock::new);
    DeferredBlock<Block> GLEEMSLATE_STAIRS = register("gleemslate_stairs", (p) -> new GleemslateStairsBlock(GLEEMSLATE.get().defaultBlockState(), p));
    DeferredBlock<Block> GLEEMSLATE_WALL = register("gleemslate_wall", GleemslateWallBlock::new);

    //cut gleemslate
    DeferredBlock<Block> CUT_GLEEMSLATE = register("cut_gleemslate", GleemslateBlock::new);
    DeferredBlock<Block> CUT_GLEEMSLATE_SLAB = register("cut_gleemslate_slab", GleemslateSlabBlock::new);
    DeferredBlock<Block> CUT_GLEEMSLATE_STAIRS = register("cut_gleemslate_stairs", (p) -> new GleemslateStairsBlock(CUT_GLEEMSLATE.get().defaultBlockState(), p));
    DeferredBlock<Block> CUT_GLEEMSLATE_WALL = register("cut_gleemslate_wall", GleemslateWallBlock::new);

    //gleemslate tiles
    DeferredBlock<Block> GLEEMSLATE_TILES = register("gleemslate_tiles", GleemslateBlock::new);
    DeferredBlock<Block> GLEEMSLATE_TILES_SLAB = register("gleemslate_tiles_slab", GleemslateSlabBlock::new);
    DeferredBlock<Block> GLEEMSLATE_TILES_STAIRS = register("gleemslate_tiles_stairs", (p) -> new GleemslateStairsBlock(GLEEMSLATE_TILES.get().defaultBlockState(), p));
    DeferredBlock<Block> GLEEMSLATE_TILES_WALL = register("gleemslate_tiles_wall", GleemslateWallBlock::new);

    //gleemslate bricks
    DeferredBlock<Block> GLEEMSLATE_BRICKS = register("gleemslate_bricks", GleemslateBlock::new);
    DeferredBlock<Block> GLEEMSLATE_BRICKS_SLAB = register("gleemslate_bricks_slab", GleemslateSlabBlock::new);
    DeferredBlock<Block> GLEEMSLATE_BRICKS_STAIRS = register("gleemslate_bricks_stairs", (p) -> new GleemslateStairsBlock(GLEEMSLATE_BRICKS.get().defaultBlockState(), p));
    DeferredBlock<Block> GLEEMSLATE_BRICKS_WALL = register("gleemslate_bricks_wall", GleemslateWallBlock::new);


    //sculk
    DeferredBlock<Block> SCULK_PILLAR = register("sculk_pillar", (d) -> new RotatedPillarBlock(d.strength(1.5F, 6.0F).sound(SoundType.BONE_BLOCK)));
    DeferredBlock<Block> SCULK_SLAB = register("sculk_slab", (d) -> new SlabBlock(d.strength(1.5F, 6.0F).sound(SoundType.BONE_BLOCK)));
    DeferredBlock<Block> SCULK_TENDRIL = register("sculk_tendril", SculkTendrilBlock::new);



    //sculked deepslate
    DeferredBlock<Block> SCULKED_DEEPSLATE = register("sculked_deepslate", (d) -> new Block(d.strength(1.5F, 6.0F).sound(SoundType.DEEPSLATE)));
    DeferredBlock<Block> SCULKED_DEEPSLATE_SLAB = register("sculked_deepslate_slab", (d) -> new SlabBlock(d.strength(1.5F, 6.0F).sound(SoundType.DEEPSLATE)));
    DeferredBlock<Block> SCULKED_DEEPSLATE_STAIRS = register("sculked_deepslate_stairs", (d) -> new StairBlock(SCULKED_DEEPSLATE.get().defaultBlockState(), d.strength(1.5F, 6.0F).sound(SoundType.DEEPSLATE)));
    DeferredBlock<Block> SCULKED_DEEPSLATE_WALL = register("sculked_deepslate_wall", (d) -> new WallBlock(d.strength(1.5F, 6.0F).sound(SoundType.DEEPSLATE)));

    static DeferredBlock<Block> register(String name, Function<BlockBehaviour.Properties, ? extends Block> supplier)
    {
        DeferredBlock<Block> block = BLOCKS.registerBlock(name, supplier);
        ECItems.ITEMS.registerSimpleBlockItem(block);
        return block;
    }

    static void register(IEventBus modEventBus)
    {
        BLOCKS.register(modEventBus);
        ;
    }
}
