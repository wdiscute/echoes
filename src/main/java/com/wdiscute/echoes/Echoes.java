package com.wdiscute.echoes;

import com.mojang.logging.LogUtils;
import com.wdiscute.echoes.anomaly.Anomaly;
import com.wdiscute.echoes.registry.*;
import com.wdiscute.echoes.swappers.Swapper;
import com.wdiscute.echoes.timeless.TimelessInstance;
import com.wdiscute.echoes.timeless.TimelessProcessor;
import com.wdiscute.echoes.upgrades.BlacksmithTrade;
import com.wdiscute.echoes.perk.Perk;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.slf4j.Logger;

import java.text.DecimalFormat;

@Mod(Echoes.MOD_ID)
public class Echoes
{
    public static final String MOD_ID = "echoes";
    public static final DecimalFormat FORMAT = new DecimalFormat("#.##");
    public static final ResourceLocation MISSINGNO = Echoes.rl("missingno");
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final ResourceKey<Level> TIMELESS = ResourceKey.create(Registries.DIMENSION, rl("timeless"));

    public static final ResourceKey<Registry<Perk>> PERK =
            ResourceKey.createRegistryKey(Echoes.rl("perk"));

    public static final ResourceKey<Registry<Anomaly>> ANOMALY =
            ResourceKey.createRegistryKey(Echoes.rl("anomaly"));

    public static final ResourceKey<Registry<Swapper>> SWAPPER =
            ResourceKey.createRegistryKey(Echoes.rl("swapper"));

    //registry
    public static final Registry<Perk> PERK_REGISTRY = new RegistryBuilder<>(PERK)
            .sync(true)
            .defaultKey(Echoes.rl("empty"))
            .create();

    public static final Registry<Swapper> SWAPPER_REGISTRY = new RegistryBuilder<>(SWAPPER)
            .sync(true)
            .defaultKey(Echoes.rl("empty"))
            .create();

    public static final Registry<Anomaly> ANOMALY_REGISTRY = new RegistryBuilder<>(ANOMALY)
            .sync(true)
            .defaultKey(Echoes.rl("empty"))
            .create();

    //resource keys
    public static final ResourceKey<Registry<BlacksmithTrade>> BLACKSMITH_TRADE_KEY =
            ResourceKey.createRegistryKey(Echoes.rl("blacksmith_trade"));

    public static ResourceLocation rl(String path)
    {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public Echoes(IEventBus modEventBus, ModContainer modContainer)
    {
        ECItems.register(modEventBus);
        ECBlocks.register(modEventBus);
        ECBlockEntities.register(modEventBus);
        ECEntities.register(modEventBus);
        ECArmorMaterials.register(modEventBus);
        ECAOEs.register(modEventBus);
        ECDataComponents.register(modEventBus);
        ECDataAttachments.register(modEventBus);
        ECDataEntries.register(modEventBus);
        ECEntityDataSerializers.register(modEventBus);
        ECPerks.register(modEventBus);
        ECCreativeModeTabs.register(modEventBus);
        ECParticles.register(modEventBus);
        ECInventorySwappers.register(modEventBus);

        TimelessInstance.RingCache.init(128);
        TimelessInstance.SphereCache.init(30);
        TimelessProcessor.addDefaultProcessors();

        modContainer.registerConfig(ModConfig.Type.CLIENT, ECConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.SERVER, ECConfig.SPEC_SERVER);
    }

}
