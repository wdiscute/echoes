package com.wdiscute.echoes.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;

public class GleemslateSlabBlock extends SlabBlock
{
    public GleemslateSlabBlock(Properties properties)
    {
        super(properties
                .requiresCorrectToolForDrops()
                .strength(1.5F, 6.0F)
                .sound(SoundType.AMETHYST)
        );
    }
}
