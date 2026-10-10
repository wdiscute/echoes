package com.wdiscute.echoes.upgrades;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.Rarity;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.Utils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

import java.util.List;

public record BlacksmithTrade(List<Entry> entries, int weight, boolean hasUpgrades)
{
    public Entry getBase()
    {
        return entries.stream().filter(o -> o.rarity.equals(Rarity.COMMON)).findAny().orElse(Entry.EMPTY);
    }

    public List<MaybeStack> getDisplayCost()
    {
        return getBase().cost;
    }

    public static Utils.Duo<Entry, ResourceLocation> getRandomTradeForDisplay(ServerLevel sl)
    {
        Utils.Duo<BlacksmithTrade, ResourceLocation> randomTrade = getRandomTrade(sl);
        return new Utils.Duo<>(randomTrade.first().getBase(), randomTrade.second());
    }

    public static Utils.Duo<BlacksmithTrade, ResourceLocation> getRandomTrade(ServerLevel sl)
    {
        Registry<BlacksmithTrade> registry = sl.registryAccess().registryOrThrow(Echoes.BLACKSMITH_TRADE_KEY);
        List<BlacksmithTrade> trades = registry.stream().toList();

        int totalWeight = trades.stream()
                .mapToInt(BlacksmithTrade::weight)
                .sum();

        if (totalWeight <= 0)
            throw new IllegalArgumentException("There are no Blacksmith trades registered or they have no weights");

        int random = sl.getRandom().nextInt(totalWeight);

        for (BlacksmithTrade trade : trades)
        {
            random -= trade.weight();

            if (random < 0)
                return new Utils.Duo<>(trade, registry.getKey(trade));
        }

        throw new IllegalStateException("New Advancement Obtained: How did we get here?");
    }

    public static final BlacksmithTrade EMPTY = new BlacksmithTrade(List.of(), 0, false);

    public static final Codec<BlacksmithTrade> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Entry.CODEC.listOf().fieldOf("entries").forGetter(BlacksmithTrade::entries),
            Codec.INT.fieldOf("weight").forGetter(BlacksmithTrade::weight),
            Codec.BOOL.fieldOf("has_upgrades").forGetter(BlacksmithTrade::hasUpgrades)
    ).apply(instance, BlacksmithTrade::new));

    public Entry getNext(Rarity rarity)
    {
        return entries.stream().filter(o -> o.rarity == rarity.next()).findAny().orElse(null);
    }

    public record Entry(Rarity rarity, MaybeStack item, List<MaybeStack> cost)
    {
        public static final Entry EMPTY = new Entry(Rarity.COMMON, MaybeStack.EMPTY, List.of());

        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Rarity.CODEC.fieldOf("rarity").forGetter(Entry::rarity),
                MaybeStack.CODEC.fieldOf("item").forGetter(Entry::item),
                MaybeStack.CODEC.listOf().fieldOf("cost").forGetter(Entry::cost)
        ).apply(instance, Entry::new));
    }
}
