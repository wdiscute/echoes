package com.wdiscute.echoes.item;

import com.wdiscute.echoes.blocks.floater.FloaterBlockEntity;
import com.wdiscute.echoes.registry.ECDataComponents;
import com.wdiscute.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class MagicalStick extends Item
{
    public MagicalStick(Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();

        if(player == null || stack == null || level == null)
            return super.useOn(context);

        BlockPos linkedPos = stack.getOrDefault(ECDataComponents.LINKED_BLOCK, BlockPos.ZERO);
        if (level.getBlockEntity(linkedPos) instanceof FloaterBlockEntity be && !(level.getBlockEntity(clickedPos) instanceof FloaterBlockEntity))
        {
            if(!level.isClientSide && !clickedPos.equals(be.getBlockPos()))
            {
                be.blocks.add(new Utils.Duo<>(clickedPos.subtract(be.getBlockPos()), level.getBlockState(clickedPos)));
                be.setChanged();
                level.sendBlockUpdated(linkedPos, level.getBlockState(linkedPos), level.getBlockState(linkedPos), 0);
                level.setBlockAndUpdate(clickedPos, Blocks.AIR.defaultBlockState());
            }
            return InteractionResult.SUCCESS;
        }

        player.displayClientMessage(Component.literal("linked " + clickedPos), true);
        stack.set(ECDataComponents.LINKED_BLOCK, clickedPos);
        return InteractionResult.SUCCESS;
    }
}
