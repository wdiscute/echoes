package com.wdiscute.echoes.network;

import com.wdiscute.echoes.Echoes;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ECDBPlaySoundPayload(String sound, float volume, float pitch) implements CustomPacketPayload
{
    public static final Type<ECDBPlaySoundPayload> TYPE = new Type<>(Echoes.rl("track_fish"));

    public static final StreamCodec<ByteBuf, ECDBPlaySoundPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            ECDBPlaySoundPayload::sound,
            ByteBufCodecs.FLOAT,
            ECDBPlaySoundPayload::volume,
            ByteBufCodecs.FLOAT,
            ECDBPlaySoundPayload::pitch,
            ECDBPlaySoundPayload::new
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
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;

            SoundEvent sound = switch (this.sound)
            {
                case "shriek" -> SoundEvents.SCULK_SHRIEKER_SHRIEK;
                case "beacon_deactivate" -> SoundEvents.BEACON_DEACTIVATE;
                case "beacon_activate" -> SoundEvents.BEACON_ACTIVATE;
                default -> SoundEvents.TOTEM_USE;
            };

            Minecraft.getInstance().getSoundManager().play(
                    SimpleSoundInstance.forUI(sound, pitch, volume)
            );
        });
    }
}

