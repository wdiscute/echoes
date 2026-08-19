package com.wdiscute.echoes.network;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.EchoesClient;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ECCBPlaySoundPayload(String sound, float volume, float pitch) implements CustomPacketPayload
{
    public static final Type<ECCBPlaySoundPayload> TYPE = new Type<>(Echoes.rl("play_sound"));

    public static final StreamCodec<ByteBuf, ECCBPlaySoundPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            ECCBPlaySoundPayload::sound,
            ByteBufCodecs.FLOAT,
            ECCBPlaySoundPayload::volume,
            ByteBufCodecs.FLOAT,
            ECCBPlaySoundPayload::pitch,
            ECCBPlaySoundPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }

    public void handle(IPayloadContext context)
    {
        context.enqueueWork(() ->
        {
            EchoesClient.playSoundPayload(this);
        });
    }
}

