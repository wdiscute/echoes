package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.utils.MaybeStack;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.UUID;

public interface ECEntityDataSerializers
{
    DeferredRegister<EntityDataSerializer<?>> SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, Echoes.MOD_ID);

    StreamCodec<ByteBuf, UUID> NULLABLE_UUID_CODEC =
            new StreamCodec<>()
            {
                @Override
                public UUID decode(ByteBuf buf)
                {
                    return buf.readBoolean()
                            ? UUIDUtil.STREAM_CODEC.decode(buf)
                            : null;
                }

                @Override
                public void encode(ByteBuf buf, UUID value)
                {
                    buf.writeBoolean(value != null);
                    if (value != null)
                    {
                        UUIDUtil.STREAM_CODEC.encode(buf, value);
                    }
                }
            };

    EntityDataSerializer<UUID> UUID = EntityDataSerializer.forValueType(NULLABLE_UUID_CODEC);

    DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<UUID>> UUID_HOLDER =
            SERIALIZERS.register("uuid", () -> UUID);


    EntityDataSerializer<MaybeStack> STACK = EntityDataSerializer.forValueType(MaybeStack.STREAM_CODEC);

    DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<MaybeStack>> STACK_HOLDER =
            SERIALIZERS.register("stack", () -> STACK);

    static void register(IEventBus eventBus)
    {
        SERIALIZERS.register(eventBus);
    }
}
