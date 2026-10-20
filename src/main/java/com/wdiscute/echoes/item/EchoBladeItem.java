package com.wdiscute.echoes.item;

import com.wdiscute.echoes.perk.Perk;
import com.wdiscute.echoes.registry.ECDataComponents;
import com.wdiscute.echoes.registry.ECPerks;
import com.wdiscute.echoes.perk.PerkInstance;
import com.wdiscute.echoes.perk.unique.EchoBladePerk;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.*;

public class EchoBladeItem extends TimelessWeaponItem
{
    public EchoBladeItem(Properties properties, float speed)
    {
        super(properties, speed);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack)
    {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity user)
    {
        return 20;
    }

    @Override
    public Component getName(ItemStack itemStack)
    {
        Component prisma = Component.translatable("item.echoes.prisma_blade");
        if (itemStack.getOrDefault(ECDataComponents.IS_PRISMA_BLADE, false))
            return prisma;
        else
            return Component.translatable(this.getDescriptionId(itemStack));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity)
    {
        Optional<PerkInstance> echoBladePerk = stack.getOrDefault(ECDataComponents.PERKS,
                List.<PerkInstance>of()).stream().filter(o -> o.perk() instanceof EchoBladePerk).findAny();

        if (entity instanceof Player player)
            player.getCooldowns().addCooldown(this, 20);

        if (echoBladePerk.isPresent())
        {
            boolean isPrisma = stack.getOrDefault(ECDataComponents.IS_PRISMA_BLADE, false);
            //swap is_prisma_blade
            stack.set(ECDataComponents.IS_PRISMA_BLADE, !isPrisma);

            PerkInstance instance = echoBladePerk.get();

            List<PerkInstance> newPerks = new ArrayList<>();

            if (!isPrisma)
            {
                entity.playSound(SoundEvents.BEACON_ACTIVATE, 1, 2);
                newPerks.add(new PerkInstance(ECPerks.EXTRA_DAMAGE, Perk.DAMAGE, instance.get(EchoBladePerk.PRISMA_DAMAGE)));
                newPerks.add(new PerkInstance(ECPerks.EXTRA_PERCENTAGE_SOULS, Perk.VALUE, instance.get(EchoBladePerk.PRISMA_SOULS_PERCENTAGE)));
                newPerks.add(new PerkInstance(ECPerks.EXTRA_FLAT_SOULS, Perk.VALUE, instance.get(EchoBladePerk.PRISMA_SOULS_FLAT)));
                newPerks.add(instance);
            }
            else
            {
                entity.playSound(SoundEvents.BEACON_DEACTIVATE, 1, 2);
                newPerks.add(new PerkInstance(ECPerks.EXTRA_DAMAGE, Perk.DAMAGE, instance.get(EchoBladePerk.ECHO_DAMAGE)));
                newPerks.add(new PerkInstance(ECPerks.EXTRA_DAMAGE_CONSUMES_SOULS, Map.of(
                        Perk.COST, instance.get(EchoBladePerk.ECHO_SOULS_COST),
                        Perk.DAMAGE, instance.get(EchoBladePerk.ECHO_EXTRA_DAMAGE)
                )));
                newPerks.add(instance);
            }

            stack.set(ECDataComponents.PERKS, newPerks);
        }

        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack)
    {
        return true;
    }
}
