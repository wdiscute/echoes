package com.wdiscute.echoes.mixin;

import net.irisshaders.iris.shaderpack.materialmap.NamespacedId;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Pseudo
@Mixin(targets = "net.irisshaders.iris.shaderpack.ShaderPack", remap = false)
public class ShaderDimensionMixin
{
    @Shadow
    private Map<NamespacedId, String> dimensionMap;

    @Shadow
    @Final
    private List<String> dimensionIds;

    @Inject(
            method = "<init>(Ljava/nio/file/Path;Ljava/util/Map;Lcom/google/common/collect/ImmutableList;Z)V",
            at = @At("RETURN")
    )
    private void echoes$mapTimelessToEnd(CallbackInfo ci)
    {
        if (!this.dimensionIds.contains("world1")) return;

        this.dimensionMap.putIfAbsent(new NamespacedId("echoes", "timeless"), "world1");
    }
}
