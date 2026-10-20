package com.wdiscute.echoes.datagen;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.Rarity;
import com.wdiscute.echoes.perk.unique.EchoBladePerk;
import com.wdiscute.echoes.registry.ECBlocks;
import com.wdiscute.echoes.registry.ECDataComponents;
import com.wdiscute.echoes.registry.ECItems;
import com.wdiscute.echoes.registry.ECPerks;
import com.wdiscute.echoes.upgrades.BlacksmithTrade;
import com.wdiscute.echoes.perk.Perk;
import com.wdiscute.echoes.perk.PerkInstance;
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
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.*;
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
        //  ,--.           ,--.          ,--.               ,--.
        //,-'  '-. ,--.--. `--' ,--,--,  |  |,-.   ,---.  ,-'  '-.  ,---.
        //'-.  .-' |  .--' ,--. |      \ |     /  | .-. : '-.  .-' (  .-'
        //  |  |   |  |    |  | |  ||  | |  \  \  \   --.   |  |   .-'  `)
        //  `--'   `--'    `--' `--''--' `--'`--'  `----'   `--'   `----'
        //

        allRarities(context, ECItems.RECOVERY_TOTEM_ITEM.get(),
                List.of(
                        new RaritifiedCost(ECItems.PRISMA_SHARD.get(), new RarityValues(1, 5, 15, 32, 64)),
                        new RaritifiedCost(ECItems.LATTICE.get(), new RarityValues(0, 2, 5, 10, 25))
                ),

                new RaritifiedPerk(ECPerks.RECOVERY_TOTEM,
                        new RarityValues(Perk.COST, 0.25f, 0.5F, 1, 1.5f, 2),
                        new RarityValues(Perk.HEAL, 20, 20, 20, 20, 20),
                        new RarityValues(Perk.COOLDOWN, 4F, 4.5F, 5F, 5.5F, 6F),
                        new RarityValues(Perk.RANGE, 2, 2, 2, 2, 2)
                )

        );


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
        resourceTrade(context, Rarity.COMMON, 10, new MaybeStack(ECItems.HOLLOWED_SPINE.get()),
                new MaybeStack(ECItems.ECHOING_MARROW.get(), 10));

        //10 teeth > 1 brain
        resourceTrade(context, Rarity.COMMON, 10, new MaybeStack(ECItems.ROT_BRAIN.get()),
                new MaybeStack(ECItems.SCULKED_TEETH.get(), 10));

        //4 rot brains, 4 hollowed spine > soul heart container
        resourceTrade(context, Rarity.COMMON, 10, new MaybeStack(ECItems.SOUL_HEART_CONTAINER.get()),
                new MaybeStack(ECItems.ROT_BRAIN.get(), 4), new MaybeStack(ECItems.HOLLOWED_SPINE.get(), 4));


        //
        //
        //,--.   ,--.  ,---.   ,--,--.  ,---.   ,---.  ,--,--,   ,---.
        //|  |.'.|  | | .-. : ' ,-.  | | .-. | | .-. | |      \ (  .-'
        //|   .'.   | \   --. \ '-'  | | '-' ' ' '-' ' |  ||  | .-'  `)
        //'--'   '--'  `----'  `--`--' |  |-'   `---'  `--''--' `----'
        //                             `--'

        //echo/prisma blade
        allRarities(context, ECItems.ECHO_BLADE.get(),
                List.of(
                        new RaritifiedCost(ECItems.SCULKED_TEETH.get(), new RarityValues(1, 5, 15, 32, 64)),
                        new RaritifiedCost(ECItems.HOLLOWED_SPINE.get(), new RarityValues(0, 2, 5, 10, 25))
                ),

                new RaritifiedPerk(ECPerks.EXTRA_DAMAGE,
                        new RarityValues(Perk.DAMAGE, 4F, 5F, 6F, 7F, 8F)),

                new RaritifiedPerk(ECPerks.EXTRA_DAMAGE_CONSUMES_SOULS,
                        new RarityValues(Perk.COST, 4f, 4f, 4f, 4f, 4f),
                        new RarityValues(Perk.DAMAGE, 4F, 6F, 8F, 10F, 12F)
                ),

                new RaritifiedPerk(ECPerks.ECHO_BLADE,
                        //flat damage echo
                        new RarityValues(EchoBladePerk.ECHO_DAMAGE, 4F, 5F, 6F, 7F, 8F),
                        //flat damage prisma
                        new RarityValues(EchoBladePerk.PRISMA_DAMAGE, 4F, 5F, 6F, 7F, 8F),

                        //echo extra damage when consuming soul
                        new RarityValues(EchoBladePerk.ECHO_EXTRA_DAMAGE, 4F, 6F, 8F, 10F, 12F),
                        //echo souls consumed
                        new RarityValues(EchoBladePerk.ECHO_SOULS_COST, 2F, 3F, 4F, 5F, 6F),

                        //extra souls gathered percentage
                        new RarityValues(EchoBladePerk.PRISMA_SOULS_PERCENTAGE, 0.6F, 0.8F, 1F, 1.2F, 2F),
                        //extra souls gathered flat
                        new RarityValues(EchoBladePerk.PRISMA_SOULS_FLAT, 0.5F, 1F, 1.5F, 2F, 5F)
                )
        );


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


        //ramatra
        allRarities(context, ECItems.RAMATTRA.get(),
                List.of(
                        new RaritifiedCost(ECItems.SCULKED_TEETH.get(), new RarityValues(10, 25, 40, 70, 120)),
                        new RaritifiedCost(ECItems.HOLLOWED_SPINE.get(), new RarityValues(1, 3, 6, 10, 32))
                ),

                new RaritifiedPerk(ECPerks.RAMATTRA,
                        new RarityValues(Perk.DAMAGE, 1, 2, 3, 4, 5)
                )
        );

        //time reaper
        allRarities(context, ECItems.TIME_REAPER.get(),
                List.of(
                        new RaritifiedCost(ECBlocks.SCULK_TENDRIL.get().asItem(), new RarityValues(1, 5, 15, 32, 64)),
                        new RaritifiedCost(ECItems.ECHOING_MARROW.get(), new RarityValues(3, 7, 12, 30, 57)),
                        new RaritifiedCost(ECItems.ROT_BRAIN.get(), new RarityValues(1, 1, 1, 1, 1))
                ),

                new RaritifiedPerk(ECPerks.EXTRA_DAMAGE, new RarityValues(Perk.DAMAGE, 6, 8, 10, 15, 20)),
                new RaritifiedPerk(ECPerks.EXTRA_DAMAGE_CONSUMES_TIME,
                        new RarityValues(Perk.COST, 20, 20, 20, 20, 20),
                        new RarityValues(Perk.DAMAGE, 1, 2, 3, 4, 5)
                )
        );

        //gloombringer
        allRarities(context, ECItems.GLOOMBRINGER.get(),
                List.of(
                        new RaritifiedCost(ECItems.SCULKED_TEETH.get(), new RarityValues(1, 5, 15, 32, 64)),
                        new RaritifiedCost(ECItems.HOLLOWED_SPINE.get(), new RarityValues(0, 2, 5, 10, 25))
                ),

                new RaritifiedPerk(ECPerks.EXTRA_DAMAGE, new RarityValues(Perk.DAMAGE,12, 16, 20, 25, 30))
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

        //lucent will
        allRarities(context, ECItems.LUCENT_WILL.get(),
                List.of(
                        new RaritifiedCost(ECItems.LUCENT_SHARD.get(), new RarityValues(5, 15, 32, 64, 128)),
                        new RaritifiedCost(ECItems.CRYSTAL_CORE.get(), new RarityValues(1, 5, 15, 32, 64))
                ),

                new RaritifiedPerk(ECPerks.EXTRA_DAMAGE, new RarityValues(Perk.DAMAGE, 4, 5, 6, 7, 8)),
                new RaritifiedPerk(ECPerks.EXTRA_DAMAGE_CONSUMES_SOULS,
                        //souls cost
                        new RarityValues(Perk.COST, 4f, 4f, 4f, 4f, 4f),
                        //extra damage
                        new RarityValues(Perk.DAMAGE, 4f, 5f, 6f, 7f, 10f)
                )
        );

        //time keeper
        allRarities(context, ECItems.TIME_KEEPER.get(),
                List.of(
                        new RaritifiedCost(ECItems.PRISMA_SHARD.get(), new RarityValues(5, 15, 32, 64, 128)),
                        new RaritifiedCost(ECItems.CRYSTAL_CORE.get(), new RarityValues(1, 5, 15, 32, 64))
                ),

                new RaritifiedPerk(ECPerks.EXTRA_DAMAGE, new RarityValues(Perk.DAMAGE,3, 4, 5, 6, 7)),
                new RaritifiedPerk(ECPerks.EXTRA_TICKS_PER_KILL, new RarityValues(Perk.VALUE,200, 300, 400, 500, 600))
        );

        //
        //
        // ,--,--. ,--.--. ,--,--,--.  ,---.  ,--.--.
        //' ,-.  | |  .--' |        | | .-. | |  .--'
        //\ '-'  | |  |    |  |  |  | ' '-' ' |  |
        // `--`--' `--'    `--`--`--'  `---'  `--'
        //

        allRarities(context, ECItems.TIMELOST_HELMET.get(),
                List.of(
                        new RaritifiedCost(ECItems.PRISMA_SHARD.get(), new RarityValues(5, 15, 32, 64, 128)),
                        new RaritifiedCost(ECItems.CRYSTAL_CORE.get(), new RarityValues(1, 5, 15, 32, 64))
                ),

                new RaritifiedPerk(ECPerks.FLAT_REDUCE_DAMAGE_TAKEN, new RarityValues(Perk.VALUE,0.2f, 0.4f, 0.6f, 0.8f, 1f)),
                new RaritifiedPerk(ECPerks.INCREASE_TIME_ON_ENTERING_LEVEL, new RarityValues(Perk.VALUE,500, 800, 1100, 1400, 1700))
        );

        allRarities(context, ECItems.TIMELOST_CHESTPLATE.get(),
                List.of(
                        new RaritifiedCost(ECItems.PRISMA_SHARD.get(), new RarityValues(5, 15, 32, 64, 128)),
                        new RaritifiedCost(ECItems.CRYSTAL_CORE.get(), new RarityValues(1, 5, 15, 32, 64))
                ),

                new RaritifiedPerk(ECPerks.FLAT_REDUCE_DAMAGE_TAKEN, new RarityValues(Perk.VALUE,0.2f, 0.4f, 0.6f, 0.8f, 1f)),
                new RaritifiedPerk(ECPerks.INCREASE_TIME_ON_ENTERING_LEVEL, new RarityValues(Perk.VALUE,500, 800, 1100, 1400, 1700))
        );

        allRarities(context, ECItems.TIMELOST_LEGGINGS.get(),
                List.of(
                        new RaritifiedCost(ECItems.PRISMA_SHARD.get(), new RarityValues(5, 15, 32, 64, 128)),
                        new RaritifiedCost(ECItems.CRYSTAL_CORE.get(), new RarityValues(1, 5, 15, 32, 64))
                ),

                new RaritifiedPerk(ECPerks.FLAT_REDUCE_DAMAGE_TAKEN, new RarityValues(Perk.VALUE,0.2f, 0.4f, 0.6f, 0.8f, 1f)),
                new RaritifiedPerk(ECPerks.INCREASE_TIME_ON_ENTERING_LEVEL, new RarityValues(Perk.VALUE,500, 800, 1100, 1400, 1700))
        );

        allRarities(context, ECItems.TIMELOST_BOOTS.get(),
                List.of(
                        new RaritifiedCost(ECItems.PRISMA_SHARD.get(), new RarityValues(5, 15, 32, 64, 128)),
                        new RaritifiedCost(ECItems.CRYSTAL_CORE.get(), new RarityValues(1, 5, 15, 32, 64))
                ),

                new RaritifiedPerk(ECPerks.FLAT_REDUCE_DAMAGE_TAKEN, new RarityValues(Perk.VALUE,0.2f, 0.4f, 0.6f, 0.8f, 1f)),
                new RaritifiedPerk(ECPerks.INCREASE_TIME_ON_ENTERING_LEVEL, new RarityValues(Perk.VALUE,500, 800, 1100, 1400, 1700))
        );
    }

    public static void register(BootstrapContext<BlacksmithTrade> context, BlacksmithTrade trade)
    {
        context.register(getKey(trade), trade);
    }

    public static ResourceKey<BlacksmithTrade> getKey(BlacksmithTrade trade)
    {
        return ResourceKey.create(Echoes.BLACKSMITH_TRADE_KEY, Utils.rl("echoes", trade.getBase().item().identifier().getPath()));
    }

    //list must have 5 entries
    private record RaritifiedCost(Item item, RarityValues rarityValues)
    {
        public MaybeStack makeMaybeStack(Rarity rarity)
        {
            return new MaybeStack(item, (int) rarityValues.getForRarity(rarity));
        }
    }

    private record RaritifiedPerk(Holder<Perk> perk, RarityValues... rarityValues)
    {
        public PerkInstance makePerkInstance(Rarity rarity)
        {
            Map<String, Float> valuesForRarity = new HashMap<>();

            for (RarityValues value : rarityValues)
            {
                if (value.id.isEmpty())
                    throw new IllegalStateException("tried to make perk instance with empty id: " + perk.value());
                valuesForRarity.put(value.id, value.getForRarity(rarity));
            }


            return new PerkInstance(perk.value(), valuesForRarity);
        }
    }

    private record RarityValues(String id, float common, float uncommon, float rare, float epic, float legendary)
    {
        public RarityValues(float common, float uncommon, float rare, float epic, float legendary)
        {
            this("", common, uncommon, rare, epic, legendary);
        }

        public RarityValues of(String id, float common, float uncommon, float rare, float epic, float legendary)
        {
            return new RarityValues(id, common, uncommon, rare, epic, legendary);
        }

        float getForRarity(Rarity rarity)
        {
            return switch (rarity)
            {
                case COMMON -> common;
                case UNCOMMON -> uncommon;
                case RARE -> rare;
                case EPIC -> epic;
                case LEGENDARY -> legendary;
                default -> common;
            };
        }
    }

    static void resourceTrade(BootstrapContext<BlacksmithTrade> context, Rarity rarity, int weight, MaybeStack stack, MaybeStack... cost)
    {
        //register trade
        register(context, new BlacksmithTrade(List.of(new BlacksmithTrade.Entry(Rarity.COMMON, stack, Arrays.stream(cost).toList())), weight, false));
    }

    static void allRarities(BootstrapContext<BlacksmithTrade> context, Item item, List<RaritifiedCost> cost, RaritifiedPerk... perks)
    {
        List<BlacksmithTrade.Entry> entries = new ArrayList<>();
        //for each rarity
        for (Rarity rarity : Rarity.values())
        {
            //all rarities does not include unique
            if (rarity.equals(Rarity.UNIQUE))
                break;

            //get all perks to add to item based on rarity
            List<PerkInstance> perksToAdd = Arrays.stream(perks).map(o -> o.makePerkInstance(rarity)).toList();

            //build DataComponentPatch
            DataComponentPatch patch = DataComponentPatch.builder()
                    .set(ECDataComponents.PERKS.get(), perksToAdd)
                    .set(ECDataComponents.RARITY.get(), rarity)
                    .build();

            //add rarity to entries
            entries.add(new BlacksmithTrade.Entry(rarity, new MaybeStack(BuiltInRegistries.ITEM.getKey(item), 1, patch),
                    cost.stream().map(o -> o.makeMaybeStack(rarity)).filter(o -> o.count() > 0).toList()));

        }

        //register trade
        register(context, new BlacksmithTrade(entries, 20, true)
        );
    }


    @Override
    public String getName()
    {
        return "BlacksmithTrades";
    }
}
