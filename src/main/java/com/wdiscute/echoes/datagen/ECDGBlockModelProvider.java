package com.wdiscute.echoes.datagen;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.registry.ECBlocks;
import com.wdiscute.echoes.registry.ECItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ECDGBlockModelProvider extends BlockStateProvider
{
    public ECDGBlockModelProvider(PackOutput output, ExistingFileHelper existingFileHelper)
    {
        super(output, Echoes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels()
    {

        simpleBlock(ECBlocks.CHISELED_GLEEMSLATE.get());

        axisBlock(
                ECBlocks.GLEEMSLATE_PILLAR.get(),
                modLoc("block/gleemslate_pillar_side"),
                modLoc("block/gleemslate_pillar_top")
        );

        axisBlock(
                ECBlocks.TRIMMED_GLEEMSLATE.get(),
                modLoc("block/trimmed_gleemslate_side"),
                modLoc("block/trimmed_gleemslate_top")
        );

        simpleBlock(ECBlocks.GLEEMSLATE.get());

        stairsBlock(
                ECBlocks.GLEEMSLATE_STAIRS.get(),
                blockTexture(ECBlocks.GLEEMSLATE.get())
        );

        slabBlock(
                ECBlocks.GLEEMSLATE_SLAB.get(),
                blockTexture(ECBlocks.GLEEMSLATE.get()),
                blockTexture(ECBlocks.GLEEMSLATE.get())
        );

        wallBlock(
                ECBlocks.GLEEMSLATE_WALL.get(),
                modLoc("block/gleemslate")
        );

        simpleBlock(ECBlocks.CUT_GLEEMSLATE.get());

        stairsBlock(
                ECBlocks.CUT_GLEEMSLATE_STAIRS.get(),
                blockTexture(ECBlocks.CUT_GLEEMSLATE.get())
        );

        slabBlock(
                ECBlocks.CUT_GLEEMSLATE_SLAB.get(),
                blockTexture(ECBlocks.CUT_GLEEMSLATE.get()),
                blockTexture(ECBlocks.CUT_GLEEMSLATE.get())
        );

        wallBlock(
                ECBlocks.CUT_GLEEMSLATE_WALL.get(),
                blockTexture(ECBlocks.CUT_GLEEMSLATE.get())
        );

        simpleBlock(ECBlocks.GLEEMSLATE_TILES.get());

        stairsBlock(
                ECBlocks.GLEEMSLATE_TILES_STAIRS.get(),
                blockTexture(ECBlocks.GLEEMSLATE_TILES.get())
        );

        slabBlock(
                ECBlocks.GLEEMSLATE_TILES_SLAB.get(),
                blockTexture(ECBlocks.GLEEMSLATE_TILES.get()),
                blockTexture(ECBlocks.GLEEMSLATE_TILES.get())
        );

        wallBlock(
                ECBlocks.GLEEMSLATE_TILES_WALL.get(),
                blockTexture(ECBlocks.GLEEMSLATE_TILES.get())
        );

        simpleBlock(ECBlocks.GLEEMSLATE_BRICKS.get());

        stairsBlock(
                ECBlocks.GLEEMSLATE_BRICKS_STAIRS.get(),
                blockTexture(ECBlocks.GLEEMSLATE_BRICKS.get())
        );

        slabBlock(
                ECBlocks.GLEEMSLATE_BRICKS_SLAB.get(),
                blockTexture(ECBlocks.GLEEMSLATE_BRICKS.get()),
                blockTexture(ECBlocks.GLEEMSLATE_BRICKS.get())
        );

        wallBlock(
                ECBlocks.GLEEMSLATE_BRICKS_WALL.get(),
                blockTexture(ECBlocks.GLEEMSLATE_BRICKS.get())
        );

        axisBlock(
                ECBlocks.SCULK_PILLAR.get(),
                modLoc("block/sculk_pillar_side"),
                modLoc("block/sculk_pillar_top")
        );

        horizontalBlock(
                ECBlocks.TIMELESS_MARKER.get(),
                modLoc("block/timeless_marker_side"),
                modLoc("block/timeless_marker_side"),
                modLoc("block/timeless_marker_top")
        );

        simpleBlock(ECBlocks.SCULKED_DEEPSLATE.get());

        stairsBlock(
                ECBlocks.SCULKED_DEEPSLATE_STAIRS.get(),
                blockTexture(ECBlocks.SCULKED_DEEPSLATE.get())
        );

        slabBlock(
                ECBlocks.SCULKED_DEEPSLATE_SLAB.get(),
                blockTexture(ECBlocks.SCULKED_DEEPSLATE.get()),
                blockTexture(ECBlocks.SCULKED_DEEPSLATE.get())
        );

        wallBlock(
                ECBlocks.SCULKED_DEEPSLATE_WALL.get(),
                blockTexture(ECBlocks.SCULKED_DEEPSLATE.get())
        );

        simpleBlock(ECBlocks.SCULKED_DEEPSLATE_BRICKS.get());

        stairsBlock(
                ECBlocks.SCULKED_DEEPSLATE_BRICKS_STAIRS.get(),
                blockTexture(ECBlocks.SCULKED_DEEPSLATE_BRICKS.get())
        );

        slabBlock(
                ECBlocks.SCULKED_DEEPSLATE_BRICKS_SLAB.get(),
                blockTexture(ECBlocks.SCULKED_DEEPSLATE_BRICKS.get()),
                blockTexture(ECBlocks.SCULKED_DEEPSLATE_BRICKS.get())
        );

        wallBlock(
                ECBlocks.SCULKED_DEEPSLATE_BRICKS_WALL.get(),
                blockTexture(ECBlocks.SCULKED_DEEPSLATE_BRICKS.get())
        );
    }

    private String name(Block block)
    {
        return block.toString()
                .replace(Echoes.MOD_ID + ":", "")
                .replace("Block{", "")
                .replace("}", "");
    }
}