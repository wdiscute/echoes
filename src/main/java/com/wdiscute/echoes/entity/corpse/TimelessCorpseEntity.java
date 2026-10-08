package com.wdiscute.echoes.entity.corpse;

import com.wdiscute.echoes.SculkAura;
import com.wdiscute.echoes.registry.ECEntityDataSerializers;
import com.wdiscute.echoes.timeless.TimelessInstance;
import com.wdiscute.echoes.timeless.TimelessManager;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.ValueHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TimelessCorpseEntity extends Entity implements SculkAura
{
    public static final EntityDataAccessor<MaybeStack> STACK = SynchedEntityData.defineId(TimelessCorpseEntity.class, ECEntityDataSerializers.STACK_HOLDER.get());

    public TimelessCorpseEntity(EntityType<?> type, Level level)
    {
        super(type, level);
    }

    public void setStack(ItemStack stack)
    {
        entityData.set(STACK, new MaybeStack(stack));
    }

    public ItemStack getStack()
    {
        return entityData.get(STACK).toStack();
    }

    @Override
    public boolean isPickable()
    {
        return true;
    }

    float auraSize = 5;

    @Override
    public void tick()
    {
        if (entityData.get(STACK).isEmpty())
            auraSize -= 0.5f;

        super.tick();
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand)
    {
        ItemStack stack = getStack();

        if (player.level().isClientSide())
            return stack.isEmpty() ? InteractionResult.FAIL : InteractionResult.SUCCESS;

        if (stack.isEmpty())
            return InteractionResult.FAIL;

        player.level().playSound(null, xo, yo, zOld, SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.AMBIENT);

        player.addItem(stack);

        TimelessInstance closest = TimelessManager.getClosest(level().getServer(), blockPosition());
        if (closest != null && closest.getPlayers((ServerLevel) level()).size() == 1)
            entityData.set(STACK, MaybeStack.EMPTY);
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData)
    {
        entityData.define(STACK, MaybeStack.EMPTY);
    }

    @Override
    public boolean hurt(DamageSource source, float amount)
    {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag)
    {
        entityData.set(STACK, ValueHelper.read("item", MaybeStack.CODEC, tag).orElse(MaybeStack.EMPTY));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag)
    {
        ValueHelper.store("item", MaybeStack.CODEC, entityData.get(STACK), tag);
    }

    @Override
    public float getSculkAura(ServerLevel sl)
    {
        return auraSize;
    }
}
