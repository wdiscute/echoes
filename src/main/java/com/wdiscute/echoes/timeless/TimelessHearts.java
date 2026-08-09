package com.wdiscute.echoes.timeless;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.echoes.registry.ECDataAttachments;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;

public record TimelessHearts(int soulHearts, int soulHP
)
{
    public static final TimelessHearts EMPTY = new TimelessHearts(0, 0);

    public static final Codec<TimelessHearts> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("soul_hearts").forGetter(t -> t.soulHearts),
                    Codec.INT.fieldOf("soul_hp").forGetter(t -> t.soulHP)
            ).apply(instance, TimelessHearts::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, TimelessHearts> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, data -> data.soulHearts,
            ByteBufCodecs.INT, data -> data.soulHP,
            TimelessHearts::new
    );

    public static TimelessHearts get(Player player)
    {
        return player.getData(ECDataAttachments.TIMELESS_HEARTS);
    }

    private TimelessHearts withSoulHP(int soulHP)
    {
        return new TimelessHearts(soulHearts, soulHP);
    }

    private TimelessHearts withHearts(int hearts)
    {
        return new TimelessHearts(hearts, soulHP);
    }

    public static int getAbsorbedDamage(Player player, int damage)
    {
        TimelessHearts data = player.getData(ECDataAttachments.TIMELESS_HEARTS);
        int damageAbsorbed = Math.min(damage, data.soulHP);
        player.setData(ECDataAttachments.TIMELESS_HEARTS, data.withSoulHP(Math.max(0, data.soulHP - damageAbsorbed)));
        return damageAbsorbed;
    }

    public static void addHeart(Player player)
    {
        TimelessHearts data = player.getData(ECDataAttachments.TIMELESS_HEARTS);
        player.setData(ECDataAttachments.TIMELESS_HEARTS, data.withHearts(data.soulHearts + 1));
    }

    public static void absorbSoul(Player player)
    {
        TimelessHearts data = player.getData(ECDataAttachments.TIMELESS_HEARTS);
        if (data.soulHP < data.soulHearts * 4)
            player.setData(ECDataAttachments.TIMELESS_HEARTS, data.withSoulHP(data.soulHP + 1));
    }
}
