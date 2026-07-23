package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.registry.entity.lantern.LanternEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public interface ECEntities
{
    DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Echoes.MOD_ID);

    DeferredHolder<EntityType<?>, EntityType<LanternEntity>> LANTERN =
            register("lantern", LanternEntity::new, MobCategory.MISC,
                    b -> b.sized(1f, 1f));


    static void register(IEventBus eventBus)
    {
        ENTITY_TYPES.register(eventBus);
    }

    static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String name, EntityType.EntityFactory<T> factory,
                                                                                    MobCategory category, UnaryOperator<EntityType.Builder<T>> provider)
    {
        return ENTITY_TYPES.register(name, () -> provider.apply(EntityType.Builder.of(factory, category))
                .build(ResourceKey.create(Registries.ENTITY_TYPE, Echoes.rl(name))));
    }

}
