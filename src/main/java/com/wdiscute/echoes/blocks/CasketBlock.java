package com.wdiscute.echoes.blocks;

import com.wdiscute.echoes.ECConfig;
import com.wdiscute.echoes.entity.soul.SoulEntity;
import com.wdiscute.echoes.registry.ECEntities;
import com.wdiscute.echoes.registry.ECItems;
import com.wdiscute.echoes.timeless.SoulsApi;
import com.wdiscute.echoes.timeless.TimelessInstance;
import com.wdiscute.echoes.timeless.TimelessLootEntry;
import com.wdiscute.echoes.timeless.TimelessManager;
import com.wdiscute.echoes.upgrades.Perk;
import com.wdiscute.echoes.upgrades.PerkInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class CasketBlock extends Block
{
    public CasketBlock(Properties properties)
    {
        super(properties.noOcclusion());
    }

    protected static final VoxelShape SHAPE = Block.box(0.0F, 0.0F, 0.0F, 16.0F, 13.0F, 16.0F);

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        if (level instanceof ServerLevel sl)
        {
            level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BARREL_OPEN, SoundSource.BLOCKS, 0.6F, 0.5F);
            level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.BLOCKS);

            List<PerkInstance> activePerks = Perk.getActivePerks(player, player.getMainHandItem());

            TimelessInstance closest = TimelessManager.getClosest(sl.getServer(), player.blockPosition());

            float rolls = ECConfig.BASE_CHEST_ROLLS.get();

            //add extra rolls from perks
            for (PerkInstance activePerk : activePerks)
                rolls += activePerk.perk().addChestRolls(player, activePerk.amplifiers());

            //get loot
            float baseSoulsToSpawn = 0;
            List<ItemStack> stacks = TimelessLootEntry.resolveStacks(level.getRandom(), closest == null ? 0 : closest.depth, rolls, true);
            for (ItemStack stack : stacks)
            {
                if (stack.is(ECItems.SOUL))
                {
                    baseSoulsToSpawn++;
                }
                else
                {
                    Vec3 itemPos = Vec3.atLowerCornerWithOffset(pos, 0.5, 1.01, 0.5).offsetRandom(level.getRandom(), 0.7F);
                    ItemEntity entity = new ItemEntity(level, itemPos.x(), itemPos.y(), itemPos.z(), stack);
                    entity.setDefaultPickUpDelay();
                    level.addFreshEntity(entity);
                }
            }

            int souls = SoulsApi.calculateSouls(player, ItemStack.EMPTY, baseSoulsToSpawn);
            if (souls >= 1)
            {
                SoulEntity soulEntity = ECEntities.SOUL.get().spawn(sl, pos, MobSpawnType.TRIGGERED);
                if (soulEntity != null && souls > 1)
                    soulEntity.extraSoulsToSpawn = souls - 1;
            }

            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }
        return ItemInteractionResult.SUCCESS;
    }
}
