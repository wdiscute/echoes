package com.wdiscute.echoes;

import com.wdiscute.echoes.registry.*;
import com.wdiscute.echoes.timeless.TimelessInstance;
import com.wdiscute.echoes.timeless.TimelessProcessor;
import com.wdiscute.echoes.upgrades.BlacksmithTrade;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(Echoes.MOD_ID)
public class Echoes
{
    public static final String MOD_ID = "echoes";

    public static final ResourceKey<Level> TIMELESS = ResourceKey.create(Registries.DIMENSION, rl("timeless"));

    //resource keys
    public static final ResourceKey<Registry<BlacksmithTrade>> BLACKSMITH_TRADE_KEY =
            ResourceKey.createRegistryKey(Echoes.rl("blacksmith_trade"));

    public static final Identifier MISSINGNO = Echoes.rl("missingno");

    public static Identifier rl(String path)
    {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public Echoes(IEventBus modEventBus, ModContainer modContainer)
    {
        ECItems.register(modEventBus);
        ECBlocks.register(modEventBus);
        ECDataAttachments.register(modEventBus);
        ECBlockEntities.register(modEventBus);
        ECEntities.register(modEventBus);
        ECDataEntries.register(modEventBus);
        ECEntityDataSerializers.register(modEventBus);

        TimelessInstance.SphereCache.init(128);
        TimelessProcessor.addDefaultProcessors();
    }
}
