package com.mojang.minecraft.mob;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.model.BaseModel;
import com.mojang.minecraft.model.CreeperModel;

public class Creeper extends Mob {
	private static BaseModel CREEPER_MODEL = new CreeperModel();

	public Creeper(Level var1, float var2, float var3, float var4) {
		super(var1);
		this.heightOffset = 1.62F;
		this.model = CREEPER_MODEL;
		this.textureName = "/mob/creeper.png";
		this.ai = new CreeperAI(this);
		this.ai.defaultLookAngle = 45;
		this.setPos(var2, var3, var4);
	}

	public float getBrightness(float var1) {
		float var2 = (float)(20 - this.health) / 20.0F;
		var2 = (float)(Math.sin((double)((float)this.tickCount + var1)) * 0.5D + 0.5D) * var2 * 0.5F + 0.25F + var2 * 0.25F;
		return var2 * super.getBrightness(var1);
	}

	public void die(Entity var1) {
		if(var1 != null) {
			var1.awardKillScore(this, 250);
		}

		super.die(var1);
	}
}
