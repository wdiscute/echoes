package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.anomaly.Anomaly;
import com.wdiscute.echoes.anomaly.EmptyAnomaly;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface ECAnomalies
{
    DeferredRegister<Anomaly> ANOMALIES =
            DeferredRegister.create(Echoes.ANOMALY, Echoes.MOD_ID);

    DeferredHolder<Anomaly, Anomaly> EMPTY_ANOMALY = register("empty", EmptyAnomaly::new);


    static DeferredHolder<Anomaly, Anomaly> register(String name, Supplier<Anomaly> anomaly)
    {
        return ANOMALIES.register(name, anomaly);
    }

    static void register(IEventBus eventBus)
    {
        ANOMALIES.register(eventBus);
    }
}
