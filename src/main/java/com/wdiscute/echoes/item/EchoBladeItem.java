package com.wdiscute.echoes.item;

import com.wdiscute.echoes.registry.ECDataComponents;
import com.wdiscute.echoes.registry.ECPerks;
import com.wdiscute.echoes.upgrades.PerkInstance;
import com.wdiscute.echoes.upgrades.perks.EchoBladePerk;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EchoBladeItem extends TimelessWeaponItem
{
    public EchoBladeItem(Properties properties, float speed)
    {
        super(properties.useCooldown(1.0F), speed);
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack itemStack)
    {
        return ItemUseAnimation.BOW;
    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity user)
    {
        return 20;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity)
    {
        Optional<PerkInstance> echoBladePerk = stack.getOrDefault(ECDataComponents.PERKS,
                List.<PerkInstance>of()).stream().filter(o -> o.perk() instanceof EchoBladePerk).findAny();

        if (echoBladePerk.isPresent())
        {
            boolean isPrisma = stack.getOrDefault(ECDataComponents.IS_PRISMA_BLADE, false);
            //swap is_prisma_blade
            stack.set(ECDataComponents.IS_PRISMA_BLADE, !isPrisma);

            PerkInstance perk = echoBladePerk.get();
            List<Float> values = perk.amplifiers();

            List<PerkInstance> newPerks = new ArrayList<>();

            if (!isPrisma)
            {
                entity.playSound(SoundEvents.BEACON_ACTIVATE, 1, 2);
                newPerks.add(new PerkInstance(ECPerks.EXTRA_DAMAGE, values.get(1)));
                newPerks.add(new PerkInstance(ECPerks.EXTRA_PERCENTAGE_SOULS, values.get(4)));
                newPerks.add(new PerkInstance(ECPerks.EXTRA_FLAT_SOULS, values.get(5)));
                newPerks.add(perk);
            }
            else
            {
                entity.playSound(SoundEvents.BEACON_DEACTIVATE, 1, 2);
                newPerks.add(new PerkInstance(ECPerks.EXTRA_DAMAGE, values.get(0)));
                newPerks.add(new PerkInstance(ECPerks.EXTRA_DAMAGE_CONSUMES_SOULS, values.get(2), values.get(3)));
                newPerks.add(perk);
            }

            stack.set(ECDataComponents.PERKS, newPerks);
        }

        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand)
    {
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack)
    {
        return true;
    }
}
