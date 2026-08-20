package com.wdiscute.echoes.entity.trader;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.entity.heart.SculkHeartRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;

public class SoulTraderModel extends EntityModel<SoulTraderRenderState>
{
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Echoes.rl("soul_trader"), "main");

    public static final Identifier TEXTURE_LOCATION = Echoes.rl("textures/entity/soul_trader.png");

    private final ModelPart bb_main;

    public SoulTraderModel(ModelPart root)
    {
        super(root);
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(1, 2).addBox(-3.5F, -17.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-3.2F, -16.5F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(1, 28).addBox(4.0F, -11.0F, -1.5F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(1, 28).addBox(-7.0F, -11.0F, -1.5F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(19, 25).addBox(-2.5F, -10.0F, -2.5F, 5.0F, 10.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(25, 2).addBox(-1.5F, -10.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 40, 40);
    }
}