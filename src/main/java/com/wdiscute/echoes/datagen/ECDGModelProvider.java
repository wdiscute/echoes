package com.wdiscute.echoes.datagen;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.registry.ECBlocks;
import com.wdiscute.echoes.registry.ECItems;
import net.minecraft.client.color.item.GrassColorSource;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Holder;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ECDGModelProvider extends ModelProvider
{
    public ECDGModelProvider(PackOutput output)
    {
        super(output, Echoes.MOD_ID);
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems()
    {
        List<Holder<Item>> list = new ArrayList<>();

        //6 sided blocks
        list.add(ECBlocks.GLEEMSLATE.asItem().builtInRegistryHolder());

        list.add(ECBlocks.GLEEMSLATE.asItem().builtInRegistryHolder());
        list.add(ECBlocks.GLEEMSLATE_SLAB.asItem().builtInRegistryHolder());
        list.add(ECBlocks.GLEEMSLATE_STAIRS.asItem().builtInRegistryHolder());
        list.add(ECBlocks.GLEEMSLATE_WALL.asItem().builtInRegistryHolder());
        list.add(ECBlocks.GLEEMSLATE_TILES.asItem().builtInRegistryHolder());
        list.add(ECBlocks.CHISELED_GLEEMSLATE.asItem().builtInRegistryHolder());
        list.add(ECBlocks.CUT_GLEEMSLATE.asItem().builtInRegistryHolder());
        list.add(ECBlocks.GLEEMSLATE_BRICKS.asItem().builtInRegistryHolder());
        list.add(ECBlocks.GLEEMSLATE_PILLAR.asItem().builtInRegistryHolder());
        list.add(ECBlocks.TRIMMED_GLEEMSLATE.asItem().builtInRegistryHolder());

        list.add(ECBlocks.PRISMA_PANE.asItem().builtInRegistryHolder());


        list.add(ECBlocks.SCULK_PILLAR.asItem().builtInRegistryHolder());
        list.add(ECBlocks.TIMELESS_MARKER.asItem().builtInRegistryHolder());
        list.add(ECBlocks.SCULKED_DEEPSLATE.asItem().builtInRegistryHolder());

        //items
        list.add(ECItems.SCULK_TISSUE.getDelegate());
        list.add(ECItems.ECHO_BLADE.getDelegate());

        return list.stream();
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks()
    {
        List<DeferredBlock<Block>> list = new ArrayList<>();

        list.add(ECBlocks.GLEEMSLATE);
        list.add(ECBlocks.GLEEMSLATE_SLAB);
        list.add(ECBlocks.GLEEMSLATE_STAIRS);
        list.add(ECBlocks.GLEEMSLATE_WALL);

        list.add(ECBlocks.CHISELED_GLEEMSLATE);
        list.add(ECBlocks.GLEEMSLATE_PILLAR);
        list.add(ECBlocks.TRIMMED_GLEEMSLATE);
        list.add(ECBlocks.CUT_GLEEMSLATE);
        list.add(ECBlocks.GLEEMSLATE_BRICKS);


        list.add(ECBlocks.SCULK_PILLAR);

        list.add(ECBlocks.SCULKED_DEEPSLATE);
        list.add(ECBlocks.SCULKED_DEEPSLATE_STAIRS);
        list.add(ECBlocks.SCULKED_DEEPSLATE_SLAB);
        list.add(ECBlocks.SCULKED_DEEPSLATE_WALL);

        list.add(ECBlocks.TIMELESS_MARKER);


        return list.stream();
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels)
    {
        //
        //         ,--.                                    ,--.            ,--.
        //  ,---.  |  |  ,---.   ,---.  ,--,--,--.  ,---.  |  |  ,--,--. ,-'  '-.  ,---.
        // | .-. | |  | | .-. : | .-. : |        | (  .-'  |  | ' ,-.  | '-.  .-' | .-. :
        // ' '-' ' |  | \   --. \   --. |  |  |  | .-'  `) |  | \ '-'  |   |  |   \   --.
        // .`-  /  `--'  `----'  `----' `--`--`--' `----'  `--'  `--`--'   `--'    `----'
        // `---'

        //non family blocks
        blockModels.createTrivialCube(ECBlocks.CHISELED_GLEEMSLATE.get());

        blockModels.createAxisAlignedPillarBlock(ECBlocks.GLEEMSLATE_PILLAR.get(), TexturedModel.COLUMN);
        blockModels.createAxisAlignedPillarBlock(ECBlocks.TRIMMED_GLEEMSLATE.get(), TexturedModel.COLUMN);


        // Gleemslate grass
        //made manually

        //gleemslate
        {
            BlockFamily family = new BlockFamily.Builder(ECBlocks.GLEEMSLATE.get())
                    .wall(ECBlocks.GLEEMSLATE_WALL.get())
                    .stairs(ECBlocks.GLEEMSLATE_STAIRS.get())
                    .slab(ECBlocks.GLEEMSLATE_SLAB.get())
                    .getFamily();

            blockModels.createTrivialCube(ECBlocks.GLEEMSLATE.get());
            blockModels.familyWithExistingFullBlock(family.getBaseBlock()).generateFor(family);
        }

        //cut gleemslate
        {
            BlockFamily family = new BlockFamily.Builder(ECBlocks.CUT_GLEEMSLATE.get())
                    .wall(ECBlocks.CUT_GLEEMSLATE_WALL.get())
                    .stairs(ECBlocks.CUT_GLEEMSLATE_STAIRS.get())
                    .slab(ECBlocks.CUT_GLEEMSLATE_SLAB.get())
                    .getFamily();

            blockModels.createTrivialCube(ECBlocks.CUT_GLEEMSLATE.get());
            blockModels.familyWithExistingFullBlock(family.getBaseBlock()).generateFor(family);
        }

        //gleemslate tiles
        {
            BlockFamily family = new BlockFamily.Builder(ECBlocks.GLEEMSLATE_TILES.get())
                    .wall(ECBlocks.GLEEMSLATE_TILES_WALL.get())
                    .stairs(ECBlocks.GLEEMSLATE_TILES_STAIRS.get())
                    .slab(ECBlocks.GLEEMSLATE_TILES_SLAB.get())
                    .getFamily();

            blockModels.createTrivialCube(ECBlocks.GLEEMSLATE_TILES.get());
            blockModels.familyWithExistingFullBlock(family.getBaseBlock()).generateFor(family);
        }

        //gleemslate bricks
        {
            BlockFamily family = new BlockFamily.Builder(ECBlocks.GLEEMSLATE_BRICKS.get())
                    .wall(ECBlocks.GLEEMSLATE_BRICKS_WALL.get())
                    .stairs(ECBlocks.GLEEMSLATE_BRICKS_STAIRS.get())
                    .slab(ECBlocks.GLEEMSLATE_BRICKS_SLAB.get())
                    .getFamily();

            blockModels.createTrivialCube(ECBlocks.GLEEMSLATE_BRICKS.get());
            blockModels.familyWithExistingFullBlock(family.getBaseBlock()).generateFor(family);
        }


        blockModels.createAxisAlignedPillarBlock(ECBlocks.SCULK_PILLAR.get(), TexturedModel.COLUMN);
        blockModels.createHorizontallyRotatedBlock(ECBlocks.TIMELESS_MARKER.get(), TexturedModel.COLUMN);

        //sculked deepslate
        {
            BlockFamily family = new BlockFamily.Builder(ECBlocks.SCULKED_DEEPSLATE.get())
                    .wall(ECBlocks.SCULKED_DEEPSLATE_WALL.get())
                    .stairs(ECBlocks.SCULKED_DEEPSLATE_STAIRS.get())
                    .slab(ECBlocks.SCULKED_DEEPSLATE_SLAB.get())
                    .getFamily();

            blockModels.createTrivialCube(ECBlocks.SCULKED_DEEPSLATE.get());
            blockModels.familyWithExistingFullBlock(family.getBaseBlock()).generateFor(family);
        }


        itemModels.generateFlatItem(ECItems.SCULK_TISSUE.get(), ModelTemplates.FLAT_ITEM);

        //weapons
        itemModels.generateFlatItem(ECItems.ECHO_BLADE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

    }
}
