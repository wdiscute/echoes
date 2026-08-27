package com.wdiscute.echoes;

import com.mojang.serialization.Codec;
import com.wdiscute.utils.StringRepresentableAutoForEnums;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public enum Rarity implements StringRepresentableAutoForEnums
{
    COMMON(0),
    UNCOMMON(1),
    RARE(2),
    EPIC(3),
    LEGENDARY(4);

    final int order;

    Rarity(int order)
    {
        this.order = order;
    }

    public static final Codec<Rarity> CODEC = StringRepresentable.fromEnum(Rarity::values);
    public static final StreamCodec<FriendlyByteBuf, Rarity> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(Rarity.class);

    public String toTranslationKey()
    {
        return "echoes.rarity." + getSerializedName();
    }

    public String wrapWithRarityMarkdownAsString(String s)
    {
        return "<ec" + getSerializedName() + ">" + s + "</ec" + getSerializedName() + ">";
    }

    public Component wrapWithRarityMarkdown(String s)
    {
        return Component.literal("<ec" + getSerializedName() + ">" + s + "</ec" + getSerializedName() + ">");
    }

    public int order()
    {
        return order;
    }
}
