package com.mojang.minecraft.tilerenderer;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.level.tile.Tile;

public final class TileRenderer {
	public Minecraft minecraft;
	public Tile tile = null;
	public float progress = 0.0F;
	public float oProgress = 0.0F;
	public int rot = 0;
	public boolean move = false;

	public TileRenderer(Minecraft var1) {
		this.minecraft = var1;
	}
}
