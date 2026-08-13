package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.blocks.GleemslateGrass;
import com.wdiscute.echoes.blocks.PrismaPaneBlock;
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

    DeferredBlock<Block> PORTAL = register("portal", PortalBlock::new);
    DeferredBlock<Block> TIMELESS_MARKER = register("timeless_marker", TimelessMarkerBlock::new);
    DeferredBlock<Block> DISPLAY = register("display", DisplayBlock::new);


    DeferredBlock<Block> GLEEMSLATE_GRASS = register("gleemslate_grass", (d) ->  new Block(d.sound(SoundType.AMETHYST)));

    //gleemslate
    DeferredBlock<Block> GLEEMSLATE = register("gleemslate", (d) ->  new Block(d.sound(SoundType.AMETHYST)));
    DeferredBlock<Block> GLEEMSLATE_SLAB = register("gleemslate_slab", (d) ->  new SlabBlock(d.sound(SoundType.AMETHYST)));
    DeferredBlock<Block> GLEEMSLATE_STAIRS = register("gleemslate_stairs", (d) ->  new StairBlock(GLEEMSLATE.get().defaultBlockState(), d.sound(SoundType.AMETHYST)));
    DeferredBlock<Block> GLEEMSLATE_WALL = register("gleemslate_wall", (d) ->  new WallBlock(d.sound(SoundType.AMETHYST)));

    //cut gleemslate
    DeferredBlock<Block> CUT_GLEEMSLATE = register("cut_gleemslate", (d) ->  new Block(d.sound(SoundType.AMETHYST)));
    DeferredBlock<Block> CUT_GLEEMSLATE_SLAB = register("cut_gleemslate_slab", (d) ->  new SlabBlock(d.sound(SoundType.AMETHYST)));
    DeferredBlock<Block> CUT_GLEEMSLATE_STAIRS = register("cut_gleemslate_stairs", (d) ->  new StairBlock(CUT_GLEEMSLATE.get().defaultBlockState(), d.sound(SoundType.AMETHYST)));
    DeferredBlock<Block> CUT_GLEEMSLATE_WALL = register("cut_gleemslate_wall", (d) ->  new WallBlock(d.sound(SoundType.AMETHYST)));

    //gleemslate tiles
    DeferredBlock<Block> GLEEMSLATE_TILES = register("gleemslate_tiles", (d) ->  new Block(d.sound(SoundType.AMETHYST)));
    DeferredBlock<Block> GLEEMSLATE_TILES_SLAB = register("gleemslate_tiles_slab", (d) ->  new SlabBlock(d.sound(SoundType.AMETHYST)));
    DeferredBlock<Block> GLEEMSLATE_TILES_STAIRS = register("gleemslate_tiles_stairs", (d) ->  new StairBlock(GLEEMSLATE_TILES.get().defaultBlockState(), d.sound(SoundType.AMETHYST)));
    DeferredBlock<Block> GLEEMSLATE_TILES_WALL = register("gleemslate_tiles_wall", (d) ->  new WallBlock(d.sound(SoundType.AMETHYST)));

    //gleemslate bricks
    DeferredBlock<Block> GLEEMSLATE_BRICKS = register("gleemslate_bricks", (d) ->  new Block(d.sound(SoundType.AMETHYST)));
    DeferredBlock<Block> GLEEMSLATE_BRICKS_SLAB = register("gleemslate_bricks_slab", (d) ->  new SlabBlock(d.sound(SoundType.AMETHYST)));
    DeferredBlock<Block> GLEEMSLATE_BRICKS_STAIRS = register("gleemslate_bricks_stairs", (d) ->  new StairBlock(GLEEMSLATE_BRICKS.get().defaultBlockState(), d.sound(SoundType.AMETHYST)));
    DeferredBlock<Block> GLEEMSLATE_BRICKS_WALL = register("gleemslate_bricks_wall", (d) ->  new WallBlock(d.sound(SoundType.AMETHYST)));




    //rotatable
    DeferredBlock<Block> GLEEMSLATE_PILLAR = register("gleemslate_pillar", (d) -> new RotatedPillarBlock(d.sound(SoundType.AMETHYST)));
    DeferredBlock<Block> TRIMMED_GLEEMSLATE = register("trimmed_gleemslate", (d) ->  new RotatedPillarBlock(d.sound(SoundType.AMETHYST)));
    DeferredBlock<Block> CHISELED_GLEEMSLATE = register("chiseled_gleemslate", (d) ->  new Block(d.sound(SoundType.AMETHYST)));



    DeferredBlock<Block> PRISMA_PANE = register("prisma_pane", PrismaPaneBlock::new);

    DeferredBlock<Block> SCULK_PILLAR = register("sculk_pillar", (d) -> new RotatedPillarBlock(d.sound(SoundType.BONE_BLOCK)));
    DeferredBlock<Block> SCULK_SLAB = register("sculk_slab", (d) ->  new SlabBlock(d.sound(SoundType.BONE_BLOCK)));

    //sculked deepslate
    DeferredBlock<Block> SCULKED_DEEPSLATE = register("sculked_deepslate", (d) ->  new Block(d.sound(SoundType.DEEPSLATE)));
    DeferredBlock<Block> SCULKED_DEEPSLATE_SLAB = register("sculked_deepslate_slab", (d) ->  new SlabBlock(d.sound(SoundType.BONE_BLOCK)));
    DeferredBlock<Block> SCULKED_DEEPSLATE_STAIRS = register("sculked_deepslate_stairs", (d) ->  new StairBlock(SCULKED_DEEPSLATE.get().defaultBlockState(), d.sound(SoundType.BONE_BLOCK)));
    DeferredBlock<Block> SCULKED_DEEPSLATE_WALL = register("sculked_deepslate_wall", (d) ->  new WallBlock(d.sound(SoundType.BONE_BLOCK)));

    static DeferredBlock<Block> register(String name, Function<BlockBehaviour.Properties, ? extends Block> supplier)
    {
        DeferredBlock<Block> block = BLOCKS.registerBlock(name, supplier);
        ECItems.ITEMS.registerSimpleBlockItem(block);
        return block;
    }

    static void register(IEventBus modEventBus)
    {
        BLOCKS.register(modEventBus);;
    }
}
