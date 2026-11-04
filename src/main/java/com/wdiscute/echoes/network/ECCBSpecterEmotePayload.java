package com.wdiscute.echoes.network;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.entity.misc.specter.SpecterEmote;
import com.wdiscute.echoes.entity.misc.specter.SpecterEntity;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;
import java.util.UUID;

public record ECCBSpecterEmotePayload(SpecterEmote emote, UUID uuid) implements CustomPacketPayload
{
    public static final Type<ECCBSpecterEmotePayload> TYPE = new Type<>(Echoes.rl("emote"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ECCBSpecterEmotePayload> STREAM_CODEC = StreamCodec.composite(
            SpecterEmote.STREAM_CODEC,
            ECCBSpecterEmotePayload::emote,
            UUIDUtil.STREAM_CODEC,
            ECCBSpecterEmotePayload::uuid,
            ECCBSpecterEmotePayload::new
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
            Player player = context.player();
            List<SpecterEntity> list = player.level().getEntitiesOfClass(
                    SpecterEntity.class,
                    new AABB(player.blockPosition()).inflate(10000)
            );

            for (SpecterEntity specterEntity : list)
            {
                if (specterEntity.getEntityData().get(SpecterEntity.PLAYER_UUID).equals(uuid))
                {
                    specterEntity.playEmote(emote);
                }
            }
        });
    }
}

