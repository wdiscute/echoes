package com.wdiscute.echoes.upgrades;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.registry.ECDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Arrays;
import java.util.List;

public record PerkInstance(Perk perk, List<Float> amplifiers)
{
    public PerkInstance(Holder<Perk> perk, Float... amplifiers)
    {
        this(perk.value(), Arrays.stream(amplifiers).toList());
    }

    public static DataComponentPatch toPatch(PerkInstance... list)
    {
        return DataComponentPatch.builder()
                .set(ECDataComponents.PERKS.get(), Arrays.stream(list).toList())
                .build();
    }

    public static final Codec<PerkInstance> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Echoes.PERK_REGISTRY.byNameCodec()
                            .fieldOf("perk")
                            .forGetter(PerkInstance::perk),

                    Codec.FLOAT.listOf()
                            .fieldOf("amplifiers")
                            .forGetter(PerkInstance::amplifiers)
            ).apply(instance, PerkInstance::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Perk> PERK_STREAM_CODEC =
            ByteBufCodecs.registry(Echoes.PERK);

    public static final StreamCodec<RegistryFriendlyByteBuf, PerkInstance> STREAM_CODEC =
            StreamCodec.composite(
                    PERK_STREAM_CODEC,
                    PerkInstance::perk,

                    ByteBufCodecs.FLOAT.apply(ByteBufCodecs.list()),
                    PerkInstance::amplifiers,

                    PerkInstance::new
            );
}