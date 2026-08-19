package com.wdiscute.echoes;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface ECTags
{
    TagKey<Block> SKIPS_SCULK_TRANSFORMATION = createBlock("skips_sculk_transformation");
    TagKey<Block> SUPPORTS_SCULK_TENDRIL = createBlock("supports_sculk_tendril");



    static TagKey<Block> createBlock(String name)
    {
        return BlockTags.create(Echoes.rl(name));
    }

    static TagKey<Item> createItem(String name)
    {
        return ItemTags.create(Echoes.rl(name));
    }

}
