package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.swappers.Swapper;
import com.wdiscute.echoes.swappers.VanillaSwapper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface ECInventorySwappers
{
    DeferredRegister<Swapper> SWAPPERS =
            DeferredRegister.create(Echoes.SWAPPER_REGISTRY, Echoes.MOD_ID);

    DeferredHolder<Swapper, Swapper> EMPTY_SWAPPER = register("empty", Swapper::new);
    DeferredHolder<Swapper, Swapper> VANILLA_SWAPPER = register("vanilla", VanillaSwapper::new);


    static DeferredHolder<Swapper, Swapper> register(String name, Supplier<Swapper> swapper)
    {
        return SWAPPERS.register(name, swapper);
    }

    static void register(IEventBus eventBus)
    {
        SWAPPERS.register(eventBus);
    }
}
