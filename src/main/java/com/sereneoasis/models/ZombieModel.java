package com.sereneoasis.models;// Made with Blockbench 4.10.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.sereneoasis.skeleton.*;

public class ZombieModel{
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -12.0F, -2.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)) //left leg
		.texOffs(0, 0).addBox(3.0F, -12.0F, -2.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)) //right leg
		.texOffs(-12, -2).addBox(-7.0F, -24.0F, -3.0F, 13.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)) //body
		.texOffs(-12, -5).addBox(-4.0F, -32.0F, -5.0F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)) //head
		.texOffs(-18, -11).addBox(4.0F, -23.0F, -11.0F, 4.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)) // right arm
		.texOffs(-18, -11).addBox(-9.0F, -23.0F, -11.0F, 4.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F)); // left arm

		return LayerDefinition.create(meshdefinition, 16, 16);
	}
}