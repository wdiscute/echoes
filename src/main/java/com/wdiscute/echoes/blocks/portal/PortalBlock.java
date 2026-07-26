package com.wdiscute.echoes.blocks.portal;

import com.wdiscute.echoes.TimelessInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;


public class PortalBlock extends Block
{
    public PortalBlock(Properties properties)
    {
        super(properties.noCollision());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
    {
        if(level.isClientSide()) return InteractionResult.SUCCESS;

        new TimelessInstance((ServerPlayer) player);

        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise)
    {
        super.entityInside(state, level, pos, entity, effectApplier, isPrecise);

        if(entity.is(EntityType.PLAYER))
        {
            //teleport
        }
        else
        {
            //push away
        }
    }
}
