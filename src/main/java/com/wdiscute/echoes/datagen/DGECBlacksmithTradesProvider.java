package com.wdiscute.echoes.datagen;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.registry.ECBlocks;
import com.wdiscute.echoes.registry.ECItems;
import com.wdiscute.echoes.upgrades.BlacksmithTrade;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.Utils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
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
                new MaybeStack(Items.COPPER_SWORD),
                BlacksmithTrade.Rarity.COMMON,
                List.of(
                        new MaybeStack(ECItems.SCULK_TISSUE.getId(), 20)
                ),
                10,
                10
        ));

        register(context, new BlacksmithTrade(
                new MaybeStack(Items.DIAMOND_SWORD),
                BlacksmithTrade.Rarity.EPIC,
                List.of(
                        new MaybeStack(ECItems.SCULK_TISSUE.getId(), 20)
                ),
                10,
                10
        ));

        register(context, new BlacksmithTrade(
                new MaybeStack(Items.NETHERITE_SWORD),
                BlacksmithTrade.Rarity.LEGENDARY,
                List.of(
                        new MaybeStack(Items.NETHER_STAR)
                ),
                10,
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
