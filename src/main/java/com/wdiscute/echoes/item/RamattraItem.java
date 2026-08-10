package com.wdiscute.echoes.item;

import com.wdiscute.echoes.entity.unleashedsoul.UnleashedSoulEntity;
import com.wdiscute.echoes.registry.ECDataComponents;
import com.wdiscute.echoes.registry.ECEntities;
import com.wdiscute.echoes.registry.ECPerks;
import com.wdiscute.echoes.timeless.TimelessData;
import com.wdiscute.echoes.upgrades.PerkInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class RamattraItem extends Item
{
    public RamattraItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity user)
    {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int ticksRemaining)
    {
        if (livingEntity instanceof ServerPlayer player && level.getGameTime() % 3 == 0 && TimelessData.consumeSouls(player, 1))
        {

            //get damage to spawn soul with
            List<PerkInstance> list = itemStack.getOrDefault(ECDataComponents.PERKS, List.<PerkInstance>of()).stream().filter(o -> o.perk().equals(ECPerks.RAMATTRA)).toList();

            float damage;
            if (list.isEmpty()) damage = 1;
            else damage = list.getFirst().amplifiers().getFirst();

            Vec3 direction = player.getLookAngle();
            UnleashedSoulEntity projectile = new UnleashedSoulEntity(
                    ECEntities.UNLEASHED_SOUL.get(),
                    player,
                    direction,
                    level,
                    20,
                    damage
            );

            level.addFreshEntity(projectile);
        }
        super.onUseTick(level, livingEntity, itemStack, ticksRemaining);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        if (canContinueUsing(stack, stack))
        {
            player.startUsingItem(hand);
            return InteractionResult.CONSUME;
        }
        else
            return InteractionResult.PASS;
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot)
    {
        if (owner instanceof Player player)
            itemStack.set(ECDataComponents.RAMATTRA_CAN_USE.get(), TimelessData.get(player).souls() >= 1);

        super.inventoryTick(itemStack, level, owner, slot);
    }

    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack)
    {
        return super.canContinueUsing(oldStack, newStack) && newStack.getOrDefault(ECDataComponents.RAMATTRA_CAN_USE.get(), false);
    }
}
