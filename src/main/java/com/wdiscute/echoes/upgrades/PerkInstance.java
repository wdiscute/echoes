package com.wdiscute.echoes.upgrades;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.echoes.Echoes;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;

public record PerkInstance(Holder<Perk> perk, float amplifier)
{

    public static final Codec<PerkInstance> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    RegistryFixedCodec.create(Echoes.PERK).fieldOf("perk").forGetter(o -> o.perk),
                    Codec.FLOAT.fieldOf("amplifier").forGetter(o -> o.amplifier)
            ).apply(instance, PerkInstance::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Perk>> PERK_HOLDER_STREAM_CODEC = ByteBufCodecs.holderRegistry(Echoes.PERK);

    public static final StreamCodec<RegistryFriendlyByteBuf, PerkInstance> STREAM_CODEC = StreamCodec.composite(
            PERK_HOLDER_STREAM_CODEC,
            PerkInstance::perk,
            ByteBufCodecs.FLOAT,
            PerkInstance::amplifier,
            PerkInstance::new
    );
}
