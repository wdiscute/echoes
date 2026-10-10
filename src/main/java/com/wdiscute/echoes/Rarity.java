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
    COMMON(),
    UNCOMMON(),
    RARE(),
    EPIC(),
    LEGENDARY(),
    UNIQUE(),
    ;

    private static final Rarity[] vals = values();
    Rarity()
    {
    }

    public static final Codec<Rarity> CODEC = StringRepresentable.fromEnum(Rarity::values);
    public static final StreamCodec<FriendlyByteBuf, Rarity> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(Rarity.class);

    public String toTranslationKey()
    {
        return "echoes.rarity." + getSerializedName();
    }

    public String toTranslationKeySimple()
    {
        return "echoes.rarity." + getSerializedName() + ".simple";
    }

    public String wrapWithRarityMarkdownAsString(String s)
    {
        return "<ec" + getSerializedName() + ">" + s + "</ec" + getSerializedName() + ">";
    }

    public Component wrapWithRarityMarkdown(String s)
    {
        return Component.literal("<ec" + getSerializedName() + ">" + s + "</ec" + getSerializedName() + ">");
    }

    public Rarity next()
    {
        int lenght = vals.length;
        return vals[(this.ordinal() + 1) % lenght];
    }
}
