package com.wdiscute.echoes.datagen;

import com.wdiscute.echoes.Echoes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Echoes.MOD_ID)
public class ECDataGenerators
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        //data entries
        ECDGDataEntriesProvider.start(gen, output, lookupProvider);

        //blacksmith entries
        event.createProvider(DGECBlacksmithTradesProvider::new);

        //block models
        gen.addProvider(event.includeServer(), new ECDGBlockModelProvider(output, existingFileHelper));

        //item models
        gen.addProvider(event.includeServer(), new ECDGItemModelProvider(output, existingFileHelper));

        //recipes
        gen.addProvider(event.includeServer(), new DGSCRecipeProvider(output, lookupProvider));

        //block tags
        BlockTagsProvider btp = new DGECBlocksTagsProvider(output, lookupProvider, existingFileHelper);
        gen.addProvider(event.includeServer(), btp);

        //item tags
        ItemTagsProvider itp = new DGECItemTagsProvider(output, lookupProvider, btp.contentsGetter(), existingFileHelper);
        gen.addProvider(event.includeServer(), itp);

        //loot table
        gen.addProvider(true, new LootTableProvider(output, Collections.emptySet(),
                List.of(
                        new LootTableProvider.SubProviderEntry(DGECBlockLootTableProvider::new, LootContextParamSets.BLOCK),
                        new LootTableProvider.SubProviderEntry(DGECEntityLootTableProvider::new, LootContextParamSets.ENTITY)
                ),
                lookupProvider));
    }
}
