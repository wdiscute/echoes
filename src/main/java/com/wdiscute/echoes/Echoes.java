package com.wdiscute.echoes;

import com.wdiscute.echoes.registry.*;
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
    }
}
