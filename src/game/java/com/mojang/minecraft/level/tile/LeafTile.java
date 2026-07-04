package com.mojang.minecraft.level.tile;

public final class LeafTile extends BaseLeafTile {
	protected LeafTile(int var1, int var2) {
		super(18, 22, true);
	}

	public final int getResourceCount() {
		return random.nextInt(6) == 0 ? 1 : 0;
	}

	public final int getId() {
		return Tile.bush.id;
	}
}
