package com.mojang.minecraft.gamemode;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.player.Player;

public final class SurvivalGameMode extends GameMode {
	private int x = -1;
	private int y = -1;
	private int z = -1;
	private int oDestroyProgress = 0;
	private int destroyProgress = 0;
	private int delay = 0;

	public SurvivalGameMode(Minecraft var1) {
		super(var1);
	}

	public final void destroyBlock(int var1, int var2, int var3) {
		int var4 = this.minecraft.level.getTile(var1, var2, var3);
		Tile.tiles[var4].spawnResources(this.minecraft.level, var1, var2, var3);
		super.destroyBlock(var1, var2, var3);
	}

	public final boolean removeResource(int var1) {
		return this.minecraft.player.inventory.removeResource(var1);
	}

	public final void startDestroyBlock(int var1, int var2, int var3) {
		int var4 = this.minecraft.level.getTile(var1, var2, var3);
		if(var4 > 0 && Tile.tiles[var4].getDestroyProgress() == 0) {
			this.destroyBlock(var1, var2, var3);
		}

	}

	public final void tick() {
		this.oDestroyProgress = 0;
		this.delay = 0;
	}

	public final void stopDestroyingBlock(int var1, int var2, int var3, int var4) {
		if(this.delay > 0) {
			--this.delay;
		} else if(var1 == this.x && var2 == this.y && var3 == this.z) {
			int var5 = this.minecraft.level.getTile(var1, var2, var3);
			if(var5 != 0) {
				Tile var6 = Tile.tiles[var5];
				this.destroyProgress = var6.getDestroyProgress();
				var6.destroy(this.minecraft.level, var1, var2, var3, var4, this.minecraft.particleEngine);
				++this.oDestroyProgress;
				if(this.oDestroyProgress == this.destroyProgress + 1) {
					this.destroyBlock(var1, var2, var3);
					this.oDestroyProgress = 0;
					this.delay = 5;
				}

			}
		} else {
			this.oDestroyProgress = 0;
			this.x = var1;
			this.y = var2;
			this.z = var3;
		}
	}

	public final void render(float var1) {
		if(this.oDestroyProgress <= 0) {
			this.minecraft.levelRenderer.hurtTime = 0.0F;
		} else {
			this.minecraft.levelRenderer.hurtTime = ((float)this.oDestroyProgress + var1 - 1.0F) / (float)this.destroyProgress;
		}
	}

	public final float getPickRange() {
		return 4.0F;
	}

	public final boolean removeResource(Player var1, int var2) {
		Tile var3 = Tile.tiles[var2];
		if(var3 == Tile.mushroom2 && this.minecraft.player.inventory.removeResource(var2)) {
			var1.hurt((Entity)null, 3);
			return true;
		} else if(var3 == Tile.mushroom1 && this.minecraft.player.inventory.removeResource(var2)) {
			var1.heal(5);
			return true;
		} else {
			return false;
		}
	}
}
