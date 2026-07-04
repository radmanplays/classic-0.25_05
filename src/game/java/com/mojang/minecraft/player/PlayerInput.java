package com.mojang.minecraft.player;

import com.mojang.minecraft.mob.ai.BasicAI;

final class PlayerInput extends BasicAI {
	private Input input;

	PlayerInput(Player var1, Input var2) {
		this.input = var2;
	}

	protected final void tick() {
		this.jumping = this.input.jumping;
		this.xxa = this.input.ya;
		this.yya = this.input.xa;
	}
}
