package com.wdiscute.echoes.datagen;

import com.wdiscute.echoes.ECTags;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.registry.ECBlocks;
import com.wdiscute.echoes.registry.ECItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class DGECItemTagsProvider extends ItemTagsProvider
{

    public DGECItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider)
    {
        super(output, lookupProvider, Echoes.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        tag(ECTags.REMOVED_ON_TIMELESS_EXIT)
                .add(Items.POTION)
                .add(Items.GLASS_BOTTLE)
        ;

        tag(ItemTags.HEAD_ARMOR)
                .add(ECItems.TIMELOST_HELMET.get())
        ;

        tag(ItemTags.CHEST_ARMOR)
                .add(ECItems.TIMELOST_CHESTPLATE.get())
        ;

        tag(ItemTags.LEG_ARMOR)
                .add(ECItems.TIMELOST_LEGGINGS.get())
        ;

        tag(ItemTags.FOOT_ARMOR)
                .add(ECItems.TIMELOST_BOOTS.get())
        ;

    }
}
