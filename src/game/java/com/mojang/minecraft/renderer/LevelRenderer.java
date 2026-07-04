package com.mojang.minecraft.renderer;

import com.mojang.minecraft.HitResult;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.player.Player;
import com.mojang.util.GLAllocation;

import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.lwjgl.opengl.GL11;

public final class LevelRenderer {
	public Level level;
	public Textures textures;
	public int surroundLists;
	public IntBuffer ib = GLAllocation.createIntBuffer(65536);
	public Set allDirtyChunks = new HashSet();
	private Chunk[] sortedChunks;
	public Chunk[] chunks;
	private int xChunks;
	private int yChunks;
	private int zChunks;
	private int glLists;
	private Minecraft minecraft;
	private int[] chunkBuffer = new int['\uc350'];
	public int cloudTickCounter = 0;
	private float lX = -9999.0F;
	private float lY = -9999.0F;
	private float lZ = -9999.0F;
	public float hurtTime;

	public LevelRenderer(Minecraft var1, Textures var2) {
		this.minecraft = var1;
		this.textures = var2;
		this.surroundLists = GL11.glGenLists(2);
		this.glLists = GL11.glGenLists(4096 << 6 << 3);
	}

	public final void compileSurroundingGround() {
		int var1;
		if(this.chunks != null) {
			for(var1 = 0; var1 < this.chunks.length; ++var1) {
				this.chunks[var1].clear();
			}
		}

		this.xChunks = this.level.width / 16;
		this.yChunks = this.level.depth / 16;
		this.zChunks = this.level.height / 16;
		this.chunks = new Chunk[this.xChunks * this.yChunks * this.zChunks];
		this.sortedChunks = new Chunk[this.xChunks * this.yChunks * this.zChunks];
		var1 = 0;

		int var4;
		for(int var2 = 0; var2 < this.xChunks; ++var2) {
			for(int var3 = 0; var3 < this.yChunks; ++var3) {
				for(var4 = 0; var4 < this.zChunks; ++var4) {
					this.chunks[(var4 * this.yChunks + var3) * this.xChunks + var2] = new Chunk(this.level, var2 << 4, var3 << 4, var4 << 4, 16, this.glLists + var1);
					this.sortedChunks[(var4 * this.yChunks + var3) * this.xChunks + var2] = this.chunks[(var4 * this.yChunks + var3) * this.xChunks + var2];
					var1 += 8;
				}
			}
		}

		this.allDirtyChunks.clear();
		GL11.glNewList(this.surroundLists, GL11.GL_COMPILE);
		LevelRenderer var9 = this;
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textures.loadTexture("/rock.png"));
		float var10 = 0.5F;
		GL11.glColor4f(var10, var10, var10, 1.0F);
		Tesselator var11 = Tesselator.instance;
		float var12 = this.level.getGroundLevel();
		int var5 = 128;
		if(128 > this.level.width) {
			var5 = this.level.width;
		}

		if(var5 > this.level.height) {
			var5 = this.level.height;
		}

		int var6 = 2048 / var5;
		var11.begin();

		int var7;
		for(var7 = -var5 * var6; var7 < var9.level.width + var5 * var6; var7 += var5) {
			for(int var8 = -var5 * var6; var8 < var9.level.height + var5 * var6; var8 += var5) {
				var10 = var12;
				if(var7 >= 0 && var8 >= 0 && var7 < var9.level.width && var8 < var9.level.height) {
					var10 = 0.0F;
				}

				var11.vertexUV((float)var7, var10, (float)(var8 + var5), 0.0F, (float)var5);
				var11.vertexUV((float)(var7 + var5), var10, (float)(var8 + var5), (float)var5, (float)var5);
				var11.vertexUV((float)(var7 + var5), var10, (float)var8, (float)var5, 0.0F);
				var11.vertexUV((float)var7, var10, (float)var8, 0.0F, 0.0F);
			}
		}

