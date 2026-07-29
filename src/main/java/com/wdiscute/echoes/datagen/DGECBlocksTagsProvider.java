package com.wdiscute.echoes.datagen;

import com.wdiscute.echoes.ECTags;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.registry.ECBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

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
                .add(ECBlocks.PORTAL.get());
    }
}
