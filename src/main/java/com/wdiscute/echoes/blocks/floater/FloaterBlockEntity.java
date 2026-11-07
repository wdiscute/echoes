package com.wdiscute.echoes.blocks.floater;

import com.wdiscute.echoes.registry.ECBlockEntities;
import com.wdiscute.utils.TickableBlockEntity;
import com.wdiscute.utils.Utils;
import com.wdiscute.utils.ValueHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class FloaterBlockEntity extends BlockEntity implements TickableBlockEntity
{
    public List<Utils.Duo<BlockPos, BlockState>> blocks = new ArrayList<>();

    public int offset = Utils.r.nextInt(1000);

    public FloaterBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
        super(ECBlockEntities.FLOATER.get(), worldPosition, blockState);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state)
    {
        TickableBlockEntity.super.tick(level, pos, state);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries)
    {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.saveAdditional(tag, registries);
        ValueHelper.store("blocks", Utils.Duo.codec(BlockPos.CODEC, BlockState.CODEC).listOf(), blocks, tag);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.loadAdditional(tag, registries);
        blocks = new ArrayList<>(ValueHelper.read("blocks", Utils.Duo.codec(BlockPos.CODEC, BlockState.CODEC).listOf(), tag).orElse(List.of()));
    }
}
