package com.mojang.minecraft.mob.ai;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.mob.Mob;

public abstract class AI {
	public int defaultLookAngle = 0;

	public void tick(Level var1, Mob var2) {
	}

	public void beforeRemove() {
	}
}
