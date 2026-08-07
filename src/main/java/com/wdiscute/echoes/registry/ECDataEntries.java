package com.wdiscute.echoes.registry;

import com.mojang.serialization.Codec;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.utils.DataEntry;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;

import java.util.List;
import java.util.Map;

public interface ECDataEntries
{
    DataEntry<List<Identifier>> STRUCTURE_ENTRIES = DataEntry.register(Echoes.rl("timeless_arenas"),
            Identifier.CODEC.listOf(),
            List.of());

    DataEntry<List<Identifier>> HUBS = DataEntry.register(Echoes.rl("timeless_hubs"),
            Identifier.CODEC.listOf(),
            List.of());

    DataEntry<Map<Identifier, Float>> SOULS = DataEntry.register(Echoes.rl("souls_per_entity"),
            Codec.unboundedMap(Identifier.CODEC, Codec.FLOAT),
            Map.of());

    static void register(IEventBus bus){}
}
