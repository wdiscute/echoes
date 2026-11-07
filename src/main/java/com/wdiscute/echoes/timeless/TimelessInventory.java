package com.wdiscute.echoes.timeless;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.registry.ECDataAttachments;
import com.wdiscute.echoes.swappers.Swapper;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.Utils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public record TimelessInventory(Map<ResourceLocation, MaybeStack> inventory)
{
    public TimelessInventory(List<Utils.Duo<ResourceLocation, MaybeStack>> entries)
    {
        this(entries.stream().collect(Collectors.toMap(Utils.Duo::first, Utils.Duo::second)));
    }

    public void swap(Player player)
    {
        Map<ResourceLocation, MaybeStack> inventoryToStore = new ConcurrentHashMap<>();

        for (Swapper swapper : Echoes.SWAPPER_REGISTRY.stream().toList())
            swapper.handle(player, inventoryToStore, Map.copyOf(inventory));

        player.setData(ECDataAttachments.TIMELESS_INVENTORY, new TimelessInventory(Map.copyOf(inventoryToStore)));
    }

    public List<Utils.Duo<ResourceLocation, MaybeStack>> asDuo()
    {
        List<Utils.Duo<ResourceLocation, MaybeStack>> list = new ArrayList<>();
        for (Map.Entry<ResourceLocation, MaybeStack> entry : inventory.entrySet())
            list.add(new Utils.Duo<>(entry.getKey(), entry.getValue()));
        return list;
    }

    public static final Codec<TimelessInventory> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Utils.Duo.codec(ResourceLocation.CODEC, MaybeStack.CODEC).listOf()
                            .fieldOf("storage").forGetter(TimelessInventory::asDuo)
            ).apply(instance, TimelessInventory::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, TimelessInventory> STREAM_CODEC = StreamCodec.composite(
            Utils.Duo.streamCodec(ResourceLocation.STREAM_CODEC, MaybeStack.STREAM_CODEC).apply(ByteBufCodecs.list()),
            TimelessInventory::asDuo,
            TimelessInventory::new
    );
}
