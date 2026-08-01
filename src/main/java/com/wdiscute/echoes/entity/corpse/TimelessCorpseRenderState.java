package com.wdiscute.echoes.entity.corpse;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemStack;

public class TimelessCorpseRenderState extends EntityRenderState
{
    public final ItemStackRenderState item = new ItemStackRenderState();

    ItemStack stack = ItemStack.EMPTY;

    boolean isSlim = false;
}
