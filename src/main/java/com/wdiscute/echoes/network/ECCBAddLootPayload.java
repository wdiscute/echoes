package com.wdiscute.echoes.network;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.EchoesClient;
import com.wdiscute.echoes.TimelessGUILayer;
import com.wdiscute.utils.MaybeStack;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ECCBAddLootPayload(MaybeStack stack, boolean showNotif) implements CustomPacketPayload
{
    public static final Type<ECCBAddLootPayload> TYPE = new Type<>(Echoes.rl("add_loot"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ECCBAddLootPayload> STREAM_CODEC = StreamCodec.composite(
            MaybeStack.STREAM_CODEC,
            ECCBAddLootPayload::stack,
            ByteBufCodecs.BOOL,
            ECCBAddLootPayload::showNotif,
            ECCBAddLootPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }

    public void handle(IPayloadContext context)
    {
        context.enqueueWork(() -> TimelessGUILayer.addLoot(stack, showNotif));
    }
}

