package com.wdiscute.echoes.registry;

import com.mojang.serialization.Codec;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.timeless.TimelessLevelEntry;
import com.wdiscute.echoes.timeless.TimelessEnemyEntry;
import com.wdiscute.echoes.timeless.TimelessLootEntry;
import com.wdiscute.utils.DataEntry;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;

import java.util.List;
import java.util.Map;

public interface ECDataEntries
{
    DataEntry<List<TimelessLevelEntry>> TIMELESS_LEVELS = DataEntry.register(Echoes.rl("timeless_levels"),
            TimelessLevelEntry.CODEC.listOf(),
            List.of());

    DataEntry<List<TimelessLootEntry>> TIMELESS_LOOT = DataEntry.register(Echoes.rl("timeless_loot"),
            TimelessLootEntry.CODEC.listOf(),
            List.of());

    DataEntry<Map<Identifier, Float>> SOULS = DataEntry.register(Echoes.rl("souls_per_entity"),
            Codec.unboundedMap(Identifier.CODEC, Codec.FLOAT),
            Map.of());

    DataEntry<List<TimelessEnemyEntry>> GROUND_MELEE_ENEMIES = DataEntry.register(Echoes.rl("ground_melee_enemies"),
            TimelessEnemyEntry.CODEC.listOf(),
            List.of());

    DataEntry<List<TimelessEnemyEntry>> GROUND_RANGED_ENEMIES = DataEntry.register(Echoes.rl("ground_ranged_enemies"),
            TimelessEnemyEntry.CODEC.listOf(),
            List.of());

    DataEntry<List<TimelessEnemyEntry>> FLYING_ENEMIES = DataEntry.register(Echoes.rl("flying_enemies"),
            TimelessEnemyEntry.CODEC.listOf(),
            List.of());

    DataEntry<MaybeStack> STARTER_ITEM = DataEntry.register(Echoes.rl("starter_item"),
            MaybeStack.CODEC,
            MaybeStack.EMPTY);

    static void register(IEventBus bus){}
}
