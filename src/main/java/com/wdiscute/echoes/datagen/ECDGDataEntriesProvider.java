package com.wdiscute.echoes.datagen;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.registry.*;
import com.wdiscute.echoes.timeless.TimelessEnemyInstance;
import com.wdiscute.echoes.upgrades.BlacksmithTrade;
import com.wdiscute.echoes.upgrades.PerkInstance;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.Utils;
import com.wdiscute.utils.datagen.DataEntryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ECDGDataEntriesProvider
{
    public static void start(DataGenerator gen, PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
    {
        gen.addProvider(true,
                new DataEntryProvider<>(output, lookup, ECDataEntries.STARTER_ITEM,
                        new MaybeStack(BuiltInRegistries.ITEM.getKey(ECItems.ECHO_BLADE.get()), 1,
                                DataComponentPatch.builder()
                                        .set(ECDataComponents.PERKS.get(), List.of(
                                                new PerkInstance(ECPerks.EXTRA_DAMAGE, 4f),
                                                new PerkInstance(ECPerks.EXTRA_PERCENTAGE_SOULS, 1.6F)
                                        ))
                                        .set(ECDataComponents.RARITY.get(), BlacksmithTrade.Rarity.COMMON)
                                        .build()
                        )
                )
        );

        gen.addProvider(true,
                new DataEntryProvider<>(output, lookup, ECDataEntries.STRUCTURE_ENTRIES,
                        List.of(Echoes.rl("arena/first"))
                )
        );

        gen.addProvider(true,
                new DataEntryProvider<>(output, lookup, ECDataEntries.HUBS,
                        List.of(
                                Echoes.rl("hub/first")
                        )
                )
        );

        gen.addProvider(true,
                new DataEntryProvider<>(output, lookup, ECDataEntries.SOULS,
                        Map.of(
                                Utils.rl("echoes", "hollowed"), 0.8f,
                                Utils.rl("echoes", "sculked"), 1.3f
                        )
                )
        );

        gen.addProvider(true,
                new DataEntryProvider<>(output, lookup, ECDataEntries.GROUND_MELEE_ENEMIES,
                        List.of(
                                new TimelessEnemyInstance(ECEntities.SCULKED.getId(),
                                        0, 10, 0, 2, 1)


                        )
                )
        );

        gen.addProvider(true,
                new DataEntryProvider<>(output, lookup, ECDataEntries.GROUND_RANGED_ENEMIES,
                        List.of(
                                new TimelessEnemyInstance(ECEntities.HOLLOWED.getId(),
                                        0, 10, 0, 2, 1)


                        )
                )
        );


    }
}
