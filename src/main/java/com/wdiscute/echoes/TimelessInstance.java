package com.wdiscute.echoes;

import com.mojang.datafixers.util.Pair;
import com.wdiscute.echoes.entity.lantern.LanternEntity;
import com.wdiscute.echoes.registry.ECDataAttachments;
import com.wdiscute.echoes.registry.ECDataEntries;
import com.wdiscute.echoes.registry.ECEntities;
import com.wdiscute.echoes.entity.heart.SculkHeartEntity;
import com.wdiscute.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class TimelessInstance
{
    public static final List<TimelessInstance> INSTANCES = new ArrayList<>();
    private static final int MAX_GLOBAL_AURA = 30;

    public int closingSequence = -1;
    public float globalAuraBoost = 0;
    public float cachedGlobalAuraBoost = 0;
    public SculkHeartEntity heart;
    public boolean removed;
    public BlockPos origin;
    public List<ServerPlayer> players = new ArrayList<>();
    public final Map<BlockPos, BlockState> STORED_STATES = new HashMap<>();
    public final Set<BlockPos> IS_FLIPPED = new HashSet<>();

    List<Pair<Vec3, Float>> auras = new ArrayList<>();
    List<Pair<Vec3, Float>> oldAuras = new ArrayList<>();
    List<Utils.Trio<Vec3, Float, Float>> rings = new ArrayList<>();

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
        //                         ,--. ,--.
        //  ,---.   ,---. ,--.,--. |  | |  |,-.
        // (  .-'  | .--' |  ||  | |  | |     /
        // .-'  `) \ `--. '  ''  ' |  | |  \  \
        // `----'   `---'  `----'  `--' `--'`--'
        //

        //for (int x = -51; x < 51; x++)
        //    for (int z = -51; z < 51; z++)
        //        sl.setBlock(new BlockPos(origin.getX() + x, origin.getY(), origin.getZ() + z), Blocks.SCULK.defaultBlockState(), 0);

        //spawn sculk structure
        spawnStructure(sl, true);

        //after sculk, store all blocks and clear zone
        for (int x = -48; x < 48; x++)
        {
            for (int y = 0; y < 48; y++)
            {
                for (int z = -48; z < 48; z++)
                {
                    BlockPos bpToStore = new BlockPos(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
                    BlockState stateToSave = sl.getBlockState(bpToStore);

                    if (stateToSave.is(Blocks.GOLD_BLOCK))
                    {
                        SculkHeartEntity heart = ECEntities.SCULK_HEART.get().create(sl, EntitySpawnReason.TRIGGERED);
                        heart.snapTo(bpToStore.getCenter().x, bpToStore.getCenter().y, bpToStore.getCenter().z);
                        heart.setInstance(this);
                        this.heart = heart;
                        sl.addFreshEntityWithPassengers(heart);
                        stateToSave = Blocks.AIR.defaultBlockState();
                    }

                    if (!stateToSave.isEmpty())
                        STORED_STATES.put(bpToStore, stateToSave);
                    sl.setBlock(bpToStore, Blocks.AIR.defaultBlockState(), 0);
                }
            }
        }


        //
        //                    ,--.                               ,--. ,--.
        // ,--,--,   ,---.  ,-'  '-.      ,---.   ,---. ,--.,--. |  | |  |,-.
        // |      \ | .-. | '-.  .-'     (  .-'  | .--' |  ||  | |  | |     /
        // |  ||  | ' '-' '   |  |       .-'  `) \ `--. '  ''  ' |  | |  \  \
        // `--''--'  `---'    `--'       `----'   `---'  `----'  `--' `--'`--'
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

        sl.setBlockAndUpdate(origin, Blocks.DIAMOND_BLOCK.defaultBlockState());
    }

    public void onHeartHit(ServerLevel level)
    {
        globalAuraBoost = Math.clamp(globalAuraBoost + 5, 0, MAX_GLOBAL_AURA + 1);

        level.playSound(null, heart.blockPosition(), SoundEvents.SCULK_BLOCK_PLACE, SoundSource.HOSTILE, 1f, 1f);

        //if not on closing sequence, spawn ring
        //if (closingSequence == -1)
        //    submitRing(heart.position(), heart.getSculkAura(null) + globalAuraBoost, 0.5f);
    }

    public void tick(ServerLevel sl)
    {
        //passive decay of global aura
        if (globalAuraBoost > 0 && closingSequence == -1)
            globalAuraBoost -= 0.1F;

        //start closing sequence
        if (globalAuraBoost >= MAX_GLOBAL_AURA && closingSequence == -1)
        {
            closingSequence = 0;
        }

        //add all entities with SculkAura aura
        sl.getEntities(
                (Entity) null,
                new AABB(origin).inflate(1000),
                entity -> entity instanceof SculkAura
        ).forEach(o ->
        {
            submitAura(o.position(), ((SculkAura) o).getSculkAura(sl));
            if (o instanceof SculkHeartEntity she) she.setInstance(this);
            if (o instanceof LanternEntity she) she.setInstance(this);
        });

        //process auras
        processAuras(sl);

        //process rings
        processRings(sl);

        //closing sequence logic
        if (closingSequence != -1)
        {
            //increase closing sequence
            closingSequence++;

            if (closingSequence == 1)
                sl.playSound(null, BlockPos.containing(heart.position()), SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.HOSTILE);

            if (closingSequence == 20)
                sl.playSound(null, BlockPos.containing(heart.position()), SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.HOSTILE, 1, 0.8f);

            if (closingSequence == 40)
                sl.playSound(null, BlockPos.containing(heart.position()), SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.HOSTILE, 1, 1.3f);

            if (closingSequence == 60)
                sl.playSound(null, BlockPos.containing(heart.position()), SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.HOSTILE, 1, 1f);

            if (closingSequence == 80)
                sl.playSound(null, BlockPos.containing(heart.position()), SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.HOSTILE, 1, 0.4f);

            if (closingSequence == 100)
                sl.playSound(null, BlockPos.containing(heart.position()), SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.HOSTILE, 1, 0.5f);

            if (closingSequence == 70)
                sl.playSound(null, BlockPos.containing(heart.position()), SoundEvents.BEACON_DEACTIVATE, SoundSource.HOSTILE);

            if (closingSequence == 70)
                submitRing(heart.position(), 30, -1);

            if (closingSequence == 90)
                submitRing(heart.position(), 30, -1);

            if (closingSequence == 90)
            {
                sl.playSound(null, BlockPos.containing(heart.position()), SoundEvents.BEACON_DEACTIVATE, SoundSource.HOSTILE);
                sl.playSound(null, BlockPos.containing(heart.position()), SoundEvents.BEACON_ACTIVATE, SoundSource.HOSTILE);
            }

            if (closingSequence > 100)
            {
                globalAuraBoost--;
            }
        }

        for (ServerPlayer player : players)
        {
            //collapse instance
            if (ticksRemaining <= 0)
            {
                removePlayer(player);
                removed = true;
            }
            else
                ticksRemaining--;
        }
    }

    private void processRings(ServerLevel sl)
    {
        List<Utils.Trio<Vec3, Float, Float>> newRings = new ArrayList<>();

        for (Utils.Trio<Vec3, Float, Float> ring : rings)
        {
            if (ring.second() < 120)
            {
                newRings.add(new Utils.Trio<>(ring.first(), ring.second() + ring.third(), ring.third()));
                List<BlockPos> blockPos = SphereCache.get(ring.second().intValue());
                if (ring.third() > 0)
                    blockPos.forEach(o -> setToSculk(sl, o.offset(BlockPos.containing(ring.first()))));
                else
                    blockPos.forEach(o -> setToNotSculk(sl, o.offset(BlockPos.containing(ring.first()))));
            }
        }

        rings.clear();
        rings.addAll(newRings);
    }

    public void submitRing(Vec3 position, float startSize, float increase)
    {
        rings.add(new Utils.Trio<>(position, startSize, increase));
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
                new Vec3(origin.getX() + 7, origin.getY() + 23, origin.getZ() + 25),
                Vec3.ZERO,
                -90,
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

        if (currentState.isEmpty() && storedState.isEmpty()) return;

        if (IS_FLIPPED.contains(bp))
            IS_FLIPPED.remove(bp);
        else
            IS_FLIPPED.add(bp);

        STORED_STATES.put(bp, currentState);
        int flags =
                Block.UPDATE_CLIENTS
                | Block.UPDATE_KNOWN_SHAPE
                | Block.UPDATE_SUPPRESS_DROPS
                | Block.UPDATE_MOVE_BY_PISTON
                | Block.UPDATE_SKIP_BLOCK_ENTITY_SIDEEFFECTS;

        sl.setBlock(bp, storedState, flags);
    }

    public void setToSculk(ServerLevel sl, BlockPos bp)
    {
        BlockState currentState = sl.getBlockState(bp);
        BlockState storedState = STORED_STATES.getOrDefault(bp, Blocks.AIR.defaultBlockState());

        if (currentState.isEmpty() && storedState.isEmpty()) return;

        if (IS_FLIPPED.contains(bp))
            return;
        else
            IS_FLIPPED.add(bp);

        STORED_STATES.put(bp, currentState);
        int flags =
                Block.UPDATE_CLIENTS
                | Block.UPDATE_KNOWN_SHAPE
                | Block.UPDATE_SUPPRESS_DROPS
                | Block.UPDATE_MOVE_BY_PISTON
                | Block.UPDATE_SKIP_BLOCK_ENTITY_SIDEEFFECTS;

        sl.setBlock(bp, storedState, flags);
    }

    public void setToNotSculk(ServerLevel sl, BlockPos bp)
    {
        BlockState currentState = sl.getBlockState(bp);
        BlockState storedState = STORED_STATES.getOrDefault(bp, Blocks.AIR.defaultBlockState());

        if (currentState.isEmpty() && storedState.isEmpty()) return;

        if (IS_FLIPPED.contains(bp))
            IS_FLIPPED.remove(bp);
        else
            return;

        STORED_STATES.put(bp, currentState);
        int flags =
                Block.UPDATE_CLIENTS
                | Block.UPDATE_KNOWN_SHAPE
                | Block.UPDATE_SUPPRESS_DROPS
                | Block.UPDATE_MOVE_BY_PISTON
                | Block.UPDATE_SKIP_BLOCK_ENTITY_SIDEEFFECTS;

        sl.setBlock(bp, storedState, flags);
    }

    private void processAuras(ServerLevel sl)
    {
        if (auras.equals(oldAuras) && globalAuraBoost == cachedGlobalAuraBoost && rings.isEmpty())
        {
            auras.clear();
            return;
        }

        Set<BlockPos> currentInAura = new HashSet<>();

        //add all blocks that should be in aura
        for (var aura : auras)
        {
            int size = (int) (aura.getSecond() + globalAuraBoost);

            List<BlockPos> blockPos = filledSphere(size);

            blockPos.forEach(o -> currentInAura.add(o.offset(BlockPos.containing(aura.getFirst()))));
        }

        //un-flip blocks not on aura
        List<BlockPos> toFlip = new ArrayList<>();

        for (BlockPos bp : IS_FLIPPED)
            if (!currentInAura.contains(bp))
                toFlip.add(bp);

        for (BlockPos bp : toFlip)
            flipBlock(sl, bp);

        //flip unflipped blocks
        //filter blocks in aura to blocks which are currently not flipped, flip them and store
        currentInAura.stream().filter(bp -> !IS_FLIPPED.contains(bp)).forEach(bp -> flipBlock(sl, bp));

        oldAuras.clear();
        cachedGlobalAuraBoost = globalAuraBoost;
        oldAuras.addAll(auras);
        auras.clear();
    }

    public void submitAura(Vec3 pos, float radius)
    {
        auras.add(Pair.of(pos, radius));
    }

    public void spawnStructure(ServerLevel sl, boolean sculk)
    {
        Utils.Duo<Identifier, Identifier> duo = ECDataEntries.STRUCTURE_ENTRIES.get().stream().findAny()
                .orElse(new Utils.Duo<>(BuiltinStructures.IGLOO.identifier(), BuiltinStructures.IGLOO.identifier()));

        Identifier template = sculk ? duo.first() : duo.second();

        //checkLoaded(sl, ChunkPos.containing(origin), ChunkPos.containing(origin.offset(structureTemplate.getSize())));
        StructurePlaceSettings placeSettings =
                new StructurePlaceSettings().setMirror(Mirror.NONE).setRotation(Rotation.NONE).setKnownShape(false);

        StructureTemplateManager manager = sl.getStructureManager();


        StructureTemplate structureTemplatese = manager.get(template.withSuffix("_se")).get();
        structureTemplatese.placeInWorld(sl, origin.offset(0, 0, 0), origin, placeSettings, StructureBlockEntity.createRandom(0), 2 | (0));

        StructureTemplate structureTemplatesw = manager.get(template.withSuffix("_sw")).get();
        structureTemplatesw.placeInWorld(sl, origin.offset(-48, 0, 0), origin, placeSettings, StructureBlockEntity.createRandom(0), 2 | (0));

        StructureTemplate structureTemplatenw = manager.get(template.withSuffix("_nw")).get();
        structureTemplatenw.placeInWorld(sl, origin.offset(-48, 0, -48), origin, placeSettings, StructureBlockEntity.createRandom(0), 2 | (0));

        StructureTemplate structureTemplatene = manager.get(template.withSuffix("_ne")).get();
        structureTemplatene.placeInWorld(sl, origin.offset(0, 0, -48), origin, placeSettings, StructureBlockEntity.createRandom(0), 2 | (0));


    }

    public static final class SphereCache
    {
        private static final Map<Integer, List<BlockPos>> CACHE = new HashMap<>();

        public static void init(int maxRadius)
        {
            for (int r = 1; r <= maxRadius; r++)
                CACHE.put(r, buildSphereShell(r));
        }

        public static List<BlockPos> get(int radius)
        {
            return CACHE.computeIfAbsent(radius, SphereCache::buildSphereShell);
        }

        private static List<BlockPos> buildSphereShell(int radius)
        {
            Set<BlockPos> shell = new HashSet<>();

            int r2 = radius * radius;
            int inner2 = (radius - 1) * (radius - 1);

            for (int x = -radius; x <= radius; x++)
            {
                int xx = x * x;

                for (int y = -radius; y <= radius; y++)
                {
                    int yy = y * y;

                    int maxZ = (int) Math.floor(Math.sqrt(r2 - xx - yy));

                    for (int z = -maxZ; z <= maxZ; z++)
                    {
                        int d2 = xx + yy + z * z;

                        if (d2 >= inner2 && d2 <= r2)
                        {
                            shell.add(new BlockPos(x, y, z));
                        }
                    }
                }
            }

            return List.copyOf(shell);
        }
    }

    public static List<BlockPos> filledSphere(int radius)
    {
        List<BlockPos> blocks = new ArrayList<>();

        int r2 = radius * radius;

        for (int z = -radius; z <= radius; z++)
        {
            int z2 = z * z;

            int sliceRadius = (int) Math.floor(Math.sqrt(r2 - z2));
            int sliceR2 = sliceRadius * sliceRadius;

            for (int y = -sliceRadius; y <= sliceRadius; y++)
            {
                int xMax = (int) Math.floor(Math.sqrt(sliceR2 - y * y));

                for (int x = -xMax; x <= xMax; x++)
                {
                    blocks.add(new BlockPos(x, y, z));
                }
            }
        }

        return blocks;
    }
}
