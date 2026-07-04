package com.mojang.minecraft.level;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.HitResult;
import com.mojang.minecraft.LevelLoaderListener;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.gui.Font;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.mob.Creeper;
import com.mojang.minecraft.mob.Mob;
import com.mojang.minecraft.mob.Pig;
import com.mojang.minecraft.mob.Skeleton;
import com.mojang.minecraft.mob.Zombie;
import com.mojang.minecraft.model.Vec3;
import com.mojang.minecraft.particle.ParticleEngine;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.renderer.LevelRenderer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.lax1dude.eaglercraft.Random;

public class Level implements Serializable {
	public static final long serialVersionUID = 0L;
	public int width;
	public int height;
	public int depth;
	public byte[] blocks;
	public String name;
	public String creator;
	public long createTime;
	public int xSpawn;
	public int ySpawn;
	public int zSpawn;
	public float rotSpawn;
	private transient ArrayList levelListeners = new ArrayList();
	private transient int[] heightMap;
	private transient Random random = new Random();
	private transient int randValue = this.random.nextInt();
	private transient ArrayList tickNextTickList = new ArrayList();
	public BlockMap blockMap;
	private boolean networkMode = false;
	public transient Minecraft rendererContext;
	public int waterLevel;
	public int skyColor;
	public int fogColor;
	public int cloudColor;
	int unprocessed = 0;
	private int tickCount = 0;
	public Entity player;
	public transient ParticleEngine particleEngine;
	public transient Font font;

	public void initTransient() {
		if(this.blocks == null) {
			throw new RuntimeException("The level is corrupt!");
		} else {
			this.levelListeners = new ArrayList();
			this.heightMap = new int[this.width * this.height];
			Arrays.fill(this.heightMap, this.depth);
			this.calcLightDepths(0, 0, this.width, this.height);
			this.random = new Random();
			this.randValue = this.random.nextInt();
			this.tickNextTickList = new ArrayList();
			if(this.waterLevel == 0) {
				this.waterLevel = this.depth / 2;
			}

			if(this.skyColor == 0) {
				this.skyColor = 10079487;
			}

			if(this.fogColor == 0) {
				this.fogColor = 16777215;
			}

			if(this.cloudColor == 0) {
				this.cloudColor = 16777215;
			}

			if(this.xSpawn == 0 && this.ySpawn == 0 && this.zSpawn == 0) {
				this.findSpawn();
			}

			if(this.blockMap == null) {
				this.blockMap = new BlockMap(this.width, this.depth, this.height);
			}

		}
	}

	public void setData(int var1, int var2, int var3, byte[] var4) {
		this.width = var1;
		this.height = var3;
		this.depth = var2;
		this.blocks = var4;
		this.heightMap = new int[var1 * var3];
		Arrays.fill(this.heightMap, this.depth);
		this.calcLightDepths(0, 0, var1, var3);

		for(var1 = 0; var1 < this.levelListeners.size(); ++var1) {
			((LevelRenderer)this.levelListeners.get(var1)).compileSurroundingGround();
		}

		this.tickNextTickList.clear();
		this.findSpawn();
		this.initTransient();
		System.gc();
	}

	public void findSpawn() {
		Random var1 = new Random();
		int var2 = 0;

		int var3;
		int var4;
		int var5;
		do {
			++var2;
			var3 = var1.nextInt(this.width / 2) + this.width / 4;
			var4 = var1.nextInt(this.height / 2) + this.height / 4;
			var5 = this.getHighestTile(var3, var4) + 1;
			if(var2 == 10000) {
				this.xSpawn = var3;
				this.ySpawn = -100;
				this.zSpawn = var4;
				return;
			}
		} while((float)var5 <= this.getWaterLevel());

		this.xSpawn = var3;
		this.ySpawn = var5;
		this.zSpawn = var4;
	}

	public void calcLightDepths(int var1, int var2, int var3, int var4) {
		for(int var5 = var1; var5 < var1 + var3; ++var5) {
			for(int var6 = var2; var6 < var2 + var4; ++var6) {
				int var7 = this.heightMap[var5 + var6 * this.width];

				int var8;
				for(var8 = this.depth - 1; var8 > 0 && !this.isLightBlocker(var5, var8, var6); --var8) {
				}

				this.heightMap[var5 + var6 * this.width] = var8 + 1;
				if(var7 != var8) {
					int var9 = var7 < var8 ? var7 : var8;
					var7 = var7 > var8 ? var7 : var8;

					for(var8 = 0; var8 < this.levelListeners.size(); ++var8) {
						LevelRenderer var10 = (LevelRenderer)this.levelListeners.get(var8);
						var10.setDirty(var5 - 1, var9 - 1, var6 - 1, var5 + 1, var7 + 1, var6 + 1);
					}
				}
			}
		}

	}

