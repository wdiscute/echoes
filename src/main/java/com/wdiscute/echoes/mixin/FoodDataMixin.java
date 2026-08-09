package com.wdiscute.echoes.mixin;

import com.wdiscute.echoes.Echoes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public abstract class FoodDataMixin
{

    @Shadow
    public abstract void setFoodLevel(int food);

    @Shadow
    private int foodLevel;

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    private void echoes$tick(ServerPlayer player, CallbackInfo ci)
    {
        if (player.level() == null) return;
        if (player.level().dimension().equals(Echoes.TIMELESS))
        {
            if (foodLevel < 7)
                setFoodLevel(7);
            ci.cancel();
        }
    }
}
