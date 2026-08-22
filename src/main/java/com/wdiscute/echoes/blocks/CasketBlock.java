package com.wdiscute.echoes.blocks;

import com.wdiscute.echoes.entity.soul.SoulEntity;
import com.wdiscute.echoes.registry.ECEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CasketBlock extends Block
{
    public CasketBlock(Properties properties)
    {
        super(properties.noOcclusion());
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        if(level instanceof ServerLevel sl)
        {
            SoulEntity soul = ECEntities.SOUL.get().spawn(sl, pos, EntitySpawnReason.TRIGGERED);
            soul.extraSoulsToSpawn = 10;
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }
        return InteractionResult.SUCCESS;
    }
}
