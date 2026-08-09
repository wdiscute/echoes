package com.wdiscute.echoes.registry;

import com.mojang.serialization.Codec;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.timeless.TimelessData;
import com.wdiscute.echoes.timeless.TimelessHearts;
import com.wdiscute.echoes.upgrades.PerkInstance;
import net.minecraft.network.codec.ByteBufCodecs;
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
            "timeless_data", () -> AttachmentType.builder(() -> TimelessData.EMPTY)
                    .sync(TimelessData.STREAM_CODEC)
                    .serialize(TimelessData.CODEC.fieldOf("data"))
                    .copyOnDeath()
                    .build()
    );

    Supplier<AttachmentType<TimelessHearts>> TIMELESS_HEARTS = ATTACHMENT_TYPES.register(
            "timeless_hearts", () -> AttachmentType.builder(() -> TimelessHearts.EMPTY)
                    .sync(TimelessHearts.STREAM_CODEC)
                    .serialize(TimelessHearts.CODEC.fieldOf("hearts"))
                    .copyOnDeath()
                    .build()
    );

    //used for checking if an entity can pick up a lantern of if it already has one
    Supplier<AttachmentType<Boolean>> HAS_LANTERN = ATTACHMENT_TYPES.register(
            "has_lantern", () -> AttachmentType.builder(() -> false)
                    .build()
    );

    Supplier<AttachmentType<List<PerkInstance>>> PERKS = ATTACHMENT_TYPES.register(
            "perks", () -> AttachmentType.builder(() -> List.<PerkInstance>of())
                    .serialize(PerkInstance.CODEC.listOf().fieldOf("perks"))
                    .sync(PerkInstance.STREAM_CODEC.apply(ByteBufCodecs.list()))
                    .build()
    );

    static void register(IEventBus eventBus)
    {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
