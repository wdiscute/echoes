package com.wdiscute.echoes.blocks;

import com.mojang.serialization.MapCodec;
import com.wdiscute.echoes.ECTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SculkTendrilBlock extends VegetationBlock
{
    public SculkTendrilBlock(Properties properties)
    {
        super(properties
                .noOcclusion()
                .noCollision()
                .sound(SoundType.SCULK)
        );
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
    {
        BlockState belowBlockState = level.getBlockState(pos.below());
        return this.mayPlaceOn(belowBlockState, level, pos.below());
    }

    private static final VoxelShape SHAPE = Block.column(12.0, 0.0, 12.0);
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos)
    {
        return state.is(ECTags.SUPPORTS_SCULK_TENDRIL);
    }

    @Override
    protected MapCodec<? extends VegetationBlock> codec()
    {
        return null;
    }
}
