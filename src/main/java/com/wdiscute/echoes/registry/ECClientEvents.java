package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.TimelessGUILayer;
import com.wdiscute.echoes.blocks.display.DisplayGuiLayer;
import com.wdiscute.echoes.blocks.display.DisplayRenderer;
import com.wdiscute.echoes.blocks.marker.TimelessMarkerRenderer;
import com.wdiscute.echoes.blocks.portal.PortalRenderer;
import com.wdiscute.echoes.entity.corpse.TimelessCorpseModel;
import com.wdiscute.echoes.entity.corpse.TimelessCorpseModelSlim;
import com.wdiscute.echoes.entity.corpse.TimelessCorpseRenderer;
import com.wdiscute.echoes.entity.heart.SculkHeartRenderer;
import com.wdiscute.echoes.entity.lantern.LanternModel;
import com.wdiscute.echoes.entity.lantern.LanternRenderer;
import com.wdiscute.echoes.ECPostProcessing;
import com.wdiscute.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

@EventBusSubscriber(modid = Echoes.MOD_ID, value = Dist.CLIENT)
public class ECClientEvents
{
    @SubscribeEvent
    public static void registerHUD(RegisterGuiLayersEvent event)
    {
        event.registerAboveAll(Echoes.rl("echoes_gui"), new TimelessGUILayer());
        event.registerAboveAll(Echoes.rl("display_gui"), new DisplayGuiLayer());
        event.registerAboveAll(Echoes.rl("post_processing"), new ECPostProcessing());
    }

    @SubscribeEvent
    public static void registerHUD(RegisterRenderPipelinesEvent event)
    {
        event.registerPipeline(ECRenderPipelines.PORTAL);
    }

    @SubscribeEvent
    public static void renderTooltip(ItemTooltipEvent event)
    {
        var perkComps = event.getItemStack().getOrDefault(ECDataComponents.PERKS, List.<Utils.Duo<Identifier, Float>>of()).stream().map(o -> ECPerks.get(o.first()).getTooltip(o.second())).toList();

        for (var perkComp : perkComps)
            if (!event.getToolTip().isEmpty())
                event.getToolTip().add(1, perkComp.withStyle(ChatFormatting.GRAY));
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
        event.registerBlockEntityRenderer(ECBlockEntities.PORTAL.get(), PortalRenderer::new);
    }
}
