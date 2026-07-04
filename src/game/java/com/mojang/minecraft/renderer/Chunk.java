package com.mojang.minecraft.renderer;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.player.Player;
import org.lwjgl.opengl.GL11;

public final class Chunk {
	private Level level;
	private int lists = -1;
	private static Tesselator t = Tesselator.instance;
	public static int updates = 0;
	private int x0;
	private int y0;
	private int z0;
	private int x1;
	private int y1;
	private int z1;
	public boolean isInFrustum = false;
	private boolean[] skipRenderPass = new boolean[8];

	public Chunk(Level var1, int var2, int var3, int var4, int var5, int var6) {
		this.level = var1;
		this.x0 = var2;
		this.y0 = var3;
		this.z0 = var4;
		this.x1 = this.y1 = this.z1 = 16;
		Math.sqrt((double)(this.x1 * this.x1 + this.y1 * this.y1 + this.z1 * this.z1));
		this.lists = var6;
		this.reset();
	}

	public final void rebuild(boolean var1) {
		Tile.isNormalTile = var1;
		++updates;
		int var2 = this.x0;
		int var3 = this.y0;
		int var4 = this.z0;
		int var5 = this.x0 + this.x1;
		int var6 = this.y0 + this.y1;
		int var7 = this.z0 + this.z1;

		for(int var8 = 0; var8 < 8; ++var8) {
			this.skipRenderPass[var8] = true;
			GL11.glNewList(this.lists + var8, GL11.GL_COMPILE);
			t.begin();
			byte var9 = 0;
			byte var10 = 0;
			byte var11 = 0;
			float var12 = 1.0F;
			if(var8 == 2 || var8 == 3) {
				var12 = 0.6F;
			}

			if(var8 == 4 || var8 == 5) {
				var12 = 0.8F;
			}

			if(var8 == 0) {
				var12 = 0.5F;
			}

			if(var8 == 0) {
				var10 = -1;
			}

			if(var8 == 1) {
				var10 = 1;
			}

			if(var8 == 2) {
				var11 = -1;
			}

			if(var8 == 3) {
				var11 = 1;
			}

			if(var8 == 4) {
				var9 = -1;
			}

			if(var8 == 5) {
				var9 = 1;
			}

			for(int var13 = var3; var13 < var6; ++var13) {
				int var14;
				int var15;
				int var16;
				int var17;
				int var19;
				int var20;
				float var21;
				Tile var23;
				if(var8 != 5 && var8 != 4) {
					for(var14 = var4; var14 < var7; ++var14) {
						for(var15 = var2; var15 < var5; ++var15) {
							var16 = this.level.getTile(var15, var13, var14);
							Tile var24;
							if(var8 == 6) {
								if(var16 > 0 && !Tile.isOpaque[var16] && !Tile.isLiquid[var16]) {
									var24 = Tile.tiles[var16];
									if(var24.render(t, this.level, 0, var15, var13, var14)) {
										this.skipRenderPass[var8] = false;
									}
								}
							} else if(var8 == 7) {
								if(Tile.isLiquid[var16]) {
									var24 = Tile.tiles[var16];
									if(var24.render(t, this.level, 1, var15, var13, var14)) {
										this.skipRenderPass[var8] = false;
									}
								}
							} else {
								var17 = this.level.getTile(var15 + var9, var13 + var10, var14 + var11);
								if(Tile.isOpaque[var16] && !Tile.isLiquid[var16] && !Tile.isSolid[var17]) {
									var17 = 0;
									float var25 = var12 * this.level.getBrightness(var15 + var9, var13 + var10, var14 + var11);
									if(var1) {
										while(var15 + var17 < var5) {
											++var17;
											var19 = this.level.getTile(var15 + var17, var13, var14);
											var20 = this.level.getTile(var15 + var17 + var9, var13 + var10, var14 + var11);
											var21 = var12 * this.level.getBrightness(var15 + var17 + var9, var13 + var10, var14 + var11);
											if(var25 != var21 || var19 != var16 || !Tile.isOpaque[var19] || Tile.isSolid[var20]) {
												break;
											}
										}
									} else {
										var17 = 1;
									}

									var23 = Tile.tiles[var16];
									t.color(var25, var25, var25);
									var23.renderFace(t, var15, var13, var14, var8, var17 - 1);
									this.skipRenderPass[var8] = false;
									var15 += var17 - 1;
								}
							}
						}
					}
				} else {
					for(var14 = var2; var14 < var5; ++var14) {
						for(var15 = var4; var15 < var7; ++var15) {
							var16 = this.level.getTile(var14, var13, var15);
							var17 = this.level.getTile(var14 + var9, var13 + var10, var15 + var11);
							if(Tile.isOpaque[var16] && !Tile.isLiquid[var16] && !Tile.isSolid[var17]) {
								float var22 = var12 * this.level.getBrightness(var14 + var9, var13 + var10, var15 + var11);
								int var18 = 0;
								if(var1) {
									while(var15 + var18 < var7) {
										++var18;
										var19 = this.level.getTile(var14, var13, var15 + var18);
										var20 = this.level.getTile(var14 + var9, var13 + var10, var15 + var18 + var11);
										var21 = var12 * this.level.getBrightness(var14 + var9, var13 + var10, var15 + var18 + var11);
										if(var22 != var21 || var19 != var16 || !Tile.isOpaque[var19] || Tile.isSolid[var20]) {
											break;
										}
									}
								} else {
									var18 = 1;
								}

								var23 = Tile.tiles[var16];
								t.color(var22, var22, var22);
								var23.renderFace(t, var14, var13, var15, var8, var18 - 1);
								this.skipRenderPass[var8] = false;
								var15 += var18 - 1;
							}
						}
					}
				}
			}

			t.end();
			GL11.glEndList();
		}

		Tile.isNormalTile = false;
	}

