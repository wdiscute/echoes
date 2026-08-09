package com.wdiscute.echoes.mixin;

import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import com.wdiscute.echoes.ECPostProcessing;
import com.wdiscute.echoes.Echoes;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelTargetBundle;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    @Final
    private CrossFrameResourcePool resourcePool;

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V",
                    shift = At.Shift.AFTER
            )
    )
    private void afterWorldRender(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci)
    {
        int intensityFishEye = Math.clamp(ECPostProcessing.fishEye, 0,  ECPostProcessing.MAX_FISHEYE);
        int intensityCA = Math.clamp(ECPostProcessing.ca, 0,  ECPostProcessing.MAX_CA);
        int intensityCG = Math.clamp(ECPostProcessing.cg, 0,  ECPostProcessing.MAX_CG);

        //todo optimize CA and DG json shaders since they only go up to like 10 max,
        //todo so the jsons can be hardcoded instead of bitmask up to 64

        //yeah i love bit masking (sike)
        //for (int bit = 1; bit <= 64; bit <<= 1)
        //    if ((intensityFishEye & bit) != 0)
        //        apply("fisheye_" + bit);

        for (int bit = 1; bit <= 64; bit <<= 1)
            if ((intensityCA & bit) != 0)
                apply("ca_" + bit);

        for (int bit = 1; bit <= 64; bit <<= 1)
            if ((intensityCG & bit) != 0)
                apply("cg_" + bit);
    }

    private void apply(String name)
    {
        minecraft.getShaderManager().getPostChain(Echoes.rl(name), LevelTargetBundle.MAIN_TARGETS).process(minecraft.getMainRenderTarget(), resourcePool);
    }
}
