package com.wdiscute.echoes;

import com.wdiscute.echoes.registry.ECDataAttachments;
import com.wdiscute.echoes.registry.ECDataEntries;
import com.wdiscute.echoes.registry.ECEntities;
import com.wdiscute.echoes.registry.TimelessData;
import com.wdiscute.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class TimelessInstance
{
    public static final List<TimelessInstance> INSTANCES = new ArrayList<>();

    public boolean removed;
    public BlockPos origin;
    public List<ServerPlayer> players = new ArrayList<>();
    public final Map<BlockPos, BlockState> STORED_STATES = new HashMap<>();
    public final Set<BlockPos> IS_FLIPPED = new HashSet<>();

    List<Vec3> auras = new ArrayList<>();

    public int ticksRemaining;

    public TimelessInstance(ServerPlayer player)
    {
        //add created instance to list
        INSTANCES.add(this);

        //generate origin
        Random r = new Random(System.currentTimeMillis());
        origin = new BlockPos(r.nextInt(50000000 / 2), 100, r.nextInt(50000000 / 2));

        ServerLevel sl = player.level().getServer().getLevel(Echoes.TIMELESS);

        ticksRemaining = 30000;

        addPlayer(player);

        //
        //                        ,--. ,--.
        // ,---.   ,---. ,--.,--. |  | |  |,-.
        //(  .-'  | .--' |  ||  | |  | |     /
        //.-'  `) \ `--. '  ''  ' |  | |  \  \
        //`----'   `---'  `----'  `--' `--'`--'
        //

        //for (int x = -51; x < 51; x++)
        //    for (int z = -51; z < 51; z++)
        //        sl.setBlock(new BlockPos(origin.getX() + x, origin.getY(), origin.getZ() + z), Blocks.SCULK.defaultBlockState(), 0);

        //spawn sculk structure
        spawnStructure(sl, true);

        //after sculk, store all blocks and clear zone
        for (int x = -50; x < 50; x++)
        {
            for (int y = -2; y < 20; y++)
            {
                for (int z = -50; z < 50; z++)
                {
                    BlockPos bpToStore = new BlockPos(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
                    BlockState stateToSave = sl.getBlockState(bpToStore);
                    if (!stateToSave.isEmpty())
                        STORED_STATES.put(bpToStore, stateToSave);
                    sl.setBlock(bpToStore, Blocks.AIR.defaultBlockState(), 0);
                }
            }
        }


        //
        //                   ,--.                               ,--. ,--.
        //,--,--,   ,---.  ,-'  '-.      ,---.   ,---. ,--.,--. |  | |  |,-.
        //|      \ | .-. | '-.  .-'     (  .-'  | .--' |  ||  | |  | |     /
        //|  ||  | ' '-' '   |  |       .-'  `) \ `--. '  ''  ' |  | |  \  \
        //`--''--'  `---'    `--'       `----'   `---'  `----'  `--' `--'`--'
        //

        //for (int x = origin.getX() - 50; x < origin.getX() + 50; x++)
        //{
        //    for (int z = origin.getZ() - 50; z < origin.getZ() + 50; z++)
        //    {
        //        sl.setBlock(new BlockPos(x, origin.getY(), z), Blocks.STONE.defaultBlockState(), 0);
        //    }
        //}

        //spawn non sculk structure
        spawnStructure(sl, false);
    }

    public void spawnStructure(ServerLevel sl, boolean sculk)
    {
        Utils.Duo<Identifier, Identifier> duo = ECDataEntries.STRUCTURE_ENTRIES.get().stream().findAny()
                .orElse(new Utils.Duo<>(BuiltinStructures.IGLOO.identifier(), BuiltinStructures.IGLOO.identifier()));

        Optional<Holder.Reference<Structure>> structureHolder = sl.structureManager().registryAccess().lookupOrThrow(Registries.STRUCTURE)
                .get(sculk ? duo.first() : duo.second());

        Structure structure = structureHolder.get().value();
        ChunkGenerator chunkGenerator = sl.getChunkSource().getGenerator();
        StructureStart start = structure.generate(structureHolder.get(), sl.dimension(), sl.registryAccess(),
                chunkGenerator, chunkGenerator.getBiomeSource(), sl.getChunkSource().randomState(),
                sl.getStructureManager(), sl.getSeed(), ChunkPos.containing(origin.offset(10, 5, 10)), 0, sl, b -> true);

        if (!start.isValid())
            System.out.println("not valid structure");
        else
        {
            BoundingBox boundingBox = start.getBoundingBox();
            ChunkPos chunkMin = new ChunkPos(SectionPos.blockToSectionCoord(boundingBox.minX()), SectionPos.blockToSectionCoord(boundingBox.minZ()));
            ChunkPos chunkMax = new ChunkPos(SectionPos.blockToSectionCoord(boundingBox.maxX()), SectionPos.blockToSectionCoord(boundingBox.maxZ()));

            ChunkPos.rangeClosed(chunkMin, chunkMax)
                    .forEach(
                            c -> start.placeInChunk(
                                    sl,
                                    sl.structureManager(),
                                    chunkGenerator,
                                    sl.getRandom(),
                                    new BoundingBox(c.getMinBlockX(), sl.getMinY(), c.getMinBlockZ(), c.getMaxBlockX(),
                                            sl.getMaxY() + 1, c.getMaxBlockZ()),
                                    c
                            )
                    );
        }
    }

    public void removePlayer(ServerPlayer player)
    {
        TimelessData data = player.getData(ECDataAttachments.TIMELESS_STATS);

        ServerLevel sl = player.level();

        ServerLevel level = sl.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, data.levelToReturn()));

        if (level == null)
            level.getServer().getLevel(player.getRespawnConfig().respawnData().dimension());

        TeleportTransition trans = new TeleportTransition(
                level,
                data.positionToExit(),
                new Vec3(0, 0, 0), 0, 0,
                (_) ->
                {
                }
        );

        player.teleport(trans);

        player.removeData(ECDataAttachments.TIMELESS_STATS);
    }

    public void addPlayer(ServerPlayer player)
    {
        ServerLevel sl = player.level().getServer().getLevel(Echoes.TIMELESS);

        TeleportTransition trans = new TeleportTransition(sl,
                new Vec3(origin.getX(), origin.getY() + 3, origin.getZ()),
                Vec3.ZERO,
                0,
                0,
                (p) ->
                {

                });

        player.setData(ECDataAttachments.TIMELESS_STATS,
                new TimelessData(
                        System.currentTimeMillis() + ticksRemaining * 50L,
                        player.position(),
                        player.level().dimension().identifier()
                )
        );

        player.teleport(trans);
        players.add(player);
    }

    public void flipBlock(ServerLevel sl, BlockPos bp)
    {
        BlockState currentState = sl.getBlockState(bp);
        BlockState storedState = STORED_STATES.getOrDefault(bp, Blocks.AIR.defaultBlockState());

        if (IS_FLIPPED.contains(bp))
            IS_FLIPPED.remove(bp);
        else
            IS_FLIPPED.add(bp);

        STORED_STATES.put(bp, currentState);
        sl.setBlockAndUpdate(bp, storedState);
    }

    private void processAuras(ServerLevel sl)
    {
        Set<BlockPos> currentInAura = new HashSet<>();

        //add all blocks that should be in aura
        for (Vec3 aura : auras)
            for (int x = -10; x < 10; x++)
                for (int y = -10; y < 10; y++)
                    for (int z = -10; z < 10; z++)
                    {
                        BlockPos bp = new BlockPos((int) (aura.x + x), (int) (aura.y + y), (int) (aura.z + z));

                        //only add if closer than 6 blocks for circular area type thing
                        if (bp.closerToCenterThan(aura, 7))
                            currentInAura.add(bp);
                    }


        //un-flip blocks not on aura
        new HashSet<>(IS_FLIPPED).stream().filter(bp -> !currentInAura.contains(bp)).forEach(bp ->
        {
            flipBlock(sl, bp);
        });

        //flip unflipped blocks
        //filter blocks in aura to blocks which are currently not flipped, flip them and store
        currentInAura.stream().filter(bp -> !IS_FLIPPED.contains(bp)).forEach(bp ->
        {
            flipBlock(sl, bp);
        });

        auras.clear();
    }


    private void submitAura(Vec3 pos)
    {
        auras.add(pos);
    }

    public void tick(ServerLevel sl)
    {
        //get all entities within 100 blocks of the origin
        AABB box = new AABB(origin).inflate(100);

        List<Entity> entities = sl.getEntities(
                (Entity) null,
                box,
                entity -> entity.is(ECEntities.LANTERN)
        );

        //add all lanterns to aura
        entities.forEach(o -> submitAura(o.position()));

        //add end goal to aura
        //submitAura(goal);

        //process auras
        processAuras(sl);

        for (ServerPlayer player : players)
        {
            //collapse instance
            if (ticksRemaining <= 0)
            {
                INSTANCES.remove(this);
                removePlayer(player);
                removed = true;
            }
            else
                ticksRemaining--;
        }
    }
}
