package com.wdiscute.echoes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public record TimelessData(long timeToExit, Vec3 positionToExit, Identifier levelToReturn)
{
    public static final TimelessData EMPTY = new TimelessData(0, Vec3.ZERO, Level.OVERWORLD.identifier());

    public static final Codec<TimelessData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.LONG.fieldOf("time_to_exit").forGetter(t -> t.timeToExit),
                    Vec3.CODEC.fieldOf("position_to_exist").forGetter(t -> t.positionToExit),
                    Identifier.CODEC.fieldOf("level_to_return").forGetter(t -> t.levelToReturn)
            ).apply(instance, TimelessData::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, TimelessData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.LONG, data -> data.timeToExit,
            Vec3.STREAM_CODEC, data -> data.positionToExit,
            Identifier.STREAM_CODEC, data -> data.levelToReturn,
            TimelessData::new
    );

}
