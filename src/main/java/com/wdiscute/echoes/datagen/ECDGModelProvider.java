package com.wdiscute.echoes.datagen;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.registry.ECBlocks;
import com.wdiscute.echoes.registry.ECItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.core.Holder;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
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
        list.add(ECBlocks.GLEEMSLATE_SLAB.asItem().builtInRegistryHolder());
        list.add(ECBlocks.GLEEMSLATE_STAIRS.asItem().builtInRegistryHolder());
        list.add(ECBlocks.GLEEMSLATE_WALL.asItem().builtInRegistryHolder());

        list.add(ECBlocks.CHISELED_GLEEMSLATE.asItem().builtInRegistryHolder());
        list.add(ECBlocks.CUT_GLEEMSLATE.asItem().builtInRegistryHolder());
        list.add(ECBlocks.GLEEMSLATE_BRICKS.asItem().builtInRegistryHolder());
        list.add(ECBlocks.GLEEMSLATE_PILLAR.asItem().builtInRegistryHolder());
        list.add(ECBlocks.TRIMMED_GLEEMSLATE.asItem().builtInRegistryHolder());

        list.add(ECBlocks.PRISMA_PANE.asItem().builtInRegistryHolder());





        list.add(ECBlocks.SCULK_PILLAR.asItem().builtInRegistryHolder());

        list.add(ECBlocks.TIMELESS_MARKER.asItem().builtInRegistryHolder());

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

        //all 6 sides the same
        blockModels.createTrivialCube(ECBlocks.GLEEMSLATE.get());
        blockModels.createTrivialCube(ECBlocks.CUT_GLEEMSLATE.get());
        blockModels.createRotatableColumn(ECBlocks.CHISELED_GLEEMSLATE.get());
        blockModels.createTrivialCube(ECBlocks.GLEEMSLATE_PILLAR.get());

        blockModels.createAxisAlignedPillarBlock(ECBlocks.GLEEMSLATE_PILLAR.get(), TexturedModel.COLUMN);
        blockModels.createAxisAlignedPillarBlock(ECBlocks.TRIMMED_GLEEMSLATE.get(), TexturedModel.COLUMN);

        BlockFamily GLEEMSLATE_FAMILY = new BlockFamily.Builder(ECBlocks.GLEEMSLATE.get())
                .wall(ECBlocks.GLEEMSLATE_WALL.get())
                .stairs(ECBlocks.GLEEMSLATE_STAIRS.get())
                .slab(ECBlocks.GLEEMSLATE_SLAB.get())
                .generateStonecutterRecipe()
                .getFamily();

        blockModels.familyWithExistingFullBlock(GLEEMSLATE_FAMILY.getBaseBlock()).generateFor(GLEEMSLATE_FAMILY);






        blockModels.createAxisAlignedPillarBlock(ECBlocks.SCULK_PILLAR.get(), TexturedModel.COLUMN);

        blockModels.createHorizontallyRotatedBlock(ECBlocks.TIMELESS_MARKER.get(), TexturedModel.COLUMN);






        itemModels.generateFlatItem(ECItems.SCULK_TISSUE.get(), ModelTemplates.FLAT_ITEM);

        //weapons
        itemModels.generateFlatItem(ECItems.ECHO_BLADE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

    }
}
