package com.mojang.minecraft;

import com.mojang.minecraft.level.tile.Tile;
import java.util.ArrayList;
import java.util.List;

public final class User {
	private static List creativeTiles;
	public String name;
	public String sessionId;

	public User(String var1, String var2) {
		this.name = var1;
		this.sessionId = var2;
	}

	static {
		(creativeTiles = new ArrayList()).add(Tile.rock);
		creativeTiles.add(Tile.stoneBrick);
		creativeTiles.add(Tile.dirt);
		creativeTiles.add(Tile.wood);
		creativeTiles.add(Tile.log);
		creativeTiles.add(Tile.leaf);
		creativeTiles.add(Tile.bush);
		creativeTiles.add(Tile.flower);
		creativeTiles.add(Tile.rose);
		creativeTiles.add(Tile.mushroom1);
		creativeTiles.add(Tile.mushroom2);
		creativeTiles.add(Tile.sand);
		creativeTiles.add(Tile.gravel);
		creativeTiles.add(Tile.glass);
		creativeTiles.add(Tile.sponge);
		creativeTiles.add(Tile.goldBlock);
		creativeTiles.add(Tile.clothRed);
		creativeTiles.add(Tile.clothOrange);
		creativeTiles.add(Tile.clothYellow);
		creativeTiles.add(Tile.clothChartreuse);
		creativeTiles.add(Tile.clothGreen);
		creativeTiles.add(Tile.clothSpringGreen);
		creativeTiles.add(Tile.clothCyan);
		creativeTiles.add(Tile.clothCapri);
		creativeTiles.add(Tile.clothUltramarine);
		creativeTiles.add(Tile.clothViolet);
		creativeTiles.add(Tile.clothPurple);
		creativeTiles.add(Tile.clothMagenta);
		creativeTiles.add(Tile.clothRose);
		creativeTiles.add(Tile.clothDarkGray);
		creativeTiles.add(Tile.clothGray);
		creativeTiles.add(Tile.clothWhite);
	}
}
