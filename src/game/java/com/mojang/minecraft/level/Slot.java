package com.mojang.minecraft.level;

import com.mojang.minecraft.Entity;

final class Slot {
	int xSlot;
	int ySlot;
	int zSlot;
	private BlockMap blockInMapArray;

	private Slot(BlockMap var1) {
		this.blockInMapArray = var1;
	}

	public final Slot init(float var1, float var2, float var3) {
		this.xSlot = (int)(var1 / 16.0F);
		this.ySlot = (int)(var2 / 16.0F);
		this.zSlot = (int)(var3 / 16.0F);
		if(this.xSlot < 0) {
			this.xSlot = 0;
		}

		if(this.ySlot < 0) {
			this.ySlot = 0;
		}

		if(this.zSlot < 0) {
			this.zSlot = 0;
		}

		BlockMap var4 = this.blockInMapArray;
		if(this.xSlot >= var4.width) {
			var4 = this.blockInMapArray;
			this.xSlot = var4.width - 1;
		}

		var4 = this.blockInMapArray;
		if(this.ySlot >= var4.depth) {
			var4 = this.blockInMapArray;
			this.ySlot = var4.depth - 1;
		}

		var4 = this.blockInMapArray;
		if(this.zSlot >= var4.height) {
			var4 = this.blockInMapArray;
			this.zSlot = var4.height - 1;
		}

		return this;
	}

	public final void add(Entity var1) {
		if(this.xSlot >= 0 && this.ySlot >= 0 && this.zSlot >= 0) {
			BlockMap var2 = this.blockInMapArray;
			int var10001 = this.zSlot * var2.depth + this.ySlot;
			var2 = this.blockInMapArray;
			this.blockInMapArray.entityGrid[var10001 * var2.width + this.xSlot].add(var1);
		}

	}

	public final void remove(Entity var1) {
		if(this.xSlot >= 0 && this.ySlot >= 0 && this.zSlot >= 0) {
			BlockMap var2 = this.blockInMapArray;
			int var10001 = this.zSlot * var2.depth + this.ySlot;
			var2 = this.blockInMapArray;
			this.blockInMapArray.entityGrid[var10001 * var2.width + this.xSlot].remove(var1);
		}

	}

	Slot(BlockMap var1, UnusedSyntheticClass var2) {
		this(var1);
	}
}
