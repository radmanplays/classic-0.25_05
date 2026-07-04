package com.mojang.minecraft.gamemode;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.player.Player;

public class GameMode {
	protected final Minecraft minecraft;
	public boolean mode = false;

	public GameMode(Minecraft var1) {
		this.minecraft = var1;
	}

	public void startDestroyBlock(int var1, int var2, int var3) {
		this.destroyBlock(var1, var2, var3);
	}

	public boolean removeResource(int var1) {
		return true;
	}

	public void destroyBlock(int var1, int var2, int var3) {
		Level var4 = this.minecraft.level;
		Tile var5 = Tile.tiles[var4.getTile(var1, var2, var3)];
		boolean var6 = var4.netSetTile(var1, var2, var3, 0);
		if(var5 != null && var6) {

			if(var5.soundType != Tile.SoundType.none) {
				var4.playSound("step." + var5.soundType.name, (float)var1, (float)var2, (float)var3, (var5.soundType.getVolume() + 1.0F) / 2.0F, var5.soundType.getPitch() * 0.8F);
			}

			var5.destroy(var4, var1, var2, var3, this.minecraft.particleEngine);
		}

	}

	public void stopDestroyBlock(int var1, int var2, int var3, int var4) {
	}

	public void tick() {
	}

	public void render(float var1) {
	}

	public float getPickRange() {
		return 5.0F;
	}

	public boolean removeResource(Player var1, int var2) {
		return false;
	}
}
