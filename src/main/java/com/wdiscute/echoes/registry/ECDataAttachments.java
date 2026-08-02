package com.wdiscute.echoes.registry;

import com.mojang.serialization.Codec;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.timeless.TimelessData;
import com.wdiscute.utils.Utils;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.function.Supplier;

public interface ECDataAttachments
{
    DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(
            NeoForgeRegistries.ATTACHMENT_TYPES, Echoes.MOD_ID);

    Supplier<AttachmentType<TimelessData>> TIMELESS_DATA = ATTACHMENT_TYPES.register(
            "timeless_stats", () -> AttachmentType.builder(() -> TimelessData.EMPTY)
                    .sync(TimelessData.STREAM_CODEC)
                    .serialize(TimelessData.CODEC.fieldOf("stats"))
                    .copyOnDeath()
                    .build()
    );

    Supplier<AttachmentType<Boolean>> HAS_LANTERN = ATTACHMENT_TYPES.register(
            "has_lantern", () -> AttachmentType.builder(() -> false)
                    .serialize(Codec.BOOL.fieldOf("has_lantern"))
                    .build()
    );

    Supplier<AttachmentType<List<Utils.Duo<Identifier, Float>>>> PERKS = ATTACHMENT_TYPES.register(
            "perks", () -> AttachmentType.builder(() -> List.<Utils.Duo<Identifier, Float>>of())
                    .serialize(Utils.Duo.codec(Identifier.CODEC, Codec.FLOAT).listOf().fieldOf("perks"))
                    .sync(Utils.Duo.streamCodec(Identifier.STREAM_CODEC, ByteBufCodecs.FLOAT).apply(ByteBufCodecs.list()))
                    .build()
    );

    static void register(IEventBus eventBus)
    {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
