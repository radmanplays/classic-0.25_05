package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.renderer.Tesselator;
import net.lax1dude.eaglercraft.Random;

public class Flower extends Tile {
	protected Flower(int var1, int var2) {
		super(var1);
		this.tex = var2;
		this.setTicking(true);
		float var3 = 0.2F;
		this.setShape(0.5F - var3, 0.0F, 0.5F - var3, var3 + 0.5F, var3 * 2.0F, var3 + 0.5F);
	}

	public void tick(Level var1, int var2, int var3, int var4, Random var5) {
		int var6 = var1.getTile(var2, var3 - 1, var4);
		if(!var1.isLit(var2, var3, var4) || var6 != Tile.dirt.id && var6 != Tile.grass.id) {
			var1.setTile(var2, var3, var4, 0);
		}

	}

	public final boolean render(Tesselator var1, Level var2, int var3, int var4, int var5, int var6) {
		float var7 = var2.getBrightness(var4, var5, var6);
		var1.color(var7, var7, var7);
		this.renderFlower(var1, (float)var4, (float)var5, (float)var6);
		return true;
	}

	private void renderFlower(Tesselator var1, float var2, float var3, float var4) {
		int var15 = this.getTexture(15);
		int var5;
		float var6;
		float var7;
		int var8;
		float var16;
		float var17;
		if(!isNormalTile) {
			var5 = var15 % 16 << 4;
			var8 = var15 / 16 << 4;
			var16 = (float)var5 / 256.0F;
			var17 = ((float)var5 + 15.99F) / 256.0F;
			var6 = (float)var8 / 256.0F;
			var7 = ((float)var8 + 15.99F) / 256.0F;
		} else {
			var5 = var15 % 16;
			var8 = (var5 << 4) + var15 / 16 << 4;
			var16 = 0.0F;
			var17 = 1.0F;
			var6 = (float)var8 / 4096.0F;
			var7 = ((float)var8 + 15.99F) / 4096.0F;
		}

		for(var8 = 0; var8 < 2; ++var8) {
			float var9 = (float)(Math.sin((double)var8 * Math.PI / (double)2 + Math.PI * 0.25D) * 0.5D);
			float var10 = (float)(Math.cos((double)var8 * Math.PI / (double)2 + Math.PI * 0.25D) * 0.5D);
			float var11 = var2 + 0.5F - var9;
			var9 += var2 + 0.5F;
			float var13 = var3 + 1.0F;
			float var14 = var4 + 0.5F - var10;
			var10 += var4 + 0.5F;
			var1.vertexUV(var11, var13, var14, var17, var6);
			var1.vertexUV(var9, var13, var10, var16, var6);
			var1.vertexUV(var9, var3, var10, var16, var7);
			var1.vertexUV(var11, var3, var14, var17, var7);
			var1.vertexUV(var9, var13, var10, var17, var6);
			var1.vertexUV(var11, var13, var14, var16, var6);
			var1.vertexUV(var11, var3, var14, var16, var7);
			var1.vertexUV(var9, var3, var10, var17, var7);
		}

	}

	public final AABB getTileAABB(int var1, int var2, int var3) {
		return null;
	}

	public final boolean blocksLight() {
		return false;
	}

	public final boolean isSolid() {
		return false;
	}

	public final void renderGuiTile(Tesselator var1) {
		var1.setNormal(0.0F, 1.0F, 0.0F);
		var1.begin();
		this.renderFlower(var1, 0.0F, 0.4F, -0.3F);
		var1.end();
	}

	public final boolean isOpaque() {
		return false;
	}
}
