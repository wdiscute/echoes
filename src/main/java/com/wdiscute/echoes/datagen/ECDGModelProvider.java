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
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

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

        //blocks
        list.add(ECBlocks.GLEEMSLATE_PILLAR.asItem().builtInRegistryHolder());
        list.add(ECBlocks.GLEEMSLATE_TILES.asItem().builtInRegistryHolder());
        list.add(ECBlocks.CUT_GLEEMSLATE.asItem().builtInRegistryHolder());

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

        list.add(ECBlocks.GLEEMSLATE_PILLAR);
        list.add(ECBlocks.GLEEMSLATE_TILES);
        list.add(ECBlocks.CUT_GLEEMSLATE);

        list.add(ECBlocks.SCULK_PILLAR);

        list.add(ECBlocks.TIMELESS_MARKER);


        return list.stream();
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels)
    {
        blockModels.createTrivialCube(ECBlocks.GLEEMSLATE_TILES.get());
        blockModels.createTrivialCube(ECBlocks.CUT_GLEEMSLATE.get());

        blockModels.createAxisAlignedPillarBlock(ECBlocks.GLEEMSLATE_PILLAR.get(), TexturedModel.COLUMN);

        blockModels.createAxisAlignedPillarBlock(ECBlocks.SCULK_PILLAR.get(), TexturedModel.COLUMN);

        blockModels.createHorizontallyRotatedBlock(ECBlocks.TIMELESS_MARKER.get(), TexturedModel.COLUMN);

        itemModels.generateFlatItem(ECItems.SCULK_TISSUE.get(), ModelTemplates.FLAT_ITEM);

        //weapons
        itemModels.generateFlatItem(ECItems.ECHO_BLADE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
    }
}
