package com.wdiscute.echoes.perk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.echoes.Echoes;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.HashMap;
import java.util.Map;

public record PerkInstance(Perk perk, Map<String, Float> amplifiers)
{
    public PerkInstance(Holder<Perk> perk)
    {
        this(perk.value(), Map.of());
    }

    public PerkInstance(Holder<Perk> perk, Map<String, Float> amplifiers)
    {
        this(perk.value(), amplifiers);
    }

    public PerkInstance(Holder<Perk> perk, String id, float value)
    {
        this(perk.value(), Map.of(id, value));
    }

    public String format(PerkInstance instance, String id)
    {
        return "[" + Echoes.FORMAT.format(instance.get(id)) + "]";
    }

    public float get(String id)
    {
        return amplifiers.getOrDefault(id, 0f);
    }

    public Map<String, Float> getAsMap(String id)
    {
        return new HashMap<>(){{put(id, amplifiers.getOrDefault(id, 0f));}};
    }

    public float getCost()
    {
        return amplifiers.getOrDefault(Perk.COST, 0f);
    }

    public float getDuration()
    {
        return amplifiers.getOrDefault(Perk.DURATION, 0f);
    }

    public float getDamage()
    {
        return amplifiers.getOrDefault(Perk.DAMAGE,0f);
    }

    public float getValue()
    {
        return amplifiers.getOrDefault(Perk.VALUE,0f);
    }

    public float getCooldown()
    {
        return amplifiers.getOrDefault(Perk.COOLDOWN,0f);
    }
    public float getHeal()
    {
        return amplifiers.getOrDefault(Perk.HEAL,0f);
    }
    public float getRange()
    {
        return amplifiers.getOrDefault(Perk.RANGE,0f);
    }

    public static final Codec<PerkInstance> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Echoes.PERK_REGISTRY.byNameCodec().fieldOf("perk").forGetter(PerkInstance::perk),
                    Codec.unboundedMap(Codec.STRING, Codec.FLOAT).fieldOf("amplifiers").forGetter(PerkInstance::amplifiers)
            ).apply(instance, PerkInstance::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Perk> PERK_STREAM_CODEC =
            ByteBufCodecs.registry(Echoes.PERK);

    public static final StreamCodec<RegistryFriendlyByteBuf, PerkInstance> STREAM_CODEC = StreamCodec.composite(
            PERK_STREAM_CODEC, PerkInstance::perk,
            ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, ByteBufCodecs.FLOAT), PerkInstance::amplifiers,
            PerkInstance::new
    );
}