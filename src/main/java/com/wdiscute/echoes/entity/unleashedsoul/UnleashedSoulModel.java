package com.wdiscute.echoes.entity.unleashedsoul;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.entity.soul.SoulRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;

public class UnleashedSoulModel extends EntityModel<UnleashedSoulRenderState>
{
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Echoes.rl("soul"), "main");
    public static final Identifier TEXTURE_LOCATION = Echoes.rl("textures/entity/soul.png");

    private final ModelPart bb_main;

    public UnleashedSoulModel(ModelPart root)
    {
        super(root);
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 11).addBox(-2.0F, -8.0F, 0.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -4.0F, -1.0F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -4.0F, -1.0F, 0.0F, 3.1416F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
}