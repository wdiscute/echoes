package com.wdiscute.echoes.timeless;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.echoes.registry.ECDataAttachments;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public record TimelessData(long timeToExit,
                           int currentStage,
                           int maxStage,
                           List<MaybeStack> inventory,
                           float souls,
                           float maxSouls
)
{
    public static final TimelessData EMPTY = new TimelessData(Long.MAX_VALUE, -1, -1, List.of(), 0, 100);

    public static final Codec<TimelessData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.LONG.fieldOf("time_to_exit").forGetter(t -> t.timeToExit),
                    Codec.INT.fieldOf("current_stage").forGetter(t -> t.currentStage),
                    Codec.INT.fieldOf("max_stage").forGetter(t -> t.maxStage),
                    MaybeStack.CODEC.listOf().fieldOf("inventory").forGetter(t -> t.inventory),
                    Codec.FLOAT.fieldOf("souls").forGetter(t -> t.souls),
                    Codec.FLOAT.fieldOf("max_souls").forGetter(t -> t.maxSouls)
            ).apply(instance, TimelessData::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, TimelessData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.LONG, data -> data.timeToExit,
            ByteBufCodecs.INT, data -> data.currentStage,
            ByteBufCodecs.INT, data -> data.maxStage,
            MaybeStack.STREAM_CODEC.apply(ByteBufCodecs.list()), data -> data.inventory,
            ByteBufCodecs.FLOAT, data -> data.souls,
            ByteBufCodecs.FLOAT, data -> data.maxSouls,
            TimelessData::new
    );

    public static TimelessData get(Player player)
    {
        return player.getData(ECDataAttachments.TIMELESS_DATA);
    }

    private TimelessData withTimeToExit(long timeToExit)
    {
        return new TimelessData(timeToExit, currentStage, maxStage, inventory, souls, maxSouls);
    }

    private TimelessData withInventory(List<MaybeStack> inventory)
    {
        return new TimelessData(timeToExit, currentStage, maxStage, inventory, souls, maxSouls);
    }

    private TimelessData withCurrentStage(int currentStage)
    {
        return new TimelessData(timeToExit, currentStage, maxStage, inventory, souls, maxSouls);
    }

    private TimelessData withMaxStage(int maxStage)
    {
        return new TimelessData(timeToExit, currentStage, maxStage, inventory, souls, maxSouls);
    }

    private TimelessData withSouls(float souls)
    {
        return new TimelessData(timeToExit, currentStage, maxStage, inventory, souls, maxSouls);
    }

    private TimelessData withMaxSouls(int maxSouls)
    {
        return new TimelessData(timeToExit, currentStage, maxStage, inventory, souls, maxSouls);
    }

    //setters helpers
    //time to exit
    public static void setTimeToExit(Player player, long timeToExit)
    {
        TimelessData data = player.getData(ECDataAttachments.TIMELESS_DATA);
        player.setData(ECDataAttachments.TIMELESS_DATA, data.withTimeToExit(timeToExit));
    }

    //currentStage
    public static void setCurrentStage(Player player, int currentStage)
    {
        TimelessData data = player.getData(ECDataAttachments.TIMELESS_DATA);
        player.setData(ECDataAttachments.TIMELESS_DATA, data.withCurrentStage(currentStage));
    }

    //max stage
    public static void setMaxStage(Player player, int currentStage)
    {
        //do not set if tutorial level
        if (currentStage == -1) return;

        //set to 1 if died on hub (lol)
        if (currentStage == 0)
            currentStage = 1;

        TimelessData data = player.getData(ECDataAttachments.TIMELESS_DATA);
        if (currentStage > data.maxStage())
            player.setData(ECDataAttachments.TIMELESS_DATA, data.withMaxStage(currentStage));
    }

    //inventory
    public static void setInventory(Player player, List<MaybeStack> inventory)
    {
        TimelessData data = player.getData(ECDataAttachments.TIMELESS_DATA);
        player.setData(ECDataAttachments.TIMELESS_DATA, data.withInventory(inventory));
    }

    //
    //                         ,--.
    // ,---.   ,---.  ,--.,--. |  |  ,---.
    //(  .-'  | .-. | |  ||  | |  | (  .-'
    //.-'  `) ' '-' ' '  ''  ' |  | .-'  `)
    //`----'   `---'   `----'  `--' `----'
    //

    //returns if souls were consume. souls are only consumed if player has enough
    public static boolean consumeSouls(Player player, float souls)
    {
        TimelessData data = player.getData(ECDataAttachments.TIMELESS_DATA);

        float newSouls = data.souls - souls;
        if (newSouls >= 0)
        {
            player.setData(ECDataAttachments.TIMELESS_DATA, data.withSouls(newSouls));
            return true;
        }

        return false;
    }

    public static void clearSouls(Player player)
    {
        TimelessData data = player.getData(ECDataAttachments.TIMELESS_DATA);
        player.setData(ECDataAttachments.TIMELESS_DATA, data.withSouls(0));
    }

    public static void awardSoul(Player player, float souls)
    {
        TimelessData data = player.getData(ECDataAttachments.TIMELESS_DATA);
        player.setData(ECDataAttachments.TIMELESS_DATA, data.withSouls(Math.min(data.souls + souls, data.maxSouls)));
    }

    public static boolean increaseMaxSouls(Player player, float souls)
    {
        TimelessData data = player.getData(ECDataAttachments.TIMELESS_DATA);

        return data.souls >= souls;
    }


}
