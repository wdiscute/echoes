package com.wdiscute.echoes.blocks.portal;

import com.mojang.serialization.Codec;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.registry.ECDataAttachments;
import com.wdiscute.echoes.registry.ECParticles;
import com.wdiscute.echoes.timeless.TimelessData;
import com.wdiscute.echoes.timeless.TimelessManager;
import com.wdiscute.echoes.timeless.TimelessInstance;
import com.wdiscute.echoes.registry.ECBlockEntities;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SculkSpreader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class PortalBlockEntity extends BlockEntity implements TickableBlockEntity
{
    public static final SculkSpreader SCULK_SPREADER = SculkSpreader.createLevelSpreader();

    List<MaybeStack> loot = new ArrayList<>();
    int lootCooldown = 0;

    public PortalBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
        super(ECBlockEntities.PORTAL.get(), worldPosition, blockState);
    }

    public UUID instanceUUID = UUID.randomUUID();
    public static Set<BlockPos> portals = new HashSet<>();

    public static BlockPos getClosestOpenPortal(BlockPos pos)
    {
        float closest = 100000;
        BlockPos closestBP = BlockPos.ZERO;

        for (BlockPos portal : portals)
        {
            int maybeNewDistance = pos.distManhattan(portal);

            if (maybeNewDistance < closest)
            {
                closest = maybeNewDistance;
                closestBP = portal;
            }
        }

        return closestBP;
    }

    @Override
    public void tickClient(Level level, BlockPos pos, BlockState state)
    {
        TickableBlockEntity.super.tickClient(level, pos, state);

        if (state.getValueOrElse(PortalBlock.STATE, PortalBlock.State.CLOSED).equals(PortalBlock.State.OPEN))
            portals.add(pos);
        else
            portals.remove(pos);

        //if not closed, do portal particles and sound
        if (!state.getValueOrElse(PortalBlock.STATE, PortalBlock.State.CLOSED).equals(PortalBlock.State.CLOSED))
        {
            RandomSource random = level.getRandom();

            //particles
            level.addParticle(ParticleTypes.END_ROD, false, true,
                    pos.getX() + 0.5d, pos.getY() + 1.7d + random.nextFloat(), pos.getZ() + 0.5d,
                    0f, 0f, 0f);

            //sculk particles close
            if (random.nextFloat() > 0.8f)
                level.addParticle(ECParticles.SCULK.get(), false, true,
                        (double) pos.getX() + random.nextFloat(), (double) pos.getY() + 1.3d + random.nextFloat() * 2, (double) pos.getZ() + random.nextFloat(),
                        0f, 0f, 0f);

            //sculk particles far
            if (random.nextFloat() > 0.8f)
                level.addParticle(ECParticles.SCULK.get(),
                        (double) pos.getX() + random.nextInt(8) - 4 + random.nextFloat(),
                        (double) pos.getY() + random.nextInt(3) - 1.5f + 1.3d + random.nextFloat() * 2,
                        (double) pos.getZ() + random.nextInt(8) - 4 + random.nextFloat(), 0, 0, 0);


            if (random.nextFloat() > 0.9f)
                level.addParticle(ParticleTypes.SCULK_SOUL, false, true,
                        (double) pos.getX() + random.nextFloat(), (double) pos.getY() + 1.1d + random.nextFloat() / 10, (double) pos.getZ() + random.nextFloat(),
                        0f, 0f, 0f);

            Vec3 center = pos.getCenter();

            //sounds
            if (level.getRandom().nextFloat() > 0.99f)
                level.playLocalSound(center.x, center.y, center.z,
                        SoundEvents.SCULK_BLOCK_SPREAD, SoundSource.BLOCKS, 1, 0.3f, false);

            if (level.getRandom().nextFloat() > 0.99f)
                level.playLocalSound(center.x, center.y, center.z,
                        SoundEvents.SCULK_BLOCK_CHARGE, SoundSource.BLOCKS, 1, 1f, false);

            if (level.getRandom().nextFloat() > 0.8f && state.getValueOrElse(PortalBlock.STATE, PortalBlock.State.CLOSED).equals(PortalBlock.State.OPEN))
                level.playLocalSound(center.x, center.y, center.z,
                        SoundEvents.BEACON_AMBIENT, SoundSource.BLOCKS, 1f, 0.3f, false);

        }
    }

    public void setLooting(List<MaybeStack> loot)
    {
        level.setBlockAndUpdate(getBlockPos(), getBlockState().trySetValue(PortalBlock.STATE, PortalBlock.State.LOOTING));

        //add to loot (add instead of setting so hub loot doesn't replace the current loot when closing instances
        this.loot = new ArrayList<>(this.loot);
        this.loot.addAll(loot);

        setChanged();

        lootCooldown = 60;
    }

    @Override
    public void tickServer(ServerLevel sl, BlockPos pos, BlockState state)
    {
        TickableBlockEntity.super.tickServer(sl, pos, state);

        SCULK_SPREADER.updateCursors(sl, pos, sl.getRandom(), true);

        if (state.getValueOrElse(PortalBlock.STATE, PortalBlock.State.CLOSED).equals(PortalBlock.State.LOOTING))
        {
            BlockPos bp = getBlockPos();
            if (loot == null || loot.isEmpty())
            {
                sl.setBlockAndUpdate(bp, getBlockState().trySetValue(PortalBlock.STATE, PortalBlock.State.OPEN));
                sl.playSound(null, bp, SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS);
                return;
            }

            lootCooldown--;

            if (lootCooldown > 10 && sl.getRandom().nextFloat() > 0.9)
            {
                Vec3 center = bp.above().getCenter();
                sl.sendParticles(ParticleTypes.EXPLOSION, center.x, center.y, center.z,
                        1, 0, 0, 0, 0);

                sl.playSound(null, bp.above(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 0.3f, 0.3f);
            }

            //drop item
            if (lootCooldown == 0)
            {
                Vec3 itemPos = Vec3.atLowerCornerWithOffset(pos, 0.5, 2, 0.5).add(0, sl.getRandom().nextFloat() / 2, 0);
                ItemEntity entity = new ItemEntity(sl, itemPos.x(), itemPos.y(), itemPos.z(), loot.getFirst().toStack());
                entity.setDefaultPickUpDelay();
                sl.addFreshEntity(entity);
                lootCooldown = Math.max(1, (int) Math.round(10.0 / Math.sqrt(loot.size())));
                loot.removeFirst();
            }
        }

        //if portal is not open
        if (!state.getValueOrElse(PortalBlock.STATE, PortalBlock.State.CLOSED).equals(PortalBlock.State.OPEN)) return;

        for (ServerPlayer player : sl.getEntitiesOfClass(Player.class, new AABB(pos.above().above()))
                .stream()
                .filter(o -> !o.isSpectator())
                .map(o -> ((ServerPlayer) o))
                .toList())
        {
            int maxStage = player.getData(ECDataAttachments.TIMELESS_DATA).maxStage();

            //if player on timeless
            MinecraftServer server = sl.getServer();
            if (player.level().dimension().equals(Echoes.TIMELESS))
            {
                TimelessInstance currentInstance = TimelessManager.getClosest(server, player.blockPosition());
                if (currentInstance == null)
                    throw new IllegalStateException("player in timeless tried to use    a portal but there's no instances active at all. player should not be in timeless.");

                //if on hub
                if(currentInstance.isHub())
                {
                    //end hub
                    currentInstance.depth = 0;
                    currentInstance.phase = TimelessInstance.Phase.FINISHED;

                    //get linked
                    TimelessInstance linked = TimelessManager.getOrCreate(server, currentInstance.linkedInstance);

                    //if linked has no stage
                    if(linked.depth == Integer.MIN_VALUE)
                        linked.depth = maxStage;

                    //linked instance (non-hub) should link with hub
                    linked.linkedInstance = currentInstance.uuid;

                    linked.addPlayer(player, currentInstance.portalPos, currentInstance.portalDimension);
                }
                //if not on hub
                else
                {
                    //if is in tutorial
                    if(currentInstance.depth == -1)
                    {
                        TimelessInstance hub = TimelessManager.getOrCreate(server, currentInstance.linkedInstance);
                        hub.setDepth(0);
                        hub.portalDimension = currentInstance.portalDimension;
                        hub.portalPos = currentInstance.portalPos;
                        hub.addPlayer(player, currentInstance.portalPos, currentInstance.portalDimension);
                    }
                    //not in tutorial
                    else
                    {
                        TimelessInstance hub = TimelessManager.getOrNull(server, currentInstance.linkedInstance);

                        //if hub not linked (something went wrong!)
                        if(hub == null)
                        {
                            currentInstance.removePlayer(player);
                            return;
                        }

                        TimelessInstance nextInstance;
                        //get next instance
                        {
                            //if hub is linked and hub's link is not where you're already at, set nextInstance to hub's link
                            //this only happens if 2 players are in the same "ground" and one player jumps 2 levels ahead
                            if(hub.isHub() && hub.linkedInstance != currentInstance.uuid)
                                nextInstance = TimelessManager.getOrCreate(server, hub.linkedInstance);
                            else
                                nextInstance = TimelessManager.getOrCreate(server, UUID.randomUUID());
                        }

                        //if nextInstance has no stage
                        if(nextInstance.depth == Integer.MIN_VALUE)
                            nextInstance.depth = maxStage + 1;

                        hub.linkedInstance = nextInstance.uuid;
                        nextInstance.linkedInstance = hub.uuid;

                        TimelessData.increaseStageCount(player, currentInstance.structure);

                        //add player to next instance
                        nextInstance.addPlayer(player, currentInstance.portalPos, currentInstance.portalDimension);

                        //add spectators to new instance
                        currentInstance.getPlayers(sl)
                                .stream()
                                .filter(Player::isSpectator)
                                .forEach(o ->
                                {
                                    //set to survival
                                    o.setGameMode(GameType.SURVIVAL);
                                    nextInstance.addPlayer(o, currentInstance.portalPos, currentInstance.portalDimension);
                                });
                    }
                }
            }
            //if player not on timeless, teleport to either ongoing hub, new hub, or tutorial level
            else
            {
                TimelessInstance hub = TimelessManager.getOrCreate(server, instanceUUID);
                //set stage to either 0 (hub) or -1 if player never reached hub
                hub.depth = Math.min(0, player.getData(ECDataAttachments.TIMELESS_DATA).maxStage());
                //add player to either current ongoing instance or make a new one
                hub.addPlayer(player, pos, sl.dimension().identifier());
            }
        }
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);

        portals.remove(pos);

        if (instanceUUID != null && !level.isClientSide())
            TimelessManager.remove(level.getServer(), instanceUUID);
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        if (instanceUUID != null)
            output.store("instance_uuid", Codec.STRING, instanceUUID.toString());

        if (loot != null && !loot.isEmpty())
            output.store("loot", MaybeStack.CODEC.listOf(), loot);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        instanceUUID = input.read("instance_uuid", Codec.STRING).map(UUID::fromString).orElseGet(UUID::randomUUID);

        loot = new ArrayList<>(input.read("loot", MaybeStack.CODEC.listOf()).orElse(List.of()));
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state)
    {
        TickableBlockEntity.super.tick(level, pos, state);
    }
}