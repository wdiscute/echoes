package com.wdiscute.echoes.timeless;

import com.wdiscute.echoes.ECTags;
import com.wdiscute.echoes.blocks.display.DisplayBlock;
import com.wdiscute.echoes.blocks.display.DisplayBlockEntity;
import com.wdiscute.echoes.blocks.marker.TimelessMarkerBlock;
import com.wdiscute.echoes.blocks.portal.PortalBlock;
import com.wdiscute.echoes.entity.corpse.TimelessCorpse;
import com.wdiscute.echoes.entity.heart.SculkHeartEntity;
import com.wdiscute.echoes.registry.ECBlocks;
import com.wdiscute.echoes.registry.ECDataEntries;
import com.wdiscute.echoes.registry.ECEntities;
import com.wdiscute.echoes.upgrades.BlacksmithTrade;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;


public class TimelessProcessor
{
    public static final List<Processor> PROCESSORS = new ArrayList<>();

    public static void addDefaultProcessors()
    {
        //timeless marker processor
        add((instance, sl, state, bp, _) ->
        {
            if (!state.is(ECBlocks.TIMELESS_MARKER))
                return;

            sl.setBlock(bp, Blocks.AIR.defaultBlockState(), TimelessInstance.FLAGS);

            TimelessMarkerBlock.Type type = state.getValue(TimelessMarkerBlock.TYPE);

            //set spawnpoint
            if (type.equals(TimelessMarkerBlock.Type.SPAWN_POINT))
                instance.spawnPoint = bp;

            //spawn timeless corpse
            if (type.equals(TimelessMarkerBlock.Type.TIMELESS_CORPSE))
            {
                TimelessCorpse corpse = ECEntities.TIMELESS_CORPSE.get().create(sl, EntitySpawnReason.TRIGGERED);
                corpse.snapTo(bp.getCenter().x, bp.getCenter().y - 0.7, bp.getCenter().z);

                if (state.getValue(TimelessMarkerBlock.FACING).equals(Direction.NORTH))
                    corpse.setYRot(0);
                else if (state.getValue(TimelessMarkerBlock.FACING).equals(Direction.WEST))
                    corpse.setYRot(90);
                else if (state.getValue(TimelessMarkerBlock.FACING).equals(Direction.SOUTH))
                    corpse.setYRot(180);
                else if (state.getValue(TimelessMarkerBlock.FACING).equals(Direction.EAST))
                    corpse.setYRot(270);

                corpse.setStack(ECDataEntries.STARTER_ITEM.get().toStack());
                sl.addFreshEntityWithPassengers(corpse);
            }

            //spawn heart entity
            if (type.equals(TimelessMarkerBlock.Type.HEART))
            {
                SculkHeartEntity heart = ECEntities.SCULK_HEART.get().create(sl, EntitySpawnReason.TRIGGERED);
                heart.setPersistenceRequired();
                heart.snapTo(bp.getCenter().x, bp.getCenter().y, bp.getCenter().z);
                sl.addFreshEntityWithPassengers(heart);
            }

            //spawn lanterns
            if (type.equals(TimelessMarkerBlock.Type.LANTERN))
            {
                Entity entity = ECEntities.LANTERN.get().spawn(sl, bp, EntitySpawnReason.TRIGGERED);
                entity.snapTo(bp.getCenter().x, bp.getCenter().y, bp.getCenter().z);
                sl.addFreshEntityWithPassengers(entity);
            }

            //spawn ground melee enemy
            if (type.equals(TimelessMarkerBlock.Type.GROUND_MELEE_ENEMY))
            {
                TimelessEnemyEntry randomEnemy = TimelessEnemyEntry.getRandomEnemy(sl, ECDataEntries.GROUND_MELEE_ENEMIES.get(), instance.stage);
                if(randomEnemy != null)
                {
                    Entity entity = sl.registryAccess().lookupOrThrow(Registries.ENTITY_TYPE).getOrThrow(ResourceKey.create(Registries.ENTITY_TYPE, randomEnemy.id()))
                            .value().spawn(sl, bp, EntitySpawnReason.STRUCTURE);
                    if(entity instanceof Mob mob)
                    {
                        mob.setPersistenceRequired();
                        addToAttrib(mob, Attributes.MAX_HEALTH, randomEnemy.healthIncrease() * instance.stage);
                        addToAttrib(mob, Attributes.ATTACK_DAMAGE, randomEnemy.damageIncrease()  * instance.stage);
                    }
                }
            }

            //spawn random ranged enemy
            if (type.equals(TimelessMarkerBlock.Type.GROUND_RANGED_ENEMY))
            {
                TimelessEnemyEntry randomEnemy = TimelessEnemyEntry.getRandomEnemy(sl, ECDataEntries.GROUND_RANGED_ENEMIES.get(), instance.stage);
                if(randomEnemy != null)
                {
                    Entity entity = sl.registryAccess().lookupOrThrow(Registries.ENTITY_TYPE).getOrThrow(ResourceKey.create(Registries.ENTITY_TYPE, randomEnemy.id()))
                            .value().spawn(sl, bp, EntitySpawnReason.STRUCTURE);

                    if(entity instanceof Mob mob)
                    {
                        mob.setPersistenceRequired();
                        addToAttrib(mob, Attributes.MAX_HEALTH, randomEnemy.healthIncrease() * instance.stage);
                        addToAttrib(mob, Attributes.ATTACK_DAMAGE, randomEnemy.damageIncrease()  * instance.stage);
                    }
                }
            }

            //spawn blacksmith npc
            if (type.equals(TimelessMarkerBlock.Type.BLACKSMITH_NPC))
            {
                Villager entity = EntityType.VILLAGER.spawn(sl, bp, EntitySpawnReason.TRIGGERED);
                entity.snapTo(bp.getCenter().x, bp.getCenter().y, bp.getCenter().z);
                entity.setCustomName(Component.literal("souls-themed blacksmith npc (not a villager)"));
                entity.setCustomNameVisible(true);
                sl.addFreshEntityWithPassengers(entity);
            }

            //spawn blacksmith stand
            if (type.equals(TimelessMarkerBlock.Type.BLACKSMITH_STAND))
            {
                sl.setBlockAndUpdate(bp, ECBlocks.DISPLAY.get().defaultBlockState());
                sl.getChunkSource().addTicketAndLoadWithRadius(TicketType.ENDER_PEARL, ChunkPos.containing(bp), 1);
                if (sl.getBlockEntity(bp) instanceof DisplayBlockEntity dbe)
                {
                    dbe.trade = BlacksmithTrade.getRandomTrade(sl);
                    dbe.setChanged();

                    sl.setBlockAndUpdate(bp, ECBlocks.DISPLAY.get().defaultBlockState()
                            .setValue(HorizontalDirectionalBlock.FACING, state.getValueOrElse(HorizontalDirectionalBlock.FACING, Direction.NORTH))
                            .setValue(DisplayBlock.RARITY, dbe.trade.rarity())
                    );
                }
            }

            //spawn portal
            if (type.equals(TimelessMarkerBlock.Type.PORTAL))
            {
                sl.getChunkSource().addTicketAndLoadWithRadius(TicketType.ENDER_PEARL, ChunkPos.containing(bp), 1);
                BlockState blockState = ECBlocks.PORTAL.get().defaultBlockState();
                blockState = blockState.trySetValue(PortalBlock.STATE, PortalBlock.State.OPEN);
                sl.setBlockAndUpdate(bp, blockState);
            }

            //spawn portal
            if (type.equals(TimelessMarkerBlock.Type.CHEST))
            {
                sl.getChunkSource().addTicketAndLoadWithRadius(TicketType.ENDER_PEARL, ChunkPos.containing(bp), 1);
                BlockState blockState = Blocks.CHEST.defaultBlockState();
                sl.setBlockAndUpdate(bp, blockState);
            }

        });

        //sculk structure processor
        add((instance, sl, state, bp, isSculk) ->
        {
            //only run for sculk
            if (!isSculk) return;

            if (!state.isEmpty())
                instance.STORED_STATES.put(bp, state);

            //set to air if not part of skip tag
            if (!state.is(ECTags.SKIPS_SCULK_TRANSFORMATION))
                sl.setBlock(bp, Blocks.AIR.defaultBlockState(), 0);
        });
    }

    public static void addToAttrib(Mob entity, Holder<Attribute> attrib, float value)
    {
        if(entity.getAttributes().hasAttribute(attrib))
        {
            entity.getAttributes().getInstance(attrib)
                    .addPermanentModifier(
                            new AttributeModifier(attrib.getKey().identifier(), value, AttributeModifier.Operation.ADD_VALUE));
        }
    }

    public static void add(Processor processor)
    {
        PROCESSORS.add(processor);
    }

    public static void process(TimelessInstance instance, ServerLevel sl, BlockState state, BlockPos bp, boolean isSculk)
    {
        for (Processor p : PROCESSORS)
            p.process(instance, sl, state, bp, isSculk);
    }

    public interface Processor
    {
        void process(TimelessInstance instance, ServerLevel sl, BlockState state, BlockPos bp, boolean isSculk);
    }
}
