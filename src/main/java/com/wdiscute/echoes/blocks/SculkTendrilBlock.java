package com.wdiscute.echoes.blocks;

import com.mojang.serialization.MapCodec;
import com.wdiscute.echoes.ECTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SculkTendrilBlock extends BushBlock
{
    public SculkTendrilBlock(Properties properties)
    {
        super(properties
                .noOcclusion()
                .noCollission()
                .sound(SoundType.SCULK)
        );
    }

    @Override
    protected MapCodec<? extends BushBlock> codec()
    {
        return null;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
    {
        BlockState belowBlockState = level.getBlockState(pos.below());
        return this.mayPlaceOn(belowBlockState, level, pos.below());
    }

    protected static final VoxelShape SHAPE = Block.box(2.0F, 0.0F, 2.0F, 14.0F, 12.0F, 14.0F);

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
}
