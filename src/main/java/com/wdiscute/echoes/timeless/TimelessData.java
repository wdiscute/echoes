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

public record TimelessData(long timeToExit, int currentStage, int maxStage, List<MaybeStack> inventory)
{
    public static final TimelessData EMPTY = new TimelessData(Long.MAX_VALUE, -1, -1, List.of());

    public static final Codec<TimelessData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.LONG.fieldOf("time_to_exit").forGetter(t -> t.timeToExit),
                    Codec.INT.fieldOf("current_stage").forGetter(t -> t.currentStage),
                    Codec.INT.fieldOf("max_stage").forGetter(t -> t.maxStage),
                    MaybeStack.CODEC.listOf().fieldOf("inventory").forGetter(t -> t.inventory)
            ).apply(instance, TimelessData::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, TimelessData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.LONG, data -> data.timeToExit,
            ByteBufCodecs.INT, data -> data.currentStage,
            ByteBufCodecs.INT, data -> data.maxStage,
            MaybeStack.STREAM_CODEC.apply(ByteBufCodecs.list()), data -> data.inventory,
            TimelessData::new
    );

    private TimelessData withTimeToExit(long timeToExit)
    {
        return new TimelessData(timeToExit, currentStage, maxStage, inventory);
    }

    private TimelessData withInventory(List<MaybeStack> inventory)
    {
        return new TimelessData(timeToExit, currentStage, maxStage, inventory);
    }

    private TimelessData withCurrentStage(int currentStage)
    {
        return new TimelessData(timeToExit, currentStage, maxStage, inventory);
    }

    private TimelessData withMaxStage(int maxStage)
    {
        return new TimelessData(timeToExit, currentStage, maxStage, inventory);
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

    //currentStage
    public static void attemptToSetMaxStage(Player player, int currentStage)
    {
        //do not set if tutorial level
        if(currentStage == -1) return;

        TimelessData data = player.getData(ECDataAttachments.TIMELESS_DATA);
        if (currentStage / 5 * 5 > data.maxStage)
            player.setData(ECDataAttachments.TIMELESS_DATA, data.withMaxStage(currentStage / 5 * 5));
    }

    //inventory
    public static void setInventory(Player player, List<MaybeStack> inventory)
    {
        TimelessData data = player.getData(ECDataAttachments.TIMELESS_DATA);
        player.setData(ECDataAttachments.TIMELESS_DATA, data.withInventory(inventory));
    }
}
