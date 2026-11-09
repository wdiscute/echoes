package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.swappers.CuriosSwapper;
import com.wdiscute.echoes.swappers.EmptySwapper;
import com.wdiscute.echoes.swappers.Swapper;
import com.wdiscute.echoes.swappers.VanillaSwapper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface ECInventorySwappers
{
    DeferredRegister<Swapper> SWAPPERS =
            DeferredRegister.create(Echoes.SWAPPER_REGISTRY, Echoes.MOD_ID);

    DeferredHolder<Swapper, Swapper> EMPTY = SWAPPERS.register("empty", EmptySwapper::new);
    DeferredHolder<Swapper, Swapper> VANILLA = SWAPPERS.register("vanilla", VanillaSwapper::new);
    DeferredHolder<Swapper, Swapper> CURIOS = loaded("curios") ? SWAPPERS.register("curios", CuriosSwapper::new) : null;

    static boolean loaded(String modid)
    {
        return ModList.get().isLoaded(modid);
    }

    static void register(IEventBus eventBus)
    {
        SWAPPERS.register(eventBus);
    }
}