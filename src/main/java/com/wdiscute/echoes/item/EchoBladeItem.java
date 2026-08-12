package com.wdiscute.echoes.item;

import com.wdiscute.echoes.registry.ECDataComponents;
import com.wdiscute.echoes.registry.ECPerks;
import com.wdiscute.echoes.upgrades.PerkInstance;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;

public class EchoBladeItem extends Item
{
    public EchoBladeItem(Properties properties)
    {
        super(properties
                .attributes(createAttributes())
                .component(DataComponents.TOOL, MaceItem.createToolProperties())
                .stacksTo(1)
                .component(ECDataComponents.PERKS, List.of(
                new PerkInstance(ECPerks.EXTRA_DAMAGE, 6f),
                new PerkInstance(ECPerks.EXTRA_PERCENTAGE_SOULS, 1.7f)
        )));
    }

    public static ItemAttributeModifiers createAttributes()
    {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID,
                        -2.4F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }
}
