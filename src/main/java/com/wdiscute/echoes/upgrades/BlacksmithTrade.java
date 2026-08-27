package com.wdiscute.echoes.upgrades;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.Rarity;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.server.level.ServerLevel;

import java.util.List;

public record BlacksmithTrade(MaybeStack stack, Rarity rarity, List<MaybeStack> cost, int weight)
{

    public static BlacksmithTrade getRandomTrade(ServerLevel sl)
    {
        List<BlacksmithTrade> trades = sl.registryAccess().lookupOrThrow(Echoes.BLACKSMITH_TRADE_KEY).stream().toList();

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
                return trade;
        }

        throw new IllegalStateException("New Advancement Obtained: How did we get here?");
    }

    public static final BlacksmithTrade EMPTY = new BlacksmithTrade(MaybeStack.EMPTY, Rarity.COMMON, List.of(), 0);

    public static final Codec<BlacksmithTrade> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MaybeStack.CODEC.fieldOf("stack").forGetter(BlacksmithTrade::stack),
            Rarity.CODEC.fieldOf("rarity").forGetter(BlacksmithTrade::rarity),
            MaybeStack.CODEC.listOf().fieldOf("cost").forGetter(BlacksmithTrade::cost),
            //Codec.INT.fieldOf("power_level").forGetter(BlacksmithTrade::powerLevel),
            Codec.INT.fieldOf("weight").forGetter(BlacksmithTrade::weight)
    ).apply(instance, BlacksmithTrade::new));
}
