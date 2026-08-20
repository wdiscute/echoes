package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.TimelessGUILayer;
import com.wdiscute.echoes.blocks.display.DisplayGuiLayer;
import com.wdiscute.echoes.blocks.display.DisplayRenderer;
import com.wdiscute.echoes.blocks.marker.TimelessMarkerRenderer;
import com.wdiscute.echoes.blocks.pane.PrismaPaneRenderer;
import com.wdiscute.echoes.blocks.portal.PortalRenderer;
import com.wdiscute.echoes.entity.corpse.TimelessCorpseModel;
import com.wdiscute.echoes.entity.corpse.TimelessCorpseModelSlim;
import com.wdiscute.echoes.entity.corpse.TimelessCorpseRenderer;
import com.wdiscute.echoes.entity.enemy.hollowed.HollowedRenderer;
import com.wdiscute.echoes.entity.enemy.sculked.SculkedRenderer;
import com.wdiscute.echoes.entity.heart.HeartModel;
import com.wdiscute.echoes.entity.heart.SculkHeartRenderer;
import com.wdiscute.echoes.entity.lantern.LanternModel;
import com.wdiscute.echoes.entity.lantern.LanternRenderer;
import com.wdiscute.echoes.ECPostProcessing;
import com.wdiscute.echoes.entity.soul.SoulModel;
import com.wdiscute.echoes.entity.soul.SoulRenderer;
import com.wdiscute.echoes.entity.trader.SoulTraderEntity;
import com.wdiscute.echoes.entity.trader.SoulTraderModel;
import com.wdiscute.echoes.entity.trader.SoulTraderRenderer;
import com.wdiscute.echoes.entity.unleashedsoul.UnleashedSoulModel;
import com.wdiscute.echoes.entity.unleashedsoul.UnleashedSoulRenderer;
import com.wdiscute.echoes.particles.SculkParticle;
import com.wdiscute.echoes.upgrades.PerkInstance;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        List<MutableComponent> perkComps = new ArrayList<>();
        for (var perk : event.getItemStack().getOrDefault(ECDataComponents.PERKS, List.<PerkInstance>of()))
        {
            List<MutableComponent> shopExtendedTooltip = perk.perk().getShopExtendedTooltip(perk.amplifiers());

            if (shopExtendedTooltip.isEmpty())
                perkComps.addAll(perk.perk().getItemTooltip(perk.amplifiers()));

            perkComps.addAll(shopExtendedTooltip.reversed());
        }

        for (var perkComp : perkComps)
            if (!event.getToolTip().isEmpty())
                event.getToolTip().add(1, perkComp.withStyle(ChatFormatting.GRAY));

    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        EntityRenderers.register(ECEntities.SOUL_TRADER.get(), SoulTraderRenderer::new);
        EntityRenderers.register(ECEntities.LANTERN.get(), LanternRenderer::new);
        EntityRenderers.register(ECEntities.SCULK_HEART.get(), SculkHeartRenderer::new);
        EntityRenderers.register(ECEntities.TIMELESS_CORPSE.get(), TimelessCorpseRenderer::new);
        EntityRenderers.register(ECEntities.SOUL.get(), SoulRenderer::new);
        EntityRenderers.register(ECEntities.UNLEASHED_SOUL.get(), UnleashedSoulRenderer::new);
        EntityRenderers.register(ECEntities.SCULKED.get(), SculkedRenderer::new);
        EntityRenderers.register(ECEntities.HOLLOWED.get(), HollowedRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        event.registerLayerDefinition(SoulTraderModel.LAYER_LOCATION, SoulTraderModel::createBodyLayer);
        event.registerLayerDefinition(HeartModel.LAYER_LOCATION, HeartModel::createBodyLayer);
        event.registerLayerDefinition(LanternModel.LAYER_LOCATION, LanternModel::createBodyLayer);
        event.registerLayerDefinition(SoulModel.LAYER_LOCATION, SoulModel::createBodyLayer);
        event.registerLayerDefinition(UnleashedSoulModel.LAYER_LOCATION, UnleashedSoulModel::createBodyLayer);
        event.registerLayerDefinition(TimelessCorpseModel.LAYER_LOCATION, TimelessCorpseModel::createBodyLayer);
        event.registerLayerDefinition(TimelessCorpseModelSlim.LAYER_LOCATION, TimelessCorpseModelSlim::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerBlockEntityRenderer(ECBlockEntities.TIMELESS_MARKER.get(), TimelessMarkerRenderer::new);
        event.registerBlockEntityRenderer(ECBlockEntities.PRISMA_PANE.get(), PrismaPaneRenderer::new);
        event.registerBlockEntityRenderer(ECBlockEntities.DISPLAY.get(), DisplayRenderer::new);
        event.registerBlockEntityRenderer(ECBlockEntities.PORTAL.get(), PortalRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event)
    {
        event.registerSpriteSet(ECParticles.SCULK.get(), SculkParticle.Provider::new);
    }

    @SubscribeEvent
    public static void onRenderGuiLayer(RenderGuiLayerEvent.Pre event)
    {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null)
            return;

        if (mc.player.level().dimension().equals(Echoes.TIMELESS) && event.getName().equals(VanillaGuiLayers.FOOD_LEVEL))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void registerBlockColorHandlers(RegisterColorHandlersEvent.BlockTintSources event)
    {
        event.register(
                List.of(BlockTintSources.grassBlock()),
                ECBlocks.GLEEMSLATE_GRASS.get()
        );
    }
}