	public final float compare(Player var1) {
		float var2 = var1.x - (float)this.x0;
		float var3 = var1.y - (float)this.y0;
		float var4 = var1.z - (float)this.z0;
		return var2 * var2 + var3 * var3 + var4 * var4;
	}

	private void reset() {
		for(int var1 = 0; var1 < 8; ++var1) {
			this.skipRenderPass[var1] = true;
		}

	}

	public final void clear() {
		this.reset();
		this.level = null;
	}

	public final int render(int[] var1, int var2, int var3, float var4, float var5, float var6) {
		if(!this.isInFrustum) {
			return var2;
		} else {
			if(var3 == 0) {
				if(!this.skipRenderPass[0] && var5 < (float)(this.y0 + this.y1) + 0.5F) {
					var1[var2++] = this.lists;
				}

				if(!this.skipRenderPass[1] && var5 > (float)this.y0 - 0.5F) {
					var1[var2++] = this.lists + 1;
				}

				if(!this.skipRenderPass[2] && var6 < (float)(this.z0 + this.z1) + 0.5F) {
					var1[var2++] = this.lists + 2;
				}

				if(!this.skipRenderPass[3] && var6 > (float)this.z0 - 0.5F) {
					var1[var2++] = this.lists + 3;
				}

				if(!this.skipRenderPass[4] && var4 < (float)(this.x0 + this.x1) + 0.5F) {
					var1[var2++] = this.lists + 4;
				}

				if(!this.skipRenderPass[5] && var4 > (float)this.x0 - 0.5F) {
					var1[var2++] = this.lists + 5;
				}

				if(!this.skipRenderPass[6]) {
					var1[var2++] = this.lists + 6;
				}
			} else if(!this.skipRenderPass[7]) {
				var1[var2++] = this.lists + 7;
			}

			return var2;
		}
	}

	public final void isInFrustum(Frustum var1) {
		this.isInFrustum = var1.cubeInFrustum((float)this.x0, (float)this.y0, (float)this.z0, (float)(this.x0 + this.x1), (float)(this.y0 + this.y1), (float)(this.z0 + this.z1));
	}
}
