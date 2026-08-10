package com.wdiscute.echoes.entity.enemy.sculked;

import com.wdiscute.echoes.Echoes;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.Identifier;

public class SculkedRenderer extends ZombieRenderer
{
    private static final Identifier SCULKED_LOCATION = Echoes.rl("textures/entity/sculked.png");
    private static final Identifier BABY_SCULKED_LOCATION = Echoes.rl("textures/entity/sculked_baby.png");

    public SculkedRenderer(EntityRendererProvider.Context context)
    {
        super(context, ModelLayers.HUSK, ModelLayers.HUSK_BABY, ModelLayers.HUSK_ARMOR, ModelLayers.HUSK_BABY_ARMOR);
    }

    @Override
    public Identifier getTextureLocation(ZombieRenderState state)
    {
        return state.isBaby ? BABY_SCULKED_LOCATION : SCULKED_LOCATION;
    }
}
