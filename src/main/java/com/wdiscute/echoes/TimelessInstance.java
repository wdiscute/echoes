package com.wdiscute.echoes;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.echoes.network.ECDBPlaySoundPayload;
import com.wdiscute.echoes.registry.ECDataAttachments;
import com.wdiscute.echoes.registry.ECDataEntries;
import com.wdiscute.echoes.registry.ECEntities;
import com.wdiscute.echoes.entity.heart.SculkHeartEntity;
import com.wdiscute.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
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
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.*;

public class TimelessInstance
{
    public static final Codec<TimelessInstance> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    UUIDUtil.CODEC.fieldOf("uuid").forGetter(o -> o.uuid),
                    BlockPos.CODEC.fieldOf("origin").forGetter(o -> o.origin),
                    BlockPos.CODEC.optionalFieldOf("spawn_point", BlockPos.ZERO).forGetter(o -> o.spawnPoint),
                    Phase.CODEC.fieldOf("phase").forGetter(o -> o.phase),
                    Utils.Duo.codec(BlockPos.CODEC, BlockState.CODEC).listOf().fieldOf("stored_states").forGetter(TimelessInstance::getSculkBlocks),
                    BlockPos.CODEC.listOf().fieldOf("flipped_blocks").forGetter(TimelessInstance::getFlippedBlocks),
                    Codec.LONG.optionalFieldOf("lasts_until", Long.MAX_VALUE).forGetter(o -> o.lastsUntil)
            ).apply(instance, TimelessInstance::new));

    public List<Utils.Duo<BlockPos, BlockState>> getSculkBlocks()
    {
        List<Utils.Duo<BlockPos, BlockState>> listToReturn = new ArrayList<>();

        for (Map.Entry<BlockPos, BlockState> entry : STORED_STATES.entrySet())
            listToReturn.add(new Utils.Duo<>(entry.getKey(), entry.getValue()));

        return listToReturn;
    }

    public List<BlockPos> getFlippedBlocks()
    {
        return FLIPPED_BLOCKS.stream().toList();
    }

    public enum Phase implements StringRepresentable
    {
        NEW("new"),
        ONGOING("ongoing"),
        FINISHED("finished");

        final String key;

        Phase(String key)
        {
            this.key = key;
        }

        public static final Codec<Phase> CODEC = StringRepresentable.fromEnum(Phase::values);
        public static final StreamCodec<FriendlyByteBuf, Phase> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(Phase.class);

        @Override
        public String getSerializedName()
        {
            return key;
        }
    }

    public TimelessInstance(UUID uuid,
                            BlockPos origin, BlockPos spawnPoint,
                            Phase phase,
                            List<Utils.Duo<BlockPos, BlockState>> storedStates,
                            List<BlockPos> flippedStates,
                            long lastsUntil
    )
    {
        this.uuid = uuid;
        this.origin = origin;
        this.spawnPoint = spawnPoint;
        this.phase = phase;
        this.lastsUntil = lastsUntil;
        storedStates.forEach(o -> STORED_STATES.put(o.first(), o.second()));
        FLIPPED_BLOCKS.addAll(flippedStates);
    }

    public static TimelessInstance create()
    {
        BlockPos origin = new BlockPos(Utils.r.nextInt(50000000 / 2), 100, Utils.r.nextInt(50000000 / 2));
        return new TimelessInstance(
                UUID.randomUUID(),
                origin,
                origin,
                Phase.NEW,
                List.of(),
                List.of(),
                Long.MAX_VALUE
                );
    }

    private static final int MAX_GLOBAL_AURA = 30;

    //saved data
    public final UUID uuid;
    public final BlockPos origin;
    public BlockPos spawnPoint;
    public Phase phase;
    public long lastsUntil;

    //converted to list for saving
    public final Map<BlockPos, BlockState> STORED_STATES = new HashMap<>();
    public final Set<BlockPos> FLIPPED_BLOCKS = new HashSet<>();

    //non saved
    public List<Player> playersInInstance = new ArrayList<>();
    public int closingSequence = -1;
    public float globalAuraBoost = 0;
    public float cachedGlobalAuraBoost = 0;
    public SculkHeartEntity heart;


    List<Pair<Vec3, Float>> auras = new ArrayList<>();
    List<Pair<Vec3, Float>> oldAuras = new ArrayList<>();
    List<Utils.Trio<Vec3, Float, Float>> rings = new ArrayList<>();

    public void load(ServerLevel sl)
    {
        if(phase != Phase.NEW) return;

        phase = Phase.ONGOING;

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

                    if (stateToSave.is(Blocks.DIAMOND_BLOCK))
                    {
                        spawnPoint = bpToStore;
                        stateToSave = Blocks.AIR.defaultBlockState();
                    }

                    if (!stateToSave.isEmpty())
                        STORED_STATES.put(bpToStore, stateToSave);


                    sl.setBlock(bpToStore, Blocks.AIR.defaultBlockState(), 0);
                }
            }
        }

        if(spawnPoint.equals(BlockPos.ZERO))
            throw new IllegalStateException("Spawn point (diamond block) not found when loading instance " + uuid);

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

        //removed spawnpoint blocks
        sl.setBlockAndUpdate(spawnPoint, Blocks.AIR.defaultBlockState());
        STORED_STATES.put(spawnPoint, Blocks.AIR.defaultBlockState());
        FLIPPED_BLOCKS.remove(spawnPoint);

        //set origin block for debug
        sl.setBlockAndUpdate(origin, Blocks.EMERALD_BLOCK.defaultBlockState());
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
            //if (o instanceof LanternEntity she) she.setInstance(this);
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
                PacketDistributor.sendToPlayersNear(
                        sl, null, heart.getX(), heart.getY(), heart.getZ(), 1000,
                        new ECDBPlaySoundPayload("shriek", 1, 1));

            if (closingSequence == 20)
                PacketDistributor.sendToPlayersNear(
                        sl, null, heart.getX(), heart.getY(), heart.getZ(), 1000,
                        new ECDBPlaySoundPayload("shriek", 0.4f, 0.8f));

            if (closingSequence == 40)
                PacketDistributor.sendToPlayersNear(
                        sl, null, heart.getX(), heart.getY(), heart.getZ(), 1000,
                        new ECDBPlaySoundPayload("shriek", 1, 1.3f));

            if (closingSequence == 60)
                PacketDistributor.sendToPlayersNear(
                        sl, null, heart.getX(), heart.getY(), heart.getZ(), 1000,
                        new ECDBPlaySoundPayload("shriek", 0.3f, 1f));

            if (closingSequence == 80)
                PacketDistributor.sendToPlayersNear(
                        sl, null, heart.getX(), heart.getY(), heart.getZ(), 1000,
                        new ECDBPlaySoundPayload("shriek", 1, 0.4f));

            if (closingSequence == 100)
                PacketDistributor.sendToPlayersNear(
                        sl, null, heart.getX(), heart.getY(), heart.getZ(), 1000,
                        new ECDBPlaySoundPayload("shriek", 1, 0.5f));

            if (closingSequence == 70)
                PacketDistributor.sendToPlayersNear(
                        sl, null, heart.getX(), heart.getY(), heart.getZ(), 1000,
                        new ECDBPlaySoundPayload("beacon_deactivate", 1, 1f));

            if (closingSequence == 70)
                submitRing(heart.position(), 30, -1);

            if (closingSequence == 90)
                submitRing(heart.position(), 30, -1);

            if (closingSequence == 90)
            {
                PacketDistributor.sendToPlayersNear(
                        sl, null, heart.getX(), heart.getY(), heart.getZ(), 1000,
                        new ECDBPlaySoundPayload("beacon_deactivate", 1, 1f));

                PacketDistributor.sendToPlayersNear(
                        sl, null, heart.getX(), heart.getY(), heart.getZ(), 1000,
                        new ECDBPlaySoundPayload("beacon_activate", 1, 1f));
            }

            if (closingSequence > 100)
            {
                globalAuraBoost--;
            }
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

        playersInInstance.remove(player);

        player.removeData(ECDataAttachments.TIMELESS_STATS);
    }

    public void addPlayer(ServerPlayer player)
    {
        //create teleport transition
        ServerLevel sl = player.level().getServer().getLevel(Echoes.TIMELESS);

        TeleportTransition trans = new TeleportTransition(sl,
                new Vec3(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ()),
                Vec3.ZERO,
                -90,
                0,
                Utils::nothing
        );

        player.setData(ECDataAttachments.TIMELESS_STATS,
                new TimelessData(
                        lastsUntil,
                        player.position(),
                        player.level().dimension().identifier()
                )
        );

        player.teleport(trans);

        //remove has_lantern just in case
        player.removeData(ECDataAttachments.HAS_LANTERN);

        //add player to players
        playersInInstance.add(player);

        //load after player is teleported so the chunks are already loaded on server.
        //this prevents insane lag spike when structures are places since the chunks keep loading and unloading for each structure bit
        //client only receives packet later anyways so no "falling in void" happens

        //load if instance has not been loaded yet
        if(phase.equals(Phase.NEW))
        {
            load(player.level());
            //teleport player to spawnPoint as spawnPoint is only calculated after loading structures, player will be at origin at this point
            player.teleportTo(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ());
        }
    }

    public void flipBlock(ServerLevel sl, BlockPos bp)
    {
        BlockState currentState = sl.getBlockState(bp);
        BlockState storedState = STORED_STATES.getOrDefault(bp, Blocks.AIR.defaultBlockState());

        if (currentState.isEmpty() && storedState.isEmpty()) return;

        if (FLIPPED_BLOCKS.contains(bp))
            FLIPPED_BLOCKS.remove(bp);
        else
            FLIPPED_BLOCKS.add(bp);

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

        if (FLIPPED_BLOCKS.contains(bp))
            return;
        else
            FLIPPED_BLOCKS.add(bp);

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

        if (FLIPPED_BLOCKS.contains(bp))
            FLIPPED_BLOCKS.remove(bp);
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

        for (BlockPos bp : FLIPPED_BLOCKS)
            if (!currentInAura.contains(bp))
                toFlip.add(bp);

        for (BlockPos bp : toFlip)
            flipBlock(sl, bp);

        //flip unflipped blocks
        //filter blocks in aura to blocks which are currently not flipped, flip them and store
        currentInAura.stream().filter(bp -> !FLIPPED_BLOCKS.contains(bp)).forEach(bp -> flipBlock(sl, bp));

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