	public void addListener(LevelRenderer var1) {
		this.levelListeners.add(var1);
	}

	public void finalize() {
	}

	public void removeListener(LevelRenderer var1) {
		this.levelListeners.remove(var1);
	}

	public boolean isLightBlocker(int var1, int var2, int var3) {
		Tile var4 = Tile.tiles[this.getTile(var1, var2, var3)];
		return var4 == null ? false : var4.blocksLight();
	}

	public ArrayList getCubes(AABB var1) {
		ArrayList var2 = new ArrayList();
		int var3 = (int)var1.x0;
		int var4 = (int)var1.x1 + 1;
		int var5 = (int)var1.y0;
		int var6 = (int)var1.y1 + 1;
		int var7 = (int)var1.z0;
		int var8 = (int)var1.z1 + 1;
		if(var1.x0 < 0.0F) {
			--var3;
		}

		if(var1.y0 < 0.0F) {
			--var5;
		}

		if(var1.z0 < 0.0F) {
			--var7;
		}

		for(int var11 = var3; var11 < var4; ++var11) {
			for(var3 = var5; var3 < var6; ++var3) {
				for(int var9 = var7; var9 < var8; ++var9) {
					AABB var10;
					if(var11 >= 0 && var3 >= 0 && var9 >= 0 && var11 < this.width && var3 < this.depth && var9 < this.height) {
						Tile var12 = Tile.tiles[this.getTile(var11, var3, var9)];
						if(var12 != null) {
							var10 = var12.getTileAABB(var11, var3, var9);
							if(var10 != null) {
								var2.add(var10);
							}
						}
					} else if(var11 < 0 || var3 < 0 || var9 < 0 || var11 >= this.width || var9 >= this.height) {
						var10 = Tile.unbreakable.getTileAABB(var11, var3, var9);
						if(var10 != null) {
							var2.add(var10);
						}
					}
				}
			}
		}

		return var2;
	}

	public void swap(int var1, int var2, int var3, int var4, int var5, int var6) {
		if(!this.networkMode) {
			int var7 = this.getTile(var1, var2, var3);
			int var8 = this.getTile(var4, var5, var6);
			this.setTileNoNeighborChange(var1, var2, var3, var8);
			this.setTileNoNeighborChange(var4, var5, var6, var7);
			this.updateNeighborsAt(var1, var2, var3, var8);
			this.updateNeighborsAt(var4, var5, var6, var7);
		}
	}

	public boolean setTileNoNeighborChange(int var1, int var2, int var3, int var4) {
		return this.networkMode ? false : this.netSetTileNoNeighborChange(var1, var2, var3, var4);
	}

	public boolean netSetTileNoNeighborChange(int var1, int var2, int var3, int var4) {
		if(var1 >= 0 && var2 >= 0 && var3 >= 0 && var1 < this.width && var2 < this.depth && var3 < this.height) {
			if(var4 == this.blocks[(var2 * this.height + var3) * this.width + var1]) {
				return false;
			} else {
				if(var4 == 0 && (var1 == 0 || var3 == 0 || var1 == this.width - 1 || var3 == this.height - 1) && (float)var2 >= this.getGroundLevel() && (float)var2 < this.getWaterLevel()) {
					var4 = Tile.water.id;
				}

				byte var5 = this.blocks[(var2 * this.height + var3) * this.width + var1];
				this.blocks[(var2 * this.height + var3) * this.width + var1] = (byte)var4;
				if(var5 != 0) {
					Tile.tiles[var5].onTileRemoved(this, var1, var2, var3);
				}

				if(var4 != 0) {
					Tile.tiles[var4].onTileAdded(this, var1, var2, var3);
				}

				this.calcLightDepths(var1, var3, 1, 1);

				for(var4 = 0; var4 < this.levelListeners.size(); ++var4) {
					LevelRenderer var9 = (LevelRenderer)this.levelListeners.get(var4);
					var9.setDirty(var1 - 1, var2 - 1, var3 - 1, var1 + 1, var2 + 1, var3 + 1);
				}

				return true;
			}
		} else {
			return false;
		}
	}

	public boolean setTile(int var1, int var2, int var3, int var4) {
		if(this.networkMode) {
			return false;
		} else if(this.setTileNoNeighborChange(var1, var2, var3, var4)) {
			this.updateNeighborsAt(var1, var2, var3, var4);
			return true;
		} else {
			return false;
		}
	}

	public boolean netSetTile(int var1, int var2, int var3, int var4) {
		if(this.netSetTileNoNeighborChange(var1, var2, var3, var4)) {
			this.updateNeighborsAt(var1, var2, var3, var4);
			return true;
		} else {
			return false;
		}
	}

