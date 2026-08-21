package com.wdiscute.echoes.upgrades.perks;

import com.wdiscute.echoes.registry.ECDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EchoBladePerk extends SimplePerk
{
    //first value = flat damage echo;
    //second value = flat damage prisma;

    //third value = echo extra damage;
    //forth value = echo souls consumed;

    //fifth value = prisma souls gathered percentage;
    //sixth value = prisma souls gathered flat;

    @Override
    public List<MutableComponent> getTooltip(ItemStack stack, List<Float> value)
    {
        return List.of(Component.literal("A Timeless Fight").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.BOLD));
    }

    @Override
    public List<MutableComponent> getShopExtendedTooltip(ItemStack stack, List<Float> value)
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
