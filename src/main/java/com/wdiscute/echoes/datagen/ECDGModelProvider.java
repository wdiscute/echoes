package com.wdiscute.echoes.datagen;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.registry.ECBlocks;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
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
        List<DeferredBlock<Block>> list = new ArrayList<>();

        list.add(ECBlocks.GLEEMSLATE_PILLAR);
        list.add(ECBlocks.GLEEMSLATE_TILES);
        list.add(ECBlocks.CUT_GLEEMSLATE);

        list.add(ECBlocks.SCULK_PILLAR);

        return list.stream().map(o -> o.asItem().builtInRegistryHolder());
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks()
    {
        List<DeferredBlock<Block>> list = new ArrayList<>();

        list.add(ECBlocks.GLEEMSLATE_PILLAR);
        list.add(ECBlocks.GLEEMSLATE_TILES);
        list.add(ECBlocks.CUT_GLEEMSLATE);

        list.add(ECBlocks.SCULK_PILLAR);


        return list.stream();
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels)
    {
        //itemModels.generateFlatItem(ModItems.AZURITE.get(), ModelTemplates.FLAT_ITEM);

        /* BLOCKS */
        blockModels.createTrivialCube(ECBlocks.GLEEMSLATE_TILES.get());
        blockModels.createTrivialCube(ECBlocks.CUT_GLEEMSLATE.get());

        blockModels.createAxisAlignedPillarBlock(ECBlocks.GLEEMSLATE_PILLAR.get(), TexturedModel.COLUMN);

        blockModels.createAxisAlignedPillarBlock(ECBlocks.SCULK_PILLAR.get(), TexturedModel.COLUMN);
    }
}
