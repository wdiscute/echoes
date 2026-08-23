package com.wdiscute.echoes.timeless;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.echoes.ECConfig;
import com.wdiscute.echoes.ECTags;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.SculkAura;
import com.wdiscute.echoes.blocks.portal.PortalBlock;
import com.wdiscute.echoes.blocks.portal.PortalBlockEntity;
import com.wdiscute.echoes.entity.lantern.LanternEntity;
import com.wdiscute.echoes.network.ECCBAddLootPayload;
import com.wdiscute.echoes.network.ECCBSetLootPayload;
import com.wdiscute.echoes.network.ECCBPlaySoundPayload;
import com.wdiscute.echoes.registry.ECBlocks;
import com.wdiscute.echoes.registry.ECDataAttachments;
import com.wdiscute.echoes.entity.heart.SculkHeartEntity;
import com.wdiscute.echoes.registry.ECParticles;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.StringRepresentableAutoForEnums;
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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.*;

public class TimelessInstance
{
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

    public void setStage(int stage)
    {
        this.stage = stage;
    }

    public boolean shouldTick(ServerLevel sl)
    {
        //only tick if ongoing of ending sequence
        return phase == Phase.ONGOING || phase == Phase.FINISHED;
    }

    public void attemptClose(ServerLevel sl)
    {
        //if hub
        if (isHub())
        {
            TimelessInstance maybeInstance = TimelessManager.getOrNull(sl.getServer(), linkedInstance);

            //if no players in isHub
            if (getPlayers(sl).isEmpty())
            {
                //if no linked instance, close
                if (maybeInstance == null)
                    close(sl);
                else
                    //if linked instance, delegate closing to the instance
                    maybeInstance.attemptClose(sl);
            }
        }
        //if not hub
        else
        {
            //close if time expired
            if (timeToExit < sl.getGameTime())
            {
                close(sl);
                return;
            }

            //close if marked as finished (heart destroyed) and has no players
            if (getPlayers(sl).isEmpty())
                close(sl);
        }
    }

