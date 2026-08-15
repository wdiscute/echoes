package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.entity.enemy.sculked.SculkedEntity;
import com.wdiscute.echoes.entity.soul.SoulEntity;
import com.wdiscute.echoes.entity.unleashedsoul.UnleashedSoulEntity;
import com.wdiscute.echoes.timeless.TimelessHearts;
import com.wdiscute.echoes.timeless.TimelessManager;
import com.wdiscute.echoes.timeless.TimelessInstance;
import com.wdiscute.echoes.entity.heart.SculkHeartEntity;
import com.wdiscute.echoes.network.ECDBPlaySoundPayload;
import com.wdiscute.echoes.upgrades.BlacksmithTrade;
import com.wdiscute.echoes.upgrades.Perk;
import com.wdiscute.echoes.upgrades.PerkInstance;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

import java.util.List;

@EventBusSubscriber(modid = Echoes.MOD_ID)
public class ECEvents
{
    @SubscribeEvent
    public static void timelessTick(LevelTickEvent.Post event)
    {
        if (event.getLevel().isClientSide()) return;

        ServerLevel sl = (ServerLevel) event.getLevel();

        if (sl.dimension().equals(Echoes.TIMELESS))
            TimelessManager.getSavedData(sl.getServer()).tick(sl);
    }

    @SubscribeEvent
    public static void timelessTick(EntityJoinLevelEvent event)
    {
        if (event.getLevel().dimension().equals(Echoes.TIMELESS))
        {
            if (event.getEntity() instanceof ExperienceOrb)
                event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Pre event)
    {
        if (!event.getEntity().level().dimension().equals(Echoes.TIMELESS)) return;

        LivingEntity entityDamaged = event.getEntity();
        Entity damager = event.getSource().getEntity();

        if(entityDamaged instanceof Player player)
        {
            //decrease damage based on soul hearts
            event.setNewDamage(event.getNewDamage() - TimelessHearts.getAbsorbedDamage(player, (int) event.getNewDamage()));;
        }

        //trigger perks
        if (damager instanceof Player player)
        {
            ItemStack weaponItem = event.getSource().getWeaponItem() == null ? ItemStack.EMPTY : event.getSource().getWeaponItem();

            List<PerkInstance> perks = Perk.getPerks(player, weaponItem);

            for (PerkInstance perk : perks)
            {
                float damageToAdd = perk.perk().value().addDamage(player, weaponItem, entityDamaged, perk.amplifiers());
                event.setNewDamage(event.getNewDamage() + damageToAdd * player.getAttackStrengthScale(0));
            }
        }
    }



    @SubscribeEvent
    public static void onDeathEvent(LivingDeathEvent event)
    {
        if (event.getEntity().level().isClientSide()) return;

        LivingEntity entityKilled = event.getEntity();
        Entity killer = event.getSource().getEntity();
        ServerLevel sl = ((ServerLevel) entityKilled.level());

        //if not in timeless return
        if (!entityKilled.level().dimension().equals(Echoes.TIMELESS)) return;

        //if player died
        if (entityKilled instanceof Player)
        {
            entityKilled.setHealth(5);
            entityKilled.fallDistance = 0;
            event.setCanceled(true);

            //if server side
            if (entityKilled instanceof ServerPlayer sp)
            {
                //get closest timelessInstance
                TimelessInstance closest = TimelessManager.getClosest(sp.level().getServer(), sp.blockPosition());

                //remove player
                if (closest != null) closest.removePlayer(sp);
            }
            return;
        }

        float souls = ECDataEntries.SOULS.get().getOrDefault(BuiltInRegistries.ENTITY_TYPE.getKey(entityKilled.getType()), 0f);
        Player player = sl.getNearestPlayer(entityKilled, 1000);
        ItemStack weapon = ItemStack.EMPTY;
        List<PerkInstance> perks;

        //if player that killed can be obtained from event
        if (killer instanceof Player playerKiller)
        {
            player = playerKiller;
            weapon = event.getSource().getWeaponItem() == null ? ItemStack.EMPTY : event.getSource().getWeaponItem();
        }

        //get perks to trigger
        if (player == null)
            perks = List.of();
        else
            perks = Perk.getPerks(player, weapon);

        //add souls from perks
        for (PerkInstance perk : perks)
            souls += perk.perk().value().addSouls(player, weapon, entityKilled, perk.amplifiers(), souls);

        //trigger perks
        for (PerkInstance perk : perks)
            perk.perk().value().onEntityKilled(player, weapon, entityKilled, perk.amplifiers());

        //spawn souls
        if (player != null)
        {
            float chance = souls % 1;
            //spawn extra soul based on chance
            // e.g.: if souls count is 1.7
            //spawns 1 soul, with a 70% chance of spawning a second one
            if (sl.getRandom().nextFloat() < chance)
            {
                SoulEntity soul = ECEntities.SOUL.get().create(sl, EntitySpawnReason.TRIGGERED);
                Vec3 pos = entityKilled.getEyePosition();
                soul.snapTo(pos.x, pos.y, pos.z);
                soul.getEntityData().set(SoulEntity.UUID, player.getUUID());
                sl.addFreshEntityWithPassengers(soul);
            }

            if (souls >= 1)
            {
                SoulEntity soul = ECEntities.SOUL.get().create(sl, EntitySpawnReason.TRIGGERED);
                soul.getEntityData().set(SoulEntity.UUID, player.getUUID());
                soul.extraSoulsToSpawn = (int) (Math.floor(souls));
                Vec3 pos = entityKilled.getEyePosition();
                soul.snapTo(pos.x, pos.y, pos.z);
                sl.addFreshEntityWithPassengers(soul);
            }
        }
    }

    @SubscribeEvent
    public static void registerAttributed(EntityAttributeCreationEvent event)
    {
        event.put(ECEntities.SCULK_HEART.get(), SculkHeartEntity.createAttributes().build());
        event.put(ECEntities.SCULKED.get(), SculkedEntity.createAttributes().build());
        event.put(ECEntities.HOLLOWED.get(), AbstractSkeleton.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerAttributed(RegisterPayloadHandlersEvent event)
    {
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(
                ECDBPlaySoundPayload.TYPE,
                ECDBPlaySoundPayload.STREAM_CODEC,
                ECDBPlaySoundPayload::handle
        );
    }

    @SubscribeEvent
    public static void addDatapackRegistry(DataPackRegistryEvent.NewRegistry event)
    {
        event.dataPackRegistry(
                Echoes.BLACKSMITH_TRADE_KEY, BlacksmithTrade.CODEC, BlacksmithTrade.CODEC,
                builder -> builder.maxId(1024));
    }

    @SubscribeEvent
    public static void addRegistry(NewRegistryEvent event)
    {
        event.register(Echoes.PERK_REGISTRY);
    }
}
