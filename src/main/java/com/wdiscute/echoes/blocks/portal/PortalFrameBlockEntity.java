package com.wdiscute.echoes.blocks.portal;

import com.wdiscute.echoes.registry.ECBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PortalFrameBlockEntity extends BlockEntity
{
    public PortalFrameBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
        super(ECBlockEntities.PORTAL.get(), worldPosition, blockState);
    }
}
