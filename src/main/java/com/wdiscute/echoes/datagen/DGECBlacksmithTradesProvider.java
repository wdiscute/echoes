package com.wdiscute.echoes.datagen;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.registry.ECBlocks;
import com.wdiscute.echoes.registry.ECDataComponents;
import com.wdiscute.echoes.registry.ECItems;
import com.wdiscute.echoes.registry.ECPerks;
import com.wdiscute.echoes.upgrades.BlacksmithTrade;
import com.wdiscute.echoes.upgrades.Perk;
import com.wdiscute.echoes.upgrades.PerkInstance;
import com.wdiscute.echoes.upgrades.perks.ExtraDamagePerk;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.Utils;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class DGECBlacksmithTradesProvider extends DatapackBuiltinEntriesProvider
{
    public static final RegistrySetBuilder REGISTRY = new RegistrySetBuilder().add(Echoes.BLACKSMITH_TRADE_KEY, DGECBlacksmithTradesProvider::bootstrap);

    public DGECBlacksmithTradesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries, REGISTRY, Set.of(Echoes.MOD_ID, "minecraft"));
    }

    private static void bootstrap(BootstrapContext<BlacksmithTrade> context)
    {

        //
        //
        // ,--.--.  ,---.   ,---.   ,---.  ,--.,--. ,--.--.  ,---.  ,---.
        // |  .--' | .-. : (  .-'  | .-. | |  ||  | |  .--' | .--' | .-. :
        // |  |    \   --. .-'  `) ' '-' ' '  ''  ' |  |    \ `--. \   --.
        // `--'     `----' `----'   `---'   `----'  `--'     `---'  `----'
        //   ,--.                       ,--.
        // ,-'  '-. ,--.--.  ,--,--.  ,-|  |  ,---.
        // '-.  .-' |  .--' ' ,-.  | ' .-. | | .-. :
        //   |  |   |  |    \ '-'  | \ `-' | \   --.
        //   `--'   `--'     `--`--'  `---'   `----'
        //

        //10 spine > echoing marrow
        resourceTrade(context, BlacksmithTrade.Rarity.COMMON,  10, new MaybeStack(ECItems.HOLLOWED_SPINE.get()),
                new MaybeStack(ECItems.ECHOING_MARROW.get(), 10));

        //10 teeth > 1 brain
        resourceTrade(context, BlacksmithTrade.Rarity.COMMON,  10, new MaybeStack(ECItems.ROT_BRAIN.get()),
                new MaybeStack(ECItems.SCULKED_TEETH.get(), 10));

        //4 rot brains, 4 hollowed spine > soul heart container
        resourceTrade(context, BlacksmithTrade.Rarity.COMMON,  10, new MaybeStack(ECItems.SOUL_HEART_CONTAINER.get()),
                new MaybeStack(ECItems.ROT_BRAIN.get(), 4),new MaybeStack(ECItems.HOLLOWED_SPINE.get(), 4));


        //
        //                         ,--. ,--.
        //  ,---.   ,---. ,--.,--. |  | |  |,-.
        // (  .-'  | .--' |  ||  | |  | |     /
        // .-'  `) \ `--. '  ''  ' |  | |  \  \
        // `----'   `---'  `----'  `--' `--'`--'
        // ,--.   ,--.  ,---.   ,--,--.  ,---.   ,---.  ,--,--,   ,---.
        // |  |.'.|  | | .-. : ' ,-.  | | .-. | | .-. | |      \ (  .-'
        // |   .'.   | \   --. \ '-'  | | '-' ' ' '-' ' |  ||  | .-'  `)
        // '--'   '--'  `----'  `--`--' |  |-'   `---'  `--''--' `----'
        //                              `--'

        //echo blade
        allRarities(context, ECItems.ECHO_BLADE.get(),
                List.of(
                        new RaritifiedCost(ECItems.SCULKED_TEETH.get(), new RarityValues(1, 5, 15, 32, 64)),
                        new RaritifiedCost(ECItems.HOLLOWED_SPINE.get(), new RarityValues(0, 2, 5, 10, 25))
                ),

                new RaritifiedPerk(ECPerks.EXTRA_DAMAGE, new RarityValues(4, 5, 6, 7, 8)),
                new RaritifiedPerk(ECPerks.EXTRA_PERCENTAGE_SOULS, new RarityValues(1.6F, 1.8F, 2F, 2.2F, 2.5F))
        );

        //ramatra
        allRarities(context, ECItems.RAMATTRA.get(),
                List.of(
                        new RaritifiedCost(ECItems.SCULKED_TEETH.get(), new RarityValues(10, 25, 40, 70, 120)),
                        new RaritifiedCost(ECItems.HOLLOWED_SPINE.get(), new RarityValues(1, 3, 6, 10, 32))
                ),

                new RaritifiedPerk(ECPerks.RAMATTRA,
                        new RarityValues(1, 2, 3, 4, 5)
                )
        );

        //time reaper
        allRarities(context, ECItems.TIME_REAPER.get(),
                List.of(
                        new RaritifiedCost(ECBlocks.SCULK_TENDRIL.get().asItem(), new RarityValues(1, 5, 15, 32, 64)),
                        new RaritifiedCost(ECItems.ECHOING_MARROW.get(), new RarityValues(3, 7, 12, 30, 57)),
                        new RaritifiedCost(ECItems.ROT_BRAIN.get(), new RarityValues(1, 1, 1, 1, 1))
                ),

                new RaritifiedPerk(ECPerks.EXTRA_DAMAGE, new RarityValues(6, 8, 10, 15, 20)),
                new RaritifiedPerk(ECPerks.EXTRA_DAMAGE_CONSUMES_TIME,
                        new RarityValues(20, 20, 20, 20, 20),
                        new RarityValues(1, 2, 3, 4, 5)
                )
        );

        //gloombringer
        allRarities(context, ECItems.GLOOMBRINGER.get(),
                List.of(
                        new RaritifiedCost(ECItems.SCULKED_TEETH.get(), new RarityValues(1, 5, 15, 32, 64)),
                        new RaritifiedCost(ECItems.HOLLOWED_SPINE.get(), new RarityValues(0, 2, 5, 10, 25))
                ),

                new RaritifiedPerk(ECPerks.EXTRA_DAMAGE, new RarityValues(12, 16, 20, 25, 30))
        );

        //
        //                 ,--.
        //  ,---.  ,--.--. `--'  ,---.  ,--,--,--.  ,--,--.
        // | .-. | |  .--' ,--. (  .-'  |        | ' ,-.  |
        // | '-' ' |  |    |  | .-'  `) |  |  |  | \ '-'  |
        // |  |-'  `--'    `--' `----'  `--`--`--'  `--`--'
        // `--'
        // ,--.   ,--.  ,---.   ,--,--.  ,---.   ,---.  ,--,--,   ,---.
        // |  |.'.|  | | .-. : ' ,-.  | | .-. | | .-. | |      \ (  .-'
        // |   .'.   | \   --. \ '-'  | | '-' ' ' '-' ' |  ||  | .-'  `)
        // '--'   '--'  `----'  `--`--' |  |-'   `---'  `--''--' `----'
        //                              `--'

        //prisma sword
        allRarities(context, ECItems.PRISMA_SWORD.get(),
                List.of(
                        new RaritifiedCost(ECItems.PRISMA_SHARD.get(), new RarityValues(1, 5, 15, 32, 64)),
                        new RaritifiedCost(ECItems.LATTICE.get(), new RarityValues(0, 4, 8, 16, 32))
                ),

                new RaritifiedPerk(ECPerks.EXTRA_DAMAGE, new RarityValues(4, 5, 6, 7, 8)),
                new RaritifiedPerk(ECPerks.EXTRA_FLAT_SOULS, new RarityValues(0.3f, 0.8f, 1.3f, 2f, 4f))
        );

        //lucent will
        allRarities(context, ECItems.LUCENT_WILL.get(),
                List.of(
                        new RaritifiedCost(ECItems.LUCENT_SHARD.get(), new RarityValues(5, 15, 32, 64, 128)),
                        new RaritifiedCost(ECItems.CRYSTAL_CORE.get(), new RarityValues(1, 5, 15, 32, 64))
                ),

                new RaritifiedPerk(ECPerks.EXTRA_DAMAGE, new RarityValues(4, 5, 6, 7, 8)),
                new RaritifiedPerk(ECPerks.EXTRA_DAMAGE_CONSUMES_SOULS,
                        //souls cost
                        new RarityValues(5f, 4f, 3f, 2f, 1f),
                        //extra damage
                        new RarityValues(4f, 5f, 6f, 7f, 10f)
                )
        );

        //time keeper
        allRarities(context, ECItems.TIME_KEEPER.get(),
                List.of(
                        new RaritifiedCost(ECItems.PRISMA_SHARD.get(), new RarityValues(5, 15, 32, 64, 128)),
                        new RaritifiedCost(ECItems.CRYSTAL_CORE.get(), new RarityValues(1, 5, 15, 32, 64))
                ),

                new RaritifiedPerk(ECPerks.EXTRA_DAMAGE, new RarityValues(2, 3, 4, 5, 6)),
                new RaritifiedPerk(ECPerks.EXTRA_TICKS_PER_KILL, new RarityValues(40, 50, 60, 70, 80))
        );


    }

    public static void register(BootstrapContext<BlacksmithTrade> context, BlacksmithTrade trade)
    {
        context.register(getKey(trade), trade);
    }

    public static ResourceKey<BlacksmithTrade> getKey(BlacksmithTrade trade)
    {
        return ResourceKey.create(Echoes.BLACKSMITH_TRADE_KEY, Utils.rl("echoes", trade.stack().identifier().getPath() + "_" + trade.rarity().getSerializedName()));
    }

    //list must have 5 entries
    private record RaritifiedCost(Item item, RarityValues rarityValues)
    {
        public MaybeStack makeMaybeStack(BlacksmithTrade.Rarity rarity)
        {
            return new MaybeStack(item, (int) rarityValues.getForRarity(rarity));
        }
    }

    private record RaritifiedPerk(Holder<Perk> perk, RarityValues... rarityValues)
    {
        public PerkInstance makePerkInstance(BlacksmithTrade.Rarity rarity)
        {
            List<Float> valuesForRarity = new ArrayList<>();

            for (RarityValues rarityValues : rarityValues)
            {
                valuesForRarity.add(rarityValues.getForRarity(rarity));
            }

            return new PerkInstance(perk.value(), valuesForRarity);
        }
    }

    private record RarityValues(float common, float uncommon, float rare, float epic, float legendary)
    {
        public RarityValues of(float common, float uncommon, float rare, float epic, float legendary)
        {
            return new RarityValues(common, uncommon, rare, epic, legendary);
        }

        float getForRarity(BlacksmithTrade.Rarity rarity)
        {
            return switch (rarity)
            {
                case COMMON -> common;
                case UNCOMMON -> uncommon;
                case RARE -> rare;
                case EPIC -> epic;
                case LEGENDARY -> legendary;
            };
        }
    }

    static void resourceTrade(BootstrapContext<BlacksmithTrade> context, BlacksmithTrade.Rarity rarity, int weight, MaybeStack stack, MaybeStack... cost)
    {
        //register trade
        register(context, new BlacksmithTrade(stack, rarity, Arrays.stream(cost).toList(), weight));
    }

    static void allRarities(BootstrapContext<BlacksmithTrade> context, Item item, List<RaritifiedCost> cost, RaritifiedPerk... perks)
    {
        //for each rarity
        for (BlacksmithTrade.Rarity rarity : BlacksmithTrade.Rarity.values())
        {
            int weight = switch (rarity)
            {
                case COMMON -> 20;
                case UNCOMMON -> 15;
                case RARE -> 10;
                case EPIC -> 5;
                case LEGENDARY -> 2;
            };

            //get all perks to add to item based on rarity
            List<PerkInstance> perksToAdd = Arrays.stream(perks).map(o -> o.makePerkInstance(rarity)).toList();

            //build DataComponentPatch
            DataComponentPatch patch = DataComponentPatch.builder()
                    .set(ECDataComponents.PERKS.get(), perksToAdd)
                    .set(ECDataComponents.RARITY.get(), rarity)
                    .build();

            //register trade
            register(context,
                    new BlacksmithTrade(
                            new MaybeStack(BuiltInRegistries.ITEM.getKey(item), 1, patch),
                            rarity,
                            cost.stream().map(o -> o.makeMaybeStack(rarity)).filter(o -> o.count() > 0).toList(),
                            weight
                    )
            );
        }
    }


    @Override
    public String getName()
    {
        return "BlacksmithTrades";
    }
}
