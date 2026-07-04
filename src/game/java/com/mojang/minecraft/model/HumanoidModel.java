package com.mojang.minecraft.model;

public class HumanoidModel extends BaseModel {
	public Cube head;
	public Cube hair;
	public Cube body;
	public Cube rightArm;
	public Cube leftArm;
	public Cube rightLeg;
	public Cube leftLeg;

	public HumanoidModel() {
		this(0.0F);
	}

	public HumanoidModel(float var1) {
		this.head = new Cube(0, 0);
		this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, var1);
		this.hair = new Cube(32, 0);
		this.hair.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, var1 + 0.5F);
		this.body = new Cube(16, 16);
		this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, var1);
		this.rightArm = new Cube(40, 16);
		this.rightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, var1);
		this.rightArm.setPos(-5.0F, 2.0F, 0.0F);
		this.leftArm = new Cube(40, 16);
		this.leftArm.mirror = true;
		this.leftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, var1);
		this.leftArm.setPos(5.0F, 2.0F, 0.0F);
		this.rightLeg = new Cube(0, 16);
		this.rightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, var1);
		this.rightLeg.setPos(-2.0F, 12.0F, 0.0F);
		this.leftLeg = new Cube(0, 16);
		this.leftLeg.mirror = true;
		this.leftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, var1);
		this.leftLeg.setPos(2.0F, 12.0F, 0.0F);
	}

	public final void render(float var1, float var2, float var3, float var4, float var5, float var6) {
		this.setupAnim(var1, var2, var3, var4, var5, var6);
		this.head.render(var6);
		this.body.render(var6);
		this.rightArm.render(var6);
		this.leftArm.render(var6);
		this.rightLeg.render(var6);
		this.leftLeg.render(var6);
	}

	public void setupAnim(float var1, float var2, float var3, float var4, float var5, float var6) {
		this.head.yRot = var4 / 57.29578F;
		this.head.xRot = var5 / 57.29578F;
		this.rightArm.xRot = (float)Math.cos((double)var1 * 0.6662D + Math.PI) * 2.0F * var2;
		this.rightArm.zRot = (float)(Math.cos((double)var1 * 0.2312D) + 1.0D) * var2;
		this.leftArm.xRot = (float)Math.cos((double)var1 * 0.6662D) * 2.0F * var2;
		this.leftArm.zRot = (float)(Math.cos((double)var1 * 0.2812D) - 1.0D) * var2;
		this.rightLeg.xRot = (float)Math.cos((double)var1 * 0.6662D) * 1.4F * var2;
		this.leftLeg.xRot = (float)Math.cos((double)var1 * 0.6662D + Math.PI) * 1.4F * var2;
		this.rightArm.zRot += (float)Math.cos((double)var3 * 0.09D) * 0.05F + 0.05F;
		this.leftArm.zRot -= (float)Math.cos((double)var3 * 0.09D) * 0.05F + 0.05F;
		this.rightArm.xRot += (float)Math.sin((double)var3 * 0.067D) * 0.05F;
		this.leftArm.xRot -= (float)Math.sin((double)var3 * 0.067D) * 0.05F;
	}
}
