package com.wdiscute.echoes.datagen;

import com.wdiscute.echoes.blocks.GleemslateBlock;
import com.wdiscute.echoes.registry.ECBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DGECBlockLootTableProvider extends BlockLootSubProvider
{
    protected DGECBlockLootTableProvider(HolderLookup.Provider registries)
    {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate()
    {
        dropSelf(ECBlocks.PORTAL.get());
        dropSelf(ECBlocks.DISPLAY.get());

        dropSelf(ECBlocks.PRISMA_PANE.get());

        dropSelf(ECBlocks.GLEEMSLATE_PILLAR.get());
        dropSelf(ECBlocks.TRIMMED_GLEEMSLATE.get());
        dropSelf(ECBlocks.CHISELED_GLEEMSLATE.get());
        dropSelf(ECBlocks.GLEEMSLATE_GRASS.get());

        dropSelf(ECBlocks.GLEEMSLATE.get());
        dropSelf(ECBlocks.GLEEMSLATE_STAIRS.get());
        dropSelf(ECBlocks.GLEEMSLATE_SLAB.get());
        dropSelf(ECBlocks.GLEEMSLATE_WALL.get());

        dropSelf(ECBlocks.CUT_GLEEMSLATE.get());
        dropSelf(ECBlocks.CUT_GLEEMSLATE_STAIRS.get());
        dropSelf(ECBlocks.CUT_GLEEMSLATE_SLAB.get());
        dropSelf(ECBlocks.CUT_GLEEMSLATE_WALL.get());

        dropSelf(ECBlocks.GLEEMSLATE_TILES.get());
        dropSelf(ECBlocks.GLEEMSLATE_TILES_STAIRS.get());
        dropSelf(ECBlocks.GLEEMSLATE_TILES_SLAB.get());
        dropSelf(ECBlocks.GLEEMSLATE_TILES_WALL.get());

        dropSelf(ECBlocks.GLEEMSLATE_BRICKS.get());
        dropSelf(ECBlocks.GLEEMSLATE_BRICKS_STAIRS.get());
        dropSelf(ECBlocks.GLEEMSLATE_BRICKS_SLAB.get());
        dropSelf(ECBlocks.GLEEMSLATE_BRICKS_WALL.get());

        dropSelf(ECBlocks.SCULKED_DEEPSLATE.get());
        dropSelf(ECBlocks.SCULKED_DEEPSLATE_STAIRS.get());
        dropSelf(ECBlocks.SCULKED_DEEPSLATE_SLAB.get());
        dropSelf(ECBlocks.SCULKED_DEEPSLATE_WALL.get());

        dropSelf(ECBlocks.SCULKED_DEEPSLATE_BRICKS.get());
        dropSelf(ECBlocks.SCULKED_DEEPSLATE_BRICKS_STAIRS.get());
        dropSelf(ECBlocks.SCULKED_DEEPSLATE_BRICKS_SLAB.get());
        dropSelf(ECBlocks.SCULKED_DEEPSLATE_BRICKS_WALL.get());

        dropSelf(ECBlocks.SCULK_SLAB.get());
        dropSelf(ECBlocks.SCULK_PILLAR.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks()
    {
        List<Block> list = new ArrayList<>();

        list.add(ECBlocks.PORTAL.get());
        list.add(ECBlocks.DISPLAY.get());

        list.add(ECBlocks.PRISMA_PANE.get());

        list.add(ECBlocks.GLEEMSLATE_PILLAR.get());
        list.add(ECBlocks.TRIMMED_GLEEMSLATE.get());
        list.add(ECBlocks.CHISELED_GLEEMSLATE.get());
        list.add(ECBlocks.GLEEMSLATE_GRASS.get());

        list.add(ECBlocks.GLEEMSLATE.get());
        list.add(ECBlocks.GLEEMSLATE_STAIRS.get());
        list.add(ECBlocks.GLEEMSLATE_SLAB.get());
        list.add(ECBlocks.GLEEMSLATE_WALL.get());

        list.add(ECBlocks.CUT_GLEEMSLATE.get());
        list.add(ECBlocks.CUT_GLEEMSLATE_STAIRS.get());
        list.add(ECBlocks.CUT_GLEEMSLATE_SLAB.get());
        list.add(ECBlocks.CUT_GLEEMSLATE_WALL.get());

        list.add(ECBlocks.GLEEMSLATE_TILES.get());
        list.add(ECBlocks.GLEEMSLATE_TILES_STAIRS.get());
        list.add(ECBlocks.GLEEMSLATE_TILES_SLAB.get());
        list.add(ECBlocks.GLEEMSLATE_TILES_WALL.get());

        list.add(ECBlocks.GLEEMSLATE_BRICKS.get());
        list.add(ECBlocks.GLEEMSLATE_BRICKS_STAIRS.get());
        list.add(ECBlocks.GLEEMSLATE_BRICKS_SLAB.get());
        list.add(ECBlocks.GLEEMSLATE_BRICKS_WALL.get());

        list.add(ECBlocks.SCULKED_DEEPSLATE.get());
        list.add(ECBlocks.SCULKED_DEEPSLATE_STAIRS.get());
        list.add(ECBlocks.SCULKED_DEEPSLATE_SLAB.get());
        list.add(ECBlocks.SCULKED_DEEPSLATE_WALL.get());

        list.add(ECBlocks.SCULK_SLAB.get());
        list.add(ECBlocks.SCULK_PILLAR.get());


        return list::iterator;
    }
}
