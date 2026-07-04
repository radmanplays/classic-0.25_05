package com.mojang.minecraft.mob.ai;

public class BasicAttackAI extends BasicAI {
	protected final void tick() {
		super.tick();
		this.attack(this.level.getPlayer());
	}
}
