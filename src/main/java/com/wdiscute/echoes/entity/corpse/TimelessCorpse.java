package com.wdiscute.echoes.entity.corpse;

import com.wdiscute.echoes.SculkAura;
import com.wdiscute.echoes.entity.lantern.LanternEntity;
import com.wdiscute.echoes.registry.ECEntityDataSerializers;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public class TimelessCorpse extends Entity implements SculkAura
{
    public static final EntityDataAccessor<MaybeStack> STACK = SynchedEntityData.defineId(TimelessCorpse.class, ECEntityDataSerializers.STACK_HOLDER.get());

    public TimelessCorpse(EntityType<?> type, Level level)
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

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location)
    {
        ItemStack stack = getStack();

        if(player.level().isClientSide())
            return stack.isEmpty() ? InteractionResult.FAIL : InteractionResult.SUCCESS;

        player.addItem(stack);

        entityData.set(STACK, MaybeStack.EMPTY);
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData)
    {
        entityData.define(STACK, MaybeStack.EMPTY);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage)
    {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input)
    {
        entityData.set(STACK, input.read("item", MaybeStack.CODEC).orElse(MaybeStack.EMPTY));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output)
    {
        output.store("item", MaybeStack.CODEC, entityData.get(STACK));
    }

    @Override
    public float getSculkAura(@Nullable ServerLevel sl)
    {
        return entityData.get(STACK).isEmpty() ? 0 : 5;
    }
}
