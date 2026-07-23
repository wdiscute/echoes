package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public interface ECDataAttachments
{
    DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(
            NeoForgeRegistries.ATTACHMENT_TYPES, Echoes.MOD_ID);

    Supplier<AttachmentType<TimelessData>> TIMELESS_STATS = ATTACHMENT_TYPES.register(
            "timeless_stats", () -> AttachmentType.builder(() -> TimelessData.EMPTY)
                    .sync(TimelessData.STREAM_CODEC)
                    .serialize(TimelessData.CODEC.fieldOf("stats"))
                    .copyOnDeath()
                    .build()
    );

    static void register(IEventBus eventBus)
    {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
