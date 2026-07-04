package com.mojang.minecraft.mob;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.model.HumanoidModel;
import com.mojang.minecraft.model.SkeletonModel;

public class Skeleton extends Zombie {
	private static HumanoidModel skeletonModel = new SkeletonModel();

	public Skeleton(Level var1, float var2, float var3, float var4) {
		super(var1, var2, var3, var4);
		this.model = this.humanoidModel = skeletonModel;
		this.textureName = "/mob/skeleton.png";
		allowAlpha = true;
	}
}