    private void close(ServerLevel sl)
    {
        //do not run if already closed
        if (phase == Phase.CLOSED) return;

        //remove players
        List<ServerPlayer> players = getPlayers(sl);
        players.forEach(this::removePlayer);

        phase = Phase.CLOSED;

        //set loot
        ServerLevel levelToReturn = sl.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, portalDimension));

        if (levelToReturn != null)
        {
            levelToReturn.getChunkSource().addTicketAndLoadWithRadius(TicketType.ENDER_PEARL, ChunkPos.containing(portalPos), 1);
            if (levelToReturn.getBlockEntity(portalPos) instanceof PortalBlockEntity pbe)
                pbe.setLooting(loot);
        }
    }

    public boolean isHub()
    {
        return stage == 0;
    }

    public void addLoot(ServerLevel sl, float lootCount)
    {
        //if luck allows for the extra .X% to give an extra drop
        //example 5.3f will give 5 rolls + 30% chance of extra
        float extraDrop = lootCount % 1;
        if (sl.getRandom().nextFloat() < extraDrop)
            lootCount++;

        for (int i = 0; i < lootCount; i++)
        {
            loot = new ArrayList<>(loot);

            TimelessLootEntry randomLoot = TimelessLootEntry.getRandomLoot(sl.getRandom(), stage);

            if (randomLoot != null)
            {
                //add random loot to instance loot
                loot.add(randomLoot.stack());

                //set notif to players
                getPlayers(sl).forEach(o -> PacketDistributor.sendToPlayer(o, new ECCBAddLootPayload(randomLoot.stack(), true)));
            }
        }
    }

    public enum Phase implements StringRepresentableAutoForEnums
    {
        NEW,
        ONGOING,
        FINISHED,
        CLOSED;

        public static final Codec<Phase> CODEC = StringRepresentable.fromEnum(Phase::values);
        public static final StreamCodec<FriendlyByteBuf, Phase> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(Phase.class);
    }

    //saved data
    public final UUID uuid;
    public final BlockPos origin;
    public BlockPos spawnPoint;
    public int direction;
    public Phase phase;
    public long timeToExit;
    public BlockPos portalPos;
    public Identifier portalDimension;
    public int stage;
    public UUID linkedInstance;
    public List<MaybeStack> loot;

    //converted to list for saving
    public final Map<BlockPos, BlockState> STORED_STATES = new HashMap<>();
    public final Set<BlockPos> FLIPPED_BLOCKS = new HashSet<>();

    //non saved
    public int closingSequence = -1;
    public float heartAuraBoost = 0;

    List<Pair<Vec3, Float>> auras = new ArrayList<>();
    List<Pair<Vec3, Float>> oldAuras = new ArrayList<>();
    List<Utils.Trio<Vec3, Float, Float>> rings = new ArrayList<>();

    public void attemptLoad(ServerPlayer player, ServerLevel sl, BlockPos portalPos, Identifier portalDimension)
    {
        if (phase != Phase.NEW) return;
        TimelessData timelessData = player.getData(ECDataAttachments.TIMELESS_DATA);

        phase = Phase.ONGOING;

        if (stage == Integer.MIN_VALUE)
            stage = timelessData.maxStage();

        this.portalDimension = portalDimension;
        this.portalPos = portalPos;

        TimelessLevelEntry entry = spawnStructure(sl);

        //set time to exit to ticks from json + config
        this.timeToExit = sl.getGameTime() + (entry == null ? 1200 : entry.ticks()) + ECConfig.GLOBAL_EXTRA_TIMELESS_DURATION.get();

        //if first level, no time limit
        if (stage == -1)
            this.timeToExit = Long.MAX_VALUE;

        //if hub apply hub linking logic
        if (isHub())
        {
            //set outside portal link to the new isHub
            ServerLevel overworld = sl.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, portalDimension));
            if (overworld.getBlockEntity(portalPos) instanceof PortalBlockEntity pbe)
            {
                pbe.instanceUUID = uuid;
                pbe.setChanged();
            }

            setTime(sl, Long.MAX_VALUE);
            phase = Phase.FINISHED;
        }

        //spawnpoint blocks
        sl.setBlockAndUpdate(spawnPoint, Blocks.AIR.defaultBlockState());
        STORED_STATES.put(spawnPoint, Blocks.AIR.defaultBlockState());
        FLIPPED_BLOCKS.remove(spawnPoint);

        //set origin block for debug in dev
        if (!FMLLoader.getCurrent().isProduction())
            sl.setBlockAndUpdate(origin, Blocks.EMERALD_BLOCK.defaultBlockState());
    }

    public void onHeartHit(ServerLevel sl, SculkHeartEntity heartEntity)
    {
        //play sound
        sl.playSound(null, heartEntity.blockPosition(), SoundEvents.SCULK_BLOCK_PLACE, SoundSource.HOSTILE, 1f, 1f);

        //set to rain
        sl.setRainLevel(1);

        //remove timer
        setTime(sl, Long.MAX_VALUE);

        if (closingSequence == -1)
            closingSequence = 0;
    }

    public SculkHeartEntity getHeart(ServerLevel sl)
    {
        List<SculkHeartEntity> list = sl.getEntitiesOfClass(
                SculkHeartEntity.class,
                new AABB(origin).inflate(1000)
        );

        if (!list.isEmpty())
            return list.getFirst();

        return null;
    }

    public void tick(ServerLevel sl)
    {
        //add all entities with SculkAura aura w
        List<Entity> entitiesInInstance = sl.getEntities(
                (Entity) null,
                new AABB(origin).inflate(1000),
                Utils::alwaysTrue
        );

        //submit sculk auras
        entitiesInInstance.forEach(o ->
        {
            if (o instanceof SculkAura sculkAura)
            {
                if (o instanceof SculkHeartEntity)
                    submitAura(o.position(), sculkAura.getSculkAura(sl) + heartAuraBoost);

                int aura = (int) (sculkAura.getSculkAura(sl) + (heartAuraBoost < 0 ? heartAuraBoost : 0));
                if (aura != 0)
                    submitAura(o.position(), aura);
            }
        });

        //process auras
        processAuras(sl);

        //process rings
        processRings(sl);

        //closing sequence logic
        if (closingSequence != -1)
        {
            sl.setRainLevel(sl.getRainLevel(0) + 0.02f);

            //increase closing sequence
            closingSequence++;

            if (closingSequence < 15)
                heartAuraBoost++;

            SculkHeartEntity heart = getHeart(sl);
            if (heart != null)
            {
                if (closingSequence == 1)
                    PacketDistributor.sendToPlayersNear(
                            sl, null, heart.getX(), heart.getY(), heart.getZ(), 1000,
                            new ECCBPlaySoundPayload("shriek", 1, 1));

                if (closingSequence == 20)
                    PacketDistributor.sendToPlayersNear(
                            sl, null, heart.getX(), heart.getY(), heart.getZ(), 1000,
                            new ECCBPlaySoundPayload("shriek", 0.4f, 0.8f));

                if (closingSequence == 40)
                    PacketDistributor.sendToPlayersNear(
                            sl, null, heart.getX(), heart.getY(), heart.getZ(), 1000,
                            new ECCBPlaySoundPayload("shriek", 1, 1.3f));

                if (closingSequence == 60)
                    PacketDistributor.sendToPlayersNear(
                            sl, null, heart.getX(), heart.getY(), heart.getZ(), 1000,
                            new ECCBPlaySoundPayload("shriek", 0.3f, 1f));

                if (closingSequence == 80)
                    PacketDistributor.sendToPlayersNear(
                            sl, null, heart.getX(), heart.getY(), heart.getZ(), 1000,
                            new ECCBPlaySoundPayload("shriek", 1, 0.4f));

                if (closingSequence == 100)
                    PacketDistributor.sendToPlayersNear(
                            sl, null, heart.getX(), heart.getY(), heart.getZ(), 1000,
                            new ECCBPlaySoundPayload("shriek", 1, 0.5f));

                if (closingSequence == 70)
                    PacketDistributor.sendToPlayersNear(
                            sl, null, heart.getX(), heart.getY(), heart.getZ(), 1000,
                            new ECCBPlaySoundPayload("beacon_deactivate", 1, 1f));

                if (closingSequence == 70)
                    submitRing(heart.position(), 30, -1);

                if (closingSequence == 90)
                    submitRing(heart.position(), 30, -1);

                if (closingSequence == 90)
                {
                    PacketDistributor.sendToPlayersNear(
                            sl, null, heart.getX(), heart.getY(), heart.getZ(), 1000,
                            new ECCBPlaySoundPayload("beacon_deactivate", 1, 1f));

                    PacketDistributor.sendToPlayersNear(
                            sl, null, heart.getX(), heart.getY(), heart.getZ(), 1000,
                            new ECCBPlaySoundPayload("beacon_activate", 1, 1f));
                }

                if (closingSequence > 115)
                    heartAuraBoost--;

                if (closingSequence == 130)
                {
                    //place portal
                    BlockState blockState = ECBlocks.PORTAL.get().defaultBlockState();
                    blockState = blockState.trySetValue(PortalBlock.STATE, PortalBlock.State.OPEN);
                    sl.setBlockAndUpdate(heart.blockPosition(), blockState);
                }

                if (closingSequence == 140)
                {
                    //sets phase to finishing (not closed, so it doest get removed)
                    phase = Phase.FINISHED;

                    entitiesInInstance.forEach(o ->
                    {
                        if (o instanceof LanternEntity)
                            o.remove(Entity.RemovalReason.DISCARDED);
                    });

                    //remove heard
                    heart.remove(Entity.RemovalReason.DISCARDED);
                }
            }

            if (closingSequence > 160)
                closingSequence = -1;
        }
    }

    private long getTimeToExit(ServerLevel sl)
    {
        if (isHub())
        {
            TimelessInstance maybeInstance = TimelessManager.getOrNull(sl.getServer(), linkedInstance);
            if (maybeInstance == null)
                return timeToExit;
            else
                return maybeInstance.getTimeToExit(sl);
        }
        return timeToExit;
    }

    public void setTime(ServerLevel sl, long ticks)
    {
        timeToExit = ticks;
        getPlayers(sl).forEach(o -> TimelessData.setTimeToExit(o, timeToExit));
    }

    public void addTime(ServerLevel sl, long ticks)
    {
        timeToExit = timeToExit + ticks;
        getPlayers(sl).forEach(o -> TimelessData.setTimeToExit(o, timeToExit));
    }

    public List<ServerPlayer> getPlayers(ServerLevel sl)
    {
        //get all players within 1000 blocks from origin
        return sl.getPlayers(o -> origin.distManhattan(o.blockPosition()) < 1000);
    }

    private void processRings(ServerLevel sl)
    {
        List<Utils.Trio<Vec3, Float, Float>> newRings = new ArrayList<>();

        for (Utils.Trio<Vec3, Float, Float> ring : rings)
        {
            if (ring.second() < 120)
            {
                newRings.add(new Utils.Trio<>(ring.first(), ring.second() + ring.third(), ring.third()));
                List<BlockPos> blockPos = RingCache.get(ring.second().intValue());
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

        //add regen
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20, 5));

        //client loot
        PacketDistributor.sendToPlayer(player, new ECCBSetLootPayload(List.of()));

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
        if (level == null)
            level = sl.getServer().getLevel(Level.OVERWORLD);

        //load dimension to return to
        level.getChunkSource().addTicketAndLoadWithRadius(TicketType.ENDER_PEARL, ChunkPos.containing(portalPos), 1);

        float x = sl.getRandom().nextFloat() / 2 - 0.5f;
        float z = sl.getRandom().nextFloat() / 2 - 0.5f;
        TeleportTransition trans = new TeleportTransition(level,
                portalPos.getCenter().add(x > 0 ? x + 1 : x - 1, 2, z > 0 ? z + 1 : z - 1),
                new Vec3(x, sl.getRandom().nextFloat() / 2, z),
                0,
                0,
                Utils::nothing
        );

        //store overworld inventory
        TimelessData.setInventory(player, list);

        //set time to exit so it doesn't render on gui
        TimelessData.setTimeToExit(player, Long.MAX_VALUE);

        //set currentStage to
        TimelessData.setCurrentStage(player, stage);
        TimelessData.setMaxStage(player, stage);

        //teleport player to timeless
        player.teleport(trans);
    }

    public void addPlayer(ServerPlayer player, BlockPos portalPos, Identifier portalDimension)
    {
        //get timeless server level
        ServerLevel sl = player.level().getServer().getLevel(Echoes.TIMELESS);

        //load dimension + structures
        attemptLoad(player, sl, portalPos, portalDimension);

        //if player is not on timeless, swap inventory
        if (!player.level().dimension().equals(Echoes.TIMELESS))
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
            TimelessData.setInventory(player, list);
        }
        else
        {
            //add loot from current instance to next instance, in case 2 players enter new instance after slower player killed a mob
            TimelessInstance closest = TimelessManager.getClosest(sl.getServer(), player.blockPosition());
            if (closest != null)
            {
                loot = new ArrayList<>(loot);
                loot.addAll(closest.loot);
                closest.loot = List.of();
            }
        }

        //set client loot to instance loot
        PacketDistributor.sendToPlayer(player, new ECCBSetLootPayload(loot));

        //set time to exit
        TimelessData.setTimeToExit(player, getTimeToExit(sl));

        //set currentStage
        TimelessData.setCurrentStage(player, stage);

        //set maxStage
        TimelessData.setMaxStage(player, stage);

        //remove has_lantern just in case
        player.removeData(ECDataAttachments.HAS_LANTERN);

        //make transition
        TeleportTransition trans = new TeleportTransition(sl,
                new Vec3(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ()),
                Vec3.ZERO,
                direction * 90 + 180,
                0,
                Utils::nothing
        );

        //teleport player to timeless
        player.teleport(trans);
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

        sl.setBlock(bp, storedState, FLAGS);
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

        sl.setBlock(bp, storedState, FLAGS);
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
        //do not process auras in isHub
        if (isHub()) return;

        //spawn particles
        auras.forEach(o ->
        {
            Vec3 pos = o.getFirst();
            float size = o.getSecond() / 3;

            sl.sendParticles(ECParticles.SCULK.get(), false, true,
                    pos.x, pos.y, pos.z, (int) size,
                    size, size, size, 0);
        });

        //don't run if auras haven't changed
        if (auras.equals(oldAuras) && rings.isEmpty())
        {
            auras.clear();
            return;
        }

        Set<BlockPos> currentInAura = new HashSet<>();

        //add all blocks that should be in aura
        for (var aura : auras)
        {
            int size = aura.getSecond().intValue();

            List<BlockPos> blockPos = SphereCache.get(size);

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
        oldAuras.addAll(auras);
        auras.clear();
    }

    public void submitAura(Vec3 pos, float radius)
    {
        auras.add(Pair.of(pos, radius));
    }

    private TimelessLevelEntry spawnStructure(ServerLevel sl)
    {
        //get template structure path
        TimelessLevelEntry randomLevel = TimelessLevelEntry.getRandomLevel(sl, stage);

        if (randomLevel == null)
            return null;

        Identifier template = randomLevel.id();

        //actually spawn structure
        doSpawnStructure(sl, template.withSuffix("/sculk_"), true);
        doSpawnStructure(sl, template.withSuffix("/prisma_"), false);
        return randomLevel;
    }

    private void doSpawnStructure(ServerLevel sl, Identifier template, boolean sculk)
    {
        StructureTemplateManager manager = sl.getStructureManager();
        StructurePlaceSettings placeSettings = new StructurePlaceSettings().setKnownShape(false);

        for (int i = 0; i < 26; i++)
        {
            String letter = String.valueOf((char) ('a' + i));

            for (int j = 0; j < 9; j++)
            {
                for (int k = 0; k < 9; k++)
                {
                    //from a00 to k99
                    Identifier id = template.withSuffix(letter + (j + 1) + (k + 1));
                    Optional<StructureTemplate> st = manager.get(id);

                    //if "structure_`letter``i`" doesn't exist, skip to next
                    if (st.isEmpty())
                        break;

                    BlockPos placementBP = origin.offset(j * 48, k * 48, i * 48);
                    st.get().placeInWorld(sl, placementBP, origin,
                            placeSettings, StructureBlockEntity.createRandom(0), 2 | (0));

                    //run processors for each block in structure bounds
                    for (int x = placementBP.getX(); x < placementBP.getX() + 48; x++)
                        for (int y = placementBP.getY(); y < placementBP.getY() + 48; y++)
                            for (int z = placementBP.getZ(); z < placementBP.getZ() + 48; z++)
                                TimelessProcessor.process(this, sl, sl.getBlockState(new BlockPos(x, y, z)), new BlockPos(x, y, z), sculk);
                }
            }
        }
    }

    public static final class RingCache
    {
        private static final Map<Integer, List<BlockPos>> CACHE = new HashMap<>();

        public static void init(int maxRadius)
        {
            for (int r = 1; r <= maxRadius; r++)
                CACHE.put(r, buildRing(r));
        }

        public static List<BlockPos> get(int radius)
        {
            return CACHE.computeIfAbsent(radius, RingCache::buildRing);
        }

        private static List<BlockPos> buildRing(int radius)
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

    public static final class SphereCache
    {
        private static final Map<Integer, List<BlockPos>> CACHE = new HashMap<>();

        public static void init(int maxRadius)
        {
            CACHE.put(0, List.of());
            for (int r = 1; r <= maxRadius; r++)
                CACHE.put(r, buildSphereShell(r));
        }

        public static List<BlockPos> get(int radius)
        {
            List<BlockPos> blockPos = CACHE.get(Math.max(0, radius));
            if (blockPos == null)
                throw new IllegalStateException("radius " + radius + " not cached");
            return blockPos;
        }

        private static List<BlockPos> buildSphereShell(int radius)
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

    public static final int FLAGS =
            Block.UPDATE_CLIENTS
            | Block.UPDATE_KNOWN_SHAPE
            | Block.UPDATE_SUPPRESS_DROPS
            | Block.UPDATE_MOVE_BY_PISTON
            | Block.UPDATE_SKIP_BLOCK_ENTITY_SIDEEFFECTS;

    public TimelessInstance(UUID uuid,
                            BlockPos origin, BlockPos spawnPoint,
                            int direction,
                            Phase phase,
                            List<Utils.Duo<BlockPos, BlockState>> storedStates,
                            List<BlockPos> flippedStates,
                            long lastsUntil,
                            Identifier portalDimension,
                            BlockPos portalPos,
                            int stage,
                            UUID nextInstance,
                            List<MaybeStack> loot
    )
    {
        this.uuid = uuid;
        this.origin = origin;
        this.spawnPoint = spawnPoint;
        this.direction = direction;
        this.phase = phase;
        this.timeToExit = lastsUntil;
        this.portalDimension = portalDimension;
        this.portalPos = portalPos;
        this.stage = stage;
        this.linkedInstance = nextInstance;
        storedStates.forEach(o -> STORED_STATES.put(o.first(), o.second()));
        FLIPPED_BLOCKS.addAll(flippedStates);
        this.loot = loot;
    }

    public static TimelessInstance create(UUID uuid)
    {
        BlockPos origin = new BlockPos(Utils.r.nextInt(50000000 / 2), 100, Utils.r.nextInt(50000000 / 2));
        return new TimelessInstance(
                uuid,
                origin,
                origin,
                0,
                Phase.NEW,
                List.of(),
                List.of(),
                Long.MAX_VALUE,
                Echoes.MISSINGNO,
                BlockPos.ZERO,
                Integer.MIN_VALUE,
                UUID.randomUUID(),
                List.of()
        );
    }

    public static final Codec<TimelessInstance> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    UUIDUtil.CODEC.fieldOf("uuid").forGetter(o -> o.uuid),
                    BlockPos.CODEC.fieldOf("origin").forGetter(o -> o.origin),
                    BlockPos.CODEC.optionalFieldOf("spawn_point", BlockPos.ZERO).forGetter(o -> o.spawnPoint),
                    Codec.INT.optionalFieldOf("direction", 0).forGetter(o -> o.direction),
                    Phase.CODEC.fieldOf("phase").forGetter(o -> o.phase),
                    Utils.Duo.codec(BlockPos.CODEC, BlockState.CODEC).listOf().fieldOf("stored_states").forGetter(TimelessInstance::getSculkBlocks),
                    BlockPos.CODEC.listOf().fieldOf("flipped_blocks").forGetter(TimelessInstance::getFlippedBlocks),
                    Codec.LONG.optionalFieldOf("lasts_until", Long.MAX_VALUE).forGetter(o -> o.timeToExit),
                    Identifier.CODEC.fieldOf("portal_dim").forGetter(o -> o.portalDimension),
                    BlockPos.CODEC.optionalFieldOf("portal_pos", BlockPos.ZERO).forGetter(o -> o.portalPos),
                    Codec.INT.fieldOf("currentStage").forGetter(o -> o.stage),
                    UUIDUtil.CODEC.fieldOf("next_instance").forGetter(o -> o.linkedInstance),
                    MaybeStack.CODEC.listOf().fieldOf("loot").forGetter(o -> o.loot)
            ).apply(instance, TimelessInstance::new));
}
