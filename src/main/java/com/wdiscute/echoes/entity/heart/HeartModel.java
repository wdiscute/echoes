package com.wdiscute.echoes.entity.heart;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.entity.lantern.LanternRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;

public class HeartModel extends EntityModel<SculkHeartRenderState>
{
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Echoes.rl("heart"), "main");

    public static final Identifier TEXTURE_LOCATION = Echoes.rl("textures/entity/heart.png");

    private final ModelPart bone;
    private final ModelPart bone2;
    private final ModelPart bone4;
    private final ModelPart bone5;
    private final ModelPart bone6;
    private final ModelPart bb_main;

    public HeartModel(ModelPart root)
    {
        super(root);
        this.bb_main = root.getChild("bb_main");
        this.bone = root.getChild("bone");
        this.bone2 = root.getChild("bone2");
        this.bone4 = root.getChild("bone4");
        this.bone5 = root.getChild("bone5");
        this.bone6 = root.getChild("bone6");
    }

    public static LayerDefinition createBodyLayer()
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offsetAndRotation(18.3802F, -0.3125F, -5.476F, 0.0588F, -0.4564F, 0.0998F));

        PartDefinition cube_r1 = bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(34, 106).addBox(14.6127F, -25.8024F, 3.0901F, 4.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.137F, -0.4313F, 7.7681F, -0.3056F, 0.3275F, -0.6626F));

        PartDefinition cube_r2 = bone.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(102, 101).addBox(12.4175F, -18.8301F, 2.1444F, 4.0F, 8.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(36, 56).addBox(15.7405F, -18.6525F, 3.1147F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.137F, -0.6313F, 7.7681F, -0.2698F, 0.3573F, -0.556F));

        PartDefinition cube_r3 = bone.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(112, 83).addBox(14.8563F, -13.677F, 2.6336F, 3.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(90, 88).addBox(9.9489F, -13.7352F, 1.626F, 5.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.137F, -0.6313F, 7.7681F, -0.2072F, 0.3968F, -0.3877F));

        PartDefinition cube_r4 = bone.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(102, 39).addBox(15.511F, -8.6988F, 2.0001F, 2.0F, 13.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.137F, -0.6313F, 7.7681F, -0.1634F, 0.4156F, -0.2789F));

        PartDefinition cube_r5 = bone.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(84, 39).addBox(15.8687F, -0.2838F, 1.5112F, 3.0F, 14.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(48, 23).addBox(8.2383F, -0.3568F, -0.553F, 8.0F, 13.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.137F, -0.8313F, 7.7681F, -0.0534F, 0.4439F, -0.0184F));

        PartDefinition cube_r6 = bone.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(70, 68).addBox(8.3689F, -9.0058F, 0.6772F, 7.0F, 12.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.137F, -0.6313F, 7.7681F, -0.1558F, 0.4184F, -0.26F));

        PartDefinition cube_r7 = bone.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(100, 59).addBox(11.6601F, 13.7615F, -2.9966F, 8.0F, 11.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 95).addBox(8.1764F, 16.5184F, 0.9531F, 2.0F, 9.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(56, 54).addBox(12.4613F, 13.222F, 10.1875F, 8.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(84, 0).addBox(21.6555F, 10.0925F, 0.0109F, 2.0F, 14.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(9.7246F, 7.9583F, -1.5787F, 12.0F, 17.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.137F, -0.8313F, 7.7681F, 0.0609F, 0.4409F, 0.2524F));

        PartDefinition bone2 = partdefinition.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offsetAndRotation(13.4892F, -1.5353F, 14.2463F, 0.7129F, -1.3251F, -0.7967F));

        PartDefinition cube_r8 = bone2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(116, 35).addBox(19.4469F, -11.263F, 13.992F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-20.5211F, -0.141F, 0.5985F, -0.1949F, 0.4019F, -0.4558F));

        PartDefinition cube_r9 = bone2.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(106, 12).addBox(18.2304F, -8.0696F, 12.9915F, 4.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-20.2211F, -0.141F, 0.3985F, -0.1499F, 0.4199F, -0.3433F));

        PartDefinition cube_r10 = bone2.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(116, 44).addBox(21.3914F, -7.964F, 13.9641F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-20.1211F, -0.041F, 0.3985F, -0.1499F, 0.4199F, -0.3433F));

        PartDefinition cube_r11 = bone2.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(102, 114).addBox(22.0585F, -4.998F, 13.5228F, 3.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(100, 72).addBox(17.2644F, -5.0219F, 12.5137F, 5.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-20.2211F, -0.141F, 0.3985F, -0.1106F, 0.4313F, -0.2483F));

        PartDefinition cube_r12 = bone2.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(48, 105).addBox(23.5302F, -1.2089F, 12.8827F, 2.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-20.2211F, -0.341F, 0.3985F, -0.0865F, 0.4364F, -0.1908F));

        PartDefinition cube_r13 = bone2.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(48, 87).addBox(24.2901F, 3.9923F, 12.4345F, 3.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-19.6211F, -1.441F, 0.2985F, 0.0419F, 0.4434F, 0.0869F));

        PartDefinition cube_r14 = bone2.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(0, 77).addBox(16.5504F, -1.643F, 11.559F, 7.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-20.2211F, -0.241F, 0.3985F, -0.0784F, 0.4379F, -0.1716F));

        PartDefinition cube_r15 = bone2.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(0, 56).addBox(16.6596F, 3.9194F, 10.3703F, 8.0F, 11.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-19.6211F, -1.341F, 0.2985F, 0.0419F, 0.4434F, 0.0869F));

        PartDefinition cube_r16 = bone2.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(106, 0).addBox(-3.8827F, -4.7174F, -7.3074F, 7.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 106).addBox(-8.3665F, -0.9605F, -2.3577F, 2.0F, 7.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(44, 76).addBox(-3.0816F, -3.257F, 5.8767F, 7.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(30, 87).addBox(5.1127F, -7.3864F, -2.2999F, 2.0F, 12.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 29).addBox(-6.8182F, -9.5207F, -5.8895F, 12.0F, 15.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.4564F, 23.6131F, 5.8684F, 0.1486F, 0.4164F, 0.3499F));

        PartDefinition bone4 = partdefinition.addOrReplaceChild("bone4", CubeListBuilder.create(), PartPose.offsetAndRotation(-18.2167F, 4.3917F, -0.7632F, 3.0564F, 1.0803F, 2.9748F));

        PartDefinition cube_r17 = bone4.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(116, 35).addBox(0.7556F, -15.9736F, -2.4853F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0986F, -8.6985F, 0.1482F, -0.4136F, 0.6776F, -0.6249F));

        PartDefinition cube_r18 = bone4.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(106, 12).addBox(-0.8509F, -10.8006F, -3.4859F, 4.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(116, 44).addBox(2.3101F, -10.695F, -2.5132F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4986F, -8.6985F, -0.1518F, -0.3319F, 0.7158F, -0.4977F));

        PartDefinition cube_r19 = bone4.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(102, 114).addBox(2.8117F, -6.0555F, -2.9545F, 3.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(100, 72).addBox(-1.9824F, -6.0795F, -3.9636F, 5.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4986F, -8.8985F, -0.1518F, -0.2574F, 0.7411F, -0.3858F));

        PartDefinition cube_r20 = bone4.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(48, 105).addBox(4.2545F, -1.2577F, -3.5946F, 2.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4986F, -8.9985F, -0.1518F, -0.2102F, 0.7533F, -0.3164F));

        PartDefinition cube_r21 = bone4.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(48, 87).addBox(5.3468F, 7.5578F, -4.0428F, 3.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 56).addBox(-2.2836F, 7.4849F, -6.1069F, 8.0F, 11.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.9986F, -9.1985F, -0.7518F, 0.0444F, 0.7748F, 0.0477F));

        PartDefinition cube_r22 = bone4.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(0, 77).addBox(-2.7233F, -1.3554F, -4.9184F, 7.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4986F, -8.9985F, -0.1518F, -0.1941F, 0.7568F, -0.2929F));

        PartDefinition cube_r23 = bone4.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(106, 0).addBox(3.7732F, 13.6723F, -8.4596F, 7.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 106).addBox(-0.7105F, 17.4292F, -3.5099F, 2.0F, 7.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(44, 76).addBox(4.5744F, 15.1328F, 4.7245F, 7.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(30, 87).addBox(12.7687F, 11.0033F, -3.4521F, 2.0F, 12.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 29).addBox(0.8377F, 8.8691F, -7.0417F, 12.0F, 15.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.1336F, -1.3912F, 1.2353F, 0.2028F, 0.7495F, 0.2813F));

        PartDefinition bone5 = partdefinition.addOrReplaceChild("bone5", CubeListBuilder.create(), PartPose.offsetAndRotation(5.5765F, 14.071F, -26.3849F, 0.1097F, -0.0522F, 0.1331F));

        PartDefinition cube_r24 = bone5.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(18, 95).addBox(19.6942F, 0.2058F, -10.5753F, 2.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.4518F, -12.2662F, 24.4219F, -0.101F, 0.45F, -0.0972F));

        PartDefinition cube_r25 = bone5.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(62, 113).addBox(15.9593F, -2.8253F, -10.522F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.4518F, -12.2662F, 24.4219F, -0.1664F, 0.4313F, -0.2502F));

        PartDefinition cube_r26 = bone5.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(112, 22).addBox(21.432F, 4.6596F, -11.2741F, 2.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(84, 23).addBox(15.2789F, 4.6152F, -12.6528F, 7.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.4518F, -12.2662F, 24.4219F, -0.0176F, 0.4538F, 0.0671F));

        PartDefinition cube_r27 = bone5.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(66, 100).addBox(15.4658F, -0.1997F, -11.6171F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.4518F, -12.2662F, 24.4219F, -0.0926F, 0.4516F, -0.0779F));

        PartDefinition cube_r28 = bone5.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(0, 111).addBox(17.5986F, 15.6726F, -15.3818F, 6.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(84, 59).addBox(18.0355F, 16.29F, -5.6681F, 6.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(86, 101).addBox(24.5337F, 13.0246F, -12.6815F, 2.0F, 11.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(48, 0).addBox(16.2037F, 9.7499F, -13.8204F, 9.0F, 14.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.4518F, -12.2662F, 24.4219F, 0.0515F, 0.4444F, 0.2337F));

        PartDefinition bone6 = partdefinition.addOrReplaceChild("bone6", CubeListBuilder.create(), PartPose.offsetAndRotation(-10.9768F, 13.0224F, 21.452F, -3.1302F, -0.1322F, 3.096F));

        PartDefinition cube_r29 = bone6.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(18, 95).addBox(3.5426F, 1.4173F, -0.5934F, 2.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1024F, -15.089F, 1.6229F, -0.0931F, 0.7793F, -0.1312F));

        PartDefinition cube_r30 = bone6.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(62, 113).addBox(-1.7141F, -7.3145F, -0.9624F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.7976F, -12.089F, 0.3229F, -0.2266F, 0.757F, -0.323F));

        PartDefinition cube_r31 = bone6.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(112, 22).addBox(5.5965F, 10.4575F, -1.0509F, 2.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1024F, -17.189F, 1.3229F, 0.0644F, 0.7765F, 0.0765F));

        PartDefinition cube_r32 = bone6.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(66, 100).addBox(-1.4407F, 0.3321F, -1.9172F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.6976F, -14.189F, 1.2229F, -0.0759F, 0.7807F, -0.1067F));

        PartDefinition cube_r33 = bone6.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(84, 23).addBox(-2.034F, 9.3845F, -3.1151F, 7.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.3976F, -16.089F, 0.8229F, 0.0644F, 0.7765F, 0.0765F));

        PartDefinition cube_r34 = bone6.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(0, 111).addBox(4.0368F, 15.7206F, -5.4691F, 6.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.7346F, -9.5818F, 3.61F, 0.2028F, 0.7495F, 0.2813F));

        PartDefinition cube_r35 = bone6.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(84, 59).addBox(-1.4639F, -4.1455F, 4.467F, 6.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5835F, 11.682F, 2.426F, 0.2028F, 0.7495F, 0.2813F));

        PartDefinition cube_r36 = bone6.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(86, 101).addBox(13.0322F, 12.0516F, -0.4616F, 2.0F, 11.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.9346F, -8.9818F, 3.51F, 0.2028F, 0.7495F, 0.2813F));

        PartDefinition cube_r37 = bone6.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(48, 0).addBox(2.1013F, 9.9173F, -3.0512F, 9.0F, 14.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.9346F, -9.5818F, 2.61F, 0.2028F, 0.7495F, 0.2813F));

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(66, 88).addBox(-3.0F, -29.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }
}