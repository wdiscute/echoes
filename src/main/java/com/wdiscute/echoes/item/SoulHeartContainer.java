package com.wdiscute.echoes.item;

import com.wdiscute.echoes.timeless.TimelessHearts;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class SoulHeartContainer extends Item
{
    public SoulHeartContainer(Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand)
    {
        if(level.isClientSide()) return InteractionResult.SUCCESS;
        TimelessHearts.addHeart(player);
        player.getItemInHand(hand).shrink(1);
        return super.use(level, player, hand);
    }
}
