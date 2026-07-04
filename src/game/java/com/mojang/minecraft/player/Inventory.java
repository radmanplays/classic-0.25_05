package com.mojang.minecraft.player;

import com.mojang.minecraft.User;
import com.mojang.minecraft.level.tile.Tile;
import java.io.Serializable;

public class Inventory implements Serializable {
	public static final long serialVersionUID = 0L;
	public static final int POP_TIME_DURATION = 5;
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

	public int getSelected() {
		return this.slots[this.selected];
	}

	private int containsTileAt(int var1) {
		for(int var2 = 0; var2 < this.slots.length; ++var2) {
			if(var1 == this.slots[var2]) {
				return var2;
			}
		}

		return -1;
	}

	public void grabTexture(int var1) {
		var1 = this.containsTileAt(var1);
		if(var1 >= 0) {
			this.selected = var1;
		}
	}

	public void swapPaint(int var1) {
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

	public void replaceSlot(int var1) {
		if(var1 >= 0) {
			this.replaceSlot((Tile)User.creativeTiles.get(var1));
		}

	}

	public void replaceSlot(Tile var1) {
		if(var1 != null) {
			int var2 = this.containsTileAt(var1.id);
			if(var2 >= 0) {
				this.slots[var2] = this.slots[this.selected];
			}

			this.slots[this.selected] = var1.id;
		}

	}

	public boolean addResource(int var1) {
		int var2 = this.containsTileAt(var1);
		if(var2 < 0) {
			var2 = this.containsTileAt(-1);
		}

		if(var2 < 0) {
			return false;
		} else if(this.count[var2] >= 99) {
			return false;
		} else {
			this.slots[var2] = var1;
			++this.count[var2];
			this.popTime[var2] = 5;
			return true;
		}
	}

	public void tick() {
		for(int var1 = 0; var1 < this.popTime.length; ++var1) {
			if(this.popTime[var1] > 0) {
				--this.popTime[var1];
			}
		}

	}

	public boolean removeResource(int var1) {
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
