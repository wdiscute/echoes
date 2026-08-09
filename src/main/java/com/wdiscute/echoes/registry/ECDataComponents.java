package com.wdiscute.echoes.registry;

import com.mojang.serialization.Codec;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.upgrades.PerkInstance;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.UnaryOperator;

public interface ECDataComponents
{
    DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Echoes.MOD_ID);

    DeferredHolder<DataComponentType<?>, DataComponentType<List<PerkInstance>>> PERKS = register(
            "perks", builder -> builder.persistent(PerkInstance.CODEC.listOf()));

    DeferredHolder<DataComponentType<?>, DataComponentType<Float>> STORED_SOULS = register(
            "stored_souls", builder -> builder.persistent(Codec.FLOAT));


    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name,
                                                                                           UnaryOperator<DataComponentType.Builder<T>> builderOperator)
    {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    static void register(IEventBus eventBus)
    {
        DATA_COMPONENT_TYPES.register(eventBus);
    }

}
