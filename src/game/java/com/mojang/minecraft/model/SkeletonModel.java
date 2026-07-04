package com.mojang.minecraft.model;

public final class SkeletonModel extends ZombieModel {
	public SkeletonModel() {
		this.rightArm = new Cube(40, 16);
		this.rightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, 0.0F);
		this.rightArm.setPos(-5.0F, 2.0F, 0.0F);
		this.leftArm = new Cube(40, 16);
		this.leftArm.mirror = true;
		this.leftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, 0.0F);
		this.leftArm.setPos(5.0F, 2.0F, 0.0F);
		this.rightLeg = new Cube(0, 16);
		this.rightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, 0.0F);
		this.rightLeg.setPos(-2.0F, 12.0F, 0.0F);
		this.leftLeg = new Cube(0, 16);
		this.leftLeg.mirror = true;
		this.leftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, 0.0F);
		this.leftLeg.setPos(2.0F, 12.0F, 0.0F);
	}
}
