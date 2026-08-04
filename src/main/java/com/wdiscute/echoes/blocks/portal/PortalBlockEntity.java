package com.wdiscute.echoes.blocks.portal;

import com.mojang.serialization.Codec;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.timeless.TimelessHandler;
import com.wdiscute.echoes.timeless.TimelessInstance;
import com.wdiscute.echoes.registry.ECBlockEntities;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
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

    UUID instanceUUID;
    public static Set<BlockPos> portals = new HashSet<>();

    public static BlockPos getClosestOpenPortal(BlockPos pos)
    {
        float closest = 100000;
        BlockPos closestBP = BlockPos.ZERO;

        for (BlockPos portal : portals)
        {
            int maybeNewDistance = pos.distManhattan(portal);

            if(maybeNewDistance < closest)
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

            if (random.nextFloat() > 0f)
                level.addParticle(ParticleTypes.ASH, false, true,
                        pos.getX() + random.nextFloat(), pos.getY() + 1.7d + random.nextFloat(), pos.getZ() + random.nextFloat(),
                        0f, 0f, 0f);

            if (random.nextFloat() > 0.9f)
                level.addParticle(ParticleTypes.SCULK_SOUL, false, true,
                        pos.getX() + random.nextFloat(), pos.getY() + 1.1d + random.nextFloat() / 10, pos.getZ() + random.nextFloat(),
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
            for (Entity entity : level.getEntities(null, new AABB(pos.above().above())))
            {
                if (entity instanceof ServerPlayer player)
                {
                    //I don't think this line is needed
                    if (instanceUUID == null) instanceUUID = UUID.randomUUID();

                    TimelessInstance instance = TimelessHandler.getOrCreate(level.getServer(), instanceUUID);

                    level.setBlockAndUpdate(pos, state.trySetValue(PortalBlock.STATE, PortalBlock.State.CLOSED));

                    //if already on timeless
                    if (level.dimension().equals(Echoes.TIMELESS))
                    {
                        TimelessInstance closest = TimelessHandler.getClosest(level.getServer(), pos);

                        if (closest == null)
                        {
                            level.getServer().sendSystemMessage(
                                    Component.literal("[Echoes] Something went wrong when a player used a portal inside The Timeless!"));
                            return;
                        }

                        //load instance as blacksmith
                        instance.setType(TimelessInstance.StructureType.BLACKSMITH);
                        instance.addPlayer(player, closest.portalPos, closest.portalDimension, false);
                    }
                    //if not on timeless, teleport player
                    else
                        instance.addPlayer(player, pos, level.dimension().identifier(), true);
                    return;
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
            TimelessHandler.remove(level.getServer(), instanceUUID);
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