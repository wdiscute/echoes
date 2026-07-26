package com.wdiscute.echoes.datagen;

import com.wdiscute.echoes.Echoes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Echoes.MOD_ID)
public class ECDataGenerators
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event)
    {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();

        //data entries
        ECDGDataEntriesProvider.start(gen, output);
        gen.addProvider(true, new ECDGModelProvider(output));
    }
}
