package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.TimelessGUILayer;
import com.wdiscute.echoes.blocks.display.DisplayGuiLayer;
import com.wdiscute.echoes.blocks.display.DisplayRenderer;
import com.wdiscute.echoes.blocks.floater.FloaterBlockRenderer;
import com.wdiscute.echoes.blocks.marker.TimelessMarkerRenderer;
import com.wdiscute.echoes.blocks.pane.PrismaPaneRenderer;
import com.wdiscute.echoes.blocks.upgrader.UpgraderGuiLayer;
import com.wdiscute.echoes.blocks.upgrader.UpgraderRenderer;
import com.wdiscute.echoes.entity.boss.sculkedtrader.SculkedTraderEntity;
import com.wdiscute.echoes.entity.boss.sculkedtrader.SculkedTraderModel;
import com.wdiscute.echoes.entity.boss.sculkedtrader.SculkedTraderRenderer;
import com.wdiscute.echoes.entity.misc.aoe.AOEEntityRenderer;
import com.wdiscute.echoes.entity.misc.corpse.TimelessCorpseModel;
import com.wdiscute.echoes.entity.misc.corpse.TimelessCorpseModelSlim;
import com.wdiscute.echoes.entity.misc.corpse.TimelessCorpseRenderer;
import com.wdiscute.echoes.entity.enemy.hollowed.HollowedRenderer;
import com.wdiscute.echoes.entity.enemy.sculked.SculkedRenderer;
import com.wdiscute.echoes.entity.misc.heart.HeartModel;
import com.wdiscute.echoes.entity.misc.heart.SculkHeartRenderer;
import com.wdiscute.echoes.entity.misc.lantern.LanternModel;
import com.wdiscute.echoes.entity.misc.lantern.LanternRenderer;
import com.wdiscute.echoes.ECPostProcessing;
import com.wdiscute.echoes.entity.misc.soul.SoulModel;
import com.wdiscute.echoes.entity.misc.soul.SoulRenderer;
import com.wdiscute.echoes.entity.misc.specter.SpecterModel;
import com.wdiscute.echoes.entity.misc.specter.SpecterRenderer;
import com.wdiscute.echoes.entity.npc.trader.SoulTraderModel;
import com.wdiscute.echoes.entity.npc.trader.SoulTraderRenderer;
import com.wdiscute.echoes.entity.misc.unleashedsoul.UnleashedSoulModel;
import com.wdiscute.echoes.entity.misc.unleashedsoul.UnleashedSoulRenderer;
import com.wdiscute.echoes.particles.SculkParticle;
import com.wdiscute.echoes.perk.PerkInstance;
import com.wdiscute.libtooltips.Tooltips;
import com.wdiscute.utils.ScreenUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GrassColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = Echoes.MOD_ID, value = Dist.CLIENT)
public class ECClientEvents
{
    @SubscribeEvent
    public static void registerHUD(RegisterGuiLayersEvent event)
    {
        event.registerAboveAll(Echoes.rl("echoes_gui"), new TimelessGUILayer());
        event.registerAboveAll(Echoes.rl("display_gui"), new DisplayGuiLayer());
        event.registerAboveAll(Echoes.rl("upgrader_gui"), new UpgraderGuiLayer());
        event.registerAboveAll(Echoes.rl("post_processing"), new ECPostProcessing());
    }

