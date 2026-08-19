package com.wdiscute.echoes.network;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.TimelessGUILayer;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record ECCBSetLootPayload(List<MaybeStack> list) implements CustomPacketPayload
{
    public static final Type<ECCBSetLootPayload> TYPE = new Type<>(Echoes.rl("clear_loot"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ECCBSetLootPayload> STREAM_CODEC = StreamCodec.composite(
            MaybeStack.STREAM_CODEC.apply(ByteBufCodecs.list()),
            ECCBSetLootPayload::list,
            ECCBSetLootPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }

    public void handle(IPayloadContext context)
    {
        context.enqueueWork(() -> TimelessGUILayer.setLoot(list));
    }
}

