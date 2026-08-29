package com.wdiscute.echoes.network;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.TimelessGUILayer;
import com.wdiscute.echoes.entity.heart.SculkHeartEntity;
import com.wdiscute.echoes.entity.specter.SpecterEmote;
import com.wdiscute.echoes.entity.specter.SpecterEntity;
import com.wdiscute.echoes.timeless.TimelessInstance;
import com.wdiscute.echoes.timeless.TimelessManager;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record ECSBSpecterAttemptEmotePayload(SpecterEmote emote) implements CustomPacketPayload
{
    public static final Type<ECSBSpecterAttemptEmotePayload> TYPE = new Type<>(Echoes.rl("emote_attempt"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ECSBSpecterAttemptEmotePayload> STREAM_CODEC = StreamCodec.composite(
            SpecterEmote.STREAM_CODEC,
            ECSBSpecterAttemptEmotePayload::emote,
            ECSBSpecterAttemptEmotePayload::new
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
            if(context.player() instanceof ServerPlayer sp)
            {
                ServerLevel sl = sp.level();
                TimelessInstance closest = TimelessManager.getClosest(sl.getServer(), sp.blockPosition());
                if(closest == null) return;

                List<SpecterEntity> list = sl.getEntitiesOfClass(
                        SpecterEntity.class,
                        new AABB(closest.origin).inflate(10000)
                );

                for (SpecterEntity specterEntity : list)
                {
                    if(specterEntity.player.getUUID() == sp.getUUID())
                    {
                        PacketDistributor.sendToPlayersNear(sl, null,
                                closest.origin.getX(),
                                closest.origin.getY(),
                                closest.origin.getZ(),
                                10000,
                                new ECCBSpecterEmotePayload(emote, sp.getUUID()) );
                    }
                }
            }
        });
    }
}

