package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import net.lax1dude.eaglercraft.Random;

public final class Bush extends Flower {
	protected Bush(int var1, int var2) {
		super(6, 15);
	}

	public final void tick(Level var1, int var2, int var3, int var4, Random var5) {
		int var6 = var1.getTile(var2, var3 - 1, var4);
		if(var1.isLit(var2, var3, var4) && (var6 == Tile.dirt.id || var6 == Tile.grass.id)) {
			if(var5.nextInt(5) == 0) {
				var1.setTileNoUpdate(var2, var3, var4, 0);
				if(!var1.maybeGrowTree(var2, var3, var4)) {
					var1.setTileNoUpdate(var2, var3, var4, this.id);
				}
			}

		} else {
			var1.setTile(var2, var3, var4, 0);
		}
	}
}
