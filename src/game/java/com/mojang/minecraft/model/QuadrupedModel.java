package com.mojang.minecraft.model;

public final class QuadrupedModel extends BaseModel {
	private Cube head = new Cube(0, 0);
	private Cube body;
	private Cube leg1;
	private Cube leg2;
	private Cube leg3;
	private Cube leg4;

	public QuadrupedModel() {
		this.head.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, 0.0F);
		this.head.setPos(0.0F, 12.0F, -6.0F);
		this.body = new Cube(28, 8);
		this.body.addBox(-5.0F, -10.0F, -7.0F, 10, 16, 8, 0.0F);
		this.body.setPos(0.0F, 11.0F, 2.0F);
		this.leg1 = new Cube(0, 16);
		this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.leg1.setPos(-3.0F, 18.0F, 7.0F);
		this.leg2 = new Cube(0, 16);
		this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.leg2.setPos(3.0F, 18.0F, 7.0F);
		this.leg3 = new Cube(0, 16);
		this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.leg3.setPos(-3.0F, 18.0F, -5.0F);
		this.leg4 = new Cube(0, 16);
		this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.leg4.setPos(3.0F, 18.0F, -5.0F);
	}

	public final void render(float var1, float var2, float var3, float var4, float var5, float var6) {
		this.head.yRot = var4 / 57.29578F;
		this.head.xRot = var5 / 57.29578F;
		this.body.xRot = 1.5707963F;
		this.leg1.xRot = (float)Math.cos((double)var1 * 0.6662D) * 1.4F * var2;
		this.leg2.xRot = (float)Math.cos((double)var1 * 0.6662D + Math.PI) * 1.4F * var2;
		this.leg3.xRot = (float)Math.cos((double)var1 * 0.6662D + Math.PI) * 1.4F * var2;
		this.leg4.xRot = (float)Math.cos((double)var1 * 0.6662D) * 1.4F * var2;
		this.head.render(var6);
		this.body.render(var6);
		this.leg1.render(var6);
		this.leg2.render(var6);
		this.leg3.render(var6);
		this.leg4.render(var6);
	}
}
