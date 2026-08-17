package com.wdiscute.echoes.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WallBlock;

public class GleemslateWallBlock extends WallBlock
{
    public GleemslateWallBlock(Properties properties)
    {
        super(properties
                .requiresCorrectToolForDrops()
                .strength(1.5F, 6.0F)
                .sound(SoundType.AMETHYST)
        );
    }
}
