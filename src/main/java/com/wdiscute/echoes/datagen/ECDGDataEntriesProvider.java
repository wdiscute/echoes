package com.wdiscute.echoes.datagen;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.registry.ECDataEntries;
import com.wdiscute.utils.Utils;
import com.wdiscute.utils.datagen.DataEntryProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;

import java.util.List;

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
                new DataEntryProvider<>(output, ECDataEntries.BLACKSMITHS,
                        List.of(
                                Echoes.rl("blacksmith/first")
                        )
                )
        );
    }


}
