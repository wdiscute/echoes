package com.wdiscute.echoes.entity.corpse;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class TimelessCorpse extends Entity
{
    ItemStack stack = new ItemStack(Items.DIAMOND_SWORD);

    public TimelessCorpse(EntityType<?> type, Level level)
    {
        super(type, level);
    }

    @Override
    public boolean isPickable()
    {
        return true;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location)
    {
        if (stack != null)
            player.addItem(stack);
        stack = null;
        return super.interact(player, hand, location);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData)
    {

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
}
