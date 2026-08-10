package com.wdiscute.echoes.entity.enemy.hollowed;

import com.wdiscute.echoes.Echoes;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AbstractSkeletonRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
import net.minecraft.resources.Identifier;

public class HollowedRenderer extends AbstractSkeletonRenderer<HollowedEntity, SkeletonRenderState>
{
    private static final Identifier HOLLOWED_SKELETON_LOCATION = Echoes.rl("textures/entity/hollowed.png");
    private static final Identifier HOLLOWED_CLOTHES_LOCATION = Echoes.rl("textures/entity/hollowed_overlay.png");

    public HollowedRenderer(EntityRendererProvider.Context context)
    {
        super(context, ModelLayers.STRAY, ModelLayers.STRAY_ARMOR);
        //this.addLayer(new SkeletonClothingLayer<>(this, context.getModelSet(), ModelLayers.STRAY_OUTER_LAYER, HOLLOWED_CLOTHES_LOCATION));
    }

    public Identifier getTextureLocation(SkeletonRenderState state)
    {
        return HOLLOWED_SKELETON_LOCATION;
    }

    public SkeletonRenderState createRenderState()
    {
        return new SkeletonRenderState();
    }
}
