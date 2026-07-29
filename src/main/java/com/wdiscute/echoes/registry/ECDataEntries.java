package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.utils.DataEntry;
import com.wdiscute.utils.Utils;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;

import java.util.List;

public interface ECDataEntries
{
    DataEntry<List<Identifier>> STRUCTURE_ENTRIES = DataEntry.register(Echoes.rl("timeless_arenas"),
            Identifier.CODEC.listOf(),
            List.of());

    DataEntry<List<Identifier>> BLACKSMITHS = DataEntry.register(Echoes.rl("timeless_blacksmiths"),
            Identifier.CODEC.listOf(),
            List.of());


    static void register(IEventBus bus){}
}
