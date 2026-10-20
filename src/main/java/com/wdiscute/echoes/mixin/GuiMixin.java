package com.wdiscute.echoes.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.wdiscute.echoes.registry.ECClientEvents;
import com.wdiscute.echoes.registry.ECDataComponents;
import com.wdiscute.utils.ScreenUtils;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin
{
    @WrapOperation(
            method = "renderItemHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;renderSlot(Lnet/minecraft/client/gui/GuiGraphics;IILnet/minecraft/client/DeltaTracker;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;I)V"
            )
    )
    private void echoes$wrapRenderSlot(Gui instance, GuiGraphics guiGraphics, int x, int y, DeltaTracker deltaTracker, Player player, ItemStack stack, int seed, Operation<Void> original)
    {
        long time = System.currentTimeMillis();

        if (stack.has(ECDataComponents.TIMELESS.get()))
            ScreenUtils.fillGradient(guiGraphics, x, y, 16, 16,
                    ScreenUtils.gradient(time, 3000, ECClientEvents.RAINBOW),
                    ScreenUtils.gradient(time + 2000, 3000, ECClientEvents.RAINBOW)
            );

        original.call(instance, guiGraphics, x, y, deltaTracker, player, stack, seed);
    }
}
