package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.entity.corpse.TimelessCorpse;
import com.wdiscute.echoes.timeless.TimelessHandler;
import com.wdiscute.echoes.timeless.TimelessInstance;
import com.wdiscute.echoes.entity.heart.SculkHeartEntity;
import com.wdiscute.echoes.network.ECDBPlaySoundPayload;
import com.wdiscute.echoes.upgrades.BlacksmithTrade;
import com.wdiscute.utils.Utils;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

import java.util.List;

@EventBusSubscriber(modid = Echoes.MOD_ID)
public class ECEvents
{
    @SubscribeEvent
    public static void addDatapackRegistry(DataPackRegistryEvent.NewRegistry event)
    {
        event.dataPackRegistry(
                Echoes.BLACKSMITH_TRADE_KEY, BlacksmithTrade.CODEC, BlacksmithTrade.CODEC,
                builder -> builder.maxId(1024));
    }

    @SubscribeEvent
    public static void timelessTick(LevelTickEvent.Post event)
    {
        if (event.getLevel().isClientSide()) return;

        ServerLevel sl = (ServerLevel) event.getLevel();

        if (sl.dimension().equals(Echoes.TIMELESS))
            TimelessHandler.getSavedData(sl.getServer()).tick(sl);
    }


    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Pre event)
    {
        if (!event.getEntity().level().dimension().equals(Echoes.TIMELESS)) return;

        LivingEntity entityDamaged = event.getEntity();
        Entity damager = event.getSource().getEntity();

        //trigger items perks
        if (damager instanceof Player player)
        {
            for (ItemStack stack : player.getInventory())
            {
                for (var perk : stack.getOrDefault(ECDataComponents.PERKS, List.<Utils.Duo<Identifier, Float>>of()))
                {
                    float damageToAdd = ECPerks.get(perk.first()).addDamage(player, event.getSource().getWeaponItem(), entityDamaged, perk.second());

                    event.setNewDamage(event.getNewDamage() + damageToAdd);
                }
            }
        }

        //trigger player perks
        if (damager instanceof Player player)
        {
            List<Utils.Duo<Identifier, Float>> data = player.getData(ECDataAttachments.PERKS);

            //trigger perks
            for (Utils.Duo<Identifier, Float> perk : data)
            {
                float damageToAdd = ECPerks.get(perk.first()).addDamage(player, event.getSource().getWeaponItem(), entityDamaged, perk.second());

                event.setNewDamage(event.getNewDamage() + damageToAdd);
            }
        }
    }

    @SubscribeEvent
    public static void onDeathEvent(LivingDeathEvent event)
    {
        LivingEntity entityKilled = event.getEntity();
        Entity killer = event.getSource().getEntity();

        //if not in timeless return
        if (!entityKilled.level().dimension().equals(Echoes.TIMELESS)) return;

        //if player
        if (entityKilled instanceof Player)
        {
            entityKilled.setHealth(5);
            entityKilled.fallDistance = 0;
            event.setCanceled(true);

            //if server side
            if (entityKilled instanceof ServerPlayer sp)
            {
                //get closest timelessInstance
                TimelessInstance closest = TimelessHandler.getClosest(sp.level().getServer(), sp.blockPosition());

                //remove player
                if (closest != null) closest.removePlayer(sp);
            }
            return;
        }

        if (killer instanceof Player player)
        {
            List<Utils.Duo<Identifier, Float>> data = player.getData(ECDataAttachments.PERKS);

            //trigger perks
            for (Utils.Duo<Identifier, Float> perk : data)
                ECPerks.get(perk.first()).onEntityKilled(player, event.getSource().getWeaponItem(), entityKilled, perk.second());
        }

    }

    @SubscribeEvent
    public static void registerAttributed(EntityAttributeCreationEvent event)
    {
        event.put(ECEntities.SCULK_HEART.get(), SculkHeartEntity.createAttributes().build());
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
}
