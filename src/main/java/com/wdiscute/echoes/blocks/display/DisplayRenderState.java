package com.wdiscute.echoes.blocks.display;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class DisplayRenderState extends BlockEntityRenderState
{
    public final ItemStackRenderState item = new ItemStackRenderState();
    ItemStack stack;
}
