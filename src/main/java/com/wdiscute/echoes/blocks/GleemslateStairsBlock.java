package com.wdiscute.echoes.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

public class GleemslateStairsBlock extends StairBlock
{
    public GleemslateStairsBlock(BlockState block, Properties properties)
    {
        super(block, properties
                .requiresCorrectToolForDrops()
                .strength(1.5F, 6.0F)
                .sound(SoundType.AMETHYST)
        );
    }
}
