package com.mojang.minecraft.mob;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.mob.ai.BasicAttackAI;
import com.mojang.minecraft.model.HumanoidModel;
import com.mojang.minecraft.model.ZombieModel;

public class Zombie extends HumanoidMob {
	private static HumanoidModel ZOMBIE_MODEL = new ZombieModel();

	public Zombie(Level var1, float var2, float var3, float var4) {
		super(var1, var2, var3, var4);
		this.model = this.humanoidModel = ZOMBIE_MODEL;
		this.textureName = "/mob/zombie.png";
		this.heightOffset = 1.62F;
		this.ai = new BasicAttackAI();
		this.ai.defaultLookAngle = 30;
	}

	public void die(Entity var1) {
		if(var1 != null) {
			var1.awardKillScore(this, 100);
		}

		super.die(var1);
	}
}
