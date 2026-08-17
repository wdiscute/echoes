package com.wdiscute.echoes.blocks.pane;

import com.wdiscute.echoes.blocks.PrismaPaneBlock;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;

public class PrismaPaneRenderState extends BlockEntityRenderState
{
    public PrismaPaneBlock.Facing facing = PrismaPaneBlock.Facing.NORTH;
}
