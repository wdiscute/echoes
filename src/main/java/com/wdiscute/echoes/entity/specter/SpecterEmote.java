package com.wdiscute.echoes.entity.specter;

import com.mojang.serialization.Codec;
import com.wdiscute.utils.StringRepresentableAutoForEnums;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public enum SpecterEmote implements StringRepresentableAutoForEnums
{
    SPIN,
    SIX_SEVEN,
    POINT,
    HEAD_EXPLODE
    ;

    public static final Codec<SpecterEmote> CODEC = StringRepresentable.fromEnum(SpecterEmote::values);
    public static final StreamCodec<FriendlyByteBuf, SpecterEmote> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(SpecterEmote.class);
}
