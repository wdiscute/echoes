package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.TimelessGUILayer;
import com.wdiscute.echoes.TimelessInstance;
import com.wdiscute.echoes.registry.entity.lantern.LanternEntity;
import com.wdiscute.echoes.registry.entity.lantern.LanternRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@EventBusSubscriber(modid = Echoes.MOD_ID)
public class ECEvents
{
    @SubscribeEvent
    public static void registerHUD(RegisterGuiLayersEvent event)
    {
        event.registerAboveAll(Echoes.rl("echoes_gui"), new TimelessGUILayer());
    }

    @SubscribeEvent
    public static void timelessTick(LevelTickEvent.Post event)
    {
        if (event.getLevel().isClientSide()) return;

        ServerLevel sl = (ServerLevel) event.getLevel();
        if (sl.dimension().equals(Echoes.TIMELESS))
            TimelessInstance.INSTANCES.forEach(o -> o.tick(sl));
        TimelessInstance.INSTANCES.removeIf(o -> o.removed);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        EntityRenderers.register(ECEntities.LANTERN.get(), LanternRenderer::new);
    }
}
