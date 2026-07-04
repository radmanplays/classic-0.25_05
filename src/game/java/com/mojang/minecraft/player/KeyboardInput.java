package com.mojang.minecraft.player;

import com.mojang.minecraft.Options;

public final class KeyboardInput extends Input {
	private boolean[] keys = new boolean[10];
	private Options options;

	public KeyboardInput(Options var1) {
		this.options = var1;
	}

	public final void setKey(int var1, boolean var2) {
		byte var3 = -1;
		if(var1 == this.options.forward.key) {
			var3 = 0;
		}

		if(var1 == this.options.back.key) {
			var3 = 1;
		}

		if(var1 == this.options.left.key) {
			var3 = 2;
		}

		if(var1 == this.options.right.key) {
			var3 = 3;
		}

		if(var1 == this.options.jump.key) {
			var3 = 4;
		}

		if(var3 >= 0) {
			this.keys[var3] = var2;
		}

	}

	public final void releaseAllKeys() {
		for(int var1 = 0; var1 < 10; ++var1) {
			this.keys[var1] = false;
		}

	}

	public final void tick() {
		this.ya = 0.0F;
		this.xa = 0.0F;
		if(this.keys[0]) {
			--this.xa;
		}

		if(this.keys[1]) {
			++this.xa;
		}

		if(this.keys[2]) {
			--this.ya;
		}

		if(this.keys[3]) {
			++this.ya;
		}

		this.jumping = this.keys[4];
	}
}
