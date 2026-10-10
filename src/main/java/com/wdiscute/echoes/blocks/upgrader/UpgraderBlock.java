package com.wdiscute.echoes.blocks.upgrader;

import com.mojang.serialization.MapCodec;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.Rarity;
import com.wdiscute.echoes.registry.ECBlockEntities;
import com.wdiscute.echoes.registry.ECDataComponents;
import com.wdiscute.utils.InventoryManagement;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

public class UpgraderBlock extends HorizontalDirectionalBlock implements EntityBlock
{
    public UpgraderBlock(Properties properties)
    {
        super(properties
                .lightLevel(bs -> 10)
                .noOcclusion()
                .strength(1.5F, 6.0F)
        );
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec()
    {
        return null;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(HorizontalDirectionalBlock.FACING);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        if (level.getBlockEntity(pos) instanceof UpgraderBlockEntity dbe)
        {
            //if no item, place item
            if (dbe.item == null && !stack.isEmpty())
            {
                //if stack has trader info, set nextItem
                Utils.Duo<Rarity, ResourceLocation> duo = stack.get(ECDataComponents.TRADE_INFO);
                if (duo != null)
                    dbe.nextItem = player.level().registryAccess()
                            .registryOrThrow(Echoes.BLACKSMITH_TRADE_KEY)
                            .getOptional(duo.second())
                            .map(o -> o.getNext(duo.first()))
                            .orElse(null);

                dbe.item = new MaybeStack(stack.split(1));

                dbe.setChanged();
                level.sendBlockUpdated(pos, state, state, 0);
                return ItemInteractionResult.SUCCESS;
            }

            //if player is crouching, remove item
            if (player.isCrouching() && dbe.item != null)
            {
                player.addItem(dbe.item.toStack());
                dbe.nextItem = null;
                dbe.item = null;
                dbe.setChanged();
                level.sendBlockUpdated(pos, state, state, 0);
                return ItemInteractionResult.SUCCESS;
            }
            else
            {
                if (dbe.nextItem == null || dbe.item == null)
                    return super.useItemOn(stack, state, level, pos, player, hand, hitResult);

                //return if player doesn't have enough items to pay
                if (!InventoryManagement.hasEnoughItems(dbe.nextItem.cost(), player.getInventory()))
                {
                    player.displayClientMessage(Component.literal("Not enough materials to upgrade..."), true);
                    return ItemInteractionResult.FAIL;
                }

                //give player item bought and store trade info
                ItemStack stackToGive = dbe.nextItem.item().toStack();

                //if item in upgrader has trade info, copy it and increase rarity
                Utils.Duo<Rarity, ResourceLocation> current = dbe.item.toStack().get(ECDataComponents.TRADE_INFO.get());
                if (current != null)
                    stackToGive.set(ECDataComponents.TRADE_INFO.get(), new Utils.Duo<>(current.first().next(), current.second()));

                player.addItem(stackToGive);

                //pay cost
                InventoryManagement.payItems(dbe.nextItem.cost(), player.getInventory());

                //playSound
                level.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1f, 2);

                //remove trade
                dbe.item = null;
                dbe.nextItem = null;
                dbe.setChanged();
                level.sendBlockUpdated(pos, state, state, 0);
                return ItemInteractionResult.SUCCESS;
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
        return ECBlockEntities.UPGRADER.get().create(worldPosition, blockState);
    }
}
