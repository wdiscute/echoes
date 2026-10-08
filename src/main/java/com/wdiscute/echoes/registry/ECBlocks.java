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
    DeferredBlock<PortalBlock> PORTAL = register("portal", PortalBlock::new);
    DeferredBlock<TimelessMarkerBlock> TIMELESS_MARKER = register("timeless_marker", TimelessMarkerBlock::new);
    DeferredBlock<DisplayBlock> DISPLAY = register("display", DisplayBlock::new);
    DeferredBlock<CasketBlock> CASKET = register("casket", CasketBlock::new);


    //prisma
    DeferredBlock<PrismaPaneBlock> PRISMA_PANE = register("prisma_pane", PrismaPaneBlock::new);

    //gleemslate
    DeferredBlock<RotatedPillarBlock> GLEEMSLATE_PILLAR = register("gleemslate_pillar", (p) -> new RotatedPillarBlock(p.sound(SoundType.AMETHYST).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    DeferredBlock<RotatedPillarBlock> TRIMMED_GLEEMSLATE = register("trimmed_gleemslate", (p) -> new RotatedPillarBlock(p.sound(SoundType.AMETHYST).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    DeferredBlock<GleemslateBlock> CHISELED_GLEEMSLATE = register("chiseled_gleemslate", GleemslateBlock::new);
    DeferredBlock<GleemslateBlock> GLEEMSLATE_GRASS = register("gleemslate_grass", GleemslateBlock::new);

    //gleemslate
    DeferredBlock<GleemslateBlock> GLEEMSLATE = register("gleemslate", GleemslateBlock::new);
    DeferredBlock<GleemslateSlabBlock> GLEEMSLATE_SLAB = register("gleemslate_slab", GleemslateSlabBlock::new);
    DeferredBlock<GleemslateStairsBlock> GLEEMSLATE_STAIRS = register("gleemslate_stairs", (p) -> new GleemslateStairsBlock(GLEEMSLATE.get().defaultBlockState(), p));
    DeferredBlock<GleemslateWallBlock> GLEEMSLATE_WALL = register("gleemslate_wall", GleemslateWallBlock::new);

    //cut gleemslate
    DeferredBlock<GleemslateBlock> CUT_GLEEMSLATE = register("cut_gleemslate", GleemslateBlock::new);
    DeferredBlock<GleemslateSlabBlock> CUT_GLEEMSLATE_SLAB = register("cut_gleemslate_slab", GleemslateSlabBlock::new);
    DeferredBlock<GleemslateStairsBlock> CUT_GLEEMSLATE_STAIRS = register("cut_gleemslate_stairs", (p) -> new GleemslateStairsBlock(CUT_GLEEMSLATE.get().defaultBlockState(), p));
    DeferredBlock<GleemslateWallBlock> CUT_GLEEMSLATE_WALL = register("cut_gleemslate_wall", GleemslateWallBlock::new);

    //gleemslate tiles
    DeferredBlock<GleemslateBlock> GLEEMSLATE_TILES = register("gleemslate_tiles", GleemslateBlock::new);
    DeferredBlock<GleemslateSlabBlock> GLEEMSLATE_TILES_SLAB = register("gleemslate_tiles_slab", GleemslateSlabBlock::new);
    DeferredBlock<GleemslateStairsBlock> GLEEMSLATE_TILES_STAIRS = register("gleemslate_tiles_stairs", (p) -> new GleemslateStairsBlock(GLEEMSLATE_TILES.get().defaultBlockState(), p));
    DeferredBlock<GleemslateWallBlock> GLEEMSLATE_TILES_WALL = register("gleemslate_tiles_wall", GleemslateWallBlock::new);

    //gleemslate bricks
    DeferredBlock<GleemslateBlock> GLEEMSLATE_BRICKS = register("gleemslate_bricks", GleemslateBlock::new);
    DeferredBlock<GleemslateSlabBlock> GLEEMSLATE_BRICKS_SLAB = register("gleemslate_bricks_slab", GleemslateSlabBlock::new);
    DeferredBlock<GleemslateStairsBlock> GLEEMSLATE_BRICKS_STAIRS = register("gleemslate_bricks_stairs", (p) -> new GleemslateStairsBlock(GLEEMSLATE_BRICKS.get().defaultBlockState(), p));
    DeferredBlock<GleemslateWallBlock> GLEEMSLATE_BRICKS_WALL = register("gleemslate_bricks_wall", GleemslateWallBlock::new);




    //sculk
    DeferredBlock<RotatedPillarBlock> SCULK_PILLAR = register("sculk_pillar", (d) -> new RotatedPillarBlock(d.strength(1.5F, 6.0F).sound(SoundType.BONE_BLOCK)));
    DeferredBlock<SlabBlock> SCULK_SLAB = register("sculk_slab", (d) -> new SlabBlock(d.strength(1.5F, 6.0F).sound(SoundType.BONE_BLOCK)));
    DeferredBlock<SculkTendrilBlock> SCULK_TENDRIL = register("sculk_tendril", SculkTendrilBlock::new);

    //sculked deepslate
    DeferredBlock<Block> SCULKED_DEEPSLATE = register("sculked_deepslate", (d) -> new Block(d.strength(1.5F, 6.0F).sound(SoundType.DEEPSLATE)));
    DeferredBlock<SlabBlock> SCULKED_DEEPSLATE_SLAB = register("sculked_deepslate_slab", (d) -> new SlabBlock(d.strength(1.5F, 6.0F).sound(SoundType.DEEPSLATE)));
    DeferredBlock<StairBlock> SCULKED_DEEPSLATE_STAIRS = register("sculked_deepslate_stairs", (d) -> new StairBlock(SCULKED_DEEPSLATE.get().defaultBlockState(), d.strength(1.5F, 6.0F).sound(SoundType.DEEPSLATE)));
    DeferredBlock<WallBlock> SCULKED_DEEPSLATE_WALL = register("sculked_deepslate_wall", (d) -> new WallBlock(d.strength(1.5F, 6.0F).sound(SoundType.DEEPSLATE)));

    //sculked deepslate bricks
    DeferredBlock<Block> SCULKED_DEEPSLATE_BRICKS = register("sculked_deepslate_bricks", (d) -> new Block(d.strength(1.5F, 6.0F).sound(SoundType.DEEPSLATE)));
    DeferredBlock<SlabBlock> SCULKED_DEEPSLATE_BRICKS_SLAB = register("sculked_deepslate_bricks_slab", (d) -> new SlabBlock(d.strength(1.5F, 6.0F).sound(SoundType.DEEPSLATE)));
    DeferredBlock<StairBlock> SCULKED_DEEPSLATE_BRICKS_STAIRS = register("sculked_deepslate_bricks_stairs", (d) -> new StairBlock(SCULKED_DEEPSLATE_BRICKS.get().defaultBlockState(), d.strength(1.5F, 6.0F).sound(SoundType.DEEPSLATE)));
    DeferredBlock<WallBlock> SCULKED_DEEPSLATE_BRICKS_WALL = register("sculked_deepslate_bricks_wall", (d) -> new WallBlock(d.strength(1.5F, 6.0F).sound(SoundType.DEEPSLATE)));



    static <T extends Block> DeferredBlock<T> register(String name, Function<BlockBehaviour.Properties, T> supplier)
    {
        DeferredBlock<T> block = BLOCKS.registerBlock(name, supplier);
        ECItems.ITEMS.registerSimpleBlockItem(block);
        return block;
    }

    static void register(IEventBus modEventBus)
    {
        BLOCKS.register(modEventBus);
        ;
    }
}
