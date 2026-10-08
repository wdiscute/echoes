package com.wdiscute.echoes.entity.enemy.hollowed;

import com.wdiscute.echoes.Echoes;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.resources.ResourceLocation;

public class HollowedRenderer extends SkeletonRenderer<HollowedEntity>
{
    private static final ResourceLocation HOLLOWED_SKELETON_LOCATION = Echoes.rl("textures/entity/hollowed.png");
    private static final ResourceLocation HOLLOWED_CLOTHES_LOCATION = Echoes.rl("textures/entity/hollowed_overlay.png");

    public HollowedRenderer(EntityRendererProvider.Context context)
    {
        super(context);
        //this.addLayer(new SkeletonClothingLayer<>(this, context.getModelSet(), ModelLayers.STRAY_OUTER_LAYER, HOLLOWED_CLOTHES_LOCATION));
    }

    @Override
    public ResourceLocation getTextureLocation(HollowedEntity state)
    {
        return HOLLOWED_SKELETON_LOCATION;
    }
}
