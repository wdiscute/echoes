package com.wdiscute.echoes.blocks.marker;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.wdiscute.echoes.registry.ECBlockEntities;
import com.wdiscute.utils.StringRepresentableAutoForEnums;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;


public class TimelessMarkerBlock extends HorizontalDirectionalBlock implements EntityBlock
{
    public static final EnumProperty<Type> TYPE = EnumProperty.create("type", Type.class);

    public TimelessMarkerBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec()
    {
        return null;
    }

    public enum Type implements StringRepresentableAutoForEnums
    {
        SPAWN_POINT,
        TIMELESS_CORPSE,
        HEART,
        LANTERN,
        GROUND_MELEE_ENEMY,
        GROUND_RANGED_ENEMY,
        FLYING_ENEMY,
        BLACKSMITH_NPC,
        BLACKSMITH_STAND,
        FOUNTAIN,
        PORTAL,
        ;

        public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);

        public Type next()
        {
            return values()[(this.ordinal() + 1) % values().length];
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(TYPE, Type.SPAWN_POINT).setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(TYPE);
        builder.add(FACING);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
    {
        level.setBlockAndUpdate(pos, state.setValue(TYPE, state.getValue(TYPE).next()));
        return InteractionResult.SUCCESS;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
        return ECBlockEntities.TIMELESS_MARKER.get().create(worldPosition, blockState);
    }
}
