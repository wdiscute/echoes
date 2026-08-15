package com.wdiscute.echoes.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.timeless.TimelessData;
import com.wdiscute.echoes.timeless.TimelessEnemyInstance;
import com.wdiscute.utils.DataEntry;
import com.wdiscute.utils.MaybeStack;
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

    DataEntry<List<TimelessEnemyInstance>> GROUND_MELEE_ENEMIES = DataEntry.register(Echoes.rl("ground_melee_enemies"),
            TimelessEnemyInstance.CODEC.listOf(),
            List.of());

    DataEntry<List<TimelessEnemyInstance>> GROUND_RANGED_ENEMIES = DataEntry.register(Echoes.rl("ground_ranged_enemies"),
            TimelessEnemyInstance.CODEC.listOf(),
            List.of());

    DataEntry<List<TimelessEnemyInstance>> FLYING_ENEMIES = DataEntry.register(Echoes.rl("flying_enemies"),
            TimelessEnemyInstance.CODEC.listOf(),
            List.of());

    DataEntry<MaybeStack> STARTER_ITEM = DataEntry.register(Echoes.rl("starter_item"),
            MaybeStack.CODEC,
            MaybeStack.EMPTY);

    static void register(IEventBus bus){}
}
