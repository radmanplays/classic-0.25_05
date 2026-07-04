package com.mojang.minecraft.player;

public final class Inventory {
	public int[] slots = new int[9];
	public int[] count = new int[9];
	public int[] popTime = new int[9];
	public int selected = 0;

	public Inventory() {
		for(int var1 = 0; var1 < 9; ++var1) {
			this.slots[var1] = -1;
			this.count[var1] = 0;
		}

	}

	public final int getSelected() {
		return this.slots[this.selected];
	}

	public int containsTileAt(int var1) {
		for(int var2 = 0; var2 < this.slots.length; ++var2) {
			if(var1 == this.slots[var2]) {
				return var2;
			}
		}

		return -1;
	}

	public final void swapPaint(int var1) {
		if(var1 > 0) {
			var1 = 1;
		}

		if(var1 < 0) {
			var1 = -1;
		}

		for(this.selected -= var1; this.selected < 0; this.selected += this.slots.length) {
		}

		while(this.selected >= this.slots.length) {
			this.selected -= this.slots.length;
		}

	}

	public final boolean removeResource(int var1) {
		var1 = this.containsTileAt(var1);
		if(var1 < 0) {
			return false;
		} else {
			if(--this.count[var1] <= 0) {
				this.slots[var1] = -1;
			}

			return true;
		}
	}
}
