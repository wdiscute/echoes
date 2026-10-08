package com.wdiscute.echoes.entity.trader;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wdiscute.echoes.Echoes;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class SoulTraderModel extends EntityModel<SoulTraderEntity>
{
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Echoes.rl("soul_trader"), "main");

    public static final ResourceLocation TEXTURE_LOCATION = Echoes.rl("textures/entity/soul_trader.png");

    private final ModelPart root;
    private final ModelPart bb_main;

    public SoulTraderModel(ModelPart root)
    {
        super();
        this.root = root;
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

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color)
    {
        root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    @Override
    public void setupAnim(SoulTraderEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {

    }
}