package com.wdiscute.echoes.blocks.portal;

import com.mojang.serialization.Codec;
import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.TimelessHandler;
import com.wdiscute.echoes.TimelessInstance;
import com.wdiscute.echoes.registry.ECBlockEntities;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SculkSpreader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PortalBlockEntity extends BlockEntity implements TickableBlockEntity
{
    public static final SculkSpreader SCULK_SPREADER = SculkSpreader.createLevelSpreader();

    public PortalBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
        super(ECBlockEntities.PORTAL.get(), worldPosition, blockState);
    }

    UUID instanceUUID;

    @Override
    public void tickClient(Level level, BlockPos pos, BlockState state)
    {
        TickableBlockEntity.super.tickClient(level, pos, state);

        if (state.getValueOrElse(PortalBlock.HAS_SHARD, false))
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

            if (level.getRandom().nextFloat() > 0.8f)
                level.playLocalSound(center.x, center.y, center.z,
                        SoundEvents.BEACON_AMBIENT, SoundSource.BLOCKS, 1f, 0.3f, false);

        }
    }

    @Override
    public void tickServer(ServerLevel level, BlockPos pos, BlockState state)
    {
        TickableBlockEntity.super.tickServer(level, pos, state);

        SCULK_SPREADER.updateCursors(level, pos, level.getRandom(), true);

        //if block doesn't have shard, don't run logic
        if (!state.getValueOrElse(PortalBlock.HAS_SHARD, false)) return;

        List<Entity> entities = level.getEntities(null, new AABB(pos.above().above()));

        for (Entity entity : entities)
        {
            if (entity instanceof ServerPlayer player)
            {
                //I don't think this line is needed
                if (instanceUUID == null) instanceUUID = UUID.randomUUID();

                TimelessInstance instance = TimelessHandler.getOrCreate(level.getServer(), instanceUUID);

                level.setBlockAndUpdate(pos, state.trySetValue(PortalBlock.HAS_SHARD, false));

                //if already on timeless
                if(level.dimension().equals(Echoes.TIMELESS))
                {
                    TimelessInstance closest = TimelessHandler.getClosest(level.getServer(), pos);

                    if(closest == null)
                    {
                        level.getServer().sendSystemMessage(
                                Component.literal("[Echoes] Something went wrong when a player used a portal inside The Timeless!"));

                        return;
                    }

                    //load instance first so adding the player doesn't run the sculk & gleemslate logic
                    instance.attemptLoad(player.level(), closest.portalPos, closest.portalDimension, TimelessInstance.StructureType.BLACKSMITH);
                    instance.addPlayer(player, closest.portalPos, closest.portalDimension, false);
                }
                //if not on timeless, teleport player
                else
                {
                    instance.addPlayer(player, pos, level.dimension().identifier(), false);
                }




                return;
            }
        }
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);

        if (instanceUUID != null && !level.isClientSide())
            TimelessHandler.remove(level.getServer(), instanceUUID);
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        if (instanceUUID != null)
            output.store("instance_uuid", Codec.STRING, instanceUUID.toString());
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        Optional<String> uuidFromDisk = input.read("instance_uuid", Codec.STRING);

        if (uuidFromDisk.isPresent())
        {
            instanceUUID = UUID.fromString(uuidFromDisk.get());
        }
        else
            instanceUUID = UUID.randomUUID();
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state)
    {
        TickableBlockEntity.super.tick(level, pos, state);
    }
}