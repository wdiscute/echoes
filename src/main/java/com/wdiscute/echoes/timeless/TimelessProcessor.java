package com.wdiscute.echoes.timeless;

import com.wdiscute.echoes.ECTags;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.blocks.display.DisplayBlockEntity;
import com.wdiscute.echoes.blocks.marker.TimelessMarkerBlock;
import com.wdiscute.echoes.blocks.portal.PortalBlock;
import com.wdiscute.echoes.entity.corpse.TimelessCorpse;
import com.wdiscute.echoes.entity.heart.SculkHeartEntity;
import com.wdiscute.echoes.registry.ECBlocks;
import com.wdiscute.echoes.registry.ECEntities;
import com.wdiscute.echoes.registry.ECItems;
import com.wdiscute.echoes.upgrades.BlacksmithTrade;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.injection.selectors.ElementNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class TimelessProcessor
{
    public static final List<Processor> PROCESSORS = new ArrayList<>();
    public static final ResourceKey<LootTable> EMPTY_LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("empty"));

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

                corpse.setStack(ECItems.ECHO_BLADE.toStack());
                sl.addFreshEntityWithPassengers(corpse);
            }

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
                Mob entity = EntityType.HUSK.spawn(sl, bp, EntitySpawnReason.TRIGGERED);
                entity.lootTable = Optional.of(EMPTY_LOOT_TABLE);
                entity.snapTo(bp.getCenter().x, bp.getCenter().y, bp.getCenter().z);
                sl.addFreshEntityWithPassengers(entity);
            }

            //spawn random enemy
            if (type.equals(TimelessMarkerBlock.Type.GROUND_RANGED_ENEMY))
            {
                Mob entity = EntityType.SKELETON.spawn(sl, bp, EntitySpawnReason.TRIGGERED);
                entity.lootTable = Optional.of(EMPTY_LOOT_TABLE);
                entity.snapTo(bp.getCenter().x, bp.getCenter().y, bp.getCenter().z);
                sl.addFreshEntityWithPassengers(entity);
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

            //spawn portal
            if (type.equals(TimelessMarkerBlock.Type.PORTAL))
            {
                sl.getChunkSource().addTicketAndLoadWithRadius(TicketType.ENDER_PEARL, ChunkPos.containing(bp), 1);
                BlockState blockState = ECBlocks.PORTAL.get().defaultBlockState();
                blockState = blockState.trySetValue(PortalBlock.STATE, PortalBlock.State.OPEN);
                sl.setBlockAndUpdate(bp, blockState);
            }

            //spawn fountain
            if (type.equals(TimelessMarkerBlock.Type.FOUNTAIN))
            {
                sl.getChunkSource().addTicketAndLoadWithRadius(TicketType.ENDER_PEARL, ChunkPos.containing(bp), 1);

                //center pillar
                sl.setBlockAndUpdate(bp, Blocks.QUARTZ_PILLAR.defaultBlockState());
                sl.setBlockAndUpdate(bp.above(), Blocks.QUARTZ_PILLAR.defaultBlockState());
                sl.setBlockAndUpdate(bp.above().above(), Blocks.QUARTZ_PILLAR.defaultBlockState());
                sl.setBlockAndUpdate(bp.above().above().above(), Blocks.LANTERN.defaultBlockState());

                //water around it
                sl.setBlockAndUpdate(bp.east(), Blocks.WATER.defaultBlockState());
                sl.setBlockAndUpdate(bp.west(), Blocks.WATER.defaultBlockState());
                sl.setBlockAndUpdate(bp.north(), Blocks.WATER.defaultBlockState());
                sl.setBlockAndUpdate(bp.south(), Blocks.WATER.defaultBlockState());

                //water border
                sl.setBlockAndUpdate(bp.south().south(), Blocks.QUARTZ_BLOCK.defaultBlockState());
                sl.setBlockAndUpdate(bp.east().east(), Blocks.QUARTZ_BLOCK.defaultBlockState());
                sl.setBlockAndUpdate(bp.west().west(), Blocks.QUARTZ_BLOCK.defaultBlockState());
                sl.setBlockAndUpdate(bp.north().north(), Blocks.QUARTZ_BLOCK.defaultBlockState());

                sl.setBlockAndUpdate(bp.north().east(), Blocks.QUARTZ_BLOCK.defaultBlockState());
                sl.setBlockAndUpdate(bp.north().west(), Blocks.QUARTZ_BLOCK.defaultBlockState());
                sl.setBlockAndUpdate(bp.south().east(), Blocks.QUARTZ_BLOCK.defaultBlockState());
                sl.setBlockAndUpdate(bp.south().west(), Blocks.QUARTZ_BLOCK.defaultBlockState());
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
