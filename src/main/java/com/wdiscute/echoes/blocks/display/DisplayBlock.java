package com.wdiscute.echoes.blocks.display;

import com.wdiscute.echoes.registry.ECBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class DisplayBlock extends Block implements EntityBlock
{
    public DisplayBlock(Properties properties)
    {
        super(properties.noOcclusion());
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
