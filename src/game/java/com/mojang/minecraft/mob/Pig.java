package com.mojang.minecraft.mob;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.model.QuadrupedModel;

public class Pig extends QuadrupedMob {
	public static final long serialVersionUID = 77479605454997290L;
	private static QuadrupedModel PIG_MODEL = new QuadrupedModel();

	public Pig(Level var1, float var2, float var3, float var4) {
		super(var1, var2, var3, var4);
		this.heightOffset = 1.72F;
		this.model = PIG_MODEL;
		this.textureName = "/mob/pig.png";
	}

	public void die(Entity var1) {
		if(var1 != null) {
			var1.awardKillScore(this, 10);
		}

		super.die(var1);
	}
}
