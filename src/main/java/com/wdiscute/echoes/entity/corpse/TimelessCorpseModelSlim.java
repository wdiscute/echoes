package com.wdiscute.echoes.entity.corpse;

import com.wdiscute.echoes.Echoes;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class TimelessCorpseModelSlim extends EntityModel<TimelessCorpseRenderState>
{
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Echoes.rl("corpse_slim"), "main");
    private final ModelPart Waist;
    private final ModelPart Head;
    private final ModelPart Body;
    private final ModelPart Right_Arm;
    private final ModelPart Left_Arm;
    private final ModelPart Right_Leg;
    private final ModelPart Left_Leg;

    public TimelessCorpseModelSlim(ModelPart root)
    {
        super(root);
        this.Waist = root.getChild("Waist");
        this.Head = this.Waist.getChild("Head");
        this.Body = this.Waist.getChild("Body");
        this.Right_Arm = this.Waist.getChild("Right_Arm");
        this.Left_Arm = this.Waist.getChild("Left_Arm");
        this.Right_Leg = root.getChild("Right_Leg");
        this.Left_Leg = root.getChild("Left_Leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Waist = partdefinition.addOrReplaceChild("Waist", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, -3.0F));

        PartDefinition Head = Waist.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -12.0F, 0.0F, -0.1047F, 0.0873F, 0.0F));

        PartDefinition Head_r1 = Head.addOrReplaceChild("Head_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -8.0F, 0.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 15.0F, 9.0F, 0.48F, -0.9163F, 0.0F));

        PartDefinition Body = Waist.addOrReplaceChild("Body", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, 0.0F));

        PartDefinition Body_r1 = Body.addOrReplaceChild("Body_r1", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, -15.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 25.0F, -1.0F, -0.6981F, 0.0F, 0.0F));

        PartDefinition Right_Arm = Waist.addOrReplaceChild("Right_Arm", CubeListBuilder.create(), PartPose.offsetAndRotation(-5.0F, -10.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

        PartDefinition Right_Arm_r1 = Right_Arm.addOrReplaceChild("Right_Arm_r1", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -12.0F, 0.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 22.0F, 9.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition Left_Arm = Waist.addOrReplaceChild("Left_Arm", CubeListBuilder.create(), PartPose.offsetAndRotation(5.0F, -10.0F, 0.0F, 0.2094F, 0.0F, 0.0F));

        PartDefinition Left_Arm_r1 = Left_Arm.addOrReplaceChild("Left_Arm_r1", CubeListBuilder.create().texOffs(32, 48).addBox(-4.0F, -12.0F, 0.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 20.0F, -2.0F, -0.6378F, -0.1582F, -0.2095F));

        PartDefinition Right_Leg = partdefinition.addOrReplaceChild("Right_Leg", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.9F, 12.0F, -3.0F, 0.192F, 0.0F, 0.0349F));

        PartDefinition Right_Leg_r1 = Right_Leg.addOrReplaceChild("Right_Leg_r1", CubeListBuilder.create().texOffs(0, 16).addBox(-3.9F, -12.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.9F, 12.0F, -12.0F, -1.3963F, 0.0F, 0.0F));

        PartDefinition Left_Leg = partdefinition.addOrReplaceChild("Left_Leg", CubeListBuilder.create(), PartPose.offsetAndRotation(1.9F, 12.0F, -3.0F, -0.1745F, 0.0F, -0.0349F));

        PartDefinition Left_Leg_r1 = Left_Leg.addOrReplaceChild("Left_Leg_r1", CubeListBuilder.create().texOffs(16, 48).addBox(-0.1F, -12.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.9F, 14.0F, -7.0F, -1.1781F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}