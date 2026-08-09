package com.wdiscute.echoes.datagen;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.registry.ECItems;
import com.wdiscute.echoes.registry.ECPerks;
import com.wdiscute.echoes.upgrades.BlacksmithTrade;
import com.wdiscute.echoes.upgrades.PerkInstance;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.Utils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
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
        //echo blade
        register(context, new BlacksmithTrade(new MaybeStack(BuiltInRegistries.ITEM.getKey(ECItems.ECHO_BLADE.get()), 1,
                PerkInstance.toPatch(
                        new PerkInstance(ECPerks.EXTRA_DAMAGE, 6f),
                        new PerkInstance(ECPerks.EXTRA_PERCENTAGE_SOULS.getDelegate(), 1.6f)
                )),
                BlacksmithTrade.Rarity.RARE,
                List.of(
                        new MaybeStack(ECItems.SCULK_TISSUE.getId(), 20),
                        new MaybeStack(Items.DIAMOND, 20),
                        new MaybeStack(Items.GOLD_INGOT, 3)
                ),
                15,
                10
        ));


        //ramatra
        register(context, new BlacksmithTrade(new MaybeStack(BuiltInRegistries.ITEM.getKey(ECItems.RAMATTRA.get()), 1,
                PerkInstance.toPatch(
                        new PerkInstance(ECPerks.RAMATTRA, 2f),
                        new PerkInstance(ECPerks.RAMATTRA, 3f)
                )),
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
