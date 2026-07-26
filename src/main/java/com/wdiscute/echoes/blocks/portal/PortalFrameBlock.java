package com.wdiscute.echoes.blocks.portal;

import com.mojang.serialization.MapCodec;
import com.wdiscute.echoes.registry.ECBlockEntities;
import com.wdiscute.echoes.registry.ECBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class PortalFrameBlock extends HorizontalDirectionalBlock implements EntityBlock
{
    public static final BooleanProperty HAS_SHARD = BooleanProperty.create("has_shard");

    public PortalFrameBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(HAS_SHARD, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(HorizontalDirectionalBlock.FACING);
        builder.add(HAS_SHARD);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec()
    {
        return null;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        if (!itemStack.is(Items.ECHO_SHARD.asItem()) || state.getValue(HAS_SHARD)) return InteractionResult.PASS;

        level.setBlockAndUpdate(pos, state.setValue(HAS_SHARD, true));

        if (!level.isClientSide())
            checkIfComplete(level, pos, state);

        return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
    }


    public static final List<BlockPos> portalArea = new ArrayList<>()
    {{
        add(new BlockPos(0, 0, -0));
        add(new BlockPos(-1, 0, -0));
        add(new BlockPos(1, 0, -0));

        add(new BlockPos(-2, 0, -1));
        add(new BlockPos(2, 0, -1));

        add(new BlockPos(-2, 0, -2));
        add(new BlockPos(2, 0, -2));

        add(new BlockPos(-2, 0, -3));
        add(new BlockPos(2, 0, -3));

        add(new BlockPos(0, 0, -4));
        add(new BlockPos(-1, 0, -4));
        add(new BlockPos(1, 0, -4));

    }};

    public void checkIfComplete(Level level, BlockPos pos, BlockState state)
    {
        Direction dir = state.getValue(FACING);

        for (BlockPos blockPos : portalArea)
        {
            if (dir.equals(Direction.NORTH))
                blockPos = blockPos.rotate(Rotation.NONE);

            if (dir.equals(Direction.EAST))
                blockPos = blockPos.rotate(Rotation.CLOCKWISE_90);

            if (dir.equals(Direction.WEST))
                blockPos = blockPos.rotate(Rotation.COUNTERCLOCKWISE_90);

            if (dir.equals(Direction.SOUTH))
                blockPos = blockPos.rotate(Rotation.CLOCKWISE_180);

            BlockPos bpToCheck = pos.offset(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            BlockState stateToCheck = level.getBlockState(bpToCheck);

            //level.setBlockAndUpdate(bpToCheck, Blocks.DIAMOND_BLOCK.defaultBlockState());

            //if state doesn't have shard, return
            if (!stateToCheck.getValueOrElse(HAS_SHARD, false)) return;
        }

        level.setBlockAndUpdate(pos.relative(dir), ECBlocks.PORTAL.get().defaultBlockState());
        level.setBlockAndUpdate(pos.relative(dir).relative(dir.getClockWise()), ECBlocks.PORTAL.get().defaultBlockState());
        level.setBlockAndUpdate(pos.relative(dir).relative(dir.getCounterClockWise()), ECBlocks.PORTAL.get().defaultBlockState());

        level.setBlockAndUpdate(pos.relative(dir).relative(dir), ECBlocks.PORTAL.get().defaultBlockState());
        level.setBlockAndUpdate(pos.relative(dir).relative(dir).relative(dir.getClockWise()), ECBlocks.PORTAL.get().defaultBlockState());
        level.setBlockAndUpdate(pos.relative(dir).relative(dir).relative(dir.getCounterClockWise()), ECBlocks.PORTAL.get().defaultBlockState());

        level.setBlockAndUpdate(pos.relative(dir).relative(dir).relative(dir), ECBlocks.PORTAL.get().defaultBlockState());
        level.setBlockAndUpdate(pos.relative(dir).relative(dir).relative(dir).relative(dir.getClockWise()), ECBlocks.PORTAL.get().defaultBlockState());
        level.setBlockAndUpdate(pos.relative(dir).relative(dir).relative(dir).relative(dir.getCounterClockWise()), ECBlocks.PORTAL.get().defaultBlockState());

    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
        return ECBlockEntities.PORTAL.get().create(worldPosition, blockState);
    }
}
