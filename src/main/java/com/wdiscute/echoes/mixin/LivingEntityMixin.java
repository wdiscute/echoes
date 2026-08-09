package com.wdiscute.echoes.mixin;

import com.wdiscute.echoes.Echoes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin
{
    @Inject(method = "makePoofParticles", at = @At(value = "HEAD"), cancellable = true)
    private void echoes$makePoofParticles(CallbackInfo ci)
    {
        LivingEntity entity = (LivingEntity) ((Object) this);
        if (entity.level() == null) return;
        if (entity.level().dimension().equals(Echoes.TIMELESS))
        {
            for (int i = 0; i < 4; i++) {
                double xa = entity.getRandom().nextGaussian() * 0.02;
                double ya = entity.getRandom().nextGaussian() * 0.02;
                double za = entity.getRandom().nextGaussian() * 0.02;
                entity.level()
                        .addParticle(ParticleTypes.SOUL, entity.getRandomX(1.0) - xa * 10.0, entity.getRandomY() - ya * 10.0, entity.getRandomZ(1.0) - za * 10.0, xa, ya, za);
            }
            ci.cancel();
        }
    }
}
