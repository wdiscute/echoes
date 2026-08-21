package com.wdiscute.echoes.datagen;

import com.wdiscute.echoes.ECTags;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.registry.ECBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.lang.classfile.TypeAnnotation;
import java.util.concurrent.CompletableFuture;

public class DGECBlocksTagsProvider extends BlockTagsProvider
{

    public DGECBlocksTagsProvider(PackOutput output,
                                  CompletableFuture<HolderLookup.Provider> lookupProvider)
    {
        super(output, lookupProvider, Echoes.MOD_ID);
    }


    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        tag(ECTags.SKIPS_SCULK_TRANSFORMATION)
                .add(ECBlocks.PORTAL.get())
                .add(ECBlocks.CASKET.get())
        ;

        tag(ECTags.SUPPORTS_SCULK_TENDRIL)
                .add(Blocks.SCULK)
                .add(ECBlocks.SCULK_PILLAR.get())
                .add(ECBlocks.SCULKED_DEEPSLATE.get())
                .add(ECBlocks.SCULKED_DEEPSLATE_BRICKS.get())
        ;

        tag(BlockTags.SUPPORTS_VEGETATION)
                .add(ECBlocks.GLEEMSLATE_GRASS.get());


        tag(BlockTags.WALLS)
                .add(ECBlocks.GLEEMSLATE_WALL.get())
                .add(ECBlocks.CUT_GLEEMSLATE_WALL.get())
                .add(ECBlocks.SCULKED_DEEPSLATE_WALL.get())
                .add(ECBlocks.GLEEMSLATE_BRICKS_WALL.get())
                .add(ECBlocks.GLEEMSLATE_TILES_WALL.get())
        ;

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ECBlocks.PORTAL.get())
                .add(ECBlocks.TIMELESS_MARKER.get())
                .add(ECBlocks.DISPLAY.get())

                .add(ECBlocks.PRISMA_PANE.get())

                .add(ECBlocks.GLEEMSLATE_PILLAR.get())
                .add(ECBlocks.TRIMMED_GLEEMSLATE.get())
                .add(ECBlocks.CHISELED_GLEEMSLATE.get())
                .add(ECBlocks.GLEEMSLATE_GRASS.get())

                .add(ECBlocks.GLEEMSLATE.get())
                .add(ECBlocks.GLEEMSLATE_SLAB.get())
                .add(ECBlocks.GLEEMSLATE_STAIRS.get())
                .add(ECBlocks.GLEEMSLATE_WALL.get())

                .add(ECBlocks.CUT_GLEEMSLATE.get())
                .add(ECBlocks.CUT_GLEEMSLATE_SLAB.get())
                .add(ECBlocks.CUT_GLEEMSLATE_STAIRS.get())
                .add(ECBlocks.CUT_GLEEMSLATE_WALL.get())

                .add(ECBlocks.GLEEMSLATE_TILES.get())
                .add(ECBlocks.GLEEMSLATE_TILES_SLAB.get())
                .add(ECBlocks.GLEEMSLATE_TILES_STAIRS.get())
                .add(ECBlocks.GLEEMSLATE_TILES_WALL.get())

                .add(ECBlocks.GLEEMSLATE_BRICKS.get())
                .add(ECBlocks.GLEEMSLATE_BRICKS_SLAB.get())
                .add(ECBlocks.GLEEMSLATE_BRICKS_STAIRS.get())
                .add(ECBlocks.GLEEMSLATE_BRICKS_WALL.get())

                .add(ECBlocks.SCULK_PILLAR.get())
                .add(ECBlocks.SCULK_SLAB.get())


                .add(ECBlocks.SCULKED_DEEPSLATE.get())
                .add(ECBlocks.SCULKED_DEEPSLATE_SLAB.get())
                .add(ECBlocks.SCULKED_DEEPSLATE_STAIRS.get())
                .add(ECBlocks.SCULKED_DEEPSLATE_WALL.get())
        ;
    }
}
