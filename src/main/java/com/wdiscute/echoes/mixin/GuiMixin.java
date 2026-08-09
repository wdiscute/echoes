package com.wdiscute.echoes.mixin;

import com.wdiscute.echoes.Echoes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Gui.class)
public class GuiMixin
{
    @Inject(method = "nextContextualInfoState", at = @At(value = "HEAD"), cancellable = true)
    public void echoes$nextContextualInfoState(CallbackInfoReturnable<Gui.ContextualInfo> cir)
    {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;

        if (level.dimension().equals(Echoes.TIMELESS))
        {
            cir.setReturnValue(Gui.ContextualInfo.EMPTY);
            cir.cancel();
        }
    }
}
