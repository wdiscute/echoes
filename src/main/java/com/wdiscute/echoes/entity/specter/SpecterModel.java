package com.wdiscute.echoes.entity.specter;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.entity.soul.SoulRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class SpecterModel extends EntityModel<SpecterRenderState>
{
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Echoes.rl("specter"), "main");

    public static final Identifier TEXTURE_LOCATION = Echoes.rl("textures/entity/soul_trader.png");

    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart arm_right;
    private final ModelPart arm_left;

    public SpecterModel(ModelPart root)
    {
        super(root);
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
        this.arm_right = this.body.getChild("arm_right");
        this.arm_left = this.body.getChild("arm_left");
    }

    public static LayerDefinition createBodyLayer()
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(19, 25).addBox(-3.0F, -3.1F, -2.0F, 0.0F, 10.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(19, 25).addBox(2.0F, -3.1F, -2.0F, 0.0F, 10.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(24, 30).addBox(-3.0F, -3.1F, -2.0F, 5.0F, 10.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(29, 30).addBox(-3.0F, -3.1F, 3.0F, 5.0F, 10.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(25, 2).addBox(-2.0F, -3.0F, -1.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 17.1F, -0.5F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(1, 2).addBox(-3.5F, -3.5F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -6.6F, 0.5F));

        PartDefinition arm_right = body.addOrReplaceChild("arm_right", CubeListBuilder.create().texOffs(1, 28).addBox(-2.0F, -2.0F, -1.5F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.5F, -2.1F, 0.5F));

        PartDefinition arm_left = body.addOrReplaceChild("arm_left", CubeListBuilder.create().texOffs(1, 28).addBox(-1.0F, -2.0F, -1.5F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(4.5F, -2.1F, 0.5F));

        return LayerDefinition.create(meshdefinition, 40, 40);
    }

    @Override
    public void setupAnim(SpecterRenderState state)
    {
        super.setupAnim(state);

        //todo make body rotate independently of the head like vanilla mobs do
        body.yRot += state.yrot * Mth.DEG_TO_RAD;;
        head.xRot = state.xrot * Mth.DEG_TO_RAD;
    }
}