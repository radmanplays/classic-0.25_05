package com.mojang.minecraft.mob;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.model.HumanoidModel;
import com.mojang.minecraft.renderer.Textures;
import org.lwjgl.opengl.GL11;

public class HumanoidMob extends Mob {
	public static final long serialVersionUID = 77479605454997290L;
	private static HumanoidModel HUMANOID_MODEL = new HumanoidModel(0.0F);
	private static HumanoidModel ARMOR_MODEL = new HumanoidModel(1.0F);
	protected HumanoidModel humanoidModel = HUMANOID_MODEL;
	private boolean helmet = Math.random() < (double)0.2F;
	private boolean armor = Math.random() < (double)0.2F;

	public HumanoidMob(Level var1, float var2, float var3, float var4) {
		super(var1);
		this.setPos(var2, var3, var4);
	}

	public void renderModel(Textures var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		this.model.render(var2, var4, (float)this.tickCount + var3, var5, var6, var7);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		if(this.allowAlpha) {
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
			GL11.glEnable(GL11.GL_CULL_FACE);
		}

		HumanoidModel var8;
		if(this.hasHair) {
			GL11.glDisable(GL11.GL_CULL_FACE);
			var8 = this.humanoidModel;
			var8.hair.yRot = var8.head.yRot;
			var8.hair.xRot = var8.head.xRot;
			var8.hair.render(var7);
			GL11.glEnable(GL11.GL_CULL_FACE);
		}

		if(this.armor || this.helmet) {
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var1.loadTexture("/armor/plate.png"));
			GL11.glDisable(GL11.GL_CULL_FACE);
			ARMOR_MODEL.head.showModel = this.helmet;
			ARMOR_MODEL.body.showModel = this.armor;
			ARMOR_MODEL.rightArm.showModel = this.armor;
			ARMOR_MODEL.leftArm.showModel = this.armor;
			ARMOR_MODEL.rightLeg.showModel = false;
			ARMOR_MODEL.leftLeg.showModel = false;
			HumanoidModel var9 = this.humanoidModel;
			var8 = ARMOR_MODEL;
			var8.head.yRot = var9.head.yRot;
			var8.head.xRot = var9.head.xRot;
			var8.rightArm.xRot = var9.rightArm.xRot;
			var8.rightArm.zRot = var9.rightArm.zRot;
			var8.leftArm.xRot = var9.leftArm.xRot;
			var8.leftArm.zRot = var9.leftArm.zRot;
			var8.rightLeg.xRot = var9.rightLeg.xRot;
			var8.leftLeg.xRot = var9.leftLeg.xRot;
			var8.head.render(var7);
			var8.body.render(var7);
			var8.rightArm.render(var7);
			var8.leftArm.render(var7);
			var8.rightLeg.render(var7);
			var8.leftLeg.render(var7);
			GL11.glEnable(GL11.GL_CULL_FACE);
			
		}

		GL11.glDisable(GL11.GL_ALPHA_TEST);
	}
}
