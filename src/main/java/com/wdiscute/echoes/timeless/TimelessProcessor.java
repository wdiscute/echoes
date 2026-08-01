package com.wdiscute.echoes.timeless;

import com.wdiscute.echoes.ECTags;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.blocks.display.DisplayBlockEntity;
import com.wdiscute.echoes.blocks.marker.TimelessMarkerBlock;
import com.wdiscute.echoes.entity.heart.SculkHeartEntity;
import com.wdiscute.echoes.registry.ECBlocks;
import com.wdiscute.echoes.registry.ECEntities;
import com.wdiscute.echoes.upgrades.BlacksmithTrade;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;


public class TimelessProcessor
{
    public static final List<Processor> PROCESSORS = new ArrayList<>();

    public static void addDefaultProcessors()
    {
        //timeless marker processor
        add((instance, sl, state, bp, structureType) ->
        {
            if (!state.is(ECBlocks.TIMELESS_MARKER))
                return;

            sl.setBlock(bp, Blocks.AIR.defaultBlockState(), TimelessInstance.FLAGS);

            TimelessMarkerBlock.Type type = state.getValue(TimelessMarkerBlock.TYPE);

            //set spawnpoint
            if (type.equals(TimelessMarkerBlock.Type.SPAWN_POINT))
                instance.spawnPoint = bp;

            //spawn heart entity
            if (type.equals(TimelessMarkerBlock.Type.HEART))
            {
                SculkHeartEntity heart = ECEntities.SCULK_HEART.get().create(sl, EntitySpawnReason.TRIGGERED);
                heart.snapTo(bp.getCenter().x, bp.getCenter().y, bp.getCenter().z);
                sl.addFreshEntityWithPassengers(heart);
            }

            //spawn random enemy
            if (type.equals(TimelessMarkerBlock.Type.LANTERN))
            {
                Entity entity = ECEntities.LANTERN.get().spawn(sl, bp, EntitySpawnReason.TRIGGERED);
                entity.snapTo(bp.getCenter().x, bp.getCenter().y, bp.getCenter().z);
                sl.addFreshEntityWithPassengers(entity);
            }

            //spawn ground melee enemy
            if (type.equals(TimelessMarkerBlock.Type.GROUND_MELEE_ENEMY))
            {
                Entity entity = EntityType.HUSK.spawn(sl, bp, EntitySpawnReason.TRIGGERED);
                entity.snapTo(bp.getCenter().x, bp.getCenter().y, bp.getCenter().z);
                sl.addFreshEntityWithPassengers(entity);
            }

            //spawn random enemy
            if (type.equals(TimelessMarkerBlock.Type.GROUND_RANGED_ENEMY))
            {
                Entity entity = EntityType.SKELETON.spawn(sl, bp, EntitySpawnReason.TRIGGERED);
                entity.snapTo(bp.getCenter().x, bp.getCenter().y, bp.getCenter().z);
                sl.addFreshEntityWithPassengers(entity);
            }

            //spawn blacksmith npc
            if (type.equals(TimelessMarkerBlock.Type.BLACKSMITH_NPC))
            {
                Entity entity = EntityType.VILLAGER.spawn(sl, bp, EntitySpawnReason.TRIGGERED);
                entity.snapTo(bp.getCenter().x, bp.getCenter().y, bp.getCenter().z);
                sl.addFreshEntityWithPassengers(entity);
            }

            //spawn blacksmith stand
            if (type.equals(TimelessMarkerBlock.Type.BLACKSMITH_STAND))
            {
                sl.getChunkSource().addTicketAndLoadWithRadius(TicketType.ENDER_PEARL, ChunkPos.containing(bp), 1);
                sl.setBlockAndUpdate(bp, ECBlocks.DISPLAY.get().defaultBlockState());
                if (sl.getBlockEntity(bp) instanceof DisplayBlockEntity dbe)
                {
                    List<BlacksmithTrade> list = sl.registryAccess().lookupOrThrow(Echoes.BLACKSMITH_TRADE_KEY).stream().toList();

                    if (!list.isEmpty())
                        dbe.trade = list.get(sl.getRandom().nextInt(list.size()));
                    dbe.setChanged();
                }
            }
        });


        //sculk structure processor (should run after the markers' logic)
        add((instance, sl, state, bp, structureType) ->
        {
            //only run for sculk
            if (!structureType.equals(TimelessInstance.StructureType.SCULK)) return;

            if (!state.isEmpty())
                instance.STORED_STATES.put(bp, state);

            //set to air if not part of skip tag
            if (!state.is(ECTags.SKIPS_SCULK_TRANSFORMATION))
                sl.setBlock(bp, Blocks.AIR.defaultBlockState(), 0);
        });


    }

    public static void add(Processor processor)
    {
        PROCESSORS.add(processor);
    }

    public static void process(TimelessInstance instance, ServerLevel sl, BlockState state, BlockPos bp, TimelessInstance.StructureType type)
    {
        for (Processor p : PROCESSORS)
            p.process(instance, sl, state, bp, type);
    }

    public interface Processor
    {
        void process(TimelessInstance instance, ServerLevel sl, BlockState state, BlockPos bp, TimelessInstance.StructureType type);
    }
}
