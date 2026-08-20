package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.entity.corpse.TimelessCorpse;
import com.wdiscute.echoes.entity.enemy.hollowed.HollowedEntity;
import com.wdiscute.echoes.entity.enemy.sculked.SculkedEntity;
import com.wdiscute.echoes.entity.heart.SculkHeartEntity;
import com.wdiscute.echoes.entity.lantern.LanternEntity;
import com.wdiscute.echoes.entity.soul.SoulEntity;
import com.wdiscute.echoes.entity.trader.SoulTraderEntity;
import com.wdiscute.echoes.entity.unleashedsoul.UnleashedSoulEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.skeleton.Stray;
import net.minecraft.world.level.block.Blocks;
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

    DeferredHolder<EntityType<?>, EntityType<SoulTraderEntity>> SOUL_TRADER =
            register("soul_trader", SoulTraderEntity::new, MobCategory.MISC,
                    b -> b.sized(1f, 2f));

    DeferredHolder<EntityType<?>, EntityType<SculkHeartEntity>> SCULK_HEART =
            register("sculk_heart", SculkHeartEntity::new, MobCategory.MONSTER,
                    b -> b.sized(3f, 3f));

    DeferredHolder<EntityType<?>, EntityType<TimelessCorpse>> TIMELESS_CORPSE =
            register("timeless_corpse", TimelessCorpse::new, MobCategory.MISC,
                    b -> b.sized(2f, 2f));

    DeferredHolder<EntityType<?>, EntityType<SoulEntity>> SOUL =
            register("soul", SoulEntity::new, MobCategory.MISC,
                    b -> b.sized(0.1f, 0.1f).noSave());

    DeferredHolder<EntityType<?>, EntityType<UnleashedSoulEntity>> UNLEASHED_SOUL =
            register("unleashed_soul", UnleashedSoulEntity::new, MobCategory.MISC,
                    b -> b.sized(0.5f, 0.5f).noSave().noSummon());


    //
    //                                    ,--.
    // ,---.  ,--,--,   ,---.  ,--,--,--. `--'  ,---.   ,---.
    //| .-. : |      \ | .-. : |        | ,--. | .-. : (  .-'
    //\   --. |  ||  | \   --. |  |  |  | |  | \   --. .-'  `)
    // `----' `--''--'  `----' `--`--`--' `--'  `----' `----'
    //


    DeferredHolder<EntityType<?>, EntityType<SculkedEntity>> SCULKED =
            register("sculked", SculkedEntity::new, MobCategory.MONSTER,
                    b -> b
                            .sized(0.6F, 1.95F)
                            .eyeHeight(1.74F)
                            .passengerAttachments(2.0125F)
                            .ridingOffset(-0.7F)
                            .clientTrackingRange(8)
                            .notInPeaceful()
            );

    DeferredHolder<EntityType<?>, EntityType<HollowedEntity>> HOLLOWED =
            register("hollowed", HollowedEntity::new, MobCategory.MONSTER,
                    b -> b
                            .sized(0.6F, 1.99F)
                            .eyeHeight(1.74F)
                            .ridingOffset(-0.7F)
                            .clientTrackingRange(8)
                            .notInPeaceful()
            );


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
