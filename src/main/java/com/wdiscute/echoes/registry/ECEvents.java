package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.TimelessInstance;
import com.wdiscute.echoes.TimelessInstancesSD;
import com.wdiscute.echoes.entity.heart.SculkHeartEntity;
import com.wdiscute.echoes.network.ECDBPlaySoundPayload;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Echoes.MOD_ID)
public class ECEvents
{
    @SubscribeEvent
    public static void timelessTick(LevelTickEvent.Post event)
    {
        if (event.getLevel().isClientSide()) return;

        ServerLevel sl = (ServerLevel) event.getLevel();

        if (sl.dimension().equals(Echoes.TIMELESS))
            TimelessInstancesSD.getSavedData(sl.getServer()).tick(sl);
    }

    @SubscribeEvent
    public static void registerAttributed(EntityAttributeCreationEvent event)
    {
        event.put(ECEntities.SCULK_HEART.get(), SculkHeartEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerAttributed(RegisterPayloadHandlersEvent event)
    {

        final PayloadRegistrar registrar = event.registrar("1");
        
        registrar.playToClient(
                ECDBPlaySoundPayload.TYPE,
                ECDBPlaySoundPayload.STREAM_CODEC,
                ECDBPlaySoundPayload::handle
        );


    }
}
