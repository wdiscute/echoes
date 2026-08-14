package com.wdiscute.echoes.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class EchoWeaponItem extends Item
{
    public EchoWeaponItem(Properties properties, float speed)
    {
        super(properties
                .attributes(createAttributes(speed))
                .component(DataComponents.TOOL, MaceItem.createToolProperties())
                .stacksTo(1)
        );
    }

    public static ItemAttributeModifiers createAttributes(float speed)
    {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID,
                        speed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }
}
