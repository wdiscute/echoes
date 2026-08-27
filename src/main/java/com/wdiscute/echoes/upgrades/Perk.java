package com.wdiscute.echoes.upgrades;

import com.wdiscute.echoes.registry.ECDataAttachments;
import com.wdiscute.echoes.registry.ECDataComponents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class Perk
{
    public static List<PerkInstance> getActivePerks(@NotNull Player player, @NotNull ItemStack weaponItem)
    {
        List<PerkInstance> perks = new ArrayList<>();

        //add all permanent perks (data attachments)
        perks.addAll(player.getData(ECDataAttachments.PERKS));

        //add all perks from weapon data component
        perks.addAll(weaponItem.getOrDefault(ECDataComponents.PERKS, List.of()));

        //add all perks from armor
        perks.addAll(player.getItemBySlot(EquipmentSlot.FEET).getOrDefault(ECDataComponents.PERKS, List.of()));
        perks.addAll(player.getItemBySlot(EquipmentSlot.LEGS).getOrDefault(ECDataComponents.PERKS, List.of()));
        perks.addAll(player.getItemBySlot(EquipmentSlot.BODY).getOrDefault(ECDataComponents.PERKS, List.of()));
        perks.addAll(player.getItemBySlot(EquipmentSlot.HEAD).getOrDefault(ECDataComponents.PERKS, List.of()));

        //todo add off-hand item??? probably not

        return perks;
    }

    public void onEntityKilled(@NotNull Player killer, @NotNull ItemStack weapon, @NotNull Entity entityKilled, List<Float> value)
    {

    }

    public float addDamage(@NotNull Player player, @NotNull ItemStack weapon, @NotNull Entity entity, List<Float> value)
    {
        return 0;
    }

    public float addSouls(Player player, ItemStack weapon, LivingEntity entityKilled, List<Float> amplifier, float currentSouls)
    {
        return 0;
    }

    public float addChestRolls(Player player, List<Float> amplifiers)
    {
        return 0;
    }

    public abstract List<MutableComponent> getItemTooltip(ItemStack stack, List<Float> value);

    public abstract List<MutableComponent> getShopTooltip(ItemStack stack, List<Float> value);

    public List<MutableComponent> getShopExtendedTooltip(ItemStack stack, List<Float> value)
    {
        return List.of();
    }

}
