package com.wdiscute.echoes.upgrades;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.StringRepresentableAutoForEnums;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.List;

public record BlacksmithTrade(MaybeStack stack, Rarity rarity, List<MaybeStack> cost,
                              int powerLevel, int weight)
{

    public static final BlacksmithTrade EMPTY = new BlacksmithTrade(MaybeStack.EMPTY, Rarity.COMMON, List.of(), 0, 0);

    public static final Codec<BlacksmithTrade> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MaybeStack.CODEC.fieldOf("stack").forGetter(BlacksmithTrade::stack),
            Rarity.CODEC.fieldOf("rarity").forGetter(BlacksmithTrade::rarity),
            MaybeStack.CODEC.listOf().fieldOf("cost").forGetter(BlacksmithTrade::cost),
            Codec.INT.fieldOf("power_level").forGetter(BlacksmithTrade::powerLevel),
            Codec.INT.fieldOf("weight").forGetter(BlacksmithTrade::weight)
    ).apply(instance, BlacksmithTrade::new));

    public enum Rarity implements StringRepresentableAutoForEnums
    {
        COMMON,
        UNCOMMON,
        RARE,
        EPIC,
        LEGENDARY;

        public static final Codec<Rarity> CODEC = StringRepresentable.fromEnum(Rarity::values);
        public static final StreamCodec<FriendlyByteBuf, Rarity> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(Rarity.class);

        public String toTranslationKey()
        {
            return "echoes.rarity." + getSerializedName();
        }
    }
}
