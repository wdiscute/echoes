package com.wdiscute.echoes.blocks.display;

import com.wdiscute.echoes.registry.ECBlockEntities;
import com.wdiscute.echoes.upgrades.BlacksmithTrade;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.List;

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
        for (MaybeStack maybeStack : trade.cost())
        {
            boolean safe = false;
            ItemStack costStack = maybeStack.toStack();
            for (ItemStack playerStack : player.getInventory())
            {
                if (playerStack.count() >= costStack.count() && costStack.is(playerStack.getItem()))
                {
                    safe = true;
                    break;
                }
            }
            if (!safe)
                return false;
        }

        return true;
    }
}