package com.wdiscute.echoes.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.echoes.registry.ECDataComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record IsPrismaItemProperty(boolean isPrisma) implements ConditionalItemModelProperty
{
    public static final MapCodec<IsPrismaItemProperty> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Codec.BOOL.optionalFieldOf("is_prisma", false).forGetter(IsPrismaItemProperty::isPrisma)
                    )
                    .apply(instance, IsPrismaItemProperty::new)
    );

    @Override
    public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext)
    {
        return stack.getOrDefault(ECDataComponents.IS_PRISMA_BLADE, false);
    }

    @Override
    public MapCodec<IsPrismaItemProperty> type()
    {
        return MAP_CODEC;
    }
}
