package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.utils.DataEntry;
import com.wdiscute.utils.Utils;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;

import java.util.List;

public interface ECDataEntries
{
    DataEntry<List<Utils.Duo<Identifier, Identifier>>> STRUCTURE_ENTRIES = DataEntry.register(Echoes.rl("timeless_structures"),
            Utils.Duo.codec(Identifier.CODEC, "sculk", Identifier.CODEC, "non_sculk").listOf(),
            List.of());

    static void register(IEventBus bus){}
}