	public void updateNeighborsAt(int var1, int var2, int var3, int var4) {
		this.neighborChanged(var1 - 1, var2, var3, var4);
		this.neighborChanged(var1 + 1, var2, var3, var4);
		this.neighborChanged(var1, var2 - 1, var3, var4);
		this.neighborChanged(var1, var2 + 1, var3, var4);
		this.neighborChanged(var1, var2, var3 - 1, var4);
		this.neighborChanged(var1, var2, var3 + 1, var4);
	}

	public boolean setTileNoUpdate(int var1, int var2, int var3, int var4) {
		if(var1 >= 0 && var2 >= 0 && var3 >= 0 && var1 < this.width && var2 < this.depth && var3 < this.height) {
			if(var4 == this.blocks[(var2 * this.height + var3) * this.width + var1]) {
				return false;
			} else {
				this.blocks[(var2 * this.height + var3) * this.width + var1] = (byte)var4;
				return true;
			}
		} else {
			return false;
		}
	}

	private void neighborChanged(int var1, int var2, int var3, int var4) {
		if(var1 >= 0 && var2 >= 0 && var3 >= 0 && var1 < this.width && var2 < this.depth && var3 < this.height) {
			Tile var5 = Tile.tiles[this.blocks[(var2 * this.height + var3) * this.width + var1]];
			if(var5 != null) {
				var5.neighborChanged(this, var1, var2, var3, var4);
			}

		}
	}

	public boolean isLit(int var1, int var2, int var3) {
		return var1 >= 0 && var2 >= 0 && var3 >= 0 && var1 < this.width && var2 < this.depth && var3 < this.height ? var2 >= this.heightMap[var1 + var3 * this.width] : true;
	}

	public int getTile(int var1, int var2, int var3) {
		return var1 >= 0 && var2 >= 0 && var3 >= 0 && var1 < this.width && var2 < this.depth && var3 < this.height ? this.blocks[(var2 * this.height + var3) * this.width + var1] & 255 : 0;
	}

	public boolean isSolidTile(int var1, int var2, int var3) {
		Tile var4 = Tile.tiles[this.getTile(var1, var2, var3)];
		return var4 == null ? false : var4.isSolid();
	}

	public void tickEntities() {
		this.blockMap.tickAll();
	}

	public void tick() {
		++this.tickCount;
		int var1 = this.width * this.height * this.depth / 64 / 64 / 64;
		int var3;
		int var4;
		if(this.random.nextInt(100) < var1) {
			Level var2 = this;
			var3 = 0;

			for(var4 = 0; var4 < var2.blockMap.all.size(); ++var4) {
				Entity var5 = (Entity)var2.blockMap.all.get(var4);
				if(var5 instanceof Mob) {
					++var3;
				}
			}

			if(var3 < var1 * 20) {
				this.maybeSpawnMobs(var1, this.player, (LevelLoaderListener)null);
			}
		}

		int var12 = 1;

		for(var1 = 1; 1 << var12 < this.width; ++var12) {
		}

		while(1 << var1 < this.height) {
			++var1;
		}

		var3 = this.height - 1;
		var4 = this.width - 1;
		int var13 = this.depth - 1;
		int var6;
		int var7;
		if(this.tickCount % 5 == 0) {
			var6 = this.tickNextTickList.size();

			for(var7 = 0; var7 < var6; ++var7) {
				Coord var8 = (Coord)this.tickNextTickList.remove(0);
				if(var8.time > 0) {
					--var8.time;
					this.tickNextTickList.add(var8);
				} else if(this.isInLevelBounds(var8.x, var8.y, var8.z)) {
					byte var9 = this.blocks[(var8.y * this.height + var8.z) * this.width + var8.x];
					if(var9 == var8.id && var9 > 0) {
						Tile.tiles[var9].tick(this, var8.x, var8.y, var8.z, this.random);
					}
				}
			}
		}

		this.unprocessed += this.width * this.height * this.depth;
		var6 = this.unprocessed / 200;
		this.unprocessed -= var6 * 200;

		for(var7 = 0; var7 < var6; ++var7) {
			this.randValue = this.randValue * 3 + 1013904223;
			int var14 = this.randValue >> 2;
			int var15 = var14 & var4;
			int var10 = var14 >> var12 & var3;
			var14 = var14 >> var12 + var1 & var13;
			byte var11 = this.blocks[(var14 * this.height + var10) * this.width + var15];
			if(Tile.shouldTick[var11]) {
				Tile.tiles[var11].tick(this, var15, var14, var10, this.random);
			}
		}

	}

	private boolean isInLevelBounds(int var1, int var2, int var3) {
		return var1 >= 0 && var2 >= 0 && var3 >= 0 && var1 < this.width && var2 < this.depth && var3 < this.height;
	}

	public float getGroundLevel() {
		return this.getWaterLevel() - 2.0F;
	}

