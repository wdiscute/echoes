package com.wdiscute.echoes.blocks.marker;

import com.wdiscute.echoes.registry.ECBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TimelessMarkerBlockEntity extends BlockEntity
{
    public TimelessMarkerBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
        super(ECBlockEntities.TIMELESS_MARKER.get(), worldPosition, blockState);
    }
}