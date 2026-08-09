package com.wdiscute.echoes.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PrismaPaneBlock extends HorizontalDirectionalBlock
{
    public PrismaPaneBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec()
    {
        return simpleCodec(PrismaPaneBlock::new);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(HorizontalDirectionalBlock.FACING);
    }

    public static final VoxelShape SHAPE_NS = Block.box(6.5, 0, 6.5, 9.5, 16, 9.5);
    public static final VoxelShape SHAPE_EW = Block.box(6.5, 0, 6.5, 9.5, 16, 9.5);

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return switch (state.getValue(FACING))
        {
            case NORTH, SOUTH -> SHAPE_NS;
            default -> SHAPE_EW;
        };
    }
}
