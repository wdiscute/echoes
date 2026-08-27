package com.wdiscute.echoes.blocks.display;

import com.mojang.serialization.MapCodec;
import com.wdiscute.echoes.Rarity;
import com.wdiscute.echoes.registry.ECBlockEntities;
import com.wdiscute.echoes.upgrades.BlacksmithTrade;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class DisplayBlock extends HorizontalDirectionalBlock implements EntityBlock
{
    public static final EnumProperty<Rarity> RARITY = EnumProperty.create("rarity", Rarity.class);

    public DisplayBlock(Properties properties)
    {
        super(properties
                .lightLevel(_ -> 10)
                .noOcclusion()
                .strength(1.5F, 6.0F)
        );
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec()
    {
        return null;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(HorizontalDirectionalBlock.FACING);
        builder.add(RARITY);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
    {

        if (level.getBlockEntity(pos) instanceof DisplayBlockEntity dbe)
            if (dbe.clickedOn(player))
                return InteractionResult.SUCCESS;
            else
                player.sendOverlayMessage(Component.literal("Not enough materials..."));

        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
        return ECBlockEntities.DISPLAY.get().create(worldPosition, blockState);
    }
}