		var11.end();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, var9.textures.loadTexture("/rock.png"));
		GL11.glColor3f(0.8F, 0.8F, 0.8F);
		var11.begin();

		for(var7 = 0; var7 < var9.level.width; var7 += var5) {
			var11.vertexUV((float)var7, 0.0F, 0.0F, 0.0F, 0.0F);
			var11.vertexUV((float)(var7 + var5), 0.0F, 0.0F, (float)var5, 0.0F);
			var11.vertexUV((float)(var7 + var5), var12, 0.0F, (float)var5, var12);
			var11.vertexUV((float)var7, var12, 0.0F, 0.0F, var12);
			var11.vertexUV((float)var7, var12, (float)var9.level.height, 0.0F, var12);
			var11.vertexUV((float)(var7 + var5), var12, (float)var9.level.height, (float)var5, var12);
			var11.vertexUV((float)(var7 + var5), 0.0F, (float)var9.level.height, (float)var5, 0.0F);
			var11.vertexUV((float)var7, 0.0F, (float)var9.level.height, 0.0F, 0.0F);
		}

		GL11.glColor3f(0.6F, 0.6F, 0.6F);

		for(var7 = 0; var7 < var9.level.height; var7 += var5) {
			var11.vertexUV(0.0F, var12, (float)var7, 0.0F, 0.0F);
			var11.vertexUV(0.0F, var12, (float)(var7 + var5), (float)var5, 0.0F);
			var11.vertexUV(0.0F, 0.0F, (float)(var7 + var5), (float)var5, var12);
			var11.vertexUV(0.0F, 0.0F, (float)var7, 0.0F, var12);
			var11.vertexUV((float)var9.level.width, 0.0F, (float)var7, 0.0F, var12);
			var11.vertexUV((float)var9.level.width, 0.0F, (float)(var7 + var5), (float)var5, var12);
			var11.vertexUV((float)var9.level.width, var12, (float)(var7 + var5), (float)var5, 0.0F);
			var11.vertexUV((float)var9.level.width, var12, (float)var7, 0.0F, 0.0F);
		}

		var11.end();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEndList();
		GL11.glNewList(this.surroundLists + 1, GL11.GL_COMPILE);
		var9 = this;
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textures.loadTexture("/water.png"));
		var10 = this.level.getWaterLevel();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		var11 = Tesselator.instance;
		var4 = 128;
		if(128 > this.level.width) {
			var4 = this.level.width;
		}

		if(var4 > this.level.height) {
			var4 = this.level.height;
		}

		var5 = 2048 / var4;
		var11.begin();

		for(var6 = -var4 * var5; var6 < var9.level.width + var4 * var5; var6 += var4) {
			for(var7 = -var4 * var5; var7 < var9.level.height + var4 * var5; var7 += var4) {
				float var13 = var10 - 0.1F;
				if(var6 < 0 || var7 < 0 || var6 >= var9.level.width || var7 >= var9.level.height) {
					var11.vertexUV((float)var6, var13, (float)(var7 + var4), 0.0F, (float)var4);
					var11.vertexUV((float)(var6 + var4), var13, (float)(var7 + var4), (float)var4, (float)var4);
					var11.vertexUV((float)(var6 + var4), var13, (float)var7, (float)var4, 0.0F);
					var11.vertexUV((float)var6, var13, (float)var7, 0.0F, 0.0F);
					var11.vertexUV((float)var6, var13, (float)var7, 0.0F, 0.0F);
					var11.vertexUV((float)(var6 + var4), var13, (float)var7, (float)var4, 0.0F);
					var11.vertexUV((float)(var6 + var4), var13, (float)(var7 + var4), (float)var4, (float)var4);
					var11.vertexUV((float)var6, var13, (float)(var7 + var4), 0.0F, (float)var4);
				}
			}
		}

		var11.end();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEndList();
		this.setDirty(0, 0, 0, this.level.width, this.level.depth, this.level.height);
	}
	
	public final void renderSurroundingGround() {
		LevelRenderer var9 = this;
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textures.loadTexture("/rock.png"));
		float var10 = 0.5F;
		GL11.glColor4f(var10, var10, var10, 1.0F);
		Tesselator var11 = Tesselator.instance;
		float var4 = this.level.getGroundLevel();
		int var5 = 128;
		if(128 > this.level.width) {
			var5 = this.level.width;
		}

		if(var5 > this.level.height) {
			var5 = this.level.height;
		}

		int var6 = 2048 / var5;
		var11.begin();

		int var7;
		for(var7 = -var5 * var6; var7 < var9.level.width + var5 * var6; var7 += var5) {
			for(int var8 = -var5 * var6; var8 < var9.level.height + var5 * var6; var8 += var5) {
				var10 = var4;
				if(var7 >= 0 && var8 >= 0 && var7 < var9.level.width && var8 < var9.level.height) {
					var10 = 0.0F;
				}

				var11.vertexUV((float)var7, var10, (float)(var8 + var5), 0.0F, (float)var5);
				var11.vertexUV((float)(var7 + var5), var10, (float)(var8 + var5), (float)var5, (float)var5);
				var11.vertexUV((float)(var7 + var5), var10, (float)var8, (float)var5, 0.0F);
				var11.vertexUV((float)var7, var10, (float)var8, 0.0F, 0.0F);
			}
		}

		var11.end();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, var9.textures.loadTexture("/rock.png"));
		GL11.glColor3f(0.8F, 0.8F, 0.8F);
		var11.begin();

		for(var7 = 0; var7 < var9.level.width; var7 += var5) {
			var11.vertexUV((float)var7, 0.0F, 0.0F, 0.0F, 0.0F);
			var11.vertexUV((float)(var7 + var5), 0.0F, 0.0F, (float)var5, 0.0F);
			var11.vertexUV((float)(var7 + var5), var4, 0.0F, (float)var5, var4);
			var11.vertexUV((float)var7, var4, 0.0F, 0.0F, var4);
			var11.vertexUV((float)var7, var4, (float)var9.level.height, 0.0F, var4);
			var11.vertexUV((float)(var7 + var5), var4, (float)var9.level.height, (float)var5, var4);
			var11.vertexUV((float)(var7 + var5), 0.0F, (float)var9.level.height, (float)var5, 0.0F);
			var11.vertexUV((float)var7, 0.0F, (float)var9.level.height, 0.0F, 0.0F);
		}

		GL11.glColor3f(0.6F, 0.6F, 0.6F);

		for(var7 = 0; var7 < var9.level.height; var7 += var5) {
			var11.vertexUV(0.0F, var4, (float)var7, 0.0F, 0.0F);
			var11.vertexUV(0.0F, var4, (float)(var7 + var5), (float)var5, 0.0F);
			var11.vertexUV(0.0F, 0.0F, (float)(var7 + var5), (float)var5, var4);
			var11.vertexUV(0.0F, 0.0F, (float)var7, 0.0F, var4);
			var11.vertexUV((float)var9.level.width, 0.0F, (float)var7, 0.0F, var4);
			var11.vertexUV((float)var9.level.width, 0.0F, (float)(var7 + var5), (float)var5, var4);
			var11.vertexUV((float)var9.level.width, var4, (float)(var7 + var5), (float)var5, 0.0F);
			var11.vertexUV((float)var9.level.width, var4, (float)var7, 0.0F, 0.0F);
		}

		var11.end();
		
		var9 = this;
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textures.loadTexture("/water.png"));
		var10 = this.level.getWaterLevel();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		var11 = Tesselator.instance;
		int var12 = 128;
		if(128 > this.level.width) {
			var12 = this.level.width;
		}

		if(var12 > this.level.height) {
			var12 = this.level.height;
		}

		var5 = 2048 / var12;
		var11.begin();

		for(var6 = -var12 * var5; var6 < var9.level.width + var12 * var5; var6 += var12) {
			for(var7 = -var12 * var5; var7 < var9.level.height + var12 * var5; var7 += var12) {
				float var13 = var10 - 0.1F;
				if(var6 < 0 || var7 < 0 || var6 >= var9.level.width || var7 >= var9.level.height) {
					var11.vertexUV((float)var6, var13, (float)(var7 + var12), 0.0F, (float)var12);
					var11.vertexUV((float)(var6 + var12), var13, (float)(var7 + var12), (float)var12, (float)var12);
					var11.vertexUV((float)(var6 + var12), var13, (float)var7, (float)var12, 0.0F);
					var11.vertexUV((float)var6, var13, (float)var7, 0.0F, 0.0F);
					var11.vertexUV((float)var6, var13, (float)var7, 0.0F, 0.0F);
					var11.vertexUV((float)(var6 + var12), var13, (float)var7, (float)var12, 0.0F);
					var11.vertexUV((float)(var6 + var12), var13, (float)(var7 + var12), (float)var12, (float)var12);
					var11.vertexUV((float)var6, var13, (float)(var7 + var12), 0.0F, (float)var12);
				}
			}
		}

		var11.end();
	}

	public final int render(Player var1, int var2) {
		float var3 = var1.x - this.lX;
		float var4 = var1.y - this.lY;
		float var5 = var1.z - this.lZ;
		if(var3 * var3 + var4 * var4 + var5 * var5 > 64.0F) {
			this.lX = var1.x;
			this.lY = var1.y;
			this.lZ = var1.z;
			Arrays.sort(this.sortedChunks, new DistanceSorter(var1));
		}

		int var6 = 0;

		for(int var7 = 0; var7 < this.sortedChunks.length; ++var7) {
			var6 = this.sortedChunks[var7].render(this.chunkBuffer, var6, var2, var1.x, var1.y, var1.z);
		}

		this.ib.clear();
		this.ib.put(this.chunkBuffer, 0, var6);
		this.ib.flip();
		if(this.ib.remaining() > 0) {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textures.loadTexture("/terrain.png"));
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
			GL11.glCallLists(this.ib);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}

		return this.ib.remaining();
	}

	public final void renderClouds(float var1) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textures.loadTexture("/clouds.png"));
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		float var2 = (float)(this.level.cloudColor >> 16 & 255) / 255.0F;
		float var3 = (float)(this.level.cloudColor >> 8 & 255) / 255.0F;
		float var4 = (float)(this.level.cloudColor & 255) / 255.0F;
		if(this.minecraft.options.anaglyph3d) {
			float var5 = (var2 * 30.0F + var3 * 59.0F + var4 * 11.0F) / 100.0F;
			var3 = (var2 * 30.0F + var3 * 70.0F) / 100.0F;
			var4 = (var2 * 30.0F + var4 * 70.0F) / 100.0F;
			var2 = var5;
			var3 = var3;
			var4 = var4;
		}

		Tesselator var11 = Tesselator.instance;
		float var6 = 0.0F;
		float var7 = 0.5F / 1024.0F;
		var6 = (float)(this.level.depth + 2);
		var1 = ((float)this.cloudTickCounter + var1) * var7 * 0.03F;
		float var8 = 0.0F;
		var11.begin();
		var11.color(var2, var3, var4);

		int var10;
		for(int var9 = -2048; var9 < this.level.width + 2048; var9 += 512) {
			for(var10 = -2048; var10 < this.level.height + 2048; var10 += 512) {
				var11.vertexUV((float)var9, var6, (float)(var10 + 512), (float)var9 * var7 + var1, (float)(var10 + 512) * var7);
				var11.vertexUV((float)(var9 + 512), var6, (float)(var10 + 512), (float)(var9 + 512) * var7 + var1, (float)(var10 + 512) * var7);
				var11.vertexUV((float)(var9 + 512), var6, (float)var10, (float)(var9 + 512) * var7 + var1, (float)var10 * var7);
				var11.vertexUV((float)var9, var6, (float)var10, (float)var9 * var7 + var1, (float)var10 * var7);
				var11.vertexUV((float)var9, var6, (float)var10, (float)var9 * var7 + var1, (float)var10 * var7);
				var11.vertexUV((float)(var9 + 512), var6, (float)var10, (float)(var9 + 512) * var7 + var1, (float)var10 * var7);
				var11.vertexUV((float)(var9 + 512), var6, (float)(var10 + 512), (float)(var9 + 512) * var7 + var1, (float)(var10 + 512) * var7);
				var11.vertexUV((float)var9, var6, (float)(var10 + 512), (float)var9 * var7 + var1, (float)(var10 + 512) * var7);
			}
		}

		var11.end();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		var11.begin();
		var1 = (float)(this.level.skyColor >> 16 & 255) / 255.0F;
		var8 = (float)(this.level.skyColor >> 8 & 255) / 255.0F;
		var2 = (float)(this.level.skyColor & 255) / 255.0F;
		if(this.minecraft.options.anaglyph3d) {
			var3 = (var1 * 30.0F + var8 * 59.0F + var2 * 11.0F) / 100.0F;
			var4 = (var1 * 30.0F + var8 * 70.0F) / 100.0F;
			var2 = (var1 * 30.0F + var2 * 70.0F) / 100.0F;
			var1 = var3;
			var8 = var4;
			var2 = var2;
		}

		var11.color(var1, var8, var2);
		var6 = (float)(this.level.depth + 10);

		for(var10 = -2048; var10 < this.level.width + 2048; var10 += 512) {
			for(int var12 = -2048; var12 < this.level.height + 2048; var12 += 512) {
				var11.vertex((float)var10, var6, (float)var12);
				var11.vertex((float)(var10 + 512), var6, (float)var12);
				var11.vertex((float)(var10 + 512), var6, (float)(var12 + 512));
				var11.vertex((float)var10, var6, (float)(var12 + 512));
			}
		}

		var11.end();
	}

	public final void render(int var1, int var2, int var3) {
		int var6 = this.level.getTile(var1, var2, var3);
		if(var6 != 0 && Tile.tiles[var6].isSolid()) {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glColor4f(0.2F, 0.2F, 0.2F, 1.0F);
			GL11.glDepthFunc(GL11.GL_LESS);
			Tesselator var4 = Tesselator.instance;
			var4.begin();

			int var5;
			for(var5 = 0; var5 < 6; ++var5) {
				Tile.tiles[var6].renderFace(var4, var1, var2, var3, var5);
			}

			var4.end();
			GL11.glCullFace(GL11.GL_FRONT);
			var4.begin();

			for(var5 = 0; var5 < 6; ++var5) {
				Tile.tiles[var6].renderFace(var4, var1, var2, var3, var5);
			}

			var4.end();
			GL11.glCullFace(GL11.GL_BACK);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDepthFunc(GL11.GL_LEQUAL);
		}
	}

	public final void renderHit(HitResult var1, int var2, int var3) {
		Tesselator var4 = Tesselator.instance;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, ((float)Math.sin((double)System.currentTimeMillis() / 100.0D) * 0.2F + 0.4F) * 0.5F);
		if(this.hurtTime > 0.0F) {
			GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			var3 = this.textures.loadTexture("/terrain.png");
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var3);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
			GL11.glPushMatrix();
			GL11.glTranslatef((float)var1.x + 0.5F, (float)var1.y + 0.5F, (float)var1.z + 0.5F);
			float var5 = 1.01F;
			GL11.glScalef(var5, var5, var5);
			GL11.glTranslatef(-((float)var1.x + 0.5F), -((float)var1.y + 0.5F), -((float)var1.z + 0.5F));
			var4.begin();
			var4.noColor();
			GL11.glDepthMask(false);

			for(var3 = 0; var3 < 6; ++var3) {
				Tile.rock.renderFaceNoTexture(var4, var1.x, var1.y, var1.z, var3, 240 + (int)(this.hurtTime * 10.0F));
			}

			var4.end();
			GL11.glDepthMask(true);
			GL11.glPopMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
	}

	public final void setDirty(int var1, int var2, int var3, int var4, int var5, int var6) {
		var1 /= 16;
		var2 /= 16;
		var3 /= 16;
		var4 /= 16;
		var5 /= 16;
		var6 /= 16;
		if(var1 < 0) {
			var1 = 0;
		}

		if(var2 < 0) {
			var2 = 0;
		}

		if(var3 < 0) {
			var3 = 0;
		}

		if(var4 > this.xChunks - 1) {
			var4 = this.xChunks - 1;
		}

		if(var5 > this.yChunks - 1) {
			var5 = this.yChunks - 1;
		}

		if(var6 > this.zChunks - 1) {
			var6 = this.zChunks - 1;
		}

		for(var1 = var1; var1 <= var4; ++var1) {
			for(int var7 = var2; var7 <= var5; ++var7) {
				for(int var8 = var3; var8 <= var6; ++var8) {
					this.allDirtyChunks.add(this.chunks[(var8 * this.yChunks + var7) * this.xChunks + var1]);
				}
			}
		}

	}
}
