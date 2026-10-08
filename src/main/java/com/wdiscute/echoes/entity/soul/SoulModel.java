package com.wdiscute.echoes.entity.soul;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wdiscute.echoes.Echoes;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class SoulModel extends EntityModel<SoulEntity>
{
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Echoes.rl("soul"), "main");
    public static final ResourceLocation TEXTURE_LOCATION = Echoes.rl("textures/entity/soul.png");

    private final ModelPart root;
    private final ModelPart bb_main;

    public SoulModel(ModelPart root)
    {
        super();
        this.root = root;
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 11).addBox(-2.0F, -8.0F, 0.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -4.0F, -1.0F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -4.0F, -1.0F, 0.0F, 3.1416F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color)
    {
        root.render(poseStack,buffer, packedLight, packedOverlay, color);
    }

    @Override
    public void setupAnim(SoulEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {

    }
}