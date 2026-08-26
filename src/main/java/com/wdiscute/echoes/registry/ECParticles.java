package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface ECParticles
{
    DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, Echoes.MOD_ID);

    Supplier<SimpleParticleType> SCULK =
            PARTICLE_TYPES.register("sculk", () -> new SimpleParticleType(true));

     static void register(IEventBus eventBus)
    {
        PARTICLE_TYPES.register(eventBus);
    }

}
