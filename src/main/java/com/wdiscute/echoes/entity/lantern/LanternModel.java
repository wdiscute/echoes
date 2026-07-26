package com.wdiscute.echoes.entity.lantern;

import com.wdiscute.echoes.Echoes;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class LanternModel extends EntityModel<LanternRenderState>
{
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Echoes.rl("lantern"), "main");

    private final ModelPart bb_main;

    public LanternModel(ModelPart root)
    {
        super(root);
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer()
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -7.0F, -3.0F, 6.0F, 7.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 13).addBox(-2.0F, -9.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(16, 15).addBox(-1.0F, -2.0F, -1.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.25F, -9.0F, -0.5F, 0.0F, 2.3562F, 0.0F));

        PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(16, 13).addBox(-1.0F, -2.0F, -1.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -9.0F, 1.0F, 0.0F, 0.7854F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
}