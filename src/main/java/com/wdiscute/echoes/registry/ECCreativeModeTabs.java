package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.upgrades.BlacksmithTrade;
import net.mcexpanded.fancytabsections.FancyTabSections;
import net.mcexpanded.fancytabsections.Section.SectionColored;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.ArrayList;
import java.util.List;

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
                        .add(ECItems.SOUL_HEART_CONTAINER)

        );

        FancyTabSections.addSection(echoes,
                new SectionColored(Echoes.rl("blacksmith_trades"))
                        .setBannerColor(0xff344545)

                        .add((dwa) ->
                        {
                            List<ItemStack> trades = new ArrayList<>();
                            for (BlacksmithTrade blacksmithTrade : dwa.lookupOrThrow(Echoes.BLACKSMITH_TRADE_KEY).stream().toList())
                                trades.add(blacksmithTrade.stack().toStack());
                            return trades;
                        })
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

                        .add(ECBlocks.SCULKED_DEEPSLATE)
                        .add(ECBlocks.SCULKED_DEEPSLATE_STAIRS)
                        .add(ECBlocks.SCULKED_DEEPSLATE_SLAB)
                        .add(ECBlocks.SCULKED_DEEPSLATE_WALL)

        );

        FancyTabSections.addSection(echoes,
                new SectionColored(Echoes.rl("gleemslate"))
                        .setBannerColor(0xff344545)

                        .add(ECBlocks.GLEEMSLATE)
                        .add(ECBlocks.GLEEMSLATE_SLAB)
                        .add(ECBlocks.GLEEMSLATE_STAIRS)
                        .add(ECBlocks.GLEEMSLATE_WALL)
                        .add(Items.AIR)
                        .add(ECBlocks.CUT_GLEEMSLATE_WALL)
                        .add(ECBlocks.CUT_GLEEMSLATE_STAIRS)
                        .add(ECBlocks.CUT_GLEEMSLATE_SLAB)
                        .add(ECBlocks.CUT_GLEEMSLATE)



                        .add(ECBlocks.GLEEMSLATE_TILES)
                        .add(ECBlocks.GLEEMSLATE_TILES_SLAB)
                        .add(ECBlocks.GLEEMSLATE_TILES_STAIRS)
                        .add(ECBlocks.GLEEMSLATE_TILES_WALL)
                        .add(Items.AIR)
                        .add(ECBlocks.GLEEMSLATE_BRICKS_WALL)
                        .add(ECBlocks.GLEEMSLATE_BRICKS_STAIRS)
                        .add(ECBlocks.GLEEMSLATE_BRICKS_SLAB)
                        .add(ECBlocks.GLEEMSLATE_BRICKS)


                        .add(Items.AIR)
                        .add(Items.AIR)
                        .add(Items.AIR)
                        .add(ECBlocks.CHISELED_GLEEMSLATE)
                        .add(ECBlocks.GLEEMSLATE_PILLAR)
                        .add(ECBlocks.TRIMMED_GLEEMSLATE)
                        .add(Items.AIR)
                        .add(Items.AIR)
                        .add(Items.AIR)


                        //.add(ECBlocks.PRISMA_PANE)


        );


    }
}
