package com.wdiscute.echoes;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.echoes.blocks.portal.PortalBlockEntity;
import com.wdiscute.echoes.network.ECDBPlaySoundPayload;
import com.wdiscute.echoes.registry.ECDataAttachments;
import com.wdiscute.echoes.registry.ECDataEntries;
import com.wdiscute.echoes.registry.ECEntities;
import com.wdiscute.echoes.entity.heart.SculkHeartEntity;
import com.wdiscute.utils.MaybeStack;
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
import net.minecraft.server.level.TicketType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
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
                    Codec.LONG.optionalFieldOf("lasts_until", Long.MAX_VALUE).forGetter(o -> o.lastsUntil),
                    Identifier.CODEC.fieldOf("portal_dim").forGetter(o -> o.portalDimension),
                    BlockPos.CODEC.optionalFieldOf("portal_pos", BlockPos.ZERO).forGetter(o -> o.portalPos)
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
                            long lastsUntil,
                            Identifier portalDimension,
                            BlockPos portalPos
    )
    {
        this.uuid = uuid;
        this.origin = origin;
        this.spawnPoint = spawnPoint;
        this.phase = phase;
        this.lastsUntil = lastsUntil;
        this.portalDimension = portalDimension;
        this.portalPos = portalPos;
        storedStates.forEach(o -> STORED_STATES.put(o.first(), o.second()));
        FLIPPED_BLOCKS.addAll(flippedStates);
    }

    public static TimelessInstance create(UUID uuid)
    {
        BlockPos origin = new BlockPos(Utils.r.nextInt(50000000 / 2), 100, Utils.r.nextInt(50000000 / 2));
        return new TimelessInstance(
                uuid,
                origin,
                origin,
                Phase.NEW,
                List.of(),
                List.of(),
                Long.MAX_VALUE,
                Echoes.MISSINGNO,
                BlockPos.ZERO
        );
    }

    private static final int MAX_GLOBAL_AURA = 30;

    //saved data
    public final UUID uuid;
    public final BlockPos origin;
    public BlockPos spawnPoint;
    public Phase phase;
    public long lastsUntil;
    public BlockPos portalPos;
    public Identifier portalDimension;

    //converted to list for saving
    public final Map<BlockPos, BlockState> STORED_STATES = new HashMap<>();
    public final Set<BlockPos> FLIPPED_BLOCKS = new HashSet<>();

    //non saved
    public int closingSequence = -1;
    public float globalAuraBoost = 0;
    public float cachedGlobalAuraBoost = 0;
    public SculkHeartEntity heart;


    List<Pair<Vec3, Float>> auras = new ArrayList<>();
    List<Pair<Vec3, Float>> oldAuras = new ArrayList<>();
    List<Utils.Trio<Vec3, Float, Float>> rings = new ArrayList<>();

    public void attemptLoad(ServerLevel sl, BlockPos portalPos, Identifier portalDimension, StructureType structureType)
    {
        if (phase != Phase.NEW) return;

        phase = Phase.ONGOING;

        this.portalDimension = portalDimension;
        this.portalPos = portalPos;

        if (structureType.isBase())
        {
            spawnStructure(sl, StructureType.SCULK);
            spawnStructure(sl, StructureType.GLEEMSLATE);
        }
        else
            spawnStructure(sl, structureType);

        //spawnpoint blocks
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
        if (heart == null)
        {
            List<SculkHeartEntity> list = sl.getEntitiesOfClass(
                    SculkHeartEntity.class,
                    new AABB(origin).inflate(1000)
            );

            if (!list.isEmpty())
                heart = list.getFirst();
        }

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
                globalAuraBoost--;

            if (closingSequence > 150)
            {
                //remove all players
                sl.getEntities(
                        (Entity) null,
                        new AABB(origin).inflate(1000),
                        entity -> entity instanceof ServerPlayer
                ).forEach(o -> removePlayer((ServerPlayer) o));

                ServerLevel levelToReturn = sl.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, portalDimension));

                levelToReturn.getChunkSource()
                        .addTicketAndLoadWithRadius(TicketType.PLAYER_SPAWN, ChunkPos.containing(BlockPos.containing(portalPos.getCenter())), 2);

                if (levelToReturn.getBlockEntity(portalPos) instanceof PortalBlockEntity)
                    PortalBlockEntity.SCULK_SPREADER.addCursors(portalPos, 10);

                phase = Phase.FINISHED;
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
        TimelessData data = player.getData(ECDataAttachments.TIMELESS_DATA);

        ServerLevel sl = player.level();

        //save overworld inventory and swap to timeless inventory
        List<MaybeStack> inventory = data.inventory();
        List<MaybeStack> list = new ArrayList<>();
        for (int i = 0; i < 100; i++)
        {
            list.add(new MaybeStack(player.getInventory().getItem(i)));

            if (inventory.size() > i)
                player.getInventory().setItem(i, inventory.get(i).toStack());
        }

        //make transition or use respawn point if no pos/dim is set
        ServerLevel level = sl.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, portalDimension));
        TeleportTransition trans = new TeleportTransition(level,
                portalPos.getCenter().add(0, 2, 0),
                Vec3.ZERO,
                0,
                0,
                Utils::nothing
        );

        //store overworld inventory
        player.setData(ECDataAttachments.TIMELESS_DATA, new TimelessData(-1, list));

        //teleport player to timeless
        player.teleport(trans);
    }

    public void addPlayer(ServerPlayer player, BlockPos portalPos, Identifier portalDimension, boolean swapInventory)
    {
        //get timeless server level
        ServerLevel sl = player.level().getServer().getLevel(Echoes.TIMELESS);

        //load dimension + structures
        attemptLoad(sl, portalPos, portalDimension, StructureType.SCULK);

        //if swap inventory (blacksmiths don't!)
        if (swapInventory)
        {
            //save overworld inventory and swap to timeless inventory
            List<MaybeStack> inventory = player.getData(ECDataAttachments.TIMELESS_DATA).inventory();
            List<MaybeStack> list = new ArrayList<>();
            for (int i = 0; i < 100; i++)
            {
                list.add(new MaybeStack(player.getInventory().getItem(i)));

                if (inventory.size() > i)
                    player.getInventory().setItem(i, inventory.get(i).toStack());
                else
                    player.getInventory().setItem(i, ItemStack.EMPTY);
            }

            //store overworld inventory
            player.setData(ECDataAttachments.TIMELESS_DATA, new TimelessData(lastsUntil, list));
        }

        //make transition
        TeleportTransition trans = new TeleportTransition(sl,
                new Vec3(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ()),
                Vec3.ZERO,
                -90,
                0,
                Utils::nothing
        );

        //teleport player to timeless
        player.teleport(trans);

        //remove has_lantern just in case
        player.removeData(ECDataAttachments.HAS_LANTERN);
    }

    public void flipBlock(ServerLevel sl, BlockPos bp)
    {
        BlockState currentState = sl.getBlockState(bp);
        BlockState storedState = STORED_STATES.getOrDefault(bp, Blocks.AIR.defaultBlockState());

        //if block is part of skips tag, set flipped (to prevent flip attempt every tick) but don't swap states
        if (currentState.is(ECTags.SKIPS_SCULK_TRANSFORMATION))
        {
            if (FLIPPED_BLOCKS.contains(bp))
                FLIPPED_BLOCKS.remove(bp);
            else
                FLIPPED_BLOCKS.add(bp);
            return;
        }

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

    public enum StructureType
    {
        SCULK,
        GLEEMSLATE,
        BLACKSMITH;

        public boolean isBase()
        {
            return this.equals(SCULK) || this.equals(GLEEMSLATE);
        }
    }

    private void spawnStructure(ServerLevel sl, StructureType structureType)
    {
        //get template structure path
        Identifier template;
        if (structureType.isBase())
            template = ECDataEntries.STRUCTURE_ENTRIES.get().stream().findAny().orElse(Echoes.MISSINGNO);
        else
            template = ECDataEntries.BLACKSMITHS.get().get(sl.getRandom().nextInt(ECDataEntries.BLACKSMITHS.get().size()));

        StructureTemplateManager manager = sl.getStructureManager();
        StructurePlaceSettings placeSettings = new StructurePlaceSettings().setKnownShape(false);


        //todo check what radius in the ticket means
        //todo test with the neoforge chunk load event which chunks get loaded when this is called?

        if (structureType.equals(StructureType.BLACKSMITH))
            template = template.withSuffix("/blacksmith_");

        if (structureType.equals(StructureType.SCULK))
            template = template.withSuffix("/sculk_");

        if (structureType.equals(StructureType.GLEEMSLATE))
            template = template.withSuffix("/gleemslate_");

        for (int i = 0; i < 24; i++)
        {
            String letter = String.valueOf((char) ('a' + i));

            //if "structure_`letter``i`" doesn't exist, break
            //Optional<StructureTemplate> stt = manager.get(template.withSuffix("_").withSuffix(letter + "1"));
            //if (stt.isEmpty())
            //    break;

            for (int j = 0; j < 24; j++)
            {
                Identifier id = template.withSuffix(letter + (j + 1));
                Optional<StructureTemplate> st = manager.get(id);

                //if "structure_`letter``i`" doesn't exist, break
                if (st.isEmpty())
                    continue;
                    //break;

                BlockPos placementBP = origin.offset(j * 48, 0, i * 48);
                st.get().placeInWorld(sl, placementBP, origin,
                        placeSettings, StructureBlockEntity.createRandom(0), 2 | (0));

                //if sculk, store blocks and replace special blocks
                if (structureType.equals(StructureType.SCULK) || structureType.equals(StructureType.BLACKSMITH))
                {
                    for (int x = placementBP.getX(); x < placementBP.getX() + 48; x++)
                    {
                        for (int y = placementBP.getY(); y < placementBP.getY() + 48; y++)
                        {
                            for (int z = placementBP.getZ(); z < placementBP.getZ() + 48; z++)
                            {
                                BlockPos bpToStore = new BlockPos(x, y, z);
                                BlockState stateToSave = sl.getBlockState(bpToStore);

                                if (stateToSave.is(Blocks.DIAMOND_BLOCK))
                                {
                                    spawnPoint = bpToStore;
                                    stateToSave = Blocks.AIR.defaultBlockState();

                                    //skip rest of looping if it's a blacksmith
                                    if (structureType.equals(StructureType.BLACKSMITH))
                                        return;
                                }

                                //skip to next block if it's a blacksmith
                                if (structureType.equals(StructureType.BLACKSMITH))
                                    break;

                                //if sculk, check for sculk heart entity
                                if (stateToSave.is(Blocks.GOLD_BLOCK))
                                {
                                    SculkHeartEntity heart = ECEntities.SCULK_HEART.get().create(sl, EntitySpawnReason.TRIGGERED);
                                    heart.snapTo(bpToStore.getCenter().x, bpToStore.getCenter().y, bpToStore.getCenter().z);
                                    heart.setInstance(this);
                                    this.heart = heart;
                                    sl.addFreshEntityWithPassengers(heart);
                                    stateToSave = Blocks.AIR.defaultBlockState();
                                }

                                //if sculk save state
                                if (!stateToSave.isEmpty())
                                    STORED_STATES.put(bpToStore, stateToSave);

                                //if sculk set to air if not part of skip tag
                                if (!stateToSave.is(ECTags.SKIPS_SCULK_TRANSFORMATION))
                                    sl.setBlock(bpToStore, Blocks.AIR.defaultBlockState(), 0);
                            }
                        }
                    }
                }
            }
        }

        //if no diamond block found in BLACKSMITH or SCULK structure types
        if (spawnPoint.equals(BlockPos.ZERO))
            throw new IllegalStateException("Spawn point (diamond block) not found when loading instance " + uuid);
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
