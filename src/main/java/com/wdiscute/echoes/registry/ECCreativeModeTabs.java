package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import net.mcexpanded.fancytabsections.FancyTabSections;
import net.mcexpanded.fancytabsections.Section.SectionColored;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;

public class ECCreativeModeTabs
{
    public static void register(IEventBus bus)
    {
        Identifier echoes = Echoes.rl("echoes");
        FancyTabSections.registerCreativeModeTab(bus, echoes, ECItems.ECHO_BLADE);


        //general stuff
        FancyTabSections.addSection(echoes,
                new SectionColored(Echoes.rl("general"))
                        .setBannerColor(0xff344545)

                        .add(ECBlocks.PORTAL)
                        .add(Items.ECHO_SHARD)
                        .add(ECBlocks.TIMELESS_MARKER)
                        .add(ECBlocks.DISPLAY)

        );

        FancyTabSections.addSection(echoes,
                new SectionColored(Echoes.rl("weapons"))
                        .setBannerColor(0xff344545)

                        .add(ECItems.ECHO_BLADE)

        );

        FancyTabSections.addSection(echoes,
                new SectionColored(Echoes.rl("materials"))
                        .setBannerColor(0xff344545)

                        .add(ECItems.SCULK_TISSUE)

        );

        FancyTabSections.addSection(echoes,
                new SectionColored(Echoes.rl("sculk"))
                        .setBannerColor(0xff344545)

                        .add(ECBlocks.SCULK_PILLAR)
                        .add(ECBlocks.SCULK_SLAB)

        );

        FancyTabSections.addSection(echoes,
                new SectionColored(Echoes.rl("prisma"))
                        .setBannerColor(0xff344545)

                        .add(ECBlocks.GLEEMSLATE_PILLAR)
                        .add(ECBlocks.GLEEMSLATE_TILES)
                        .add(ECBlocks.CUT_GLEEMSLATE)
        );


    }
}