	public float getWaterLevel() {
		return (float)this.waterLevel;
	}

	public boolean containsAnyLiquid(AABB var1) {
		int var2 = (int)var1.x0;
		int var3 = (int)var1.x1 + 1;
		int var4 = (int)var1.y0;
		int var5 = (int)var1.y1 + 1;
		int var6 = (int)var1.z0;
		int var7 = (int)var1.z1 + 1;
		if(var1.x0 < 0.0F) {
			--var2;
		}

		if(var1.y0 < 0.0F) {
			--var4;
		}

		if(var1.z0 < 0.0F) {
			--var6;
		}

		if(var2 < 0) {
			var2 = 0;
		}

		if(var4 < 0) {
			var4 = 0;
		}

		if(var6 < 0) {
			var6 = 0;
		}

		if(var3 > this.width) {
			var3 = this.width;
		}

		if(var5 > this.depth) {
			var5 = this.depth;
		}

		if(var7 > this.height) {
			var7 = this.height;
		}

		for(int var10 = var2; var10 < var3; ++var10) {
			for(var2 = var4; var2 < var5; ++var2) {
				for(int var8 = var6; var8 < var7; ++var8) {
					Tile var9 = Tile.tiles[this.getTile(var10, var2, var8)];
					if(var9 != null && var9.getLiquidType() != Liquid.none) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public boolean containsLiquid(AABB var1, Liquid var2) {
		int var3 = (int)var1.x0;
		int var4 = (int)var1.x1 + 1;
		int var5 = (int)var1.y0;
		int var6 = (int)var1.y1 + 1;
		int var7 = (int)var1.z0;
		int var8 = (int)var1.z1 + 1;
		if(var1.x0 < 0.0F) {
			--var3;
		}

		if(var1.y0 < 0.0F) {
			--var5;
		}

		if(var1.z0 < 0.0F) {
			--var7;
		}

		if(var3 < 0) {
			var3 = 0;
		}

		if(var5 < 0) {
			var5 = 0;
		}

		if(var7 < 0) {
			var7 = 0;
		}

		if(var4 > this.width) {
			var4 = this.width;
		}

		if(var6 > this.depth) {
			var6 = this.depth;
		}

		if(var8 > this.height) {
			var8 = this.height;
		}

		for(int var11 = var3; var11 < var4; ++var11) {
			for(var3 = var5; var3 < var6; ++var3) {
				for(int var9 = var7; var9 < var8; ++var9) {
					Tile var10 = Tile.tiles[this.getTile(var11, var3, var9)];
					if(var10 != null && var10.getLiquidType() == var2) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public void addToTickNextTick(int var1, int var2, int var3, int var4) {
		if(!this.networkMode) {
			Coord var5 = new Coord(var1, var2, var3, var4);
			if(var4 > 0) {
				var3 = Tile.tiles[var4].getTickDelay();
				var5.time = var3;
			}

			this.tickNextTickList.add(var5);
		}
	}

	public boolean isFree(AABB var1) {
		return this.blockMap.getEntities((Entity)null, var1).size() == 0;
	}

	public List findEntities(Entity var1, AABB var2) {
		return this.blockMap.getEntities(var1, var2);
	}

	public boolean isSolid(float var1, float var2, float var3, float var4) {
		return this.isSolid(var1 - var4, var2 - var4, var3 - var4) ? true : (this.isSolid(var1 - var4, var2 - var4, var3 + var4) ? true : (this.isSolid(var1 - var4, var2 + var4, var3 - var4) ? true : (this.isSolid(var1 - var4, var2 + var4, var3 + var4) ? true : (this.isSolid(var1 + var4, var2 - var4, var3 - var4) ? true : (this.isSolid(var1 + var4, var2 - var4, var3 + var4) ? true : (this.isSolid(var1 + var4, var2 + var4, var3 - var4) ? true : this.isSolid(var1 + var4, var2 + var4, var3 + var4)))))));
	}

	private boolean isSolid(float var1, float var2, float var3) {
		int var4 = this.getTile((int)var1, (int)var2, (int)var3);
		return var4 > 0 && Tile.tiles[var4].isSolid();
	}

	public int getHighestTile(int var1, int var2) {
		int var3;
		for(var3 = this.depth; (this.getTile(var1, var3 - 1, var2) == 0 || Tile.tiles[this.getTile(var1, var3 - 1, var2)].getLiquidType() != Liquid.none) && var3 > 0; --var3) {
		}

		return var3;
	}

	public void setSpawnPos(int var1, int var2, int var3, float var4) {
		this.xSpawn = var1;
		this.ySpawn = var2;
		this.zSpawn = var3;
		this.rotSpawn = var4;
	}

	public float getBrightness(int var1, int var2, int var3) {
		return this.isLit(var1, var2, var3) ? 1.0F : 0.6F;
	}

	public float getCaveness(float var1, float var2, float var3, float var4) {
		int var5 = (int)var1;
		int var14 = (int)var2;
		int var6 = (int)var3;
		float var7 = 0.0F;
		float var8 = 0.0F;

		for(int var9 = var5 - 6; var9 <= var5 + 6; ++var9) {
			for(int var10 = var6 - 6; var10 <= var6 + 6; ++var10) {
				if(this.isInLevelBounds(var9, var14, var10) && !this.isSolidTile(var9, var14, var10)) {
					float var11 = (float)var9 + 0.5F - var1;
					float var12 = (float)var10 + 0.5F - var3;

					float var13;
					for(var13 = (float)(Math.atan2((double)var12, (double)var11) - (double)var4 * Math.PI / 180.0D + Math.PI * 0.5D); (double)var13 < -Math.PI; var13 = (float)((double)var13 + Math.PI * 2.0D)) {
					}

					while((double)var13 >= Math.PI) {
						var13 = (float)((double)var13 - Math.PI * 2.0D);
					}

					if(var13 < 0.0F) {
						var13 = -var13;
					}

					var11 = (float)Math.sqrt((double)(var11 * var11 + 4.0F + var12 * var12));
					var11 = 1.0F / var11;
					if(var13 > 1.0F) {
						var11 = 0.0F;
					}

					if(var11 < 0.0F) {
						var11 = 0.0F;
					}

					var8 += var11;
					if(this.isLit(var9, var14, var10)) {
						var7 += var11;
					}
				}
			}
		}

		if(var8 == 0.0F) {
			return 0.0F;
		} else {
			return var7 / var8;
		}
	}

	public float getCaveness(Entity var1) {
		float var2 = (float)Math.cos((double)(-var1.yRot) * Math.PI / 180.0D + Math.PI);
		float var3 = (float)Math.sin((double)(-var1.yRot) * Math.PI / 180.0D + Math.PI);
		float var4 = (float)Math.cos((double)(-var1.xRot) * Math.PI / 180.0D);
		float var5 = (float)Math.sin((double)(-var1.xRot) * Math.PI / 180.0D);
		float var6 = var1.x;
		float var7 = var1.y;
		float var21 = var1.z;
		float var8 = 1.6F;
		float var9 = 0.0F;
		float var10 = 0.0F;

		for(int var11 = 0; var11 <= 200; ++var11) {
			float var12 = ((float)var11 / (float)200 - 0.5F) * 2.0F;

			for(int var13 = 0; var13 <= 200; ++var13) {
				float var14 = ((float)var13 / (float)200 - 0.5F) * var8;
				float var16 = var4 * var14 + var5;
				var14 = var4 - var5 * var14;
				float var17 = var2 * var12 + var3 * var14;
				var16 = var16;
				var14 = var2 * var14 - var3 * var12;

				for(int var15 = 0; var15 < 10; ++var15) {
					float var18 = var6 + var17 * (float)var15 * 0.8F;
					float var19 = var7 + var16 * (float)var15 * 0.8F;
					float var20 = var21 + var14 * (float)var15 * 0.8F;
					if(this.isSolid(var18, var19, var20)) {
						break;
					}

					++var9;
					if(this.isLit((int)var18, (int)var19, (int)var20)) {
						++var10;
					}
				}
			}
		}

		if(var9 == 0.0F) {
			return 0.0F;
		} else {
			float var22 = var10 / var9;
			var22 /= 0.1F;
			if(var22 > 1.0F) {
				var22 = 1.0F;
			}

			var22 = 1.0F - var22;
			return 1.0F - var22 * var22 * var22;
		}
	}

	public byte[] copyBlocks() {
		return Arrays.copyOf(this.blocks, this.blocks.length);
	}

	public Liquid getLiquid(int var1, int var2, int var3) {
		int var4 = this.getTile(var1, var2, var3);
		return var4 == 0 ? Liquid.none : Tile.tiles[var4].getLiquidType();
	}

	public boolean isWater(int var1, int var2, int var3) {
		int var4 = this.getTile(var1, var2, var3);
		return var4 > 0 && Tile.tiles[var4].getLiquidType() == Liquid.water;
	}

	public void setNetworkMode(boolean var1) {
		this.networkMode = var1;
	}

	public HitResult clip(Vec3 var1, Vec3 var2) {
		if(!Float.isNaN(var1.x) && !Float.isNaN(var1.y) && !Float.isNaN(var1.z)) {
			if(!Float.isNaN(var2.x) && !Float.isNaN(var2.y) && !Float.isNaN(var2.z)) {
				int var3 = (int)Math.floor((double)var2.x);
				int var4 = (int)Math.floor((double)var2.y);
				int var5 = (int)Math.floor((double)var2.z);
				int var6 = (int)Math.floor((double)var1.x);
				int var7 = (int)Math.floor((double)var1.y);
				int var8 = (int)Math.floor((double)var1.z);
				int var9 = 20;

				Vec3 var20;
				int var21;
				byte var22;
				do {
					if(var9-- < 0) {
						return null;
					}

					if(Float.isNaN(var1.x) || Float.isNaN(var1.y) || Float.isNaN(var1.z)) {
						return null;
					}

					if(var6 == var3 && var7 == var4 && var8 == var5) {
						return null;
					}

					float var10 = 999.0F;
					float var11 = 999.0F;
					float var12 = 999.0F;
					if(var3 > var6) {
						var10 = (float)var6 + 1.0F;
					}

					if(var3 < var6) {
						var10 = (float)var6;
					}

					if(var4 > var7) {
						var11 = (float)var7 + 1.0F;
					}

					if(var4 < var7) {
						var11 = (float)var7;
					}

					if(var5 > var8) {
						var12 = (float)var8 + 1.0F;
					}

					if(var5 < var8) {
						var12 = (float)var8;
					}

					float var13 = 999.0F;
					float var14 = 999.0F;
					float var15 = 999.0F;
					float var16 = var2.x - var1.x;
					float var17 = var2.y - var1.y;
					float var18 = var2.z - var1.z;
					if(var10 != 999.0F) {
						var13 = (var10 - var1.x) / var16;
					}

					if(var11 != 999.0F) {
						var14 = (var11 - var1.y) / var17;
					}

					if(var12 != 999.0F) {
						var15 = (var12 - var1.z) / var18;
					}

					boolean var19 = false;
					if(var13 < var14 && var13 < var15) {
						if(var3 > var6) {
							var22 = 4;
						} else {
							var22 = 5;
						}

						var1.x = var10;
						var1.y += var17 * var13;
						var1.z += var18 * var13;
					} else if(var14 < var15) {
						if(var4 > var7) {
							var22 = 0;
						} else {
							var22 = 1;
						}

						var1.x += var16 * var14;
						var1.y = var11;
						var1.z += var18 * var14;
					} else {
						if(var5 > var8) {
							var22 = 2;
						} else {
							var22 = 3;
						}

						var1.x += var16 * var15;
						var1.y += var17 * var15;
						var1.z = var12;
					}

					var20 = new Vec3(var1.x, var1.y, var1.z);
					var6 = (int)(var20.x = (float)Math.floor((double)var1.x));
					if(var22 == 5) {
						--var6;
						++var20.x;
					}

					var7 = (int)(var20.y = (float)Math.floor((double)var1.y));
					if(var22 == 1) {
						--var7;
						++var20.y;
					}

					var8 = (int)(var20.z = (float)Math.floor((double)var1.z));
					if(var22 == 3) {
						--var8;
						++var20.z;
					}

					var21 = this.getTile(var6, var7, var8);
				} while(var21 <= 0 || Tile.tiles[var21].getLiquidType() != Liquid.none);

				return new HitResult(var6, var7, var8, var22, var20);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public void playSound(String var1, Entity var2, float var3, float var4) {
		if(this.rendererContext != null) {
			Minecraft var5 = this.rendererContext;
			if(var5.soundManager == null || !var5.options.sound) {
				return;
			}

			if(var2.distanceToSqr(var5.player) < 1024.0F) {
				var5.soundManager.playSound(var1, var2);
			}
		}

	}

	public void playSound(String var1, float var2, float var3, float var4, float var5, float var6) {
		if(this.rendererContext != null) {
			Minecraft var7 = this.rendererContext;
			if(var7.soundManager == null || !var7.options.sound) {
				return;
			}

			var7.soundManager.playSound(var1, var2, var3, var4);
		}

	}

	public int maybeSpawnMobs(int var1, Entity var2, LevelLoaderListener var3) {
		int var4 = 0;

		for(int var5 = 0; var5 < var1; ++var5) {
			if(var3 != null) {
				var3.setLoadingProgress(var5 * 100 / (var1 - 1));
			}

			int var6 = this.random.nextInt(4);
			int var7 = this.random.nextInt(this.width);
			int var8 = (int)(Math.min(this.random.nextFloat(), this.random.nextFloat()) * (float)this.depth);
			int var9 = this.random.nextInt(this.height);
			if(!this.isSolidTile(var7, var8, var9) && this.getLiquid(var7, var8, var9) == Liquid.none && (!this.isLit(var7, var8, var9) || this.random.nextInt(5) == 0)) {
				for(int var10 = 0; var10 < 3; ++var10) {
					int var11 = var7;
					int var12 = var8;
					int var13 = var9;

					for(int var14 = 0; var14 < 3; ++var14) {
						var11 += this.random.nextInt(6) - this.random.nextInt(6);
						var12 += this.random.nextInt(1) - this.random.nextInt(1);
						var13 += this.random.nextInt(6) - this.random.nextInt(6);
						if(var11 >= 0 && var13 >= 1 && var12 >= 0 && var12 < this.depth - 2 && var11 < this.width && var13 < this.height && this.isSolidTile(var11, var12 - 1, var13) && !this.isSolidTile(var11, var12, var13) && !this.isSolidTile(var11, var12 + 1, var13)) {
							float var15 = (float)var11 + 0.5F;
							float var16 = (float)var12 + 1.0F;
							float var17 = (float)var13 + 0.5F;
							float var18;
							float var19;
							float var20;
							if(var2 != null) {
								var18 = var15 - var2.x;
								var19 = var16 - var2.y;
								var20 = var17 - var2.z;
								var18 = var18 * var18 + var19 * var19 + var20 * var20;
								if(var18 < 256.0F) {
									continue;
								}
							} else {
								var18 = var15 - (float)this.xSpawn;
								var19 = var16 - (float)this.ySpawn;
								var20 = var17 - (float)this.zSpawn;
								var18 = var18 * var18 + var19 * var19 + var20 * var20;
								if(var18 < 256.0F) {
									continue;
								}
							}

							Object var21 = null;
							if(var6 == 0) {
								var21 = new Zombie(this, var15, var16, var17);
							}

							if(var6 == 1) {
								var21 = new Skeleton(this, var15, var16, var17);
							}

							if(var6 == 2) {
								var21 = new Pig(this, var15, var16, var17);
							}

							if(var6 == 3) {
								var21 = new Creeper(this, var15, var16, var17);
							}

							if(this.isFree(((Mob)var21).bb)) {
								++var4;
								this.addEntity((Entity)var21);
							}
						}
					}
				}
			}
		}

		return var4;
	}

	public boolean maybeGrowTree(int var1, int var2, int var3) {
		int var4 = this.random.nextInt(3) + 4;
		boolean var5 = true;

		int var6;
		int var8;
		int var9;
		int var10;
		for(var6 = var2; var6 <= var2 + 1 + var4; ++var6) {
			byte var7 = 1;
			if(var6 == var2) {
				var7 = 0;
			}

			if(var6 >= var2 + 1 + var4 - 2) {
				var7 = 2;
			}

			for(var8 = var1 - var7; var8 <= var1 + var7 && var5; ++var8) {
				for(var9 = var3 - var7; var9 <= var3 + var7 && var5; ++var9) {
					if(var8 >= 0 && var6 >= 0 && var9 >= 0 && var8 < this.width && var6 < this.depth && var9 < this.height) {
						var10 = this.blocks[(var6 * this.height + var9) * this.width + var8] & 255;
						if(var10 != 0) {
							var5 = false;
						}
					} else {
						var5 = false;
					}
				}
			}
		}

		if(!var5) {
			return false;
		} else {
			var6 = this.blocks[((var2 - 1) * this.height + var3) * this.width + var1] & 255;
			if(var6 == Tile.grass.id && var2 < this.depth - var4 - 1) {
				this.setTile(var1, var2 - 1, var3, Tile.dirt.id);

				int var13;
				for(var13 = var2 - 3 + var4; var13 <= var2 + var4; ++var13) {
					var8 = var13 - (var2 + var4);
					var9 = 1 - var8 / 2;

					for(var10 = var1 - var9; var10 <= var1 + var9; ++var10) {
						int var12 = var10 - var1;

						for(var6 = var3 - var9; var6 <= var3 + var9; ++var6) {
							int var11 = var6 - var3;
							if(Math.abs(var12) != var9 || Math.abs(var11) != var9 || this.random.nextInt(2) != 0 && var8 != 0) {
								this.setTile(var10, var13, var6, Tile.leaf.id);
							}
						}
					}
				}

				for(var13 = 0; var13 < var4; ++var13) {
					this.setTile(var1, var2 + var13, var3, Tile.log.id);
				}

				return true;
			} else {
				return false;
			}
		}
	}

	public Entity getPlayer() {
		return this.player;
	}

	public void addEntity(Entity var1) {
		this.blockMap.insert(var1);
		var1.setLevel(this);
	}

	public void removeEntity(Entity var1) {
		this.blockMap.remove(var1);
	}

	public void explode(Entity var1, float var2, float var3, float var4, float var5) {
		int var6 = (int)(var2 - var5 - 1.0F);
		int var7 = (int)(var2 + var5 + 1.0F);
		int var8 = (int)(var3 - var5 - 1.0F);
		int var9 = (int)(var3 + var5 + 1.0F);
		int var10 = (int)(var4 - var5 - 1.0F);
		int var11 = (int)(var4 + var5 + 1.0F);

		int var13;
		float var15;
		float var16;
		for(int var12 = var6; var12 < var7; ++var12) {
			for(var13 = var9 - 1; var13 >= var8; --var13) {
				for(int var14 = var10; var14 < var11; ++var14) {
					var15 = (float)var12 + 0.5F - var2;
					var16 = (float)var13 + 0.5F - var3;
					float var17 = (float)var14 + 0.5F - var4;
					if(var12 >= 0 && var13 >= 0 && var14 >= 0 && var12 < this.width && var13 < this.depth && var14 < this.height && var15 * var15 + var16 * var16 + var17 * var17 < var5 * var5) {
						int var20 = this.getTile(var12, var13, var14);
						if(var20 > 0) {
							Tile.tiles[var20].wasExploded(this, var12, var13, var14, 0.3F);
							this.setTile(var12, var13, var14, 0);
						}
					}
				}
			}
		}

		List var18 = this.blockMap.getEntities(var1, (float)var6, (float)var8, (float)var10, (float)var7, (float)var9, (float)var11);

		for(var13 = 0; var13 < var18.size(); ++var13) {
			Entity var19 = (Entity)var18.get(var13);
			var15 = var19.distanceTo(var1) / var5;
			if(var15 <= 1.0F) {
				var16 = 1.0F - var15;
				var19.hurt(var1, (int)(var16 * 15.0F + 1.0F));
			}
		}

	}

	public Player findPlayer() {
		for(int var1 = 0; var1 < this.blockMap.all.size(); ++var1) {
			Entity var2 = (Entity)this.blockMap.all.get(var1);
			if(var2 instanceof Player) {
				return (Player)var2;
			}
		}

		return null;
	}
	
	public void writeTo(DataOutputStream out) throws IOException {
	    out.writeInt(this.width);
	    out.writeInt(this.height);
	    out.writeInt(this.depth);
	    out.writeUTF(this.name != null ? this.name : "");
	    out.writeUTF(this.creator != null ? this.creator : "");
	    out.writeLong(this.createTime);
	    out.writeInt(this.xSpawn);
	    out.writeInt(this.ySpawn);
	    out.writeInt(this.zSpawn);
	    out.writeFloat(this.rotSpawn);

	    if (this.blocks != null) {
	        out.writeInt(this.blocks.length);
	        out.write(this.blocks);
	    } else {
	        out.writeInt(0);
	    }

	    out.writeInt(this.waterLevel);
	    out.writeInt(this.skyColor);
	    out.writeInt(this.fogColor);
	    out.writeInt(this.cloudColor);
	    out.writeBoolean(this.networkMode);
	    out.writeInt(this.unprocessed);
	    out.writeInt(this.tickCount);
	    
	    out.writeInt(this.blockMap.all.size());
		for(int i = 0; i < this.blockMap.all.size(); ++i) {
			Entity e = (Entity)this.blockMap.all.get(i);
			out.writeUTF(e.getClass().getSimpleName());
			e.writeTo(out);
		}
	}

	public void readFrom(DataInputStream in, int version) throws IOException {
	    this.width = in.readInt();
	    this.height = in.readInt();
	    this.depth = in.readInt();
	    this.name = in.readUTF();
	    this.creator = in.readUTF();
	    this.createTime = in.readLong();
	    this.xSpawn = in.readInt();
	    this.ySpawn = in.readInt();
	    this.zSpawn = in.readInt();
	    this.rotSpawn = in.readFloat();

	    int len = in.readInt();
	    if (len > 0) {
	        this.blocks = new byte[len];
	        in.readFully(this.blocks);
	    }

	    this.waterLevel = in.readInt();
	    this.skyColor = in.readInt();
	    this.fogColor = in.readInt();
	    this.cloudColor = in.readInt();
	    this.networkMode = in.readBoolean();
	    this.unprocessed = in.readInt();
	    this.tickCount = in.readInt();

	    this.initTransient();
	    
	    try {
			int entityCount = in.readInt();
			for(int i = 0; i < entityCount; ++i) {
				String type = in.readUTF();
				Entity e = null;
				
				if(type.equals("Player")) {
					e = new Player(this);
				} else if(type.equals("Zombie")) {
					e = new Zombie(this, 0, 0, 0);
				} else if(type.equals("Skeleton")) {
					e = new Skeleton(this, 0, 0, 0);
				} else if(type.equals("Pig")) {
					e = new Pig(this, 0, 0, 0);
				} else if(type.equals("Creeper")) {
					e = new Creeper(this, 0, 0, 0);
				}
				
				if(e != null) {
					if(e instanceof Player) {
						this.removeEntity(e);
					}
					e.readFrom(in);
					this.addEntity(e);
				} else {
					in.skipBytes(in.readInt());
				}
			}
		} catch (java.io.EOFException e) {
		}
	}
}
