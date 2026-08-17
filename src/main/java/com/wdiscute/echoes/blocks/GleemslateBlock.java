package com.wdiscute.echoes.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class GleemslateBlock extends Block
{
    public GleemslateBlock(Properties properties)
    {
        super(properties
                .requiresCorrectToolForDrops()
                .strength(1.5F, 6.0F)
                .sound(SoundType.AMETHYST)
        );
    }
}
