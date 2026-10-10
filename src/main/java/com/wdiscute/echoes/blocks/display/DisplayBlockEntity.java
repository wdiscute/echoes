package com.wdiscute.echoes.blocks.display;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.Rarity;
import com.wdiscute.echoes.registry.ECBlockEntities;
import com.wdiscute.echoes.registry.ECDataComponents;
import com.wdiscute.echoes.upgrades.BlacksmithTrade;
import com.wdiscute.utils.InventoryManagement;
import com.wdiscute.utils.Utils;
import com.wdiscute.utils.ValueHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class DisplayBlockEntity extends BlockEntity
{
    public DisplayBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
        super(ECBlockEntities.DISPLAY.get(), worldPosition, blockState);
    }

    public Utils.Duo<BlacksmithTrade.Entry, ResourceLocation> trade = null;
    public int timeOffset = Utils.r.nextInt();

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
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.saveAdditional(tag, registries);
        ValueHelper.store("trade", Utils.Duo.codec(BlacksmithTrade.Entry.CODEC, ResourceLocation.CODEC), trade, tag);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.loadAdditional(tag, registries);
        trade = ValueHelper.read("trade", Utils.Duo.codec(BlacksmithTrade.Entry.CODEC, ResourceLocation.CODEC), tag).orElse(null);
    }

    public boolean clickedOn(Player player)
    {
        Registry<BlacksmithTrade> registry = player.level().registryAccess().registryOrThrow(Echoes.BLACKSMITH_TRADE_KEY);
        //if no trade, return false
        if (trade == null && level instanceof ServerLevel sl)
        {
            if (sl.getBlockEntity(getBlockPos()) instanceof DisplayBlockEntity dbe)
            {
                List<BlacksmithTrade> list = registry.stream().toList();

                if (!list.isEmpty())
                    dbe.trade = BlacksmithTrade.getRandomTradeForDisplay(sl);

                dbe.setChanged();
            }

            sl.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 0);

            return true;
        }

        if (trade == null)
            return false;

        //return if player doesn't have enough items to pay
        if (!InventoryManagement.hasEnoughItems(trade.first().cost(), player.getInventory())) return false;

        //give player item bought and store trade info
        ItemStack stack = trade.first().item().toStack();
        registry.getOptional(trade.second()).ifPresent(o ->
        {
            if(o.hasUpgrades())
                stack.set(ECDataComponents.TRADE_INFO.get(), new Utils.Duo<>(Rarity.COMMON, trade.second()));
        });
        player.addItem(stack);

        //pay cost
        InventoryManagement.payItems(trade.first().cost(), player.getInventory());

        //playSound
        level.playSound(null, getBlockPos(), SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1f, 2);

        //remove trade
        trade = null;

        return true;
    }
}