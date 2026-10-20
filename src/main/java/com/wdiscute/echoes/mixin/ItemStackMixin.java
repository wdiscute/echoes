package com.wdiscute.echoes.mixin;

import com.wdiscute.echoes.registry.ECDataComponents;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin
{
    @Inject(method = "hasFoil", at = @At(value = "RETURN"), cancellable = true)
    private void echoes$hasFoil(CallbackInfoReturnable<Boolean> cir)
    {
        if(!cir.getReturnValue())
            cir.setReturnValue(((DataComponentHolder) this).has(ECDataComponents.TIMELESS));
    }
}
