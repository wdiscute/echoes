package com.wdiscute.echoes.datagen;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.registry.ECBlocks;
import com.wdiscute.echoes.registry.ECDataComponents;
import com.wdiscute.echoes.registry.ECItems;
import com.wdiscute.echoes.registry.ECPerks;
import com.wdiscute.echoes.upgrades.BlacksmithTrade;
import com.wdiscute.echoes.upgrades.PerkInstance;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.Utils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

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

        register(context, new BlacksmithTrade(
                new MaybeStack(BuiltInRegistries.ITEM.getKey(Items.WOODEN_HOE), 1,
                        DataComponentPatch.builder().set(ECDataComponents.PERKS.get(),
                                List.of(
                                        new PerkInstance(ECPerks.EXTRA_DAMAGE.getDelegate(), 1f),
                                        new PerkInstance(ECPerks.EXTRA_PERCENTAGE_SOULS.getDelegate(), 0.3f)
                                )
                        ).build()),
                BlacksmithTrade.Rarity.RARE,
                List.of(
                        new MaybeStack(ECItems.SCULK_TISSUE.getId(), 20),
                        new MaybeStack(Items.DIAMOND, 20),
                        new MaybeStack(Items.GOLD_INGOT, 3)
                ),
                15,
                10
        ));

        register(context, new BlacksmithTrade(
                new MaybeStack(BuiltInRegistries.ITEM.getKey(Items.REDSTONE_TORCH), 1,
                        DataComponentPatch.builder().set(ECDataComponents.PERKS.get(),
                                List.of(
                                        new PerkInstance(ECPerks.EXTRA_DAMAGE.getDelegate(), 2),
                                        new PerkInstance(ECPerks.EXTRA_SCULK_DAMAGE.getDelegate(), 4),
                                        new PerkInstance(ECPerks.EXTRA_TICKS_PER_KILL.getDelegate(), 20)
                                )
                        ).build()),
                BlacksmithTrade.Rarity.RARE,
                List.of(
                        new MaybeStack(ECItems.SCULK_TISSUE.getId(), 20),
                        new MaybeStack(Items.DIAMOND, 20),
                        new MaybeStack(Items.GOLD_INGOT, 3)
                ),
                15,
                10
        ));

        //diamond sword
        register(context, new BlacksmithTrade(
                new MaybeStack(BuiltInRegistries.ITEM.getKey(Items.LEVER), 1,
                        DataComponentPatch.builder().set(ECDataComponents.PERKS.get(),
                                List.of(
                                        new PerkInstance(ECPerks.EXTRA_DAMAGE.getDelegate(), 5f)
                                )
                        ).build()),
                BlacksmithTrade.Rarity.EPIC,
                List.of(
                        new MaybeStack(ECItems.SCULK_TISSUE.getId(), 20),
                        new MaybeStack(ECItems.SCULK_TISSUE.getId(), 20)
                ),
                25,
                10
        ));

        //netherite axe
        register(context, new BlacksmithTrade(
                new MaybeStack(BuiltInRegistries.ITEM.getKey(Items.STICK), 1,
                        DataComponentPatch.builder().set(ECDataComponents.PERKS.get(),
                                List.of(
                                        new PerkInstance(ECPerks.EXTRA_DAMAGE.getDelegate(), 3f),
                                        new PerkInstance(ECPerks.EXTRA_FLAT_SOULS.getDelegate(), 0.3f)
                                )
                        ).build()),
                BlacksmithTrade.Rarity.RARE,
                List.of(
                        new MaybeStack(ECItems.SCULK_TISSUE.getId(), 20),
                        new MaybeStack(Items.DIAMOND, 20),
                        new MaybeStack(Items.GOLD_INGOT, 3)
                ),
                15,
                10
        ));

        //echo blade
        register(context, new BlacksmithTrade(
                new MaybeStack(BuiltInRegistries.ITEM.getKey(ECItems.ECHO_BLADE.get())),
                BlacksmithTrade.Rarity.RARE,
                List.of(
                        new MaybeStack(ECItems.SCULK_TISSUE.getId(), 20),
                        new MaybeStack(Items.DIAMOND, 20),
                        new MaybeStack(Items.GOLD_INGOT, 3)
                ),
                15,
                10
        ));

    }

    public static void register(BootstrapContext<BlacksmithTrade> context, BlacksmithTrade trade)
    {
        context.register(getKey(trade), trade);
    }

    public static ResourceKey<BlacksmithTrade> getKey(BlacksmithTrade trade)
    {
        return ResourceKey.create(Echoes.BLACKSMITH_TRADE_KEY, Utils.rl("echoes", trade.stack().identifier().getPath()));
    }

    @Override
    public String getName()
    {
        return "BlacksmithTrades";
    }
}
