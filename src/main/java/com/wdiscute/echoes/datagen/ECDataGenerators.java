package com.wdiscute.echoes.datagen;

import com.wdiscute.echoes.Echoes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Echoes.MOD_ID)
public class ECDataGenerators
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event)
    {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        //data entries
        ECDGDataEntriesProvider.start(gen, output);

        //models
        gen.addProvider(true, new ECDGModelProvider(output));

        //recipes
        event.getGenerator().addProvider(true, new DGSCRecipeProvider.Runner(output, lookupProvider));

        //block tags
        event.createProvider(DGECBlocksTagsProvider::new);

        //item tags
        //event.createProvider(DGECBlocksTagsProvider::new);
    }
}
