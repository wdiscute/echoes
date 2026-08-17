package com.wdiscute.echoes.blocks.portal;

import com.mojang.serialization.Codec;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.registry.ECParticles;
import com.wdiscute.echoes.timeless.TimelessManager;
import com.wdiscute.echoes.timeless.TimelessInstance;
import com.wdiscute.echoes.registry.ECBlockEntities;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
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

    List<MaybeStack> loot;
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

        //filter out empty MaybeStack's
        this.loot = new ArrayList<>(loot.stream().filter(o -> !o.isEmpty()).toList());

        lootCooldown = 60;
    }

    @Override
    public void tickServer(ServerLevel level, BlockPos pos, BlockState state)
    {
        TickableBlockEntity.super.tickServer(level, pos, state);

        SCULK_SPREADER.updateCursors(level, pos, level.getRandom(), true);

        if (state.getValueOrElse(PortalBlock.STATE, PortalBlock.State.CLOSED).equals(PortalBlock.State.LOOTING))
        {
            BlockPos bp = getBlockPos();
            if (loot == null || loot.isEmpty())
            {
                level.setBlockAndUpdate(bp, getBlockState().trySetValue(PortalBlock.STATE, PortalBlock.State.CLOSED));
                level.playSound(null, bp, SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS);
                return;
            }

            lootCooldown--;

            if (lootCooldown > 10 && level.getRandom().nextFloat() > 0.9)
            {
                Vec3 center = bp.above().getCenter();
                level.sendParticles(ParticleTypes.EXPLOSION, center.x, center.y, center.z,
                        1, 0, 0, 0, 0);

                level.playSound(null, bp.above(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 0.3f, 0.3f);
            }

            //drop item twice per second
            if (lootCooldown == 0)
            {
                Vec3 itemPos = Vec3.atLowerCornerWithOffset(pos, 0.5, 2, 0.5).add(0, level.getRandom().nextFloat() / 2, 0);
                ItemEntity entity = new ItemEntity(level, itemPos.x(), itemPos.y(), itemPos.z(), loot.getFirst().toStack());
                entity.setDefaultPickUpDelay();
                level.addFreshEntity(entity);
                lootCooldown = 10;
                loot.removeFirst();
            }
        }

        //if portal is open
        if (state.getValueOrElse(PortalBlock.STATE, PortalBlock.State.CLOSED).equals(PortalBlock.State.OPEN))
        {
            for (ServerPlayer player : level.getEntitiesOfClass(Player.class, new AABB(pos.above().above())).stream().map(o -> ((ServerPlayer) o)).toList())
            {
                //if player on timeless
                if (player.level().dimension().equals(Echoes.TIMELESS))
                {
                    TimelessInstance currentInstance = TimelessManager.getClosest(level.getServer(), player.blockPosition());
                    if (currentInstance == null)
                        throw new IllegalStateException("player in timeless tried to use a portal but there's no instances active at all. player should not be in timeless.");

                    //if currently in a isHub
                    if (currentInstance.isHub())
                    {
                        //get new dungeon
                        TimelessInstance newDungeon = TimelessManager.getOrCreate(level.getServer(), currentInstance.linkedInstance);

                        //set linked in new dungeon to the isHub
                        newDungeon.linkedInstance = currentInstance.uuid;

                        //set new dungeon stage to isHub + 1
                        newDungeon.setStage(Math.max(currentInstance.stage + 1, newDungeon.stage));

                        //add player + generate dungeon
                        newDungeon.addPlayer(player, currentInstance.portalPos, currentInstance.portalDimension);
                    }
                    //if not in a isHub
                    else
                    {
                        //get isHub
                        TimelessInstance hub = TimelessManager.getOrNull(level.getServer(), currentInstance.linkedInstance);
                        TimelessInstance newDungeon;

                        //if isHub linked instance is the one youre already in, make new one and increase stage

                        //if isHub is not linked, make new dungeon from random uuid
                        if (hub == null)
                            newDungeon = TimelessManager.getOrCreate(level.getServer(), UUID.randomUUID());
                            //if isHub is linked, teleport to linked instance
                        else
                        {
                            //if player is already on the isHub linked instance, make new one
                            if (hub.linkedInstance.equals(currentInstance.uuid))
                            {
                                newDungeon = TimelessManager.getOrCreate(level.getServer(), UUID.randomUUID());
                                hub.linkedInstance = newDungeon.uuid;
                            }
                            else
                                newDungeon = TimelessManager.getOrCreate(level.getServer(), hub.linkedInstance);
                        }

                        //set stage
                        newDungeon.setStage(Math.max(currentInstance.stage + 1, newDungeon.stage));

                        //if new dungeon is not isHub
                        if (!newDungeon.isHub())
                            //set linked to the isHub (currentInstance.linkedInstance holds the isHub)
                            newDungeon.linkedInstance = currentInstance.linkedInstance;


                        //add player
                        newDungeon.addPlayer(player, currentInstance.portalPos, currentInstance.portalDimension);
                    }

                    //set current instance to finished
                    currentInstance.phase = TimelessInstance.Phase.FINISHED;
                }
                //if player not on timeless
                else
                {
                    //add player to either current ongoing instance or make a new one
                    TimelessManager.getOrCreate(level.getServer(), instanceUUID).addPlayer(player, pos, level.dimension().identifier());
                }
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