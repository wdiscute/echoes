package com.wdiscute.echoes;

import com.wdiscute.echoes.registry.*;
import com.wdiscute.echoes.timeless.TimelessInstance;
import com.wdiscute.echoes.timeless.TimelessProcessor;
import com.wdiscute.echoes.upgrades.BlacksmithTrade;
import com.wdiscute.echoes.upgrades.Perk;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.text.DecimalFormat;

@Mod(Echoes.MOD_ID)
public class Echoes
{
    public static final String MOD_ID = "echoes";
    public static final DecimalFormat FORMAT = new DecimalFormat("#.##");
    public static final Identifier MISSINGNO = Echoes.rl("missingno");

    public static final ResourceKey<Level> TIMELESS = ResourceKey.create(Registries.DIMENSION, rl("timeless"));

    public static final ResourceKey<Registry<Perk>> PERK =
            ResourceKey.createRegistryKey(Echoes.rl("perk"));

    //registry
    public static final Registry<Perk> PERK_REGISTRY = new RegistryBuilder<>(PERK)
            .sync(true)
            .defaultKey(Echoes.rl("empty"))
            .create();

    //resource keys
    public static final ResourceKey<Registry<BlacksmithTrade>> BLACKSMITH_TRADE_KEY =
            ResourceKey.createRegistryKey(Echoes.rl("blacksmith_trade"));

    public static Identifier rl(String path)
    {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public Echoes(IEventBus modEventBus, ModContainer modContainer)
    {
        ECItems.register(modEventBus);
        ECBlocks.register(modEventBus);
        ECBlockEntities.register(modEventBus);
        ECEntities.register(modEventBus);
        ECDataComponents.register(modEventBus);
        ECDataAttachments.register(modEventBus);
        ECDataEntries.register(modEventBus);
        ECEntityDataSerializers.register(modEventBus);
        ECPerks.register(modEventBus);
        ECCreativeModeTabs.register(modEventBus);
        ECParticles.register(modEventBus);

        TimelessInstance.RingCache.init(128);
        TimelessInstance.SphereCache.init(30);
        TimelessProcessor.addDefaultProcessors();

        modContainer.registerConfig(ModConfig.Type.CLIENT, ECConfig.SPEC);
    }

}
