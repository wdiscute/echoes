package com.wdiscute.echoes.entity.specter;

import com.wdiscute.echoes.registry.ECEntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class SpecterEntity extends Entity
{
    public static final EntityDataAccessor<UUID> PLAYER_UUID = SynchedEntityData.defineId(SpecterEntity.class, ECEntityDataSerializers.UUID_HOLDER.get());

    public final AnimationState spinAnimationState = new AnimationState();
    public final AnimationState sixSevenAnimationState = new AnimationState();
    public final AnimationState headExplodeAnimationState = new AnimationState();
    public final AnimationState pointAnimationState = new AnimationState();

    public ServerPlayer player;

    private float previousRenderYRot;
    private float renderYRot;
    private float previousRenderXRot;
    private float renderXRot;

    private Vec3 previousRenderPosition;
    private Vec3 renderPosition;

    public SpecterEntity(EntityType<?> type, Level level)
    {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData)
    {
        entityData.define(PLAYER_UUID, UUID.randomUUID());
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage)
    {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input)
    {
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output)
    {
    }

    @Override
    public void tick()
    {
        super.tick();

        if (level().isClientSide())
        {
            //todo make payloads for starting animations
            //sixSevenAnimationState.startIfStopped(this.tickCount);
            updateRenderPosition();
            return;
        }

        if (player == null
            || level().getPlayerByUUID(player.getUUID()) == null
            || !player.isSpectator())
        {
            discard();
            return;
        }

        setPos(player.position());

        setYRot(player.getYRot());
        setXRot(player.getXRot());
    }

    private void updateRenderPosition()
    {
        Vec3 target = position();

        if (renderPosition == null)
        {
            renderPosition = target;
            previousRenderPosition = target;

            renderYRot = getYRot();
            previousRenderYRot = renderYRot;

            renderXRot = getXRot();
            previousRenderXRot = renderXRot;

            return;
        }

        previousRenderPosition = renderPosition;
        renderPosition = renderPosition.lerp(target, 0.2);

        previousRenderYRot = renderYRot;
        renderYRot = Mth.rotLerp(0.2f, renderYRot, getYRot());

        previousRenderXRot = renderXRot;
        renderXRot = Mth.lerp(0.2f, renderXRot, getXRot());
    }

    public float getRenderYRot(float partialTicks)
    {
        return Mth.rotLerp(partialTicks, previousRenderYRot, renderYRot);
    }

    public float getRenderXRot(float partialTicks)
    {
        return Mth.lerp(partialTicks, previousRenderXRot, renderXRot);
    }

    public Vec3 getRenderPosition(float partialTicks)
    {
        if (renderPosition == null)
            return position();

        return previousRenderPosition.lerp(renderPosition, partialTicks);
    }

    public Vec3 getNetworkPosition(float partialTicks)
    {
        return new Vec3(
                xo + (getX() - xo) * partialTicks,
                yo + (getY() - yo) * partialTicks,
                zo + (getZ() - zo) * partialTicks
        );
    }

    public void setPlayer(ServerPlayer sp)
    {
        player = sp;
        entityData.set(PLAYER_UUID, sp.getUUID());
    }

    public void playEmote(SpecterEmote emote)
    {
        sixSevenAnimationState.stop();
        spinAnimationState.stop();
        headExplodeAnimationState.stop();
        pointAnimationState.stop();

        switch (emote)
        {
            case SPIN -> spinAnimationState.startIfStopped(tickCount);
            case SIX_SEVEN -> sixSevenAnimationState.startIfStopped(tickCount);
            case POINT -> pointAnimationState.startIfStopped(tickCount);
            case HEAD_EXPLODE -> headExplodeAnimationState.startIfStopped(tickCount);
        }
    }
}