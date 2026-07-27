package com.wdiscute.echoes.blocks.portal;

import com.mojang.serialization.Codec;
import com.wdiscute.echoes.TimelessInstancesSD;
import com.wdiscute.echoes.TimelessInstance;
import com.wdiscute.echoes.registry.ECBlockEntities;
import com.wdiscute.utils.TickableBlockEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SculkSpreader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;

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

            level.addParticle(ParticleTypes.END_ROD, false, true,
                    pos.getX() + 0.5d, pos.getY() + 1.7d + random.nextFloat(), pos.getZ() + 0.5d,
                    0f, 0f, 0f);

            if (random.nextFloat() > 0f)
                level.addParticle(ParticleTypes.ASH, false, true,
                        pos.getX() + random.nextFloat(), pos.getY() + 1.7d + random.nextFloat(), pos.getZ() + random.nextFloat(),
                        0f, 0f, 0f);

            if (random.nextFloat() > 0.9f)
                level.addParticle(ParticleTypes.SCULK_SOUL, false, true,
                        pos.getX() + random.nextFloat(), pos.getY() + 1.2d + random.nextFloat(), pos.getZ() + random.nextFloat(),
                        0f, 0f, 0f);
        }
    }

    @Override
    public void tickServer(ServerLevel level, BlockPos pos, BlockState state)
    {
        TickableBlockEntity.super.tickServer(level, pos, state);

        SCULK_SPREADER.updateCursors(level, pos, level.getRandom(), true);

        System.out.println(instanceUUID);

        //if block doesn't have shard, don't run logic
        if (!state.getValueOrElse(PortalBlock.HAS_SHARD, false)) return;

        List<Entity> entities = level.getEntities(null, new AABB(pos.above().above()));

        for (Entity entity : entities)
        {
            if (entity instanceof ServerPlayer player)
            {
                //I don't think this line is needed
                if (instanceUUID == null) instanceUUID = UUID.randomUUID();

                TimelessInstance instance = TimelessInstancesSD.getOrCreate(level.getServer(), instanceUUID);

                level.setBlockAndUpdate(pos, state.trySetValue(PortalBlock.HAS_SHARD, false));

                instance.addPlayer(player, pos, level.dimension().identifier());
                return;
            }
        }
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);

        if (instanceUUID != null && !level.isClientSide())
            TimelessInstancesSD.remove(level.getServer(), instanceUUID);
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