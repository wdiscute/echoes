package com.wdiscute.echoes.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SnowyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class GleemslateGrass extends Block
{
    public GleemslateGrass(Properties properties)
    {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(SnowyBlock.SNOWY);
    }
}
