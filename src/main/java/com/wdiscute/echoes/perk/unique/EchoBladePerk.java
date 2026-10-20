package com.wdiscute.echoes.perk.unique;

import com.wdiscute.echoes.perk.PerkInstance;
import com.wdiscute.echoes.perk.SimplePerk;
import com.wdiscute.echoes.registry.ECDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EchoBladePerk extends SimplePerk
{
    public static final String ECHO_DAMAGE = "echo_damage";
    public static final String PRISMA_DAMAGE = "prisma_damage";

    public static final String ECHO_EXTRA_DAMAGE = "echo_extra_damage";
    public static final String ECHO_SOULS_COST = "echo_souls_cost";

    public static final String PRISMA_SOULS_PERCENTAGE = "prisma_souls_percentage";
    public static final String PRISMA_SOULS_FLAT = "prisma_souls_flat";


    @Override
    public List<String> getIds()
    {
        return List.of(ECHO_DAMAGE, PRISMA_DAMAGE, ECHO_EXTRA_DAMAGE, ECHO_SOULS_COST, PRISMA_SOULS_PERCENTAGE, PRISMA_SOULS_FLAT);
    }

    @Override
    public List<MutableComponent> getTooltip(ItemStack stack, PerkInstance value)
    {
        return List.of(Component.literal("A Timeless Fight").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.BOLD));
    }

    @Override
    public List<MutableComponent> getShopExtendedTooltip(ItemStack stack, PerkInstance amplifiers)
    {
        List<MutableComponent> list = new ArrayList<>();
        boolean isPrisma = stack.getOrDefault(ECDataComponents.IS_PRISMA_BLADE, false);

        list.add(Component.literal("A Timeless Fight").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.BOLD));

        if(isPrisma)
            list.add(Component.literal("[use] to transform into the Echo Blade"));
        else
            list.add(Component.literal("[use] to transform into the Prisma Blade"));

        return list;
    }
}
