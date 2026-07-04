package com.mojang.minecraft.level.tile;

public final class LogTile extends Tile {
	protected LogTile(int var1) {
		super(17);
		this.tex = 20;
	}

	public final int getResourceCount() {
		return random.nextInt(3) + 3;
	}

	public final int getId() {
		return Tile.wood.id;
	}

	protected final int getTexture(int var1) {
		return var1 == 1 ? 21 : (var1 == 0 ? 21 : 20);
	}
}
