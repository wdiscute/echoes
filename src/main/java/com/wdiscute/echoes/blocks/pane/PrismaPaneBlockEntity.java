package com.wdiscute.echoes.blocks.pane;

import com.wdiscute.echoes.blocks.marker.TimelessMarkerBlock;
import com.wdiscute.echoes.registry.ECBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class PrismaPaneBlockEntity extends BlockEntity
{
    public PrismaPaneBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
        super(ECBlockEntities.PRISMA_PANE.get(), worldPosition, blockState);
    }


    @Override
    public boolean hasCustomOutlineRendering(Player player)
    {
        return true;
    }
}