    @SubscribeEvent
    public static void renderTooltip(ItemTooltipEvent event)
    {
        List<MutableComponent> perkComps = new ArrayList<>();
        for (var perk : event.getItemStack().getOrDefault(ECDataComponents.PERKS, List.<PerkInstance>of()).reversed())
        {
            List<MutableComponent> shopExtendedTooltip = perk.perk().getShopExtendedTooltip(event.getItemStack(), perk);

            if (shopExtendedTooltip.isEmpty())
            {
                perkComps.addAll(perk.perk().getItemTooltip(event.getItemStack(), perk));
            }

            List<MutableComponent> reversed = shopExtendedTooltip.reversed();
            if (!reversed.isEmpty())
            {
                perkComps.addAll(reversed);
                perkComps.add(Component.empty());
            }
        }

        for (var perkComp : perkComps)
            if (!event.getToolTip().isEmpty())
                event.getToolTip().add(1, perkComp.withStyle(ChatFormatting.GRAY));

        var tradeInfo = event.getItemStack().get(ECDataComponents.TRADE_INFO);
        if (tradeInfo != null)
            event.getToolTip().add(1, Tooltips.resolveTagsToComponentFromTranslationKey(tradeInfo.first().toTranslationKeySimple()).withStyle(ChatFormatting.DARK_GRAY));

        var timeless = event.getItemStack().get(ECDataComponents.TIMELESS);
        if (timeless != null)
            event.getToolTip().add(1, Tooltips.resolveTagsToComponentFromTranslationKey("echoes.timeless"));

    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        //misc
        EntityRenderers.register(ECEntities.SCULK_HEART.get(), SculkHeartRenderer::new);
        EntityRenderers.register(ECEntities.LANTERN.get(), LanternRenderer::new);
        EntityRenderers.register(ECEntities.TIMELESS_CORPSE.get(), TimelessCorpseRenderer::new);
        EntityRenderers.register(ECEntities.SOUL.get(), SoulRenderer::new);
        EntityRenderers.register(ECEntities.SPECTER.get(), SpecterRenderer::new);
        EntityRenderers.register(ECEntities.UNLEASHED_SOUL.get(), UnleashedSoulRenderer::new);
        EntityRenderers.register(ECEntities.AOE.get(), AOEEntityRenderer::new);

        //npc
        EntityRenderers.register(ECEntities.SOUL_TRADER.get(), SoulTraderRenderer::new);


        //bosses
        EntityRenderers.register(ECEntities.SCULKED_TRADER.get(), SculkedTraderRenderer::new);


        //enemies
        EntityRenderers.register(ECEntities.HOLLOWED.get(), HollowedRenderer::new);
        EntityRenderers.register(ECEntities.SCULKED.get(), SculkedRenderer::new);



        ECItemProperties.addCustomItemProperties();
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        //boss
        event.registerLayerDefinition(SculkedTraderModel.LAYER_LOCATION, SculkedTraderModel::createBodyLayer);

        //enemy

        //npc
        event.registerLayerDefinition(SoulTraderModel.LAYER_LOCATION, SoulTraderModel::createBodyLayer);

        //misc
        event.registerLayerDefinition(SpecterModel.LAYER_LOCATION, SpecterModel::createBodyLayer);
        event.registerLayerDefinition(UnleashedSoulModel.LAYER_LOCATION, UnleashedSoulModel::createBodyLayer);
        event.registerLayerDefinition(TimelessCorpseModel.LAYER_LOCATION, TimelessCorpseModel::createBodyLayer);
        event.registerLayerDefinition(TimelessCorpseModelSlim.LAYER_LOCATION, TimelessCorpseModelSlim::createBodyLayer);
        event.registerLayerDefinition(HeartModel.LAYER_LOCATION, HeartModel::createBodyLayer);
        event.registerLayerDefinition(LanternModel.LAYER_LOCATION, LanternModel::createBodyLayer);
        event.registerLayerDefinition(SoulModel.LAYER_LOCATION, SoulModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerBlockEntityRenderer(ECBlockEntities.FLOATER.get(), FloaterBlockRenderer::new);
        event.registerBlockEntityRenderer(ECBlockEntities.TIMELESS_MARKER.get(), TimelessMarkerRenderer::new);
        event.registerBlockEntityRenderer(ECBlockEntities.PRISMA_PANE.get(), PrismaPaneRenderer::new);
        event.registerBlockEntityRenderer(ECBlockEntities.DISPLAY.get(), DisplayRenderer::new);
        event.registerBlockEntityRenderer(ECBlockEntities.UPGRADER.get(), UpgraderRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event)
    {
        event.registerSpriteSet(ECParticles.SCULK.get(), SculkParticle.Provider::new);
    }

    public static final int[] RAINBOW = {0xaaFFAA00, 0xaaFF55FF, 0xaaFF55FF, 0xaa55cdff};

    @SubscribeEvent
    public static void onContainerScreenRenderForeground(ContainerScreenEvent.Render.Foreground event)
    {
        for (Slot slot : event.getContainerScreen().getMenu().slots)
        {
            ItemStack stack = slot.getItem();

            if (stack.has(ECDataComponents.TIMELESS.get()))
            {
                long time = System.currentTimeMillis();

                ScreenUtils.fillGradient(event.getGuiGraphics(), slot.x, slot.y, 16, 16,
                        ScreenUtils.gradient(time, 3000, RAINBOW),
                        ScreenUtils.gradient(time + 2000, 3000, RAINBOW)
                );
            }
        }
    }

    @SubscribeEvent
    public static void onRenderGuiLayer(RenderGuiLayerEvent.Pre event)
    {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null)
            return;

        if (mc.player.level().dimension().equals(Echoes.TIMELESS) && event.getName().equals(VanillaGuiLayers.FOOD_LEVEL))
            event.setCanceled(true);

        if (mc.player.level().dimension().equals(Echoes.TIMELESS) && event.getName().equals(VanillaGuiLayers.EXPERIENCE_LEVEL))
            event.setCanceled(true);

        //if (mc.player.level().dimension().equals(Echoes.TIMELESS) && event.getName().equals(VanillaGuiLayers.CONTEXTUAL_INFO_BAR))
        //    event.setCanceled(true);
    }

    @SubscribeEvent
    public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event)
    {
        event.register(
                (state, level, pos, tintIndex) ->
                        level != null && pos != null
                                ? BiomeColors.getAverageGrassColor(level, pos)
                                : GrassColor.get(0.5D, 1.0D),
                ECBlocks.GLEEMSLATE_GRASS.get()
        );
    }
}
