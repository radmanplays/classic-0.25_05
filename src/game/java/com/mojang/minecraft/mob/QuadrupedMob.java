package com.mojang.minecraft.mob;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.model.QuadrupedModel;

public class QuadrupedMob extends Mob {
	private static QuadrupedModel QUADRUPED_MODEL = new QuadrupedModel();

	public QuadrupedMob(Level var1, float var2, float var3, float var4) {
		super(var1);
		this.setSize(1.4F, 1.2F);
		this.setPos(var2, var3, var4);
		this.model = QUADRUPED_MODEL;
	}
}
