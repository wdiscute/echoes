package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;

public interface ECArmorMaterials
{
    DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL,
                    Echoes.MOD_ID);

    ResourceKey<ArmorMaterial> TIMELOST = ResourceKey.create(Registries.ARMOR_MATERIAL, Echoes.rl("timelost"));

    Holder<ArmorMaterial> TIMELOST_MATERIAL =
            ARMOR_MATERIALS.register("custom", () ->
                    new ArmorMaterial(
                            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                                map.put(ArmorItem.Type.HELMET, 0);
                                map.put(ArmorItem.Type.CHESTPLATE, 0);
                                map.put(ArmorItem.Type.LEGGINGS, 0);
                                map.put(ArmorItem.Type.BOOTS, 0);
                                map.put(ArmorItem.Type.BODY, 0);
                            }),
                            0,
                            SoundEvents.ARMOR_EQUIP_GENERIC,
                            () -> Ingredient.of(ECItems.PRISMA_SHARD),
                            List.of(new ArmorMaterial.Layer(Echoes.rl("timelost"))),
                            0.0F,
                            0.0F
                    )
            );

    static void register(IEventBus eventBus)
    {
        ARMOR_MATERIALS.register(eventBus);
    }
}
