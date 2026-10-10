package com.wdiscute.echoes.item;

import com.wdiscute.echoes.entity.totem.TotemEntity;
import com.wdiscute.echoes.perks.TotemPerk;
import com.wdiscute.echoes.registry.ECDataComponents;
import com.wdiscute.echoes.registry.ECEntities;
import com.wdiscute.echoes.timeless.TimelessData;
import com.wdiscute.echoes.upgrades.Perk;
import com.wdiscute.echoes.upgrades.PerkInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class TotemItem extends Item
{
    public TotemItem(Properties properties)
    {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        ItemStack stack = player.getItemInHand(usedHand);

        List<PerkInstance> perks = stack.getOrDefault(ECDataComponents.PERKS, List.of());

        for (PerkInstance instance : perks)
        {
            if(instance.perk() instanceof TotemPerk totemPerk)
            {
                //if player has enough souls to spawn totem
                if(TimelessData.consumeSouls(player, instance.amplifiers().get(0)))
                {
                    TotemEntity totemEntity = ECEntities.TOTEM.get().create(level);




                }
            }
        }

        return super.use(level, player, usedHand);
    }
}
