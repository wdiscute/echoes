package com.wdiscute.echoes.blocks.upgrader;

import com.wdiscute.echoes.registry.ECBlockEntities;
import com.wdiscute.echoes.upgrades.BlacksmithTrade;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.Utils;
import com.wdiscute.utils.ValueHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class UpgraderBlockEntity extends BlockEntity
{
    public UpgraderBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
        super(ECBlockEntities.UPGRADER.get(), worldPosition, blockState);
    }

    public MaybeStack item = null;
    public BlacksmithTrade.Entry nextItem = null;
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

        if (item != null)
            ValueHelper.store("item", MaybeStack.CODEC, item, tag);

        if (nextItem != null)
            ValueHelper.store("next_item", BlacksmithTrade.Entry.CODEC, nextItem, tag);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.loadAdditional(tag, registries);
        item = ValueHelper.read("item", MaybeStack.CODEC, tag).orElse(null);
        nextItem = ValueHelper.read("next_item", BlacksmithTrade.Entry.CODEC, tag).orElse(null);
    }
}