package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.TimelessGUILayer;
import com.wdiscute.echoes.TimelessInstance;
import com.wdiscute.echoes.entity.heart.SculkHeartEntity;
import com.wdiscute.echoes.entity.heart.SculkHeartRenderer;
import com.wdiscute.echoes.entity.lantern.LanternModel;
import com.wdiscute.echoes.entity.lantern.LanternRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@EventBusSubscriber(modid = Echoes.MOD_ID, value = Dist.CLIENT)
public class ECClientEvents
{
    @SubscribeEvent
    public static void registerHUD(RegisterGuiLayersEvent event)
    {
        event.registerAboveAll(Echoes.rl("echoes_gui"), new TimelessGUILayer());
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        EntityRenderers.register(ECEntities.LANTERN.get(), LanternRenderer::new);
        EntityRenderers.register(ECEntities.SCULK_HEART.get(), SculkHeartRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        event.registerLayerDefinition(LanternModel.LAYER_LOCATION, LanternModel::createBodyLayer);
    }
}
