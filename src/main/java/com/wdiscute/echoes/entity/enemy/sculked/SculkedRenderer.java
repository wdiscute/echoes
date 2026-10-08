package com.wdiscute.echoes.entity.enemy.sculked;

import com.wdiscute.echoes.Echoes;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Zombie;

public class SculkedRenderer extends ZombieRenderer
{
    private static final ResourceLocation SCULKED_LOCATION = Echoes.rl("textures/entity/sculked.png");
    private static final ResourceLocation BABY_SCULKED_LOCATION = Echoes.rl("textures/entity/sculked_baby.png");

    public SculkedRenderer(EntityRendererProvider.Context context)
    {
        super(context, ModelLayers.HUSK, ModelLayers.HUSK_INNER_ARMOR, ModelLayers.HUSK_OUTER_ARMOR);
    }

    @Override
    public ResourceLocation getTextureLocation(Zombie entity)
    {
        return entity.isBaby() ? BABY_SCULKED_LOCATION : SCULKED_LOCATION;
    }
}
