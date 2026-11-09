package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.entity.misc.aoe.AOEEntity;
import com.wdiscute.echoes.entity.boss.sculkedtrader.SculkedTraderEntity;
import com.wdiscute.echoes.entity.misc.corpse.TimelessCorpseEntity;
import com.wdiscute.echoes.entity.enemy.hollowed.HollowedEntity;
import com.wdiscute.echoes.entity.enemy.sculked.SculkedEntity;
import com.wdiscute.echoes.entity.misc.heart.SculkHeartEntity;
import com.wdiscute.echoes.entity.misc.lantern.LanternEntity;
import com.wdiscute.echoes.entity.misc.soul.SoulEntity;
import com.wdiscute.echoes.entity.misc.specter.SpecterEntity;
import com.wdiscute.echoes.entity.misc.totem.TotemEntity;
import com.wdiscute.echoes.entity.npc.trader.SoulTraderEntity;
import com.wdiscute.echoes.entity.misc.unleashedsoul.UnleashedSoulEntity;
import net.minecraft.core.registries.BuiltInRegistries;
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
                    b -> b.sized(0.01f, 0.01f));

    DeferredHolder<EntityType<?>, EntityType<SoulTraderEntity>> SOUL_TRADER =
            register("soul_trader", SoulTraderEntity::new, MobCategory.MISC,
                    b -> b.sized(1f, 2f));

    DeferredHolder<EntityType<?>, EntityType<SpecterEntity>> SPECTER =
            register("soul_spectator", SpecterEntity::new, MobCategory.MISC,
                    b -> b.sized(1f, 2f));

    DeferredHolder<EntityType<?>, EntityType<SculkHeartEntity>> SCULK_HEART =
            register("sculk_heart", SculkHeartEntity::new, MobCategory.MONSTER,
                    b -> b.sized(3f, 3f));

    DeferredHolder<EntityType<?>, EntityType<TimelessCorpseEntity>> TIMELESS_CORPSE =
            register("timeless_corpse", TimelessCorpseEntity::new, MobCategory.MISC,
                    b -> b.sized(2f, 2f));

    DeferredHolder<EntityType<?>, EntityType<SoulEntity>> SOUL =
            register("soul", SoulEntity::new, MobCategory.MISC,
                    b -> b.sized(0.1f, 0.1f).noSave());

    DeferredHolder<EntityType<?>, EntityType<UnleashedSoulEntity>> UNLEASHED_SOUL =
            register("unleashed_soul", UnleashedSoulEntity::new, MobCategory.MISC,
                    b -> b.sized(0.5f, 0.5f).noSave().noSummon());

    DeferredHolder<EntityType<?>, EntityType<AOEEntity>> AOE =
            register("aoe", AOEEntity::new, MobCategory.MISC,
                    b -> b.sized(0.1f, 0.1f).noSave());

    DeferredHolder<EntityType<?>, EntityType<TotemEntity>> TOTEM =
            register("totem", TotemEntity::new, MobCategory.MISC,
                    b -> b.sized(0.5f, 0.5f).noSummon());


    //
    //,--.
    //|  |-.   ,---.   ,---.   ,---.   ,---.   ,---.
    //| .-. ' | .-. | (  .-'  (  .-'  | .-. : (  .-'
    //| `-' | ' '-' ' .-'  `) .-'  `) \   --. .-'  `)
    // `---'   `---'  `----'  `----'   `----' `----'
    //

    DeferredHolder<EntityType<?>, EntityType<SculkedTraderEntity>> SCULKED_TRADER =
            register("sculked_trader", SculkedTraderEntity::new, MobCategory.MONSTER,
                    b -> b
                            .sized(0.8F, 1.2F)
                            .eyeHeight(0.75F)
                            .clientTrackingRange(8)
            );

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
            );

    DeferredHolder<EntityType<?>, EntityType<HollowedEntity>> HOLLOWED =
            register("hollowed", HollowedEntity::new, MobCategory.MONSTER,
                    b -> b
                            .sized(0.6F, 1.99F)
                            .eyeHeight(1.74F)
                            .ridingOffset(-0.7F)
                            .clientTrackingRange(8)
            );


    static void register(IEventBus eventBus)
    {
        ENTITY_TYPES.register(eventBus);
    }

    static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String name, EntityType.EntityFactory<T> factory,
                                                                                    MobCategory category, UnaryOperator<EntityType.Builder<T>> provider)
    {
        return ENTITY_TYPES.register(name, () -> provider.apply(EntityType.Builder.of(factory, category)).build(name));
    }

}
