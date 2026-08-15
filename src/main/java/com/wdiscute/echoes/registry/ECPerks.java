package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.upgrades.Perk;
import com.wdiscute.echoes.upgrades.perks.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface ECPerks
{
    DeferredRegister<Perk> PERKS =
            DeferredRegister.create(Echoes.PERK, Echoes.MOD_ID);


    DeferredHolder<Perk, Perk> EMPTY_PERK = register("empty", EmptyPerk::new);
    DeferredHolder<Perk, Perk> EXTRA_DAMAGE = register("extra_damage", ExtraDamagePerk::new);
    //DeferredHolder<Perk, Perk> EXTRA_SCULK_DAMAGE = register("extra_sculk_damage", ExtraSculkDamagePerk::new);
    DeferredHolder<Perk, Perk> EXTRA_TICKS_PER_KILL = register("extra_ticks_per_kill", ExtraTicksPerKillPerk::new);
    DeferredHolder<Perk, Perk> EXTRA_FLAT_SOULS = register("extra_flat_souls", ExtraFlatSoulsPerk::new);
    DeferredHolder<Perk, Perk> EXTRA_PERCENTAGE_SOULS = register("extra_percentage_souls", ExtraPercentageSoulsPerk::new);
    DeferredHolder<Perk, Perk> EXTRA_DAMAGE_CONSUMES_SOULS = register("extra_damage_consumes_souls", ExtraDamageConsumesSoulsPerk::new);
    DeferredHolder<Perk, Perk> EXTRA_DAMAGE_CONSUMES_TIME = register("extra_damage_consumes_time", ExtraDamageConsumesTimePerk::new);

    //fake perks
    DeferredHolder<Perk, Perk> RAMATTRA = register("ramattra", RamattraPerk::new);

    static DeferredHolder<Perk, Perk> register(String name, Supplier<Perk> perk)
    {
        return PERKS.register(name, perk);
    }

    static void register(IEventBus eventBus)
    {
        PERKS.register(eventBus);
    }
}
