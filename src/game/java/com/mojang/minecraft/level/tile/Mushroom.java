package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import net.lax1dude.eaglercraft.Random;

public final class Mushroom extends Flower {
	protected Mushroom(int var1, int var2) {
		super(var1, var2);
	}

	public final void tick(Level var1, int var2, int var3, int var4, Random var5) {
		int var6 = var1.getTile(var2, var3 - 1, var4);
		if(var1.isLit(var2, var3, var4) || var6 != Tile.rock.id && var6 != Tile.gravel.id && var6 != Tile.stoneBrick.id) {
			var1.setTile(var2, var3, var4, 0);
		}

	}
}
