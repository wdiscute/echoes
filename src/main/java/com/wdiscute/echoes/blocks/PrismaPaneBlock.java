package com.wdiscute.echoes.blocks;

import com.mojang.serialization.MapCodec;
import com.wdiscute.echoes.blocks.marker.TimelessMarkerBlock;
import com.wdiscute.echoes.registry.ECBlockEntities;
import com.wdiscute.utils.StringRepresentableAutoForEnums;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class PrismaPaneBlock extends HorizontalDirectionalBlock implements EntityBlock
{
    public PrismaPaneBlock(Properties properties)
    {
        super(properties.lightLevel(o -> 15));
    }

    public static final EnumProperty<Facing> FACING = EnumProperty.create("facing", Facing.class);

    public enum Facing implements StringRepresentableAutoForEnums
    {
        NORTH,
        NORTHEAST,
        EAST,
        SOUTHEAST,
        SOUTH,
        SOUTHWEST,
        WEST,
        NORTHWEST,
        ;
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
        builder.add(FACING);
    }

    private static final VoxelShape NORTH_SOUTH_SHAPE =
            Block.box(0, 0, 4, 16, 16, 12);

    private static final VoxelShape EAST_WEST_SHAPE =
            Block.box(4, 0, 0, 12, 16, 16);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return switch (state.getValueOrElse(FACING, Facing.NORTH))
        {
            case NORTH, SOUTH -> NORTH_SOUTH_SHAPE;
            case EAST, WEST -> EAST_WEST_SHAPE;
            default -> NORTH_SOUTH_SHAPE;
        };
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context)
    {
        Facing facing = switch (context.getHorizontalDirection())
        {
            case NORTH -> Facing.NORTH;
            case SOUTH -> Facing.SOUTH;
            case WEST -> Facing.WEST;
            case EAST -> Facing.EAST;
            default -> Facing.NORTH;
        };

        return this.defaultBlockState().setValue(FACING, facing);
    }

    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.INVISIBLE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
        return ECBlockEntities.PRISMA_PANE.get().create(worldPosition, blockState);
    }
}
