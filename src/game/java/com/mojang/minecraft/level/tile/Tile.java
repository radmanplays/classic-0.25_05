package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.item.Item;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.particle.Particle;
import com.mojang.minecraft.particle.ParticleEngine;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.renderer.Tesselator;
import net.lax1dude.eaglercraft.Random;

public class Tile {
	public static boolean isNormalTile = true;
	protected static Random random = new Random();
	public static final Tile[] tiles = new Tile[256];
	public static final boolean[] shouldTick = new boolean[256];
	public static final boolean[] isSolid = new boolean[256];
	public static final boolean[] isOpaque = new boolean[256];
	public static final boolean[] isLiquid = new boolean[256];
	private static int[] tickSpeed = new int[256];
	public static final Tile rock = (new Tile(1, 1)).setSoundAndGravity(Tile.SoundType.stone, 1.0F, 1.0F, 1.0F);
	public static final Tile grass = (new GrassTile(2)).setSoundAndGravity(Tile.SoundType.grass, 0.9F, 1.0F, 0.6F);
	public static final Tile dirt = (new DirtTile(3, 2)).setSoundAndGravity(Tile.SoundType.grass, 0.8F, 1.0F, 0.5F);
	public static final Tile stoneBrick = (new Tile(4, 16)).setSoundAndGravity(Tile.SoundType.stone, 1.0F, 1.0F, 2.0F);
	public static final Tile wood = (new Tile(5, 4)).setSoundAndGravity(Tile.SoundType.wood, 1.0F, 1.0F, 2.0F);
	public static final Tile bush = (new Bush(6, 15)).setSoundAndGravity(Tile.SoundType.none, 0.7F, 1.0F, 0.0F);
	public static final Tile unbreakable = (new Tile(7, 17)).setSoundAndGravity(Tile.SoundType.stone, 1.0F, 1.0F, 100.0F);
	public static final Tile water = (new LiquidTile(8, Liquid.water)).setSoundAndGravity(Tile.SoundType.none, 1.0F, 1.0F, 100.0F);
	public static final Tile calmWater = (new CalmLiquidTile(9, Liquid.water)).setSoundAndGravity(Tile.SoundType.none, 1.0F, 1.0F, 100.0F);
	public static final Tile lava = (new LiquidTile(10, Liquid.lava)).setSoundAndGravity(Tile.SoundType.none, 1.0F, 1.0F, 100.0F);
	public static final Tile calmLava = (new CalmLiquidTile(11, Liquid.lava)).setSoundAndGravity(Tile.SoundType.none, 1.0F, 1.0F, 100.0F);
	public static final Tile sand = (new FallingTile(12, 18)).setSoundAndGravity(Tile.SoundType.gravel, 0.8F, 1.0F, 0.5F);
	public static final Tile gravel = (new FallingTile(13, 19)).setSoundAndGravity(Tile.SoundType.gravel, 0.8F, 1.0F, 0.6F);
	public static final Tile goldOre = (new Tile(14, 32)).setSoundAndGravity(Tile.SoundType.stone, 1.0F, 1.0F, 3.0F);
	public static final Tile ironOre = (new Tile(15, 33)).setSoundAndGravity(Tile.SoundType.stone, 1.0F, 1.0F, 3.0F);
	public static final Tile coalOre = (new Tile(16, 34)).setSoundAndGravity(Tile.SoundType.stone, 1.0F, 1.0F, 3.0F);
	public static final Tile log = (new LogTile(17)).setSoundAndGravity(Tile.SoundType.wood, 1.0F, 1.0F, 2.5F);
	public static final Tile leaf = (new LeafTile(18, 22)).setSoundAndGravity(Tile.SoundType.grass, 1.0F, 0.4F, 0.2F);
	public static final Tile sponge = (new SpongeTile(19)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 0.9F, 0.6F);
	public static final Tile glass = (new GlassTile(20, 49, false)).setSoundAndGravity(Tile.SoundType.metal, 1.0F, 1.0F, 0.3F);
	public static final Tile clothRed = (new Tile(21, 64)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothOrange = (new Tile(22, 65)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothYellow = (new Tile(23, 66)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothChartreuse = (new Tile(24, 67)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothGreen = (new Tile(25, 68)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothSpringGreen = (new Tile(26, 69)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothCyan = (new Tile(27, 70)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothCapri = (new Tile(28, 71)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothUltramarine = (new Tile(29, 72)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothViolet = (new Tile(30, 73)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothPurple = (new Tile(31, 74)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothMagenta = (new Tile(32, 75)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothRose = (new Tile(33, 76)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothDarkGray = (new Tile(34, 77)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothGray = (new Tile(35, 78)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile clothWhite = (new Tile(36, 79)).setSoundAndGravity(Tile.SoundType.cloth, 1.0F, 1.0F, 0.8F);
	public static final Tile flower = (new Flower(37, 13)).setSoundAndGravity(Tile.SoundType.none, 0.7F, 1.0F, 0.0F);
	public static final Tile rose = (new Flower(38, 12)).setSoundAndGravity(Tile.SoundType.none, 0.7F, 1.0F, 0.0F);
	public static final Tile mushroom1 = (new Mushroom(39, 29)).setSoundAndGravity(Tile.SoundType.none, 0.7F, 1.0F, 0.0F);
	public static final Tile mushroom2 = (new Mushroom(40, 28)).setSoundAndGravity(Tile.SoundType.none, 0.7F, 1.0F, 0.0F);
	public static final Tile goldBlock = (new Tile(41, 40)).setSoundAndGravity(Tile.SoundType.metal, 0.7F, 1.0F, 3.0F);
	public int tex;
	public final int id;
	public Tile.SoundType soundType;
	private int destroyProgress;
	private float xx0;
	private float yy0;
	private float zz0;
	private float xx1;
	private float yy1;
	private float zz1;
	public float particleGravity;

	protected Tile(int var1) {
		tiles[var1] = this;
		this.id = var1;
		this.setShape(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		isSolid[var1] = this.isSolid();
		isOpaque[var1] = this.isOpaque();
		isLiquid[var1] = false;
	}

	public boolean isOpaque() {
		return true;
	}

	protected final Tile setSoundAndGravity(Tile.SoundType var1, float var2, float var3, float var4) {
		this.particleGravity = var3;
		this.soundType = var1;
		this.destroyProgress = (int)(var4 * 20.0F);
		return this;
	}

	protected final void setTicking(boolean var1) {
		shouldTick[this.id] = var1;
	}

	protected final void setShape(float var1, float var2, float var3, float var4, float var5, float var6) {
		this.xx0 = var1;
		this.yy0 = var2;
		this.zz0 = var3;
		this.xx1 = var4;
		this.yy1 = var5;
		this.zz1 = var6;
	}

	protected Tile(int var1, int var2) {
		this(var1);
		this.tex = var2;
	}

	public final void setTickSpeed(int var1) {
		tickSpeed[this.id] = 16;
	}

	public boolean render(Tesselator var1, Level var2, int var3, int var4, int var5, int var6) {
		boolean var7 = false;
		float var8 = 0.5F;
		float var9 = 0.8F;
		float var10 = 0.6F;
		float var11;
		if(this.shouldRenderFace(var2, var4, var5 - 1, var6, var3, 0)) {
			var11 = this.getBrightness(var2, var4, var5 - 1, var6);
			var1.color(var8 * var11, var8 * var11, var8 * var11);
			this.renderFace(var1, var4, var5, var6, 0);
			var7 = true;
		}

		if(this.shouldRenderFace(var2, var4, var5 + 1, var6, var3, 1)) {
			var11 = this.getBrightness(var2, var4, var5 + 1, var6);
			var1.color(var11 * 1.0F, var11 * 1.0F, var11 * 1.0F);
			this.renderFace(var1, var4, var5, var6, 1);
			var7 = true;
		}

		if(this.shouldRenderFace(var2, var4, var5, var6 - 1, var3, 2)) {
			var11 = this.getBrightness(var2, var4, var5, var6 - 1);
			var1.color(var9 * var11, var9 * var11, var9 * var11);
			this.renderFace(var1, var4, var5, var6, 2);
			var7 = true;
		}

		if(this.shouldRenderFace(var2, var4, var5, var6 + 1, var3, 3)) {
			var11 = this.getBrightness(var2, var4, var5, var6 + 1);
			var1.color(var9 * var11, var9 * var11, var9 * var11);
			this.renderFace(var1, var4, var5, var6, 3);
			var7 = true;
		}

		if(this.shouldRenderFace(var2, var4 - 1, var5, var6, var3, 4)) {
			var11 = this.getBrightness(var2, var4 - 1, var5, var6);
			var1.color(var10 * var11, var10 * var11, var10 * var11);
			this.renderFace(var1, var4, var5, var6, 4);
			var7 = true;
		}

		if(this.shouldRenderFace(var2, var4 + 1, var5, var6, var3, 5)) {
			var11 = this.getBrightness(var2, var4 + 1, var5, var6);
			var1.color(var10 * var11, var10 * var11, var10 * var11);
			this.renderFace(var1, var4, var5, var6, 5);
			var7 = true;
		}

		return var7;
	}

	protected float getBrightness(Level var1, int var2, int var3, int var4) {
		return var1.getBrightness(var2, var3, var4);
	}

	public boolean shouldRenderFace(Level var1, int var2, int var3, int var4, int var5, int var6) {
		return var5 == 1 ? false : !var1.isSolidTile(var2, var3, var4);
	}

	protected int getTexture(int var1) {
		return this.tex;
	}

	public void renderFace(Tesselator var1, int var2, int var3, int var4, int var5) {
		int var6 = this.getTexture(var5);
		this.renderFaceNoTexture(var1, var2, var3, var4, var5, var6);
	}

	public final void renderFaceNoTexture(Tesselator var1, int var2, int var3, int var4, int var5, int var6) {
		int var7 = var6 % 16 << 4;
		var6 = var6 / 16 << 4;
		float var8 = (float)var7 / 256.0F;
		float var17 = ((float)var7 + 15.99F) / 256.0F;
		float var9 = (float)var6 / 256.0F;
		float var16 = ((float)var6 + 15.99F) / 256.0F;
		float var10 = (float)var2 + this.xx0;
		float var14 = (float)var2 + this.xx1;
		float var11 = (float)var3 + this.yy0;
		float var15 = (float)var3 + this.yy1;
		float var12 = (float)var4 + this.zz0;
		float var13 = (float)var4 + this.zz1;
		if(var5 == 0) {
			var1.vertexUV(var10, var11, var13, var8, var16);
			var1.vertexUV(var10, var11, var12, var8, var9);
			var1.vertexUV(var14, var11, var12, var17, var9);
			var1.vertexUV(var14, var11, var13, var17, var16);
		} else if(var5 == 1) {
			var1.vertexUV(var14, var15, var13, var17, var16);
			var1.vertexUV(var14, var15, var12, var17, var9);
			var1.vertexUV(var10, var15, var12, var8, var9);
			var1.vertexUV(var10, var15, var13, var8, var16);
		} else if(var5 == 2) {
			var1.vertexUV(var10, var15, var12, var17, var9);
			var1.vertexUV(var14, var15, var12, var8, var9);
			var1.vertexUV(var14, var11, var12, var8, var16);
			var1.vertexUV(var10, var11, var12, var17, var16);
		} else if(var5 == 3) {
			var1.vertexUV(var10, var15, var13, var8, var9);
			var1.vertexUV(var10, var11, var13, var8, var16);
			var1.vertexUV(var14, var11, var13, var17, var16);
			var1.vertexUV(var14, var15, var13, var17, var9);
		} else if(var5 == 4) {
			var1.vertexUV(var10, var15, var13, var17, var9);
			var1.vertexUV(var10, var15, var12, var8, var9);
			var1.vertexUV(var10, var11, var12, var8, var16);
			var1.vertexUV(var10, var11, var13, var17, var16);
		} else if(var5 == 5) {
			var1.vertexUV(var14, var11, var13, var8, var16);
			var1.vertexUV(var14, var11, var12, var17, var16);
			var1.vertexUV(var14, var15, var12, var17, var9);
			var1.vertexUV(var14, var15, var13, var8, var9);
		}
	}

	public final void renderFace(Tesselator var1, int var2, int var3, int var4, int var5, int var6) {
		int var7 = this.getTexture(var5);
		int var10006 = var7;
		var7 = var6;
		var6 = var10006;
		float var8;
		float var9;
		float var10;
		int var11;
		int var12;
		float var18;
		if(!isNormalTile) {
			var11 = var6 % 16 << 4;
			var12 = var6 / 16 << 4;
			var18 = (float)var11 / 256.0F;
			var8 = ((float)var11 + 15.99F) / 256.0F;
			var9 = (float)var12 / 256.0F;
			var10 = ((float)var12 + 15.99F) / 256.0F;
		} else {
			var11 = var6 % 16;
			var12 = (var11 << 4) + var6 / 16 << 4;
			var18 = 0.0F;
			var8 = 0.0F;
			var9 = (float)var12 / 4096.0F;
			var10 = ((float)var12 + 15.99F) / 4096.0F;
			var8 = 1.0F + (float)var7;
		}

		float var19 = 0.001F;
		float var20 = (float)var2 + this.xx0 - var19;
		float var16 = (float)var2 + this.xx1 + var19;
		float var13 = (float)var3 + this.yy0 - var19;
		float var17 = (float)var3 + this.yy1 + var19;
		float var14 = (float)var4 + this.zz0 - var19;
		float var15 = (float)var4 + this.zz1 - var19;
		if(var5 == 0) {
			var16 += (float)var7;
			var1.vertexUV(var20, var13, var15, var18, var10);
			var1.vertexUV(var20, var13, var14, var18, var9);
			var1.vertexUV(var16, var13, var14, var8, var9);
			var1.vertexUV(var16, var13, var15, var8, var10);
		} else if(var5 == 1) {
			var16 += (float)var7;
			var1.vertexUV(var16, var17, var15, var8, var10);
			var1.vertexUV(var16, var17, var14, var8, var9);
			var1.vertexUV(var20, var17, var14, var18, var9);
			var1.vertexUV(var20, var17, var15, var18, var10);
		} else if(var5 == 2) {
			var16 += (float)var7;
			var1.vertexUV(var20, var17, var14, var8, var9);
			var1.vertexUV(var16, var17, var14, var18, var9);
			var1.vertexUV(var16, var13, var14, var18, var10);
			var1.vertexUV(var20, var13, var14, var8, var10);
		} else if(var5 == 3) {
			var16 += (float)var7;
			var1.vertexUV(var20, var17, var15, var18, var9);
			var1.vertexUV(var20, var13, var15, var18, var10);
			var1.vertexUV(var16, var13, var15, var8, var10);
			var1.vertexUV(var16, var17, var15, var8, var9);
		} else if(var5 == 4) {
			var15 += (float)var7;
			var1.vertexUV(var20, var17, var15, var8, var9);
			var1.vertexUV(var20, var17, var14, var18, var9);
			var1.vertexUV(var20, var13, var14, var18, var10);
			var1.vertexUV(var20, var13, var15, var8, var10);
		} else {
			if(var5 == 5) {
				var15 += (float)var7;
				var1.vertexUV(var16, var13, var15, var18, var10);
				var1.vertexUV(var16, var13, var14, var8, var10);
				var1.vertexUV(var16, var17, var14, var8, var9);
				var1.vertexUV(var16, var17, var15, var18, var9);
			}

		}
	}

	public final void renderBackFace(Tesselator var1, int var2, int var3, int var4, int var5) {
		int var6 = this.getTexture(var5);
		float var7 = (float)(var6 % 16) / 16.0F;
		float var8 = var7 + 0.999F / 16.0F;
		float var16 = (float)(var6 / 16) / 16.0F;
		float var9 = var16 + 0.999F / 16.0F;
		float var10 = (float)var2 + this.xx0;
		float var14 = (float)var2 + this.xx1;
		float var11 = (float)var3 + this.yy0;
		float var15 = (float)var3 + this.yy1;
		float var12 = (float)var4 + this.zz0;
		float var13 = (float)var4 + this.zz1;
		if(var5 == 0) {
			var1.vertexUV(var14, var11, var13, var8, var9);
			var1.vertexUV(var14, var11, var12, var8, var16);
			var1.vertexUV(var10, var11, var12, var7, var16);
			var1.vertexUV(var10, var11, var13, var7, var9);
		}

		if(var5 == 1) {
			var1.vertexUV(var10, var15, var13, var7, var9);
			var1.vertexUV(var10, var15, var12, var7, var16);
			var1.vertexUV(var14, var15, var12, var8, var16);
			var1.vertexUV(var14, var15, var13, var8, var9);
		}

		if(var5 == 2) {
			var1.vertexUV(var10, var11, var12, var8, var9);
			var1.vertexUV(var14, var11, var12, var7, var9);
			var1.vertexUV(var14, var15, var12, var7, var16);
			var1.vertexUV(var10, var15, var12, var8, var16);
		}

		if(var5 == 3) {
			var1.vertexUV(var14, var15, var13, var8, var16);
			var1.vertexUV(var14, var11, var13, var8, var9);
			var1.vertexUV(var10, var11, var13, var7, var9);
			var1.vertexUV(var10, var15, var13, var7, var16);
		}

		if(var5 == 4) {
			var1.vertexUV(var10, var11, var13, var8, var9);
			var1.vertexUV(var10, var11, var12, var7, var9);
			var1.vertexUV(var10, var15, var12, var7, var16);
			var1.vertexUV(var10, var15, var13, var8, var16);
		}

		if(var5 == 5) {
			var1.vertexUV(var14, var15, var13, var7, var16);
			var1.vertexUV(var14, var15, var12, var8, var16);
			var1.vertexUV(var14, var11, var12, var8, var9);
			var1.vertexUV(var14, var11, var13, var7, var9);
		}

	}

	public AABB getTileAABB(int var1, int var2, int var3) {
		return new AABB((float)var1, (float)var2, (float)var3, (float)(var1 + 1), (float)(var2 + 1), (float)(var3 + 1));
	}

	public boolean blocksLight() {
		return true;
	}

	public boolean isSolid() {
		return true;
	}

	public void tick(Level var1, int var2, int var3, int var4, Random var5) {
	}

	public final void destroy(Level var1, int var2, int var3, int var4, ParticleEngine var5) {
		for(int var6 = 0; var6 < 4; ++var6) {
			for(int var7 = 0; var7 < 4; ++var7) {
				for(int var8 = 0; var8 < 4; ++var8) {
					float var9 = (float)var2 + ((float)var6 + 0.5F) / (float)4;
					float var10 = (float)var3 + ((float)var7 + 0.5F) / (float)4;
					float var11 = (float)var4 + ((float)var8 + 0.5F) / (float)4;
					var5.addParticle(new Particle(var1, var9, var10, var11, var9 - (float)var2 - 0.5F, var10 - (float)var3 - 0.5F, var11 - (float)var4 - 0.5F, this));
				}
			}
		}

	}

	public final void destroy(Level var1, int var2, int var3, int var4, int var5, ParticleEngine var6) {
		float var7 = 0.1F;
		float var8 = (float)var2 + random.nextFloat() * (1.0F - var7 * 2.0F) + var7;
		float var9 = (float)var3 + random.nextFloat() * (1.0F - var7 * 2.0F) + var7;
		float var10 = (float)var4 + random.nextFloat() * (1.0F - var7 * 2.0F) + var7;
		if(var5 == 0) {
			var9 = (float)var3 - var7;
		}

		if(var5 == 1) {
			var9 = (float)(var3 + 1) + var7;
		}

		if(var5 == 2) {
			var10 = (float)var4 - var7;
		}

		if(var5 == 3) {
			var10 = (float)(var4 + 1) + var7;
		}

		if(var5 == 4) {
			var8 = (float)var2 - var7;
		}

		if(var5 == 5) {
			var8 = (float)(var2 + 1) + var7;
		}

		var6.addParticle((new Particle(var1, var8, var9, var10, 0.0F, 0.0F, 0.0F, this)).setPower(0.2F).scale(0.6F));
	}

	public Liquid getLiquidType() {
		return Liquid.none;
	}

	public void neighborChanged(Level var1, int var2, int var3, int var4, int var5) {
	}

	public void onPlace(Level var1, int var2, int var3, int var4) {
	}

	public int getTickDelay() {
		return 0;
	}

	public void onTileAdded(Level var1, int var2, int var3, int var4) {
	}

	public void onTileRemoved(Level var1, int var2, int var3, int var4) {
	}

	public int resourceCount() {
		return 1;
	}

	public int getId() {
		return this.id;
	}

	public final int getDestroyProgress() {
		return this.destroyProgress;
	}

	public void spawnResources(Level var1, int var2, int var3, int var4) {
		this.wasExploded(var1, var2, var3, var4, 1.0F);
	}

	public void wasExploded(Level var1, int var2, int var3, int var4, float var5) {
		int var6 = this.resourceCount();

		for(int var7 = 0; var7 < var6; ++var7) {
			if(random.nextFloat() <= var5) {
				float var8 = 0.7F;
				float var9 = random.nextFloat() * var8 + (1.0F - var8) * 0.5F;
				float var10 = random.nextFloat() * var8 + (1.0F - var8) * 0.5F;
				var8 = random.nextFloat() * var8 + (1.0F - var8) * 0.5F;
				var1.addEntity(new Item(var1, (float)var2 + var9, (float)var3 + var10, (float)var4 + var8, this.getId()));
			}
		}

	}

	public void renderGuiTile(Tesselator var1) {
		var1.begin();

		for(int var2 = 0; var2 < 6; ++var2) {
			if(var2 == 0) {
				var1.setNormal(0.0F, 1.0F, 0.0F);
			}

			if(var2 == 1) {
				var1.setNormal(0.0F, -1.0F, 0.0F);
			}

			if(var2 == 2) {
				var1.setNormal(0.0F, 0.0F, 1.0F);
			}

			if(var2 == 3) {
				var1.setNormal(0.0F, 0.0F, -1.0F);
			}

			if(var2 == 4) {
				var1.setNormal(1.0F, 0.0F, 0.0F);
			}

			if(var2 == 5) {
				var1.setNormal(-1.0F, 0.0F, 0.0F);
			}

			this.renderFace(var1, 0, 0, 0, var2);
		}

		var1.end();
	}

	public static enum SoundType {
		none("-", 0, 0.0F, 0.0F),
		grass("grass", 4 , 0.6F, 1.0F),
		cloth("grass", 4 , 0.7F, 1.2F),
		gravel("gravel", 4 , 1.0F, 1.0F),
		stone("stone", 4 , 1.0F, 1.0F),
		metal("stone", 4 , 1.0F, 2.0F),
		wood("wood", 4 , 1.0F, 1.0F);

		public final String name;
		private final float volume;
		private final float pitch;
		public int num;

		private SoundType(String var3, int var2, float var4, float var5) {
			this.num = var2;
			this.name = var3;
			this.volume = var4;
			this.pitch = var5;
		}

		public final float getVolume() {
			return this.volume / (Tile.random.nextFloat() * 0.4F + 1.0F) * 0.5F;
		}

		public final float getPitch() {
			return this.pitch / (Tile.random.nextFloat() * 0.2F + 0.9F);
		}
		
		public static SoundType getSoundType(String sound) {
			switch (sound) {
			case "step.grass":
				return SoundType.grass;
			case "step.cloth":
				return SoundType.cloth;
			case "step.gravel":
				return SoundType.gravel;
			case "step.stone":
				return SoundType.stone;
			case "step.metal":
				return SoundType.metal;
			case "step.wood":
				return SoundType.wood;
			default:
				return SoundType.none;
			}
		}
	}
}
