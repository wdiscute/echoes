package com.wdiscute.echoes.timeless;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public record TimelessData(long timeToExit, List<MaybeStack> inventory)
{
    public static final TimelessData EMPTY = new TimelessData(-1, List.of());

    public static final Codec<TimelessData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.LONG.fieldOf("time_to_exit").forGetter(t -> t.timeToExit),
                    MaybeStack.CODEC.listOf().fieldOf("inventory").forGetter(t -> t.inventory)
            ).apply(instance, TimelessData::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, TimelessData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.LONG, data -> data.timeToExit,
            MaybeStack.STREAM_CODEC.apply(ByteBufCodecs.list()), data -> data.inventory,
            TimelessData::new
    );

}
