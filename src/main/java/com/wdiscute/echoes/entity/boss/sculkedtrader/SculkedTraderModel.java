package com.wdiscute.echoes.entity.boss.sculkedtrader;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.utils.ScreenUtils;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SculkedTraderModel extends HierarchicalModel<SculkedTraderEntity>
{
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Echoes.rl("sculked_trader"), "main");

    public static final ResourceLocation TEXTURE_LOCATION = Echoes.rl("textures/entity/sculked_trader.png");

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart arm_left;
    private final ModelPart Sword;
    private final ModelPart arm_right;
    private final ModelPart body;

    public SculkedTraderModel(ModelPart root)
    {
        super(RenderType::entityTranslucent);
        this.root = root.getChild("root");
        this.head = this.root.getChild("head");
        this.arm_left = this.root.getChild("arm_left");
        this.Sword = this.arm_left.getChild("Sword");
        this.arm_right = this.root.getChild("arm_right");
        this.body = this.root.getChild("body");
    }

    public static LayerDefinition createBodyLayer()
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -3.5F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 36).addBox(-4.0F, -4.5F, -4.0F, 8.0F, 5.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(-3.0F, -3.25F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.5F, 0.0F));

        PartDefinition arm_left = root.addOrReplaceChild("arm_left", CubeListBuilder.create().texOffs(0, 26).addBox(-1.0F, -1.0F, -1.5F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(38, 33).addBox(-1.5F, -1.5F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -3.0F, 0.0F));

        PartDefinition Sword = arm_left.addOrReplaceChild("Sword", CubeListBuilder.create().texOffs(100, 0).addBox(-1.0F, -2.0F, -11.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 5.0F, -0.5F));

        PartDefinition arm_right = root.addOrReplaceChild("arm_right", CubeListBuilder.create().texOffs(0, 26).addBox(-2.0F, -1.0F, -1.5F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(38, 44).addBox(-2.5F, -1.5F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -3.0F, 0.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(24, 14).addBox(-3.0F, -3.1F, -2.0F, 5.0F, 10.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(12, 26).addBox(-2.0F, -3.0F, -1.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.1F, -0.5F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public ModelPart root()
    {
        return root;
    }

    @Override
    public void setupAnim(SculkedTraderEntity e, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        // procedural stuff first (head look, walk), animations layer on top
        this.head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
        this.head.xRot = headPitch * Mth.DEG_TO_RAD;

        this.animate(e.spinAS, SculkedTraderAnimations.SPIN_ATTACK, ageInTicks);
        this.animate(e.frontSmackAS, SculkedTraderAnimations.FRONT_SMACK, ageInTicks);
        this.animate(e.frontSlashAS, SculkedTraderAnimations.FRONT_SLASH, ageInTicks);
        this.animate(e.frontStabAS, SculkedTraderAnimations.FRONT_STAND, ageInTicks);
    }
}