package com.wdiscute.echoes.item;

import com.wdiscute.echoes.timeless.TimelessHearts;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SoulHeartContainer extends Item
{
    public SoulHeartContainer(Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        if(level.isClientSide()) return InteractionResultHolder.success(player.getItemInHand(hand));
        TimelessHearts.addHeart(player);
        player.getItemInHand(hand).shrink(1);
        return super.use(level, player, hand);
    }
}
