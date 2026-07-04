package com.mojang.minecraft.level.levelgen;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.LevelLoaderListener;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.levelgen.synth.Distort;
import com.mojang.minecraft.level.levelgen.synth.PerlinNoise;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.mob.Creeper;
import com.mojang.minecraft.mob.Mob;
import com.mojang.minecraft.mob.Pig;
import com.mojang.minecraft.mob.Skeleton;
import com.mojang.minecraft.mob.Zombie;
import java.util.ArrayList;
import net.lax1dude.eaglercraft.Random;


public final class LevelGen {
	private LevelLoaderListener levelLoaderListener;
	private int width;
	private int height;
	private int depth;
	private Random random = new Random();
	private byte[] blocks;
	private int waterLevel;
	private int[] coords = new int[1048576];

	public LevelGen(LevelLoaderListener var1) {
		this.levelLoaderListener = var1;
	}

	public final Level generateLevel(String var1, int var2, int var3, int var4) {
		this.levelLoaderListener.beginLevelLoading("Generating level");
		this.width = var2;
		this.height = var3;
		this.depth = 64;
		this.waterLevel = 32;
		this.blocks = new byte[var2 * var3 << 6];
		this.levelLoaderListener.levelLoadUpdate("Raising..");
		LevelGen var5 = this;
		Distort var8 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));
		Distort var9 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));
		PerlinNoise var10 = new PerlinNoise(this.random, 6);
		int[] var11 = new int[this.width * this.height];
		float var6 = 1.3F;

		int var14;
		int var15;
		for(var14 = 0; var14 < var5.width; ++var14) {
			var5.setNextPhase(var14 * 100 / (var5.width - 1));

			for(var15 = 0; var15 < var5.height; ++var15) {
				double var16 = var8.getValue((double)((float)var14 * var6), (double)((float)var15 * var6)) / 6.0D + (double)-4;
				double var18 = var9.getValue((double)((float)var14 * var6), (double)((float)var15 * var6)) / 5.0D + 10.0D + (double)-4;
				double var20 = var10.getValue((double)var14, (double)var15) / 8.0D;
				if(var20 > 0.0D) {
					var18 = var16;
				}

				double var22 = Math.max(var16, var18) / 2.0D;
				if(var22 < 0.0D) {
					var22 *= 0.8D;
				}

				var11[var14 + var15 * var5.width] = (int)var22;
			}
		}

		this.levelLoaderListener.levelLoadUpdate("Eroding..");
		int[] var35 = var11;
		var5 = this;
		var9 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));
		Distort var41 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));

		int var32;
		int var44;
		int var47;
		for(var44 = 0; var44 < var5.width; ++var44) {
			var5.setNextPhase(var44 * 100 / (var5.width - 1));

			for(var32 = 0; var32 < var5.height; ++var32) {
				double var13 = var9.getValue((double)(var44 << 1), (double)(var32 << 1)) / 8.0D;
				var15 = var41.getValue((double)(var44 << 1), (double)(var32 << 1)) > 0.0D ? 1 : 0;
				if(var13 > 2.0D) {
					var47 = var35[var44 + var32 * var5.width];
					var47 = ((var47 - var15) / 2 << 1) + var15;
					var35[var44 + var32 * var5.width] = var47;
				}
			}
		}

		this.levelLoaderListener.levelLoadUpdate("Soiling..");
		var35 = var11;
		var5 = this;
		int var38 = this.width;
		int var43 = this.height;
		var44 = this.depth;
		PerlinNoise var34 = new PerlinNoise(this.random, 8);

		int var17;
		int var46;
		int var52;
		for(var46 = 0; var46 < var38; ++var46) {
			var5.setNextPhase(var46 * 100 / (var5.width - 1));

			for(var14 = 0; var14 < var43; ++var14) {
				var15 = (int)(var34.getValue((double)var46, (double)var14) / 24.0D) - 4;
				var47 = var35[var46 + var14 * var38] + var5.waterLevel;
				var17 = var47 + var15;
				var35[var46 + var14 * var38] = Math.max(var47, var17);
				if(var35[var46 + var14 * var38] > var44 - 2) {
					var35[var46 + var14 * var38] = var44 - 2;
				}

				if(var35[var46 + var14 * var38] < 1) {
					var35[var46 + var14 * var38] = 1;
				}

				for(var52 = 0; var52 < var44; ++var52) {
					int var19 = (var52 * var5.height + var14) * var5.width + var46;
					int var54 = 0;
					if(var52 <= var47) {
						var54 = Tile.dirt.id;
					}

					if(var52 <= var17) {
						var54 = Tile.rock.id;
					}

					if(var52 == 0) {
						var54 = Tile.lava.id;
					}

					var5.blocks[var19] = (byte)var54;
				}
			}
		}

		this.levelLoaderListener.levelLoadUpdate("Carving..");
		boolean var40 = true;
		boolean var36 = false;
		var5 = this;
		var43 = this.width;
		var44 = this.height;
		var32 = this.depth;
		var46 = var43 * var44 * var32 / 256 / 64 << 1;

		for(var14 = 0; var14 < var46; ++var14) {
			var5.setNextPhase(var14 * 100 / (var46 - 1) / 4);
			float var48 = var5.random.nextFloat() * (float)var43;
			float var49 = var5.random.nextFloat() * (float)var32;
			float var50 = var5.random.nextFloat() * (float)var44;
			var52 = (int)((var5.random.nextFloat() + var5.random.nextFloat()) * 200.0F);
			float var53 = (float)((double)var5.random.nextFloat() * Math.PI * 2.0D);
			float var55 = 0.0F;
			float var21 = (float)((double)var5.random.nextFloat() * Math.PI * 2.0D);
			float var56 = 0.0F;
			float var23 = var5.random.nextFloat() * var5.random.nextFloat();

			for(int var7 = 0; var7 < var52; ++var7) {
				var48 = (float)((double)var48 + Math.sin((double)var53) * Math.cos((double)var21));
				var50 = (float)((double)var50 + Math.cos((double)var53) * Math.cos((double)var21));
				var49 = (float)((double)var49 + Math.sin((double)var21));
				var53 += var55 * 0.2F;
				var55 *= 0.9F;
				var55 += var5.random.nextFloat() - var5.random.nextFloat();
				var21 += var56 * 0.5F;
				var21 *= 0.5F;
				var56 *= 12.0F / 16.0F;
				var56 += var5.random.nextFloat() - var5.random.nextFloat();
				if(var5.random.nextFloat() >= 0.25F) {
					float var37 = var48 + (var5.random.nextFloat() * 4.0F - 2.0F) * 0.2F;
					float var42 = var49 + (var5.random.nextFloat() * 4.0F - 2.0F) * 0.2F;
					float var12 = var50 + (var5.random.nextFloat() * 4.0F - 2.0F) * 0.2F;
					float var24 = ((float)var5.depth - var42) / (float)var5.depth;
					var24 = 1.2F + (var24 * 3.5F + 1.0F) * var23;
					var24 = (float)(Math.sin((double)var7 * Math.PI / (double)var52) * (double)var24);

					for(int var25 = (int)(var37 - var24); var25 <= (int)(var37 + var24); ++var25) {
						for(int var26 = (int)(var42 - var24); var26 <= (int)(var42 + var24); ++var26) {
							for(int var27 = (int)(var12 - var24); var27 <= (int)(var12 + var24); ++var27) {
								float var28 = (float)var25 - var37;
								float var29 = (float)var26 - var42;
								float var30 = (float)var27 - var12;
								var28 = var28 * var28 + var29 * var29 * 2.0F + var30 * var30;
								if(var28 < var24 * var24 && var25 >= 1 && var26 >= 1 && var27 >= 1 && var25 < var5.width - 1 && var26 < var5.depth - 1 && var27 < var5.height - 1) {
									int var57 = (var26 * var5.height + var27) * var5.width + var25;
									if(var5.blocks[var57] == Tile.rock.id) {
										var5.blocks[var57] = 0;
									}
								}
							}
						}
					}
				}
			}
		}

		this.addOre(Tile.coalOre.id, 90, 1, 4);
		this.addOre(Tile.ironOre.id, 70, 2, 4);
		this.addOre(Tile.goldOre.id, 50, 3, 4);
		this.levelLoaderListener.levelLoadUpdate("Watering..");
		var5 = this;
		long var39 = System.nanoTime();
		long var45 = 0L;
		var46 = Tile.calmWater.id;
		this.setNextPhase(0);

		for(var14 = 0; var14 < var5.width; ++var14) {
			var45 += var5.floodFillLiquid(var14, var5.depth / 2 - 1, 0, 0, var46);
			var45 += var5.floodFillLiquid(var14, var5.depth / 2 - 1, var5.height - 1, 0, var46);
		}

		for(var14 = 0; var14 < var5.height; ++var14) {
			var45 += var5.floodFillLiquid(0, var5.depth / 2 - 1, var14, 0, var46);
			var45 += var5.floodFillLiquid(var5.width - 1, var5.depth / 2 - 1, var14, 0, var46);
		}

		var14 = var5.width * var5.height / 8000;

		for(var15 = 0; var15 < var14; ++var15) {
			if(var15 % 100 == 0) {
				var5.setNextPhase(var15 * 100 / (var14 - 1));
			}

			var47 = var5.random.nextInt(var5.width);
			var17 = var5.waterLevel - 1 - var5.random.nextInt(2);
			var52 = var5.random.nextInt(var5.height);
			if(var5.blocks[(var17 * var5.height + var52) * var5.width + var47] == 0) {
				var45 += var5.floodFillLiquid(var47, var17, var52, 0, var46);
			}
		}

		var5.setNextPhase(100);
		long var51 = System.nanoTime();
		System.out.println("Flood filled " + var45 + " tiles in " + (double)(var51 - var39) / 1000000.0D + " ms");
		this.levelLoaderListener.levelLoadUpdate("Melting..");
		this.addLava();
		this.levelLoaderListener.levelLoadUpdate("Growing..");
		this.addBeaches(var11);
		this.levelLoaderListener.levelLoadUpdate("Planting..");
		this.plantTrees(var11);
		this.addMushrooms(var11);
		Level var33 = new Level();
		var33.waterLevel = this.waterLevel;
		var33.setData(var2, 64, var3, this.blocks);
		var33.createTime = System.currentTimeMillis();
		var33.creator = var1;
		var33.name = "A Nice World";
		this.generateTrees(var33, var11);
		this.levelLoaderListener.levelLoadUpdate("Spawning..");
		this.spawnEntities(var33);
		return var33;
	}

	private void addBeaches(int[] var1) {
		int var2 = this.width;
		int var3 = this.height;
		int var4 = this.depth;
		PerlinNoise var5 = new PerlinNoise(this.random, 8);
		PerlinNoise var6 = new PerlinNoise(this.random, 8);

		for(int var7 = 0; var7 < var2; ++var7) {
			this.setNextPhase(var7 * 100 / (this.width - 1));

			for(int var8 = 0; var8 < var3; ++var8) {
				boolean var9 = var5.getValue((double)var7, (double)var8) > 8.0D;
				boolean var10 = var6.getValue((double)var7, (double)var8) > 12.0D;
				int var11 = var1[var7 + var8 * var2];
				int var12 = (var11 * this.height + var8) * this.width + var7;
				int var13 = this.blocks[((var11 + 1) * this.height + var8) * this.width + var7] & 255;
				if((var13 == Tile.water.id || var13 == Tile.calmWater.id) && var11 <= var4 / 2 - 1 && var10) {
					this.blocks[var12] = (byte)Tile.gravel.id;
				}

				if(var13 == 0) {
					int var14 = Tile.grass.id;
					if(var11 <= var4 / 2 - 1 && var9) {
						var14 = Tile.sand.id;
					}

					this.blocks[var12] = (byte)var14;
				}
			}
		}

	}

	private void generateTrees(Level var1, int[] var2) {
		int var3 = this.width;
		int var4 = this.width * this.height / 4000;

		for(int var5 = 0; var5 < var4; ++var5) {
			this.setNextPhase(var5 * 50 / (var4 - 1) + 50);
			int var6 = this.random.nextInt(this.width);
			int var7 = this.random.nextInt(this.height);

			for(int var8 = 0; var8 < 20; ++var8) {
				int var9 = var6;
				int var10 = var7;

				for(int var11 = 0; var11 < 20; ++var11) {
					var9 += this.random.nextInt(6) - this.random.nextInt(6);
					var10 += this.random.nextInt(6) - this.random.nextInt(6);
					if(var9 >= 0 && var10 >= 0 && var9 < this.width && var10 < this.height) {
						int var12 = var2[var9 + var10 * var3] + 1;
						if(this.random.nextInt(4) == 0) {
							var1.maybeGrowTree(var9, var12, var10);
						}
					}
				}
			}
		}

	}

	private void spawnEntities(Level var1) {
		int var2 = this.width * this.height * this.depth / 800;
		int var3 = 0;

		for(int var4 = 0; var4 < var2; ++var4) {
			this.setNextPhase(var4 * 100 / (var2 - 1));
			int var5 = this.random.nextInt(4);
			int var6 = this.random.nextInt(this.width);
			int var7 = (int)(Math.min(this.random.nextFloat(), this.random.nextFloat()) * (float)this.depth);
			int var8 = this.random.nextInt(this.height);
			if(!var1.isSolidTile(var6, var7, var8) && var1.getLiquid(var6, var7, var8) == Liquid.none && (!var1.isLit(var6, var7, var8) || this.random.nextInt(5) == 0)) {
				for(int var9 = 0; var9 < 3; ++var9) {
					int var10 = var6;
					int var11 = var7;
					int var12 = var8;

					for(int var13 = 0; var13 < 3; ++var13) {
						var10 += this.random.nextInt(6) - this.random.nextInt(6);
						var11 += this.random.nextInt(1) - this.random.nextInt(1);
						var12 += this.random.nextInt(6) - this.random.nextInt(6);
						if(var10 >= 0 && var12 >= 1 && var11 >= 0 && var11 < this.depth - 2 && var10 < this.width && var12 < this.height && var1.isSolidTile(var10, var11 - 1, var12) && !var1.isSolidTile(var10, var11, var12) && !var1.isSolidTile(var10, var11 + 1, var12)) {
							float var14 = (float)var10 + 0.5F;
							float var15 = (float)var11 + 1.0F;
							float var16 = (float)var12 + 0.5F;
							Object var17 = null;
							if(var5 == 0) {
								var17 = new Zombie(var1, var14, var15, var16);
							}

							if(var5 == 1) {
								var17 = new Skeleton(var1, var14, var15, var16);
							}

							if(var5 == 2) {
								var17 = new Pig(var1, var14, var15, var16);
							}

							if(var5 == 3) {
								var17 = new Creeper(var1, var14, var15, var16);
							}

							if(var1.isFree(((Mob)var17).bb)) {
								++var3;
								var1.addEntity((Entity)var17);
							}
						}
					}
				}
			}
		}

		System.out.println(var3 + " mobs");
	}

	private void plantTrees(int[] var1) {
		int var2 = this.width;
		int var3 = this.width * this.height / 3000;

		for(int var4 = 0; var4 < var3; ++var4) {
			int var5 = this.random.nextInt(2);
			this.setNextPhase(var4 * 50 / (var3 - 1));
			int var6 = this.random.nextInt(this.width);
			int var7 = this.random.nextInt(this.height);

			for(int var8 = 0; var8 < 10; ++var8) {
				int var9 = var6;
				int var10 = var7;

				for(int var11 = 0; var11 < 5; ++var11) {
					var9 += this.random.nextInt(6) - this.random.nextInt(6);
					var10 += this.random.nextInt(6) - this.random.nextInt(6);
					if((var5 < 2 || this.random.nextInt(4) == 0) && var9 >= 0 && var10 >= 0 && var9 < this.width && var10 < this.height) {
						int var12 = var1[var9 + var10 * var2] + 1;
						boolean var13 = (this.blocks[(var12 * this.height + var10) * this.width + var9] & 255) == 0;
						if(var13) {
							int var14 = (var12 * this.height + var10) * this.width + var9;
							var12 = this.blocks[((var12 - 1) * this.height + var10) * this.width + var9] & 255;
							if(var12 == Tile.grass.id) {
								if(var5 == 0) {
									this.blocks[var14] = (byte)Tile.flower.id;
								} else if(var5 == 1) {
									this.blocks[var14] = (byte)Tile.rose.id;
								}
							}
						}
					}
				}
			}
		}

	}

	private void addMushrooms(int[] var1) {
		int var2 = this.width;
		int var3 = 0;
		int var4 = this.width * this.height * this.depth / 2000;

		for(int var5 = 0; var5 < var4; ++var5) {
			int var6 = this.random.nextInt(2);
			this.setNextPhase(var5 * 50 / (var4 - 1) + 50);
			int var7 = this.random.nextInt(this.width);
			int var8 = this.random.nextInt(this.depth);
			int var9 = this.random.nextInt(this.height);

			for(int var10 = 0; var10 < 20; ++var10) {
				int var11 = var7;
				int var12 = var8;
				int var13 = var9;

				for(int var14 = 0; var14 < 5; ++var14) {
					var11 += this.random.nextInt(6) - this.random.nextInt(6);
					var12 += this.random.nextInt(2) - this.random.nextInt(2);
					var13 += this.random.nextInt(6) - this.random.nextInt(6);
					if((var6 < 2 || this.random.nextInt(4) == 0) && var11 >= 0 && var13 >= 0 && var12 >= 1 && var11 < this.width && var13 < this.height && var12 < var1[var11 + var13 * var2] - 1) {
						boolean var15 = (this.blocks[(var12 * this.height + var13) * this.width + var11] & 255) == 0;
						if(var15) {
							int var17 = (var12 * this.height + var13) * this.width + var11;
							int var16 = this.blocks[((var12 - 1) * this.height + var13) * this.width + var11] & 255;
							if(var16 == Tile.rock.id) {
								if(var6 == 0) {
									this.blocks[var17] = (byte)Tile.mushroom1.id;
								} else if(var6 == 1) {
									this.blocks[var17] = (byte)Tile.mushroom2.id;
								}

								++var3;
							}
						}
					}
				}
			}
		}

		System.out.println("Added " + var3 + " mushrooms");
	}

	private void addOre(int var1, int var2, int var3, int var4) {
		byte var25 = (byte)var1;
		var4 = this.width;
		int var5 = this.height;
		int var6 = this.depth;
		int var7 = var4 * var5 * var6 / 256 / 64 * var2 / 100;

		for(int var8 = 0; var8 < var7; ++var8) {
			this.setNextPhase(var8 * 100 / (var7 - 1) / 4 + var3 * 100 / 4);
			float var9 = this.random.nextFloat() * (float)var4;
			float var10 = this.random.nextFloat() * (float)var6;
			float var11 = this.random.nextFloat() * (float)var5;
			int var12 = (int)((this.random.nextFloat() + this.random.nextFloat()) * 75.0F * (float)var2 / 100.0F);
			float var13 = (float)((double)this.random.nextFloat() * Math.PI * 2.0D);
			float var14 = 0.0F;
			float var15 = (float)((double)this.random.nextFloat() * Math.PI * 2.0D);
			float var16 = 0.0F;

			for(int var17 = 0; var17 < var12; ++var17) {
				var9 = (float)((double)var9 + Math.sin((double)var13) * Math.cos((double)var15));
				var11 = (float)((double)var11 + Math.cos((double)var13) * Math.cos((double)var15));
				var10 = (float)((double)var10 + Math.sin((double)var15));
				var13 += var14 * 0.2F;
				var14 *= 0.9F;
				var14 += this.random.nextFloat() - this.random.nextFloat();
				var15 += var16 * 0.5F;
				var15 *= 0.5F;
				var16 *= 0.9F;
				var16 += this.random.nextFloat() - this.random.nextFloat();
				float var18 = (float)(Math.sin((double)var17 * Math.PI / (double)var12) * (double)var2 / 100.0D + 1.0D);

				for(int var19 = (int)(var9 - var18); var19 <= (int)(var9 + var18); ++var19) {
					for(int var20 = (int)(var10 - var18); var20 <= (int)(var10 + var18); ++var20) {
						for(int var21 = (int)(var11 - var18); var21 <= (int)(var11 + var18); ++var21) {
							float var22 = (float)var19 - var9;
							float var23 = (float)var20 - var10;
							float var24 = (float)var21 - var11;
							var22 = var22 * var22 + var23 * var23 * 2.0F + var24 * var24;
							if(var22 < var18 * var18 && var19 >= 1 && var20 >= 1 && var21 >= 1 && var19 < this.width - 1 && var20 < this.depth - 1 && var21 < this.height - 1) {
								int var26 = (var20 * this.height + var21) * this.width + var19;
								if(this.blocks[var26] == Tile.rock.id) {
									this.blocks[var26] = var25;
								}
							}
						}
					}
				}
			}
		}

	}

	private void setNextPhase(int var1) {
		this.levelLoaderListener.setLoadingProgress(var1);
	}

	private void addLava() {
		int var1 = 0;
		int var2 = this.width * this.height * this.depth / 20000;

		for(int var3 = 0; var3 < var2; ++var3) {
			if(var3 % 100 == 0) {
				this.setNextPhase(var3 * 100 / (var2 - 1));
			}

			int var4 = this.random.nextInt(this.width);
			int var5 = (int)(this.random.nextFloat() * this.random.nextFloat() * (float)(this.waterLevel - 3));
			int var6 = this.random.nextInt(this.height);
			if(this.blocks[(var5 * this.height + var6) * this.width + var4] == 0) {
				++var1;
				this.floodFillLiquid(var4, var5, var6, 0, Tile.calmLava.id);
			}
		}

		this.setNextPhase(100);
		System.out.println("LavaCount: " + var1);
	}

	private long floodFillLiquid(int var1, int var2, int var3, int var4, int var5) {
		byte var20 = (byte)var5;
		ArrayList var21 = new ArrayList();
		byte var6 = 0;
		int var7 = 1;

		int var8;
		for(var8 = 1; 1 << var7 < this.width; ++var7) {
		}

		while(1 << var8 < this.height) {
			++var8;
		}

		int var9 = this.height - 1;
		int var10 = this.width - 1;
		int var22 = var6 + 1;
		this.coords[0] = ((var2 << var8) + var3 << var7) + var1;
		long var13 = 0L;
		var1 = this.width * this.height;

		while(var22 > 0) {
			--var22;
			var2 = this.coords[var22];
			if(var22 == 0 && var21.size() > 0) {
				System.out.println("IT HAPPENED!");
				this.coords = (int[])var21.remove(var21.size() - 1);
				var22 = this.coords.length;
			}

			var3 = var2 >> var7 & var9;
			int var11 = var2 >> var7 + var8;
			int var12 = var2 & var10;

			int var15;
			for(var15 = var12; var12 > 0 && this.blocks[var2 - 1] == 0; --var2) {
				--var12;
			}

			while(var15 < this.width && this.blocks[var2 + var15 - var12] == 0) {
				++var15;
			}

			int var16 = var2 >> var7 & var9;
			int var17 = var2 >> var7 + var8;
			if(var16 != var3 || var17 != var11) {
				System.out.println("hoooly fuck");
			}

			boolean var23 = false;
			boolean var24 = false;
			boolean var18 = false;
			var13 += (long)(var15 - var12);

			for(var12 = var12; var12 < var15; ++var12) {
				this.blocks[var2] = var20;
				boolean var19;
				if(var3 > 0) {
					var19 = this.blocks[var2 - this.width] == 0;
					if(var19 && !var23) {
						if(var22 == this.coords.length) {
							var21.add(this.coords);
							this.coords = new int[1048576];
							var22 = 0;
						}

						this.coords[var22++] = var2 - this.width;
					}

					var23 = var19;
				}

				if(var3 < this.height - 1) {
					var19 = this.blocks[var2 + this.width] == 0;
					if(var19 && !var24) {
						if(var22 == this.coords.length) {
							var21.add(this.coords);
							this.coords = new int[1048576];
							var22 = 0;
						}

						this.coords[var22++] = var2 + this.width;
					}

					var24 = var19;
				}

				if(var11 > 0) {
					byte var25 = this.blocks[var2 - var1];
					if((var20 == Tile.lava.id || var20 == Tile.calmLava.id) && (var25 == Tile.water.id || var25 == Tile.calmWater.id)) {
						this.blocks[var2 - var1] = (byte)Tile.rock.id;
					}

					var19 = var25 == 0;
					if(var19 && !var18) {
						if(var22 == this.coords.length) {
							var21.add(this.coords);
							this.coords = new int[1048576];
							var22 = 0;
						}

						this.coords[var22++] = var2 - var1;
					}

					var18 = var19;
				}

				++var2;
			}
		}

		return var13;
	}
}
