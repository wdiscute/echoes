package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.TimelessGUILayer;
import com.wdiscute.echoes.blocks.display.DisplayGuiLayer;
import com.wdiscute.echoes.blocks.display.DisplayRenderer;
import com.wdiscute.echoes.blocks.marker.TimelessMarkerRenderer;
import com.wdiscute.echoes.entity.corpse.TimelessCorpseModel;
import com.wdiscute.echoes.entity.corpse.TimelessCorpseModelSlim;
import com.wdiscute.echoes.entity.corpse.TimelessCorpseRenderer;
import com.wdiscute.echoes.entity.enemy.sculked.SculkedRenderer;
import com.wdiscute.echoes.entity.heart.SculkHeartRenderer;
import com.wdiscute.echoes.entity.lantern.LanternModel;
import com.wdiscute.echoes.entity.lantern.LanternRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

@EventBusSubscriber(modid = Echoes.MOD_ID, value = Dist.CLIENT)
public class ECClientEvents
{
    @SubscribeEvent
    public static void registerHUD(RegisterGuiLayersEvent event)
    {
        event.registerAboveAll(Echoes.rl("echoes_gui"), new TimelessGUILayer());
        event.registerAboveAll(Echoes.rl("display_gui"), new DisplayGuiLayer());
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        EntityRenderers.register(ECEntities.LANTERN.get(), LanternRenderer::new);
        EntityRenderers.register(ECEntities.SCULK_HEART.get(), SculkHeartRenderer::new);
        EntityRenderers.register(ECEntities.TIMELESS_CORPSE.get(), TimelessCorpseRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        event.registerLayerDefinition(LanternModel.LAYER_LOCATION, LanternModel::createBodyLayer);
        event.registerLayerDefinition(TimelessCorpseModel.LAYER_LOCATION, TimelessCorpseModel::createBodyLayer);
        event.registerLayerDefinition(TimelessCorpseModelSlim.LAYER_LOCATION, TimelessCorpseModelSlim::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerBlockEntityRenderer(ECBlockEntities.TIMELESS_MARKER.get(), TimelessMarkerRenderer::new);
        event.registerBlockEntityRenderer(ECBlockEntities.DISPLAY.get(), DisplayRenderer::new);
    }
}
