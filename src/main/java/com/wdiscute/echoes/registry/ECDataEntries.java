package com.wdiscute.echoes.registry;

import com.mojang.serialization.Codec;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.timeless.TimelessLevelEntry;
import com.wdiscute.echoes.timeless.TimelessEnemyEntry;
import com.wdiscute.echoes.timeless.TimelessLootEntry;
import com.wdiscute.utils.DataEntry;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.Utils;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;

import java.util.List;
import java.util.Map;

public interface ECDataEntries
{
    DataEntry.MultiEntry<TimelessLevelEntry> TIMELESS_LEVELS = DataEntry.MultiEntry.register(Echoes.rl("timeless_levels"),
            TimelessLevelEntry.CODEC);

    DataEntry.MultiEntry<TimelessLootEntry> TIMELESS_LOOT = DataEntry.MultiEntry.register(Echoes.rl("timeless_loot"),
            TimelessLootEntry.CODEC);

    DataEntry.MultiEntry<TimelessLootEntry> CHEST_LOOT = DataEntry.MultiEntry.register(Echoes.rl("chest_loot"),
            TimelessLootEntry.CODEC);

    DataEntry.MultiEntry<Utils.Duo<Identifier, Float>> SOULS = DataEntry.MultiEntry.register(Echoes.rl("souls_per_entity"),
            Utils.Duo.codec(Identifier.CODEC, "entity", Codec.FLOAT, "souls"));

    DataEntry.MultiEntry<TimelessEnemyEntry> GROUND_MELEE_ENEMIES = DataEntry.MultiEntry.register(Echoes.rl("ground_melee_enemies"),
            TimelessEnemyEntry.CODEC);

    DataEntry.MultiEntry<TimelessEnemyEntry> GROUND_RANGED_ENEMIES = DataEntry.MultiEntry.register(Echoes.rl("ground_ranged_enemies"),
            TimelessEnemyEntry.CODEC);

    DataEntry.MultiEntry<TimelessEnemyEntry> FLYING_ENEMIES = DataEntry.MultiEntry.register(Echoes.rl("flying_enemies"),
            TimelessEnemyEntry.CODEC);

    DataEntry<MaybeStack> STARTER_ITEM = DataEntry.register(Echoes.rl("starter_item"),
            MaybeStack.CODEC,
            MaybeStack.EMPTY);

    static void register(IEventBus bus){}
}
