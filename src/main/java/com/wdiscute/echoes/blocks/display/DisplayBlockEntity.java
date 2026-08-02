package com.wdiscute.echoes.blocks.display;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.registry.ECBlockEntities;
import com.wdiscute.echoes.registry.ECBlocks;
import com.wdiscute.echoes.upgrades.BlacksmithTrade;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.*;

public class DisplayBlockEntity extends BlockEntity
{
    public DisplayBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
        super(ECBlockEntities.DISPLAY.get(), worldPosition, blockState);
    }

    public BlacksmithTrade trade = BlacksmithTrade.EMPTY;

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries)
    {
        return saveWithoutMetadata(registries);
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        output.store("trade", BlacksmithTrade.CODEC, trade);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        trade = input.read("trade", BlacksmithTrade.CODEC).orElse(BlacksmithTrade.EMPTY);
    }

    public boolean clickedOn(Player player)
    {
        //if no trade, return false
        if ((trade == null || trade.equals(BlacksmithTrade.EMPTY)) && level instanceof ServerLevel sl)
        {
            if (sl.getBlockEntity(getBlockPos()) instanceof DisplayBlockEntity dbe)
            {
                List<BlacksmithTrade> list = sl.registryAccess().lookupOrThrow(Echoes.BLACKSMITH_TRADE_KEY).stream().toList();

                if (!list.isEmpty())
                    dbe.trade = list.get(sl.getRandom().nextInt(list.size()));
                dbe.setChanged();
            }

            sl.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 0);

            return true;
        }

        //return if player doesn't have enough items to pay
        if(!Utils.InventoryManagement.hasEnoughItems(trade.cost(), player.getInventory())) return false;

        //give player item bought
        player.addItem(trade.stack().toStack());

        //pay cost
        Utils.InventoryManagement.payItems(trade.cost(), player.getInventory());

        //playSound
        level.playSound(null, getBlockPos(), SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1f, 2);

        //remove trade
        trade = BlacksmithTrade.EMPTY;

        return true;
    }
}