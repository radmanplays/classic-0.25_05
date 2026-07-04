package com.mojang.minecraft.mob;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.model.BaseModel;
import com.mojang.minecraft.model.HumanoidModel;
import com.mojang.minecraft.renderer.Textures;
import org.lwjgl.opengl.GL11;

public class HumanoidMob extends Mob {
	public static final long serialVersionUID = 0L;
	private boolean helmet = Math.random() < (double)0.2F;
	private boolean armor = Math.random() < (double)0.2F;

	public HumanoidMob(Level var1, float var2, float var3, float var4) {
		super(var1);
		this.setPos(var2, var3, var4);
	}

	public void renderModel(Textures var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		BaseModel var8 = modelCache.getModel(this.modelName);
		var8.render(var2, var4, (float)this.tickCount + var3, var5, var6, var7);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		if(this.allowAlpha) {
			GL11.glEnable(GL11.GL_CULL_FACE);
		}

		if(this.hasHair) {
			GL11.glDisable(GL11.GL_CULL_FACE);
			HumanoidModel var10 = (HumanoidModel)var8;
			var10.hair.yRot = var10.head.yRot;
			var10.hair.xRot = var10.head.xRot;
			var10.hair.render(var7);
			GL11.glEnable(GL11.GL_CULL_FACE);
		}

		if(this.armor || this.helmet) {
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var1.loadTexture("/armor/plate.png"));
			GL11.glDisable(GL11.GL_CULL_FACE);
			HumanoidModel var9 = (HumanoidModel)modelCache.getModel("humanoid.armor");
			var9.head.showModel = this.helmet;
			var9.body.showModel = this.armor;
			var9.rightArm.showModel = this.armor;
			var9.leftArm.showModel = this.armor;
			var9.rightLeg.showModel = false;
			var9.leftLeg.showModel = false;
			HumanoidModel var11 = (HumanoidModel)var8;
			var9.head.yRot = var11.head.yRot;
			var9.head.xRot = var11.head.xRot;
			var9.rightArm.xRot = var11.rightArm.xRot;
			var9.rightArm.zRot = var11.rightArm.zRot;
			var9.leftArm.xRot = var11.leftArm.xRot;
			var9.leftArm.zRot = var11.leftArm.zRot;
			var9.rightLeg.xRot = var11.rightLeg.xRot;
			var9.leftLeg.xRot = var11.leftLeg.xRot;
			var9.head.render(var7);
			var9.body.render(var7);
			var9.rightArm.render(var7);
			var9.leftArm.render(var7);
			var9.rightLeg.render(var7);
			var9.leftLeg.render(var7);
			GL11.glEnable(GL11.GL_CULL_FACE);
		}

		GL11.glDisable(GL11.GL_ALPHA_TEST);
	}
}
