package com.wdiscute.echoes.perk.totem;

import com.wdiscute.echoes.entity.totem.TotemEntity;
import com.wdiscute.echoes.perk.Perk;
import com.wdiscute.echoes.perk.PerkInstance;
import com.wdiscute.echoes.perk.SimplePerk;
import com.wdiscute.echoes.timeless.TimelessData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class RecoveryTotemPerk extends SimplePerk implements TotemPerk
{
    @Override
    public void onTick(TotemEntity entity, PerkInstance instance)
    {
        if(entity.cooldown > 0)
        {
            entity.cooldown--;
        }
        else
        {
            if(entity.player != null)
            {
                TimelessData.consumeSouls(entity.player, instance.getCost());
            }

            List<Player> players = entity.level().getEntities(EntityType.PLAYER, new AABB(entity.blockPosition()).inflate(instance.getRange()), o -> o instanceof Player);
            for (Player player : players)
            {
                player.heal(instance.getValue());
                entity.cooldown = (int) instance.getCooldown();
            }
        }
    }

    @Override
    public List<String> getIds()
    {
        return List.of(Perk.COST, Perk.HEAL, Perk.COOLDOWN, Perk.RANGE);
    }

    @Override
    public List<MutableComponent> getTooltip(ItemStack stack, PerkInstance instance)
    {
        return List.of(Component.literal("Recovery Aura").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.BOLD));
    }

    @Override
    public List<MutableComponent> getShopExtendedTooltip(ItemStack stack, PerkInstance instance)
    {
        List<MutableComponent> list = new ArrayList<>();

        list.add(Component.literal("Recovery Aura").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.BOLD));
        list.add(Component.literal("Consumes" + format(instance, Perk.COST) + "souls to recover " + format(instance, Perk.HEAL) + " health"));
        list.add(Component.literal("every " + format(instance, Perk.COOLDOWN) + " ticks to players within " + format(instance, Perk.RANGE) + " blocks"));

        return list;
    }
}
