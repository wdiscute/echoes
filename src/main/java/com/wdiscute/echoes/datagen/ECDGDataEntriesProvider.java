package com.wdiscute.echoes.datagen;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.registry.ECDataEntries;
import com.wdiscute.utils.Utils;
import com.wdiscute.utils.datagen.DataEntryProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;

import java.util.List;
import java.util.Map;

public class ECDGDataEntriesProvider
{
    public static void start(DataGenerator gen, PackOutput output)
    {
        gen.addProvider(true,
                new DataEntryProvider<>(output, ECDataEntries.STRUCTURE_ENTRIES,
                        List.of(Echoes.rl("arena/first"))
                )
        );

        gen.addProvider(true,
                new DataEntryProvider<>(output, ECDataEntries.HUBS,
                        List.of(
                                Echoes.rl("hub/first")
                        )
                )
        );

        gen.addProvider(true,
                new DataEntryProvider<>(output, ECDataEntries.SOULS,
                        Map.of(
                                Utils.rl("husk"), 0.3f,
                                Utils.rl("skeleton"), 1f,
                                Utils.rl("zombie"), 1.6f
                        )
                )
        );
    }


}
