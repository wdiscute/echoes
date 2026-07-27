package com.wdiscute.echoes.blocks.portal;

import com.wdiscute.echoes.TimelessInstancesSD;
import com.wdiscute.echoes.TimelessInstance;
import com.wdiscute.echoes.registry.ECBlockEntities;
import com.wdiscute.utils.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.UUID;

public class PortalBlockEntity extends BlockEntity implements TickableBlockEntity
{
    public PortalBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
        super(ECBlockEntities.PORTAL.get(), worldPosition, blockState);
    }

    UUID instanceUUID;
    TimelessInstance instance;

    @Override
    public void tickServer(ServerLevel level, BlockPos pos, BlockState state)
    {
        TickableBlockEntity.super.tickServer(level, pos, state);

        List<Entity> entities = level.getEntities(null, new AABB(pos.above().above()).inflate(2));

        for (Entity entity : entities)
        {
            if(entity instanceof ServerPlayer player)
            {
                //I don't think this line is needed
                if(instanceUUID == null) instanceUUID = UUID.randomUUID();

                if(instance == null)
                    instance = TimelessInstancesSD.getOrCreate(level.getServer(), instanceUUID);

                instance.addPlayer(player);
            }
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        if(instanceUUID == null) instanceUUID = UUID.randomUUID();
        output.store("instance_uuid", UUIDUtil.CODEC, instanceUUID);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        instanceUUID = input.read("instance_uuid", UUIDUtil.CODEC).orElse(UUID.randomUUID());
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state)
    {
        TickableBlockEntity.super.tick(level, pos, state);
    }
}
