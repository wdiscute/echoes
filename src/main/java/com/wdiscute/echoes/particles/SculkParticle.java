package com.wdiscute.echoes.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class SculkParticle extends TextureSheetParticle
{
    private final SpriteSet sprites;

    protected SculkParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet)
    {
        super(level, x, y, z);

        this.xd = 0f;
        this.yd = 0f;
        this.zd = 0f;

        this.yd = level.getRandom().nextDouble() * 0.005f;
        this.xd = level.getRandom().nextDouble() * 0.005f - 0.0025f;
        this.zd = level.getRandom().nextDouble() * 0.005f - 0.0025f;

        this.quadSize = 0.2f;

        this.lifetime = (int) (level.getRandom().nextFloat() * 80) + 80;

        alpha = 0f;

        this.sprites = spriteSet;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick()
    {
        this.setSpriteFromAge(this.sprites);

        age++;

        if (age < 10)
            alpha = Math.min(alpha + 0.05f, 1);

        if (age > 10 + lifetime)
            alpha = Math.max(0, alpha - 0.02f);

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (alpha <= 0)
            this.remove();

        this.move(this.xd, this.yd, this.zd);
    }

    @Override
    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet)
        {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            return new SculkParticle(clientLevel, x, y, z, this.spriteSet);
        }
    }
}
