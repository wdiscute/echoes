package com.wdiscute.echoes.datagen;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.Rarity;
import com.wdiscute.echoes.registry.*;
import com.wdiscute.echoes.timeless.TimelessEnemyEntry;
import com.wdiscute.echoes.timeless.TimelessLevelEntry;
import com.wdiscute.echoes.timeless.TimelessLootEntry;
import com.wdiscute.echoes.upgrades.BlacksmithTrade;
import com.wdiscute.echoes.upgrades.PerkInstance;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.Utils;
import com.wdiscute.utils.datagen.DataEntryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;

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
                                                new PerkInstance(ECPerks.EXTRA_DAMAGE_CONSUMES_SOULS, 4F, 2f),
                                                new PerkInstance(ECPerks.ECHO_BLADE, 4F, 4f, 4f, 2f, 1.6f, 0.5f)
                                        ))
                                        .set(ECDataComponents.RARITY.get(), Rarity.COMMON)
                                        .build()
                        )
                )
        );

        gen.addProvider(true,
                new DataEntryProvider<>(output, lookup, ECDataEntries.TIMELESS_LEVELS,
                        List.of(
                                //starter
                                new TimelessLevelEntry(Echoes.rl("timeless/starter"), -1, 10, 0, 12000, Integer.MAX_VALUE),

                                //arenas
                                new TimelessLevelEntry(Echoes.rl("timeless/first"), 0, 10, 0, 3000, Integer.MAX_VALUE),
                                new TimelessLevelEntry(Echoes.rl("timeless/second"), 0, 10, 0, 3000, Integer.MAX_VALUE),
                                new TimelessLevelEntry(Echoes.rl("timeless/third"), 0, 10, 0, 6000, Integer.MAX_VALUE),
                                new TimelessLevelEntry(Echoes.rl("timeless/forth"), 0, 10, 0, 9000, Integer.MAX_VALUE),
                                new TimelessLevelEntry(Echoes.rl("timeless/fifth"), 0, 10, 0, 4000, Integer.MAX_VALUE)
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
                                new TimelessEnemyEntry(ECEntities.SCULKED.getId(),
                                        0, 10, 0, 1.3f, 0.5f, 1)


                        )
                )
        );

        gen.addProvider(true,
                new DataEntryProvider<>(output, lookup, ECDataEntries.GROUND_RANGED_ENEMIES,
                        List.of(
                                new TimelessEnemyEntry(ECEntities.HOLLOWED.getId(),
                                        0, 10, 0, 1.3f, 0.5f, 1)


                        )
                )
        );

        gen.addProvider(true,
                new DataEntryProvider<>(output, lookup, ECDataEntries.TIMELESS_LOOT,
                        List.of(
                                new TimelessLootEntry(new MaybeStack(Items.DIAMOND), Rarity.LEGENDARY, 0, 2, 0),
                                new TimelessLootEntry(new MaybeStack(Items.EMERALD), Rarity.EPIC, 0, 2, 0),
                                new TimelessLootEntry(new MaybeStack(Items.ECHO_SHARD), Rarity.EPIC, 0, 2, 0),
                                new TimelessLootEntry(new MaybeStack(Items.GOLD_INGOT), Rarity.RARE, 0, 10, 0),
                                new TimelessLootEntry(new MaybeStack(Items.IRON_INGOT), Rarity.UNCOMMON, 0, 10, 0),
                                new TimelessLootEntry(new MaybeStack(Items.COPPER_INGOT), Rarity.UNCOMMON, 0, 10, 0),
                                new TimelessLootEntry(new MaybeStack(Items.LAPIS_LAZULI), Rarity.COMMON, 0, 15, 0),
                                new TimelessLootEntry(new MaybeStack(Items.REDSTONE, 3), Rarity.COMMON, 0, 15, 0),
                                new TimelessLootEntry(new MaybeStack(ECItems.PRISMA_SHARD), Rarity.COMMON, 0, 20, 0)
                        )
                )
        );


        gen.addProvider(true,
                new DataEntryProvider<>(output, lookup, ECDataEntries.CHEST_LOOT,
                        List.of(
                                new TimelessLootEntry(new MaybeStack(BuiltInRegistries.ITEM.getKey(Items.POTION), 1,
                                        DataComponentPatch.builder().set(DataComponents.POTION_CONTENTS,
                                                new PotionContents(Potions.HEALING)).build()), Rarity.COMMON, 20),

                                new TimelessLootEntry(new MaybeStack(ECItems.SCULK_SPAWN), Rarity.COMMON, 10),
                                new TimelessLootEntry(new MaybeStack(ECItems.HOLLOWED_SPINE), Rarity.COMMON, 10),
                                new TimelessLootEntry(new MaybeStack(ECItems.SCULKED_TEETH), Rarity.COMMON, 10),
                                new TimelessLootEntry(new MaybeStack(ECItems.ECHOING_MARROW), Rarity.COMMON, 10),
                                new TimelessLootEntry(new MaybeStack(ECItems.ROT_BRAIN), Rarity.COMMON, 50),


                                new TimelessLootEntry(new MaybeStack(ECItems.PRISMA_SHARD), Rarity.COMMON, 10),
                                new TimelessLootEntry(new MaybeStack(ECItems.LATTICE), Rarity.COMMON, 10),
                                new TimelessLootEntry(new MaybeStack(ECItems.LUCENT_SHARD), Rarity.COMMON, 10),
                                new TimelessLootEntry(new MaybeStack(ECItems.CRYSTAL_CORE), Rarity.COMMON, 10),
                                new TimelessLootEntry(new MaybeStack(ECItems.LUCENT_DIE), Rarity.COMMON, 50),


                                new TimelessLootEntry(new MaybeStack(ECItems.SOUL), Rarity.COMMON, 2000)
                        )
                )
        );
    }
}
